package com.movlizer.connector.v12.mapper.models;


import com.movilizer.connector.v12.annotations.*;

@MasterdataPool("simple_pool")
public class SimpleBase64Model {

    @MasterdataGroup
    public static final String group = "ALL";

    @MasterdataKey
    private String key;

    @MasterdataDescription
    private String desc;

    @MasterdataFilter1
    @MasterdataEntry(name = "title")
    private String imageTitle;

    @MasterdataEntry(name = "image", type = MasterdataEntry.Type.BASE64)
    private byte[] imageData;

    public SimpleBase64Model(String key) {
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

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}
