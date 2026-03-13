package com.github.mad.logging.internal.aspect;

import com.github.mad.logging.api.annotation.LoggingField;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.env.Environment;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class LoggingFieldAspect {
    private final ExpressionParser expressionParser = new SpelExpressionParser();
    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    private final BeanFactory beanFactory;
    private final Environment environment;

    public LoggingFieldAspect(
            BeanFactory beanFactory,
            Environment environment
    ) {
        this.beanFactory = beanFactory;
        this.environment = environment;
    }

    @Around("@annotation(loggingField) || @within(loggingField)")
    public Object addLoggingFields(
            ProceedingJoinPoint joinPoint,
            LoggingField loggingField
    ) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        if (loggingField.entries().length > 0) {
            return addFieldsEntries(joinPoint, loggingField, method);
        }

        return addFieldEntry(joinPoint, loggingField, method);
    }

    private Object addFieldEntry(
            ProceedingJoinPoint joinPoint,
            LoggingField loggingField,
            Method method
    ) throws Throwable {
        String key = loggingField.key();
        String valueExpression = loggingField.value();
        String resolvedValue = resolveValue(method, joinPoint.getArgs(), valueExpression);

        try {
            MDC.put(key, resolvedValue);
            return joinPoint.proceed();
        } finally {
            MDC.remove(key);
        }
    }

    private Object addFieldsEntries(
            ProceedingJoinPoint joinPoint,
            LoggingField loggingField,
            Method method
    ) throws Throwable {
        Map<String, String> valueExpressions = new HashMap<>();
        for (LoggingField.Entry entry : loggingField.entries()) {
            valueExpressions.put(entry.key(), entry.value());
        }

        try {
            for (Map.Entry<String, String> entry : valueExpressions.entrySet()) {
                String key = entry.getKey();
                String valueExpression = entry.getValue();
                String resolvedValue = resolveValue(method, joinPoint.getArgs(), valueExpression);
                MDC.put(key, resolvedValue);
            }
            return joinPoint.proceed();
        } finally {
            for (String key : valueExpressions.keySet()) {
                MDC.remove(key);
            }
        }
    }

    private String resolveValue(
            Method method,
            Object[] args,
            String expression
    ) {
        if (expression == null || expression.isBlank()) {
            return "";
        }

        String resolvedPlaceholdersExpression = environment.resolvePlaceholders(expression);
        boolean isSPELExpression = resolvedPlaceholdersExpression.startsWith("#") ||
                resolvedPlaceholdersExpression.startsWith("@") ||
                resolvedPlaceholdersExpression.startsWith("T(");
        if (!isSPELExpression) {
            return resolvedPlaceholdersExpression;
        }

        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanFactoryResolver(beanFactory));

        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }
        }

        Object value = expressionParser.parseExpression(resolvedPlaceholdersExpression).getValue(context);
        return value != null ? value.toString() : resolvedPlaceholdersExpression;
    }
}
