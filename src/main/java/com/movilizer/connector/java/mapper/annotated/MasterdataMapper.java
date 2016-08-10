package com.movilizer.connector.java.mapper.annotated;


import com.movilitas.movilizer.v14.*;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Component
public class MasterdataMapper {
/*
    private Map<Class, MasterdataMapperCached> mapperInstance;

    public MasterdataMapper() {
        this.mapperInstance = new HashMap<>();
    }

    public Collection<MovilizerMasterdataPoolUpdate> getMasterdata(List<?> modelInstanceList,
                                                                   Action action, Object... vargs) {
        if (modelInstanceList.isEmpty())
            return null;
        MasterdataMapperCached masterdataMapperCached = getOrCreateMasterdataMapperCached(modelInstanceList);

        Map<String, MovilizerMasterdataPoolUpdate> updates = new HashMap<String, MovilizerMasterdataPoolUpdate>();

        switch (action) {
            case UPDATE:
                for (Object modelInstance : modelInstanceList) {
                    MovilizerMasterdataUpdate masterdataUpdate = new MovilizerMasterdataUpdate();
                    masterdataUpdate.setGroup(masterdataMapperCached.getGroup(modelInstance));
                    masterdataUpdate.setKey(masterdataMapperCached.getKey(modelInstance));
                    masterdataUpdate.setDescription(masterdataMapperCached.getDescription(modelInstance));
                    masterdataUpdate.setFilter1(masterdataMapperCached.getFilter1(modelInstance));
                    masterdataUpdate.setFilter2(masterdataMapperCached.getFilter2(modelInstance));
                    masterdataUpdate.setFilter3(masterdataMapperCached.getFilter3(modelInstance));
                    masterdataUpdate.setFilter4(masterdataMapperCached.getFilter4(modelInstance));
                    masterdataUpdate.setFilter5(masterdataMapperCached.getFilter5(modelInstance));
                    masterdataUpdate.setFilter6(masterdataMapperCached.getFilter6(modelInstance));
                    MovilizerGenericDataContainer dataContainer = new MovilizerGenericDataContainer();
                    dataContainer.getEntry().addAll(masterdataMapperCached.getData(modelInstance));
                    masterdataUpdate.setData(dataContainer);
                    getPoolUpdateForPool(updates, masterdataMapperCached.getPool(modelInstance, vargs)).getUpdate().add(
                            masterdataUpdate);
                }
                break;
            case DELETE:
                for (Object modelInstance : modelInstanceList) {
                    MovilizerMasterdataDelete masterdataDelete = new MovilizerMasterdataDelete();
                    masterdataDelete.setKey(masterdataMapperCached.getKey(modelInstance));
                    getPoolUpdateForPool(updates, masterdataMapperCached.getPool(modelInstance, vargs)).getDelete().add(
                            masterdataDelete);
                }
                break;
            case REFERENCE:
                for (Object modelInstance : modelInstanceList) {
                    MovilizerMasterdataReference masterdataReference = new MovilizerMasterdataReference();
                    masterdataReference.setGroup(masterdataMapperCached.getGroup(modelInstance));
                    masterdataReference.setKey(masterdataMapperCached.getKey(modelInstance));
                }
                break;
        }
        return updates.values();
    }

    private MovilizerMasterdataPoolUpdate getPoolUpdateForPool(
            Map<String, MovilizerMasterdataPoolUpdate> updates, String pool) {
        MovilizerMasterdataPoolUpdate masterdataPoolUpdate = updates.get(pool);
        if (masterdataPoolUpdate == null) {
            masterdataPoolUpdate = new MovilizerMasterdataPoolUpdate();
            masterdataPoolUpdate.setPool(pool);
            updates.put(pool, masterdataPoolUpdate);
        }
        return masterdataPoolUpdate;
    }

    protected MasterdataMapperCached getOrCreateMasterdataMapperCached(List<?> modelInstanceList) {
        if (!mapperInstance.containsKey(modelInstanceList.get(0).getClass()))
            mapperInstance.put(modelInstanceList.get(0).getClass(), new MasterdataMapperCached(
                    modelInstanceList.get(0).getClass()));
        return mapperInstance.get(modelInstanceList.get(0).getClass());
    }

    public enum Action {
        UPDATE, DELETE, REFERENCE
    }
    */
}
