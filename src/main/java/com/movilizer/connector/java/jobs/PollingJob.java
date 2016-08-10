package com.movilizer.connector.java.jobs;


import com.movilitas.movilizer.v14.MovilizerRequest;
import com.movilitas.movilizer.v14.MovilizerResponse;
import com.movilizer.connector.java.model.Processor;
import com.movilizer.connector.java.model.MovilizerCallback;
import com.movilizer.connector.java.jobs.processors.ErrorsProcessor;
import com.movilizer.connector.java.jobs.processors.DownloadProcessor;
import com.movilizer.connector.java.jobs.processors.UploadProcessor;

import com.movilizer.mds.webservice.services.MovilizerDistributionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
public class PollingJob {
    private static Log logger = LogFactory.getLog(PollingJob.class);

    @Value("${movilizer.auth.systemId}")
    private Long systemId;

    @Value("${movilizer.auth.password}")
    private String password;

    @Value("${movilizer.max-items}")
    private Integer maxItemsToDownload;

    private MovilizerDistributionService movilizer;
    private DownloadProcessor dataProcessor;
    private ErrorsProcessor errorsProcessor;
    private UploadProcessor uploadProcessor;

    private MovilizerResponse lastResponse;
    private MovilizerRequest currentRequest;
    private Calendar lastSync;
    private List<MovilizerCallback> callbacks;

    @Autowired
    public PollingJob(MovilizerDistributionService movilizer, DownloadProcessor dataProcessor,
                      ErrorsProcessor errorsProcessor, UploadProcessor uploadProcessor) {
        this.movilizer = movilizer;
        this.dataProcessor = dataProcessor;
        this.errorsProcessor = errorsProcessor;
        this.uploadProcessor = uploadProcessor;
        callbacks = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        generateCleanRequest();
    }

    //This is performed in other thread than the main execution
    @Scheduled(fixedDelayString = "${movilizer.polling-rate}", initialDelay = 1000)
    private void performSyncToCloud() {
        logger.info(String.format("Performing cloud sync for systemId %d", systemId));
        if (currentRequest == null)
            generateCleanRequest();
        //Perform task to gather the data to send to the cloud first (movelets, assignment, masterdata)
        uploadProcessor.process(currentRequest);
        lastResponse = movilizer.getReplyFromCloudSync(currentRequest);
        lastSync = Calendar.getInstance();
        generateCleanRequest();
        errorsProcessor.process(lastResponse);
        dataProcessor.process(lastResponse);

        //Execute callbacks
        for (MovilizerCallback callback : callbacks) {
            callback.execute();
        }
        // Beware that any errors during the callbacks or processors will not acknowledge the last response!
        currentRequest.setRequestAcknowledgeKey(lastResponse.getRequestAcknowledgeKey());
    }

    public void registerCallback(MovilizerCallback callback) {
        callbacks.add(callback);
    }

    public <T> void registerDownloadProcessor(Processor<T> processor, Class<T> processorClass) {
        dataProcessor.registerProcessor(processor, processorClass);
    }

    private void generateCleanRequest() {
        currentRequest = movilizer.prepareDownloadRequest(systemId, password, maxItemsToDownload, new MovilizerRequest());
    }

    public Calendar getLastSync() {
        return lastSync;
    }

    public Long getSystemId() {
        return systemId;
    }

    public String getPassword() {
        return password;
    }
}
