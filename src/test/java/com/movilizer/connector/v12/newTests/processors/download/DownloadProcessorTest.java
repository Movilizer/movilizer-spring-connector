package com.movilizer.connector.v12.newTests.processors.download;

import com.movilitas.movilizer.v12.*;
import com.movilizer.connector.java.jobs.processors.DownloadProcessor;
import com.movilizer.connector.java.jobs.processors.download.DatacontainerProcessor;
import com.movilizer.connector.java.model.Processor;
import com.movilizer.connector.java.persistence.entities.DatacontainerFromMovilizerQueue;
import com.movilizer.connector.java.queues.FromMovilizerQueueService;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.xml.datatype.DatatypeFactory;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


public class DownloadProcessorTest {
    //Test data
    private DownloadProcessor processor;
    private MovilizerResponse response;
    private final Long systemId = 1234L;
    private final String acknowledgeKey = "ack";

    private DatacontainerProcessor datacontainerProcessor;

    Processor<MovilizerMasterdataAck> masterdataAckProcesssor1;
    Processor<MovilizerMasterdataAck> masterdataAckProcesssor2;

    Processor<MovilizerMoveletDeleted> moveletDeletedProcessor;

    private List<String> masterdataAckProcesssor1Keys;
    private List<String> masterdataAckProcesssor2Keys;
    private List<String> moveletDeletedProcessorKeys;

    @Before
    public void before() throws Exception {
        processor = new DownloadProcessor();

        datacontainerProcessor = new DatacontainerProcessor();
        processor.setDatacontainerProcessor(datacontainerProcessor);

        //init proccessors
        masterdataAckProcesssor1Keys = new ArrayList<>();
        masterdataAckProcesssor1 = new Processor<MovilizerMasterdataAck>() {
            @Override
            public void process(MovilizerMasterdataAck ack) {
                masterdataAckProcesssor1Keys.add(ack.getKey());
            }
        };

        masterdataAckProcesssor2Keys = new ArrayList<>();
        masterdataAckProcesssor2 = new Processor<MovilizerMasterdataAck>() {
            @Override
            public void process(MovilizerMasterdataAck ack) {
                masterdataAckProcesssor2Keys.add(ack.getKey());
            }
        };

        moveletDeletedProcessorKeys = new ArrayList<>();
        moveletDeletedProcessor = new Processor<MovilizerMoveletDeleted>() {
            @Override
            public void process(MovilizerMoveletDeleted moveletDeleted) {
                moveletDeletedProcessorKeys.add(moveletDeleted.getMoveletKey());

            }
        };

        //register processors
        processor.registerProcessor(masterdataAckProcesssor1, MovilizerMasterdataAck.class);
        processor.registerProcessor(masterdataAckProcesssor2, MovilizerMasterdataAck.class);
        processor.registerProcessor(moveletDeletedProcessor, MovilizerMoveletDeleted.class);

        //prepare clean data
        response = new MovilizerResponse();
        response.setRequestAcknowledgeKey(acknowledgeKey);
        response.setSystemId(systemId);
    }

    @After
    public void after() {
    }


    @Test
    public void testProcessInDownloadProccessor() throws Exception {

        //add masterdata acks to response
        MovilizerMasterdataAck ack1 = new MovilizerMasterdataAck();
        ack1.setKey("ack1");
        MovilizerMasterdataAck ack2 = new MovilizerMasterdataAck();
        ack2.setKey("ack2");
        MovilizerMasterdataAck ack3 = new MovilizerMasterdataAck();
        ack3.setKey("ack3");
        response.getMasterdataAck().add(ack1);
        response.getMasterdataAck().add(ack2);
        response.getMasterdataAck().add(ack3);

        //add moveletDeleted to response
        MovilizerMoveletDeleted movilizerMoveletDeleted1 = new MovilizerMoveletDeleted();
        movilizerMoveletDeleted1.setMoveletKey("movilizerMoveletDeleted1");
        MovilizerMoveletDeleted movilizerMoveletDeleted2 = new MovilizerMoveletDeleted();
        movilizerMoveletDeleted2.setMoveletKey("movilizerMoveletDeleted2");
        response.getMoveletDeleted().add(movilizerMoveletDeleted1);
        response.getMoveletDeleted().add(movilizerMoveletDeleted2);

        processor.process(response);

        assertThat(masterdataAckProcesssor1Keys.size(), is(3));
        assertThat(masterdataAckProcesssor1Keys.get(0), is("ack1"));
        assertThat(masterdataAckProcesssor1Keys.get(1), is("ack2"));
        assertThat(masterdataAckProcesssor1Keys.get(2), is("ack3"));

        assertThat(masterdataAckProcesssor2Keys.size(), is(3));
        assertThat(masterdataAckProcesssor2Keys.get(0), is("ack1"));
        assertThat(masterdataAckProcesssor2Keys.get(1), is("ack2"));
        assertThat(masterdataAckProcesssor2Keys.get(2), is("ack3"));

        assertThat(moveletDeletedProcessorKeys.size(), is(2));
        assertThat(moveletDeletedProcessorKeys.get(0), is("movilizerMoveletDeleted1"));
        assertThat(moveletDeletedProcessorKeys.get(1), is("movilizerMoveletDeleted2"));



    }




}
