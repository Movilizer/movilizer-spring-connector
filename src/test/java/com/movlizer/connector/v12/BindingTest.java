package com.movlizer.connector.v12;

import com.movilitas.movilizer.v12.MovilizerMovelet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Tests for jibx bindings config file binding.xml.
 *
 * @author Jesús de Mula Cano <jesus.demula@movilizer.com>
 * @version 0.1-SNAPSHOT, 2014.04.15
 * @since 0.1
 */

public class BindingTest {

    private static Log logger = LogFactory.getLog(BindingTest.class);

    private final String simpleMoveletXmlPath = "/test-movelets/test-movelet-simple.mxml";

    private final String meetingAgendaMoveletXmlPath = "/movelets/AgendaView.mxmlmxml";

    private final String salesforceUserAssignmentMoveletXmlPath = "/movelets/SalesforceUserAssignment.mxml";

    private JAXBContext context;

    private Unmarshaller unmarshaller;

    @Before
    public void before() throws Exception {
        context = JAXBContext.newInstance(MovilizerMovelet.class);
        unmarshaller = context.createUnmarshaller();
        ValidationEventHandler validationEventHandler = new ValidationEventHandler() {

            @Override
            public boolean handleEvent(ValidationEvent event) {
                logger.error("Error unmarshalling Movelet: " + event.getMessage());
                return true;
            }
        };
        unmarshaller.setEventHandler(validationEventHandler);
    }

    @After
    public void after() {
    }

    @Test
    public void testSimpleMoveletBinding() throws Exception {
        String xml = new String(Files.readAllBytes(Paths.get(getClass().getResource(
                simpleMoveletXmlPath).toURI())));

        JAXBElement<MovilizerMovelet> root = unmarshaller.unmarshal(new StreamSource(new File(
                getClass().getResource(simpleMoveletXmlPath).toURI())), MovilizerMovelet.class);
        MovilizerMovelet movelet = root.getValue();
    }

    @Test
    public void testMeetingAgendaMoveletBinding() throws Exception {
        String xml = new String(Files.readAllBytes(Paths.get(getClass().getResource(
                meetingAgendaMoveletXmlPath).toURI())));

        JAXBElement<MovilizerMovelet> root = unmarshaller.unmarshal(new StreamSource(new File(
                getClass().getResource(meetingAgendaMoveletXmlPath).toURI())), MovilizerMovelet.class);
        MovilizerMovelet movelet = root.getValue();
    }

    @Test
    public void testSalesforceUserAssignmentMoveletBinding() throws Exception {
        String xml = new String(Files.readAllBytes(Paths.get(getClass().getResource(
                salesforceUserAssignmentMoveletXmlPath).toURI())));

        JAXBElement<MovilizerMovelet> root = unmarshaller.unmarshal(new StreamSource(new File(
                        getClass().getResource(salesforceUserAssignmentMoveletXmlPath).toURI())),
                MovilizerMovelet.class);
        MovilizerMovelet movelet = root.getValue();
    }
}
