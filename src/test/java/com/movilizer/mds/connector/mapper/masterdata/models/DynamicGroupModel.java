package com.movilizer.mds.connector.mapper.masterdata.models;

import com.movilizer.mds.connector.mapper.masterdata.annotations.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MasterdataPool
public class DynamicGroupModel {

    @MasterdataKey
    private String key;

    @MasterdataDescription
    private String desc;

    private String entry1name;

    public DynamicGroupModel(String key) {
        this.key = key;
    }

    @MasterdataGroup
    public String getGroup(){
        return entry1name + desc;
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
}
