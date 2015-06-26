package com.movlizer.connector.v12.mapper.models;


import com.movilizer.connector.v12.annotations.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MasterdataPool
public class DynamicPoolModel {

    @MasterdataGroup
    public static final String group = "ALL";

    private static final String POOL_FORMAT = "my_customers_%s_%s";

    @MasterdataKey
    private String key;

    @MasterdataDescription
    private String desc;

    @MasterdataEntry
    private String entry1name;

    @MasterdataEntry(type = MasterdataEntry.Type.OBJECT)
    private MapperTestObjectDC testObject;

    @MasterdataEntry(type = MasterdataEntry.Type.OBJECT)
    private List<MapperTestSubObjectDC> list;

    @MasterdataEntry(type = MasterdataEntry.Type.OBJECT)
    private Map<String, MapperTestSubObjectDC> map;

    public DynamicPoolModel(String key) {
        this.key = key;
    }

    @MasterdataPoolNameGenerator
    public String getPool(String userId, String env) {
        return String.format(POOL_FORMAT, userId, env);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEntry1name() {
        return entry1name;
    }

    public void setEntry1name(String entry1name) {
        this.entry1name = entry1name;
    }

    public MapperTestObjectDC getTestObject() {
        return testObject;
    }

    public void setTestObject(MapperTestObjectDC testObject) {
        this.testObject = testObject;
    }

    public List<MapperTestSubObjectDC> getList() {
        return list;
    }

    public void setList(List<MapperTestSubObjectDC> list) {
        this.list = list;
    }

    public Map<String, MapperTestSubObjectDC> getMap() {
        return map;
    }

    public void setMap(Map<String, MapperTestSubObjectDC> map) {
        this.map = map;
    }

    public static DynamicPoolModel createTestObject(String key) {
        DynamicPoolModel object = new DynamicPoolModel(key);
        object.setDesc("descValue");
        object.setEntry1name("entryValue");
        object.setTestObject(MapperTestObjectDC.createTestObject());

        MapperTestSubObjectDC subObject = new MapperTestSubObjectDC();
        subObject.setStringField("test");
        Map<String, MapperTestSubObjectDC> map = new HashMap<String, MapperTestSubObjectDC>();
        map.put("testKey", subObject);
        object.setMap(map);

        List<MapperTestSubObjectDC> list = new ArrayList<MapperTestSubObjectDC>();
        list.add(subObject);
        object.setList(list);

        return object;
    }
}
