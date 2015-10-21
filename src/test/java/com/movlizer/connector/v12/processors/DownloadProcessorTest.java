package com.movlizer.connector.v12.processors;

import com.movilitas.movilizer.v12.*;
import com.movilizer.connector.java.persistence.entities.DatacontainerFromMovilizerQueue;
import com.movilizer.connector.java.jobs.processors.DownloadProcessor;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests for processing data coming from the Movilizer cloud.
 *
 * @author Jesús de Mula Cano <jesus.demula@movilizer.com>
 * @version 1.4-SNAPSHOT, 2014.12.31
 * @since 1.4
 */
public class DownloadProcessorTest {
/*
    //Test data
    private final Long systemId = 1234L;

    private final String password = "pass";

    private final String acknowledgeKey = "ack";

    private DownloadProcessor processor;

    private FromMovilizerQueueService fromMovilizerQueueService;

    private MovilizerResponse response;

    private MovilizerRequest request;

    private String creationTimestamp = "2001-07-12T12:08:56.435Z";

    private String syncTimestamp = "2001-07-14T12:09:56.435Z";

    private String imagePath = "/test-images/movilizer-logo.png";

    @Before
    public void before() throws Exception {
        processor = new DownloadProcessor();
        fromMovilizerQueueService = mock(FromMovilizerQueueService.class);
        processor.setFromMovilizerQueueService(fromMovilizerQueueService);

        //prepare clean data
        response = new MovilizerResponse();
        response.setRequestAcknowledgeKey(acknowledgeKey);
        response.setSystemId(systemId);

        request = new MovilizerRequest();
        request.setSystemId(systemId);
        request.setSystemPassword(password);
    }

    @After
    public void after() {
    }

    @Test
    public void testProcessNoDatacontainersInResponse() throws Exception {
        when(fromMovilizerQueueService.offer(any((new DatacontainerFromMovilizerQueue()).getClass()))).thenReturn(
                true);

        //pre-condition
        assertThat(response.getUploadContainer().isEmpty(), is(true));

        processor.process(response, request);

        assertThat(request.getRequestAcknowledgeKey(), is(acknowledgeKey));
        verify(fromMovilizerQueueService, never()).offer(
                any((new DatacontainerFromMovilizerQueue()).getClass()));
    }

    @Test
    public void testProcessOneDatacontainerInResponse() throws Exception {
        when(fromMovilizerQueueService.offer(any((new DatacontainerFromMovilizerQueue()).getClass()))).thenReturn(
                true);

        MovilizerUploadDataContainer uploadDataContainer = new MovilizerUploadDataContainer();
        MovilizerGenericUploadDataContainer genericUploadDataContainer = new MovilizerGenericUploadDataContainer();
        genericUploadDataContainer.setDeviceAddress("+9991986");
        genericUploadDataContainer.setMoveletKey("moveletKey");
        genericUploadDataContainer.setKey("datacontainerKey");
        genericUploadDataContainer.setMoveletVersion(1L);
        genericUploadDataContainer.setParticipantKey("participantKey");
        genericUploadDataContainer.setCreationTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(
                creationTimestamp));
        genericUploadDataContainer.setSyncTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(
                syncTimestamp));

        MovilizerGenericDataContainer dataContainer = new MovilizerGenericDataContainer();
        MovilizerGenericDataContainerEntry entry = new MovilizerGenericDataContainerEntry();
        entry.setName("entry1");
        entry.setValstr("val1");

        MovilizerGenericDataContainerEntry entry2 = new MovilizerGenericDataContainerEntry();
        entry2.setName("entry2");
        entry2.setValstr("val2");

        dataContainer.getEntry().add(entry);
        dataContainer.getEntry().add(entry2);

        genericUploadDataContainer.setData(dataContainer);

        uploadDataContainer.setContainer(genericUploadDataContainer);

        response.getUploadContainer().add(uploadDataContainer);
        //pre-condition
        assertThat(response.getUploadContainer().isEmpty(), is(false));
        assertThat(response.getUploadContainer().size(), is(1));

        processor.process(response, request);

        assertThat(request.getRequestAcknowledgeKey(), is(acknowledgeKey));
        verify(fromMovilizerQueueService, times(1)).offer(
                any((new DatacontainerFromMovilizerQueue()).getClass()));
    }

    @Test
    public void testProcessThreeDatacontainersInResponse() throws Exception {
        when(fromMovilizerQueueService.offer(any((new DatacontainerFromMovilizerQueue()).getClass()))).thenReturn(
                true);

        MovilizerUploadDataContainer uploadDataContainer = new MovilizerUploadDataContainer();
        MovilizerGenericUploadDataContainer genericUploadDataContainer = new MovilizerGenericUploadDataContainer();
        genericUploadDataContainer.setDeviceAddress("+9991986");
        genericUploadDataContainer.setMoveletKey("moveletKey");
        genericUploadDataContainer.setKey("datacontainerKey");
        genericUploadDataContainer.setMoveletVersion(1L);
        genericUploadDataContainer.setParticipantKey("participantKey");
        genericUploadDataContainer.setCreationTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(
                creationTimestamp));
        genericUploadDataContainer.setSyncTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(
                syncTimestamp));

        MovilizerGenericDataContainer dataContainer = new MovilizerGenericDataContainer();
        MovilizerGenericDataContainerEntry entry = new MovilizerGenericDataContainerEntry();
        entry.setName("entry1");
        entry.setValstr("val1");

        MovilizerGenericDataContainerEntry entry2 = new MovilizerGenericDataContainerEntry();
        entry2.setName("entry2");
        entry2.setValstr("val2");

        dataContainer.getEntry().add(entry);
        dataContainer.getEntry().add(entry2);

        genericUploadDataContainer.setData(dataContainer);

        uploadDataContainer.setContainer(genericUploadDataContainer);

        MovilizerUploadDataContainer uploadDataContainer2 = new MovilizerUploadDataContainer();
        MovilizerGenericUploadDataContainer genericUploadDataContainer2 = new MovilizerGenericUploadDataContainer();
        genericUploadDataContainer2.setDeviceAddress("+9991986001");
        genericUploadDataContainer2.setMoveletKey("moveletKey2");
        genericUploadDataContainer2.setKey("datacontainerKey2");
        genericUploadDataContainer2.setMoveletVersion(3L);
        genericUploadDataContainer2.setParticipantKey("participantKey2");
        genericUploadDataContainer2.setCreationTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(
                creationTimestamp));
        genericUploadDataContainer2.setSyncTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(
                syncTimestamp));

        MovilizerGenericDataContainer dataContainer2 = new MovilizerGenericDataContainer();
        MovilizerGenericDataContainerEntry entry3 = new MovilizerGenericDataContainerEntry();
        entry3.setName("entry3");
        entry3.setValstr("val3");

        MovilizerGenericDataContainerEntry entry4 = new MovilizerGenericDataContainerEntry();
        entry4.setName("entry4");
        entry4.setValstr("val4");

        dataContainer2.getEntry().add(entry3);
        dataContainer2.getEntry().add(entry4);

        genericUploadDataContainer2.setData(dataContainer2);

        uploadDataContainer2.setContainer(genericUploadDataContainer2);

        response.getUploadContainer().add(uploadDataContainer);
        response.getUploadContainer().add(uploadDataContainer2);
        //pre-condition
        assertThat(response.getUploadContainer().isEmpty(), is(false));
        assertThat(response.getUploadContainer().size(), is(2));

        processor.process(response, request);

        assertThat(request.getRequestAcknowledgeKey(), is(acknowledgeKey));
        verify(fromMovilizerQueueService, times(2)).offer(
                any((new DatacontainerFromMovilizerQueue()).getClass()));
    }

    @Test
    public void testProcessDatacontainerWithImageInResponse() throws Exception {
        when(fromMovilizerQueueService.offer(any((new DatacontainerFromMovilizerQueue()).getClass()))).thenReturn(
                true);

        MovilizerUploadDataContainer uploadDataContainer = new MovilizerUploadDataContainer();
        MovilizerGenericUploadDataContainer genericUploadDataContainer = new MovilizerGenericUploadDataContainer();
        genericUploadDataContainer.setDeviceAddress("+9991986");
        genericUploadDataContainer.setMoveletKey("moveletKey");
        genericUploadDataContainer.setKey("datacontainerKey");
        genericUploadDataContainer.setMoveletVersion(1L);
        genericUploadDataContainer.setParticipantKey("participantKey");
        genericUploadDataContainer.setCreationTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(
                creationTimestamp));
        genericUploadDataContainer.setSyncTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(
                syncTimestamp));

        MovilizerGenericDataContainer dataContainer = new MovilizerGenericDataContainer();
        MovilizerGenericDataContainerEntry entry = new MovilizerGenericDataContainerEntry();
        entry.setName("entry1");
        entry.setValstr("val1");

        assertThat("Test image missing.", getClass().getResource(imagePath), is(notNullValue()));
        byte[] entry2Value = getImageFromPath(imagePath);

        MovilizerGenericDataContainerEntry entry2 = new MovilizerGenericDataContainerEntry();
        entry2.setName("entry2");
        entry2.setValb64(entry2Value);

        dataContainer.getEntry().add(entry);
        dataContainer.getEntry().add(entry2);

        genericUploadDataContainer.setData(dataContainer);

        uploadDataContainer.setContainer(genericUploadDataContainer);

        response.getUploadContainer().add(uploadDataContainer);
        //pre-condition
        assertThat(response.getUploadContainer().isEmpty(), is(false));
        assertThat(response.getUploadContainer().size(), is(1));

        processor.process(response, request);

        assertThat(request.getRequestAcknowledgeKey(), is(acknowledgeKey));
        verify(fromMovilizerQueueService, times(1)).offer(
                any((new DatacontainerFromMovilizerQueue()).getClass()));
    }

    @Test
    @Ignore
    public void testProcessDuplciateDatacontainerInResponse() throws Exception {
        when(fromMovilizerQueueService.offer(any((new DatacontainerFromMovilizerQueue()).getClass()))).thenReturn(
                false);
        //TODO finish his test
    }

    @Test
    @Ignore
    public void testNotAcknowledgeDatacontainersWhenError() throws Exception {
        when(fromMovilizerQueueService.offer(any((new DatacontainerFromMovilizerQueue()).getClass()))).thenThrow(
                new Exception());
        //TODO finish his test
    }

    // ------------------------------------------------------------------------------------------------------------ Util
    private byte[] getImageFromPath(String imagePath) throws IOException {
        BufferedImage img = ImageIO.read(getClass().getResource(imagePath));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        ImageIO.write(img, "png", bos);
        byte[] imgBytes = bos.toByteArray();
        bos.close();
        return imgBytes;
    }
    */
}
