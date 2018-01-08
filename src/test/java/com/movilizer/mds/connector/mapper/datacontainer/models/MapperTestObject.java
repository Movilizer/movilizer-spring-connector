package com.movilizer.mds.connector.mapper.datacontainer.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MapperTestObject {

    private String stringField;
    private int intField;
    private boolean booleanField;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private GregorianCalendar dateField;
    private MapperTestSubObject objectField;
    private List<MapperTestSubObject> objectFieldList;
    private byte[] image;

    public MapperTestObject() {
    }


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

    public MapperTestSubObject getObjectField() {
        return objectField;
    }

    public void setObjectField(MapperTestSubObject objectField) {
        this.objectField = objectField;
    }

    public List<MapperTestSubObject> getObjectFieldList() {
        return objectFieldList;
    }

    public void setObjectFieldList(List<MapperTestSubObject> objectFieldList) {
        this.objectFieldList = objectFieldList;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public static MapperTestObject createTestObject() {
        MapperTestObject testObject = new MapperTestObject();
        testObject.setBooleanField(true);
        testObject.setIntField(100);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(2015, Calendar.JANUARY, 1);
        testObject.setDateField(calendar);

        MapperTestSubObject subObject1 = new MapperTestSubObject();
        subObject1.setStringField("test");
        testObject.setObjectField(subObject1);

        MapperTestSubObject subObject2 = new MapperTestSubObject();
        subObject2.setStringField("test");
        subObject2.setIntegerField(1);

        List<MapperTestSubObject> list = new ArrayList<>();
        list.add(subObject1);
        list.add(subObject2);
        testObject.setObjectFieldList(list);
        testObject.setStringField("test");

        testObject.setImage(new byte[]{'a', 'b'});

        return testObject;
    }

}
