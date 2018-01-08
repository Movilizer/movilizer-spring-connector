package com.movilizer.mds.connector.mapper.masterdata;


import com.movilitas.movilizer.v15.*;
import com.movilizer.mds.connector.MovilizerMetricService;
import com.movilizer.mds.connector.mapper.datacontainer.GenericDataContainerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MasterdataMapper {

    private final Map<Class, MasterdataMapperCached> mapperInstance;
    private final MovilizerMetricService metricService;
    private final GenericDataContainerMapper dataContainerMapper;

    @Autowired
    public MasterdataMapper(MovilizerMetricService metricService, GenericDataContainerMapper dataContainerMapper) {
        this.metricService = metricService;
        this.dataContainerMapper = dataContainerMapper;
        this.mapperInstance = new HashMap<>();
    }

    /**
     * Generates the corresponding pool update given the action (UPDATE, DELETE or REFERENCE), the model instances to
     * modify and the
     * <p>
     * The model instances MUST belong to the same class or else the masterdata pool could not be recognized and the
     * mapper will throw a IllegalArgumentException at runtime.
     *
     * @param modelInstanceList containing the instances to modify in the Movilizer cloud
     * @param action UPDATE, DELETE or REFERENCE
     * @param poolNameMethodArguments arguments passed for the dynamic pool naming
     * @return a collection of MovilizerMasterdataPoolUpdate that can be used in a MovilizerRequest
     */
    public Collection<MovilizerMasterdataPoolUpdate> getMasterdata(List<?> modelInstanceList,
                                                                   Action action, Object... poolNameMethodArguments) {
        if (modelInstanceList.isEmpty())
            return null;
        Class<?> modelClass = getModelClass(modelInstanceList);
        MasterdataMapperCached masterdataMapperCached = getOrCreateMasterdataMapperCached(modelClass);

        Map<String, MovilizerMasterdataPoolUpdate> updates = new HashMap<String, MovilizerMasterdataPoolUpdate>();

        switch (action) {
            case UPDATE:
                for (Object modelInstance : modelInstanceList) {
                    assertSameClass(modelClass, modelInstance);

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
                    masterdataUpdate.setMafAppSpace(masterdataMapperCached.getMafAppSpace(modelInstance));
                    MovilizerGenericDataContainer dataContainer = dataContainerMapper.toDataContainer(modelInstance);
                    masterdataUpdate.setData(dataContainer);

                    String poolName = masterdataMapperCached.getPool(modelInstance, poolNameMethodArguments);
                    List<MovilizerMasterdataUpdate> poolUpdates = getPoolUpdateForPool(updates, poolName).getUpdate();
                    poolUpdates.add(masterdataUpdate);
                }
                break;
            case DELETE:
                for (Object modelInstance : modelInstanceList) {
                    assertSameClass(modelClass, modelInstance);

                    MovilizerMasterdataDelete masterdataDelete = new MovilizerMasterdataDelete();
                    masterdataDelete.setKey(masterdataMapperCached.getKey(modelInstance));
                    masterdataDelete.setGroup(masterdataMapperCached.getGroup(modelInstance));

                    List<MovilizerMasterdataDelete> poolDeletes = getPoolUpdateForPool(updates,
                            masterdataMapperCached.getPool(modelInstance, poolNameMethodArguments)).getDelete();
                    poolDeletes.add(masterdataDelete);
                }
                break;
            case REFERENCE:
                for (Object modelInstance : modelInstanceList) {
                    assertSameClass(modelClass, modelInstance);

                    MovilizerMasterdataReference masterdataReference = new MovilizerMasterdataReference();
                    masterdataReference.setKey(masterdataMapperCached.getKey(modelInstance));
                    masterdataReference.setGroup(masterdataMapperCached.getGroup(modelInstance));

                    List<MovilizerMasterdataReference> poolReferences = getPoolUpdateForPool(updates,
                            masterdataMapperCached.getPool(modelInstance, poolNameMethodArguments)).getReference();
                    poolReferences.add(masterdataReference);
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

    private Class<?> getModelClass(List<?> modelInstanceList) {
        return modelInstanceList.get(0).getClass();
    }

    MasterdataMapperCached getOrCreateMasterdataMapperCached(Class<?> modelClass) {
        if (!mapperInstance.containsKey(modelClass))
            mapperInstance.put(modelClass, new MasterdataMapperCached(
                    modelClass));
        return mapperInstance.get(modelClass);
    }

    private void assertSameClass(Class<?> clazz, Object instance) {
        if(!clazz.equals(instance.getClass())) {
            throw new IllegalArgumentException("The list of model instances should belong to the same model/class. " +
                    "Expected: " + clazz.getSimpleName() + ", found: " + instance.getClass().getSimpleName());
        }
    }

    public enum Action {
        UPDATE, DELETE, REFERENCE
    }
}
