package com.movilizer.connector.java.jobs.processors;


import com.movilitas.movilizer.v12.MovilizerRequest;
import com.movilizer.connector.java.jobs.processors.upload.MasterdataProcessor;
import com.movilizer.connector.java.jobs.processors.upload.MoveletProcessor;
import com.movilizer.connector.java.jobs.processors.upload.ParticipantProcessor;
import com.movilizer.connector.java.model.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class UploadProcessor implements Processor<MovilizerRequest> {

    @Autowired
    @Qualifier("masterdataProcessor")
    private Processor<MovilizerRequest> masterdataProcessor;

    @Autowired
    @Qualifier("moveletProcessor")
    private Processor<MovilizerRequest> moveletProcessor;

    @Autowired
    @Qualifier("participantProcessor")
    private Processor<MovilizerRequest> participantProcessor;

    public void process(MovilizerRequest request) {
        masterdataProcessor.process(request);
        moveletProcessor.process(request);
        participantProcessor.process(request);
    }

}
