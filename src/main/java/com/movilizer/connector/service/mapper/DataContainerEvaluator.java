package com.movilizer.connector.service.mapper;


import com.movilitas.movilizer.v14.MovilizerUploadDataContainer;
import com.movilizer.connector.exception.MovilizerParsingException;
import com.movilizer.connector.logging.DataContainerLogger;
import com.movilizer.connector.service.client.MovilizerClient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataContainerEvaluator {

    private static Log logger = LogFactory.getLog(DataContainerEvaluator.class);

    @Autowired
    MovilizerClient serviceMov;

    @Autowired
    DataContainerMapper mapper;

    @Autowired(required = false)
    private List<DCEvaluator<?>> evaluatorList;

    @Autowired
    private DataContainerLogger dataContainerLogger;

    @Scheduled(fixedDelayString = "${movilizer.dc.pollingRate}")
    public void perform() {
        logger.debug("Perform the scheduled DataContainer evaluation.");
        List<MovilizerUploadDataContainer> dcList = serviceMov.getAllDataContainers();

        for (MovilizerUploadDataContainer container : dcList) {
            log(container);
            evaluate(container);
        }
        serviceMov.setDataContainersAsProcessed(dcList);
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
        if (mapper.checkForClass(container, evaluator.getMappingClass())) {
            try {
                Object object = mapper.toObject(container.getContainer().getData(),
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
