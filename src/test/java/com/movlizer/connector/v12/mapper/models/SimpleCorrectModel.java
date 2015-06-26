package com.movlizer.connector.v12.mapper.models;


import com.movilizer.connector.v12.annotations.*;

@MasterdataPool("simple_pool")
public class SimpleCorrectModel {

    @MasterdataGroup
    public static final String group = "ALL";

    @MasterdataKey
    private String key;

    @MasterdataDescription
    private String desc;

    @MasterdataFilter1
    private String filter1name;

    @MasterdataFilter2
    private String filter2name;

    @MasterdataFilter3
    private String filter3name;

    @MasterdataFilter4
    private Long filter4name;

    @MasterdataFilter5
    private Long filter5name;

    @MasterdataFilter6
    private Long filter6name;

    @MasterdataEntry
    private String entry1name;

    @MasterdataEntry(name = "entry2Annotation")
    private String entry2name;

    public SimpleCorrectModel(String key) {
        this.key = key;
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

    public String getFilter1name() {
        return filter1name;
    }

    public void setFilter1name(String filter1name) {
        this.filter1name = filter1name;
    }

    public String getFilter2name() {
        return filter2name;
    }

    public void setFilter2name(String filter2name) {
        this.filter2name = filter2name;
    }

    public String getFilter3name() {
        return filter3name;
    }

    public void setFilter3name(String filter3name) {
        this.filter3name = filter3name;
    }

    public Long getFilter4name() {
        return filter4name;
    }

    public void setFilter4name(Long filter4name) {
        this.filter4name = filter4name;
    }

    public Long getFilter5name() {
        return filter5name;
    }

    public void setFilter5name(Long filter5name) {
        this.filter5name = filter5name;
    }

    public Long getFilter6name() {
        return filter6name;
    }

    public void setFilter6name(Long filter6name) {
        this.filter6name = filter6name;
    }

    public String getEntry1name() {
        return entry1name;
    }

    public void setEntry1name(String entry1name) {
        this.entry1name = entry1name;
    }

    public String getEntry2name() {
        return entry2name;
    }

    public void setEntry2name(String entry2name) {
        this.entry2name = entry2name;
    }
}
