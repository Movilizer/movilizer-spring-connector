package com.movilizer.connector.mapper.direct;


import com.movilitas.movilizer.v14.MovilizerGenericDataContainer;
import com.movilitas.movilizer.v14.MovilizerUploadDataContainer;
import com.movilizer.connector.MovilizerConnectorAPI;
import com.movilizer.connector.exceptions.MovilizerParsingException;
import com.movilizer.connector.model.mapper.DCEvaluator;
import com.movilizer.connector.model.mapper.GenericDataContainerMapper;
import com.movilizer.connector.utils.DataContainerLogger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataContainerEvaluator {

    private static Log logger = LogFactory.getLog(DataContainerEvaluator.class);

    @Autowired
    MovilizerConnectorAPI serviceMov;

    @Autowired
    GenericDataContainerMapper mapper;

    @Autowired(required = false)
    private List<DCEvaluator<?>> evaluatorList;

    @Autowired
    private DataContainerLogger dataContainerLogger;

    @Scheduled(fixedDelayString = "${movilizer.polling-rate}")
    public void perform() {
        logger.debug("Perform the scheduled DataContainer evaluation.");
        List<MovilizerUploadDataContainer> dcList = serviceMov.getAllDataContainers();

        List<String> removeList = new ArrayList<String>();

        for (MovilizerUploadDataContainer container : dcList) {
            log(container);
            evaluate(container);
            removeList.add(container.getContainer().getKey());
        }
        serviceMov.setDataContainersAsProcessed(removeList);
    }

    private void log(MovilizerUploadDataContainer container) {
        if (container.getContainer() != null) {
            logger.debug("Evaluate a DC " + container.getContainer().getKey());
            dataContainerLogger.log(container);
        }
    }

    private void evaluate(MovilizerUploadDataContainer container) {
        if (evaluatorList != null && evaluatorList.isEmpty() == false) {
            for (DCEvaluator<?> evaluator : evaluatorList) {
                checkAndEvaluate(container, evaluator);
            }
        }
    }

    private boolean checkAndEvaluate(MovilizerUploadDataContainer container, DCEvaluator evaluator) {
    	MovilizerGenericDataContainer genericDataContainer = container.getContainer().getData();
        if (mapper.isType(genericDataContainer, evaluator.getMappingClass())) {
            try {
                Object object = mapper.fromDataContainer(genericDataContainer,
                        evaluator.getMappingClass());
                return evaluator.perform(object);
            } catch (MovilizerParsingException exception) {
                logger.error(
                        "Parsing of the data Container was not successfull. A parsing error occured. Container '" +
                                container.getContainer().getKey() + "' will be deleted.", exception);
            } catch (Exception exception) {
                logger.error("severe Error while parsing DC: " + container.getContainer().getKey(),
                        exception);
            }
        }
        return false;
    }

}
