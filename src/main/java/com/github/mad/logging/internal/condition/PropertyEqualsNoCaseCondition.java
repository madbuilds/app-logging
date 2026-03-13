package com.github.mad.logging.internal.condition;

import ch.qos.logback.core.boolex.PropertyConditionBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyEqualsNoCaseCondition extends PropertyConditionBase {
    private String key;
    private String value;

    public void start() {
        if (this.key == null) {
            this.addError("In PropertyEqualsNoCaseCondition 'key' parameter cannot be null");
        } else if (this.value == null) {
            this.addError("In PropertyEqualsNoCaseCondition 'value' parameter cannot be null");
        } else {
            super.start();
        }
    }

    @Override
    public boolean evaluate() {
        if (this.key == null) {
            this.addError("key cannot be null");
            return false;
        } else {
            String val = this.p(this.key);
            return val != null && val.equalsIgnoreCase(this.value);
        }
    }
}
