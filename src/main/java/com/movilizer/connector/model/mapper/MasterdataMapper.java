package com.movilizer.connector.model.mapper;

import com.movilitas.movilizer.v15.MovilizerMasterdataUpdate;

public interface MasterdataMapper <T>{

    MovilizerMasterdataUpdate toMasterdata(T instanceOfObject);
}
