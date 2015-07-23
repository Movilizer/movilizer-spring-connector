package com.movilizer.connector.v12.service.processors;

import com.movilitas.movilizer.v14.MovilizerResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class AckProcessor implements Processor<MovilizerResponse> {

    private static Log logger = LogFactory.getLog(AckProcessor.class);

    public AckProcessor() {
    }

    @Override
    public void process(MovilizerResponse response) {
        //TODO check acks for movelet creation, sync, deletion, etc
    }
}
