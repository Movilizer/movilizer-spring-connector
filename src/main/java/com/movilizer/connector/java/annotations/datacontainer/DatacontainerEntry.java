package com.movilizer.connector.java.annotations.datacontainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DatacontainerEntry {

    String name() default "";

    Type type() default Type.STRING;

    enum Type {
        STRING, BASE64, ENTRY
    }
}
