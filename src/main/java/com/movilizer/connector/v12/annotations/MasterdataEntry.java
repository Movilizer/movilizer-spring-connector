package com.movilizer.connector.v12.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MasterdataEntry {

    String name() default "";

    Type type() default Type.STRING;

    public enum Type {
        STRING, BASE64, OBJECT
    }
}
