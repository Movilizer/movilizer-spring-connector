package com.movilizer.connector.java.mapper.annotated;

import com.movilitas.movilizer.v12.MovilizerGenericDataContainerEntry;
import com.movilizer.connector.java.annotations.datacontainer.DatacontainerEntry;
import com.movilizer.connector.java.annotations.masterdata.*;
import com.movilizer.connector.java.mapper.direct.GenericDataContainerMapperImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

public class MasterdataMapperCached {
/*
    private static Log logger = LogFactory.getLog(MasterdataMapperCached.class);

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

    private Method poolNameMethod;

    private Map<String, Field> entryFieldMap;

    private Map<String, DatacontainerEntry.Type> entryTypeMap;

    private SimpleDateFormat df;

    public MasterdataMapperCached(Class<?> model) {
        this.model = model;
        entryFieldMap = new HashMap<>();
        entryTypeMap = new HashMap<>();

        df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        MasterdataPool poolAnnotation = model.getAnnotation(MasterdataPool.class);
        assert poolAnnotation != null;
        pool = poolAnnotation.value();

        //loop vars
        DatacontainerEntry aEntryAnnotation;

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

            if (field.getAnnotation(DatacontainerEntry.class) != null) {
                aEntryAnnotation = field.getAnnotation(DatacontainerEntry.class);
                if (aEntryAnnotation.type() == DatacontainerEntry.Type.STRING) {
                    assert field.getType().equals(String.class);
                }
                if (aEntryAnnotation.type() == DatacontainerEntry.Type.BASE64) {
                    assert field.getType().equals(byte[].class);
                }
                entryFieldMap.put(
                        aEntryAnnotation.name().isEmpty() ? field.getName() : aEntryAnnotation.name(), field);
                entryTypeMap.put(
                        aEntryAnnotation.name().isEmpty() ? field.getName() : aEntryAnnotation.name(),
                        aEntryAnnotation.type());
            }
        }

        for (Method method : model.getDeclaredMethods()) {
            if (method.getAnnotation(MasterdataPoolNameGenerator.class) != null) {
                assert method.getReturnType().equals(String.class);
                poolNameMethod = method;
            }
        }
    }

    private boolean isSameType(Object instance) {
        return model.equals(instance.getClass());
    }

    public String getPool(Object instance, Object... vargs) {
        assert isSameType(instance);
        if (poolNameMethod != null)
            try {
                return (String) poolNameMethod.invoke(instance, vargs);
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error(e);
            }
        return pool;
    }

    public String getKey(Object instance) {
        assert isSameType(instance);
        return getString(keyField, instance);
    }

    public String getGroup(Object instance) {
        assert isSameType(instance);
        return getString(groupField, instance);
    }

    public String getDescription(Object instance) {
        assert isSameType(instance);
        return getString(descField, instance);
    }

    public String getFilter1(Object instance) {
        assert isSameType(instance);
        return getString(filter1Field, instance);
    }

    public String getFilter2(Object instance) {
        assert isSameType(instance);
        return getString(filter2Field, instance);
    }

    public String getFilter3(Object instance) {
        assert isSameType(instance);
        return getString(filter3Field, instance);
    }

    public Long getFilter4(Object instance) {
        assert isSameType(instance);
        return getLong(filter4Field, instance);
    }

    public Long getFilter5(Object instance) {
        assert isSameType(instance);
        return getLong(filter5Field, instance);
    }

    public Long getFilter6(Object instance) {
        assert isSameType(instance);
        return getLong(filter6Field, instance);
    }

    public List<MovilizerGenericDataContainerEntry> getData(Object instance) {
        assert isSameType(instance);
        List<MovilizerGenericDataContainerEntry> entries = new ArrayList<>();
        for (String name : entryFieldMap.keySet()) {
            if (entryTypeMap.containsKey(name)) {
                MovilizerGenericDataContainerEntry entry = new MovilizerGenericDataContainerEntry();
                entry.setName(name);
                switch (entryTypeMap.get(name)) {
                    case STRING:
                        entry.setValstr(getString(entryFieldMap.get(name), instance));
                        break;
                    case OBJECT:
                        Object fieldValue = getFieldFromObject(entryFieldMap.get(name), instance);
                        entry = new MasterDataObjectMapper().objectToContainer(fieldValue);
                        entry.setName(name);
                        break;
                    case BASE64:
                        entry.setValb64(getBase64(entryFieldMap.get(name), instance));
                        break;
                }
                entries.add(entry);
            }
        }
        return entries;
    }

    private String getString(Field field, Object instance) {
        Object fieldValue = null;
        String out = null;
        if (field != null && instance != null) {
            try {
                if (field.isAccessible()) {
                    fieldValue = field.get(instance);
                    out = safeStringFromFieldValue(fieldValue);
                } else {
                    field.setAccessible(true);
                    fieldValue = field.get(instance);
                    out = safeStringFromFieldValue(fieldValue);
                    field.setAccessible(false);
                }
                if (fieldValue != null && out == null) {
                    logger.error(String.format(
                            "Couldn't map field %s of instance type %s to a String. Current field type is %s. Instance details:\n%s\n",
                            field.getName(), instance.getClass(), field.getDeclaringClass(), instance.toString()));
                }
            } catch (IllegalAccessException e) {
                logger.error(e);
            }
        }
        return out;
    }

    private Object getFieldFromObject(Field field, Object instance) {
        Object fieldValue = null;
        if (field != null && instance != null) {
            try {
                if (field.isAccessible()) {
                    fieldValue = field.get(instance);
                } else {
                    field.setAccessible(true);
                    fieldValue = field.get(instance);
                    field.setAccessible(false);
                }
            } catch (IllegalAccessException e) {
                logger.error(e);
            }
        }
        return fieldValue;
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

    private Long getLong(Field field, Object instance) {
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
                logger.error(e);
            }
        }
        return out;
    }

    private byte[] getBase64(Field field, Object instance) {
        byte[] out = null;
        if (field != null) {
            try {
                if (field.isAccessible()) {
                    out = (byte[]) field.get(instance);
                } else {
                    field.setAccessible(true);
                    out = (byte[]) field.get(instance);
                    field.setAccessible(false);
                }
            } catch (IllegalAccessException e) {
                logger.error(e);
            }
        }
        return out;
    }

    protected Class<?> getModel() {
        return model;
    }

    protected String getPool() {
        return pool;
    }

    protected Field getKeyField() {
        return keyField;
    }

    protected Field getGroupField() {
        return groupField;
    }

    protected Field getDescField() {
        return descField;
    }

    protected Field getFilter1Field() {
        return filter1Field;
    }

    protected Field getFilter2Field() {
        return filter2Field;
    }

    protected Field getFilter3Field() {
        return filter3Field;
    }

    protected Field getFilter4Field() {
        return filter4Field;
    }

    protected Field getFilter5Field() {
        return filter5Field;
    }

    protected Field getFilter6Field() {
        return filter6Field;
    }

    protected Method getPoolNameMethod() {
        return poolNameMethod;
    }

    protected Map<String, Field> getEntryFieldMap() {
        return entryFieldMap;
    }

    protected Map<String, DatacontainerEntry.Type> getEntryTypeMap() {
        return entryTypeMap;
    }

    */
}
