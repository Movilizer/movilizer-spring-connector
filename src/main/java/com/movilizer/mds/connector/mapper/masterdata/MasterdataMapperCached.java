package com.movilizer.mds.connector.mapper.masterdata;

import com.movilizer.mds.connector.mapper.masterdata.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

class MasterdataMapperCached {

    private static Logger logger = LoggerFactory.getLogger(MasterdataMapperCached.class);

    private Class<?> model;
    private String pool;

    private Field keyField;
    private Field groupField;
    private Field descField;
    private Field filter1Field;
    private Field filter2Field;
    private Field filter3Field;
    private Field filter4Field;
    private Field filter5Field;
    private Field filter6Field;
    private Field mafAppSpaceField;

    private Method poolNameMethod;
    private Method keyMethod;
    private Method groupMethod;
    private Method descMethod;
    private Method filter1Method;
    private Method filter2Method;
    private Method filter3Method;
    private Method filter4Method;
    private Method filter5Method;
    private Method filter6Method;
    private Method mafAppSpaceMethod;

    MasterdataMapperCached(Class<?> model) {
        this.model = model;

        MasterdataPool poolAnnotation = model.getAnnotation(MasterdataPool.class);
        assert poolAnnotation != null;
        pool = poolAnnotation.value();

        for (Field field : model.getDeclaredFields()) {
            if (field.getAnnotation(MasterdataKey.class) != null) {
                assert field.getType().equals(String.class);
                keyField = field;
            }

            if (field.getAnnotation(MasterdataGroup.class) != null) {
                assert field.getType().equals(String.class);
                groupField = field;
            }

            if (field.getAnnotation(MasterdataDescription.class) != null) {
                assert field.getType().equals(String.class);
                descField = field;
            }

            if (field.getAnnotation(MasterdataFilter1.class) != null) {
                assert field.getType().equals(String.class);
                filter1Field = field;
            }

            if (field.getAnnotation(MasterdataFilter2.class) != null) {
                assert field.getType().equals(String.class);
                filter2Field = field;
            }

            if (field.getAnnotation(MasterdataFilter3.class) != null) {
                assert field.getType().equals(String.class);
                filter3Field = field;
            }

            if (field.getAnnotation(MasterdataFilter4.class) != null) {
                assert field.getType().equals(Long.class);
                filter4Field = field;
            }

            if (field.getAnnotation(MasterdataFilter5.class) != null) {
                assert field.getType().equals(Long.class);
                filter5Field = field;
            }

            if (field.getAnnotation(MasterdataFilter6.class) != null) {
                assert field.getType().equals(Long.class);
                filter6Field = field;
            }

            if (field.getAnnotation(MafAppSpace.class) != null) {
                assert field.getType().equals(Long.class);
                mafAppSpaceField = field;
            }
        }

        for (Method method : model.getDeclaredMethods()) {
            if (method.getAnnotation(MasterdataPoolNameGenerator.class) != null) {
                assert method.getReturnType().equals(String.class);
                poolNameMethod = method;
            }

            if (method.getAnnotation(MasterdataKey.class) != null) {
                assert method.getReturnType().equals(String.class);
                assert method.getParameterCount() == 0;
                keyMethod = method;
            }

            if (method.getAnnotation(MasterdataGroup.class) != null) {
                assert method.getReturnType().equals(String.class);
                assert method.getParameterCount() == 0;
                groupMethod = method;
            }

            if (method.getAnnotation(MasterdataDescription.class) != null) {
                assert method.getReturnType().equals(String.class);
                assert method.getParameterCount() == 0;
                descMethod = method;
            }

            if (method.getAnnotation(MasterdataFilter1.class) != null) {
                assert method.getReturnType().equals(String.class);
                assert method.getParameterCount() == 0;
                filter1Method = method;
            }

            if (method.getAnnotation(MasterdataFilter2.class) != null) {
                assert method.getReturnType().equals(String.class);
                assert method.getParameterCount() == 0;
                filter2Method = method;
            }

            if (method.getAnnotation(MasterdataFilter3.class) != null) {
                assert method.getReturnType().equals(String.class);
                assert method.getParameterCount() == 0;
                filter3Method = method;
            }

            if (method.getAnnotation(MasterdataFilter4.class) != null) {
                assert method.getReturnType().equals(Long.class);
                assert method.getParameterCount() == 0;
                filter4Method = method;
            }

            if (method.getAnnotation(MasterdataFilter5.class) != null) {
                assert method.getReturnType().equals(Long.class);
                assert method.getParameterCount() == 0;
                filter5Method = method;
            }

            if (method.getAnnotation(MasterdataFilter6.class) != null) {
                assert method.getReturnType().equals(Long.class);
                assert method.getParameterCount() == 0;
                filter6Method = method;
            }

            if (method.getAnnotation(MafAppSpace.class) != null) {
                assert method.getReturnType().equals(Long.class);
                assert method.getParameterCount() == 0;
                mafAppSpaceMethod = method;
            }
        }
    }

    private boolean isSameType(Object instance) {
        return model.equals(instance.getClass());
    }

    /**
     * Returns the pool name for the instance.
     * <p>
     * When using a method to generate a dynamic pool name the arguments are passed using the poolNameMethodArguments
     * argument else they are ignored.
     *
     * @param instance to find its pool name
     * @param poolNameMethodArguments arguments for the pool name generator method in case it's defined in the mapping
     *                                class
     * @return pool name of the instance
     */
    String getPool(Object instance, Object... poolNameMethodArguments) {
        assert isSameType(instance);
        if (poolNameMethod != null)
            try {
                return (String) poolNameMethod.invoke(instance, poolNameMethodArguments);
            } catch (IllegalAccessException | InvocationTargetException e) {
                if(logger.isErrorEnabled()) {
                    logger.error("Error while invoking poolNameMethod", e);
                }
            }
        return pool;
    }

