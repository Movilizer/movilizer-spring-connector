package com.movilizer.connector.mapper.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class MapperTestObjectDC {

    private String stringField;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private GregorianCalendar dateField;

    private int intField;

    private boolean booleanField;

    private MapperTestSubObjectDC objectField;

    private List<MapperTestSubObjectDC> objectFieldList;

    public String getStringField() {
        return stringField;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }

    public GregorianCalendar getDateField() {
        return dateField;
    }

    public void setDateField(GregorianCalendar dateField) {
        this.dateField = dateField;
    }

    public int getIntField() {
        return intField;
    }

    public void setIntField(int intField) {
        this.intField = intField;
    }

    public boolean isBooleanField() {
        return booleanField;
    }

    public void setBooleanField(boolean booleanField) {
        this.booleanField = booleanField;
    }

    public MapperTestSubObjectDC getObjectField() {
        return objectField;
    }

    public void setObjectField(MapperTestSubObjectDC objectField) {
        this.objectField = objectField;
    }

    public List<MapperTestSubObjectDC> getObjectFieldList() {
        return objectFieldList;
    }

    public void setObjectFieldList(List<MapperTestSubObjectDC> objectFieldList) {
        this.objectFieldList = objectFieldList;
    }

    public static MapperTestObjectDC createTestObject() {
        MapperTestObjectDC object = new MapperTestObjectDC();
        object.setBooleanField(true);
        object.setIntField(100);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(2015, 0, 1);
        object.setDateField(calendar);
        MapperTestSubObjectDC subObject = new MapperTestSubObjectDC();
        subObject.setStringField("test");
        object.setObjectField(subObject);
        List<MapperTestSubObjectDC> list = new ArrayList<MapperTestSubObjectDC>();
        list.add(subObject);
        object.setObjectFieldList(list);

        return object;
    }

}
