package com.movilizer.connector.java.model.mapper;

import com.movilitas.movilizer.v12.MovilizerMasterdataUpdate;

public interface MasterdataMapper {
    <T> MovilizerMasterdataUpdate toMasterdata(T instanceOfObject);
}