    String getKey(Object instance) {
        assert isSameType(instance);
        return getString(keyField, keyMethod, instance);
    }

    String getGroup(Object instance) {
        assert isSameType(instance);
        return getString(groupField, groupMethod, instance);
    }

    String getDescription(Object instance) {
        assert isSameType(instance);
        return getString(descField, descMethod, instance);
    }

    String getFilter1(Object instance) {
        assert isSameType(instance);
        return getString(filter1Field, filter1Method, instance);
    }

    String getFilter2(Object instance) {
        assert isSameType(instance);
        return getString(filter2Field, filter2Method, instance);
    }

    String getFilter3(Object instance) {
        assert isSameType(instance);
        return getString(filter3Field, filter3Method, instance);
    }

    Long getFilter4(Object instance) {
        assert isSameType(instance);
        return getLong(filter4Field, filter4Method, instance);
    }

    Long getFilter5(Object instance) {
        assert isSameType(instance);
        return getLong(filter5Field, filter5Method, instance);
    }

    Long getFilter6(Object instance) {
        assert isSameType(instance);
        return getLong(filter6Field, filter6Method, instance);
    }

    String getMafAppSpace(Object instance) {
        assert isSameType(instance);
        return getString(mafAppSpaceField, mafAppSpaceMethod, instance);
    }

    String getString(Field field, Method method, Object instance) {
        Object outValue;
        String out = null;
        if (field != null && instance != null) {
            try {
                if (field.isAccessible()) {
                    outValue = field.get(instance);
                    out = safeStringFromFieldValue(outValue);
                } else {
                    field.setAccessible(true);
                    outValue = field.get(instance);
                    out = safeStringFromFieldValue(outValue);
                    field.setAccessible(false);
                }
                if (outValue != null && out == null) {
                    if (logger.isErrorEnabled()) {
                        logger.error(String.format(
                                "Couldn't map field %s of instance type %s to a String. Current " +
                                        "field type is %s. Instance details:\n%s\n",
                                field.getName(), instance.getClass(), field.getDeclaringClass(),
                                instance.toString()));
                    }
                }
            } catch (IllegalAccessException e) {
                if (logger.isErrorEnabled()) {
                    logger.error(String.format("Illegal access to method %s", field.getName()), e);
                }

            }
        } else if (method != null && instance != null){
            try {
                if (method.isAccessible()) {
                    outValue = method.invoke(instance);
                    out = safeStringFromFieldValue(outValue);
                } else {
                    method.setAccessible(true);
                    outValue = method.invoke(instance);
                    out = safeStringFromFieldValue(outValue);
                    method.setAccessible(false);
                }
                if (outValue != null && out == null) {
                    if (logger.isErrorEnabled()) {
                        logger.error(String.format(
                                "Couldn't map method %s of instance type %s to a String. Current " +
                                        "method type is %s. Instance details:\n%s\n",
                                method.getName(), instance.getClass(), method.getDeclaringClass(),
                                instance.toString()));
                    }
                }
            } catch (IllegalAccessException e) {
                if (logger.isErrorEnabled()) {
                    logger.error(String.format("Illegal access to method %s", method.getName()), e);
                }

            } catch (InvocationTargetException e) {
                if (logger.isErrorEnabled()) {
                    logger.error(String.format("Invocation target exception to method %s",
                            method.getName()), e);
                }
            }
        }
        return out;
    }


    private String safeStringFromFieldValue(Object fieldValue) {
        if (fieldValue instanceof String) {
            return (String) fieldValue;
        }
        //In case we have a calendar return a String representation
        if (fieldValue instanceof Calendar) {
            //out = df.format(((Calendar) fieldValue).getTime()); //For MEL is better to pass long
            return String.valueOf(((Calendar) fieldValue).getTimeInMillis());
        }
        return null;
    }

    private Long getLong(Field field, Method method, Object instance) {
        Long out = null;
        if (field != null) {
            try {
                if (field.isAccessible()) {
                    out = (Long) field.get(instance);
                } else {
                    field.setAccessible(true);
                    out = (Long) field.get(instance);
                    field.setAccessible(false);
                }
            } catch (IllegalAccessException e) {
                if (logger.isErrorEnabled()) {
                    logger.error(String.format("Illegal access to field %s", field.getName()), e);
                }
            }
        } else if (method != null && instance != null){
            try {
                if (method.isAccessible()) {
                    out = (Long) method.invoke(instance);
                } else {
                    method.setAccessible(true);
                    out = (Long) method.invoke(instance);
                    method.setAccessible(false);
                }
            } catch (IllegalAccessException e) {
                if (logger.isErrorEnabled()) {
                    logger.error(String.format("Illegal access to method %s", method.getName()), e);
                }

            } catch (InvocationTargetException e) {
                if (logger.isErrorEnabled()) {
                    logger.error(String.format("Invocation target exception to method %s",
                            method.getName()), e);
                }
            }
        }
        return out;
    }



    protected Class<?> getModel() {
        return model;
    }

    String getPool() {
        return pool;
    }

    Field getKeyField() {
        return keyField;
    }

    Field getGroupField() {
        return groupField;
    }

    Field getDescField() {
        return descField;
    }

    Field getFilter1Field() {
        return filter1Field;
    }

    Field getFilter2Field() {
        return filter2Field;
    }

    Field getFilter3Field() {
        return filter3Field;
    }

    Field getFilter4Field() {
        return filter4Field;
    }

    Field getFilter5Field() {
        return filter5Field;
    }

    Field getFilter6Field() {
        return filter6Field;
    }

    Method getPoolNameMethod() {
        return poolNameMethod;
    }
}
