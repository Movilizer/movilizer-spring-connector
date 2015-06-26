package com.movilizer.connector.v12.service;


import com.movilitas.movilizer.v12.*;
import com.movilizer.connector.v12.exception.MovilizerException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

@Component
public class MovilizerXMLParserServiceV12 {

    private static Log logger = LogFactory.getLog(MovilizerXMLParserServiceV12.class);

    private JAXBContext moveletContext;

    private Unmarshaller moveletUnmarshaller;

    private Marshaller moveletMarshaller;

    private JAXBContext datacontainerContext;

    private Unmarshaller datacontainerUnmarshaller;

    private Marshaller datacontainerMarshaller;

    private JAXBContext masterdataDeleteContext;

    private Unmarshaller masterdataDeleteUnmarshaller;

    private Marshaller masterdataDeleteMarshaller;

    private JAXBContext masterdataUpdateContext;

    private Unmarshaller masterdataUpdateUnmarshaller;

    private Marshaller masterdataUpdateMarshaller;

    private JAXBContext masterdataReferenceContext;

    private Unmarshaller masterdataReferenceUnmarshaller;

    private Marshaller masterdataReferenceMarshaller;

    @PostConstruct
    public void init() throws JAXBException {
        moveletContext = JAXBContext.newInstance(MovilizerMovelet.class);
        moveletMarshaller = moveletContext.createMarshaller();
        moveletUnmarshaller = moveletContext.createUnmarshaller();
        //        ValidationEventHandler validationEventHandler = new javax.xml.bind.helpers.DefaultValidationEventHandler();
        moveletUnmarshaller.setEventHandler(new ValidationEventHandler() {

            @Override
            public boolean handleEvent(ValidationEvent event) {
                logger.error("Error unmarshalling Movelet: " + event.getMessage());
                return true;
            }
        });

        datacontainerContext = JAXBContext.newInstance(MovilizerUploadDataContainer.class);
        datacontainerMarshaller = datacontainerContext.createMarshaller();
        datacontainerUnmarshaller = datacontainerContext.createUnmarshaller();
        datacontainerUnmarshaller.setEventHandler(new ValidationEventHandler() {

            @Override
            public boolean handleEvent(ValidationEvent event) {
                logger.error("Error unmarshalling datacontainer: " + event.getMessage());
                return true;
            }
        });

        masterdataDeleteContext = JAXBContext.newInstance(MovilizerMasterdataDelete.class);
        masterdataDeleteMarshaller = masterdataDeleteContext.createMarshaller();
        masterdataDeleteUnmarshaller = masterdataDeleteContext.createUnmarshaller();
        masterdataDeleteUnmarshaller.setEventHandler(new ValidationEventHandler() {

            @Override
            public boolean handleEvent(ValidationEvent event) {
                logger.error("Error unmarshalling masterdata delete: " + event.getMessage());
                return true;
            }
        });

        masterdataUpdateContext = JAXBContext.newInstance(MovilizerMasterdataUpdate.class);
        masterdataUpdateMarshaller = masterdataUpdateContext.createMarshaller();
        masterdataUpdateUnmarshaller = masterdataUpdateContext.createUnmarshaller();
        masterdataUpdateUnmarshaller.setEventHandler(new ValidationEventHandler() {

            @Override
            public boolean handleEvent(ValidationEvent event) {
                logger.error("Error unmarshalling masterdata udpate: " + event.getMessage());
                return true;
            }
        });

        masterdataReferenceContext = JAXBContext.newInstance(MovilizerMasterdataReference.class);
        masterdataReferenceMarshaller = masterdataReferenceContext.createMarshaller();
        masterdataReferenceUnmarshaller = masterdataReferenceContext.createUnmarshaller();
        masterdataReferenceUnmarshaller.setEventHandler(new ValidationEventHandler() {

            @Override
            public boolean handleEvent(ValidationEvent event) {
                logger.error("Error unmarshalling masterdata reference: " + event.getMessage());
                return true;
            }
        });
    }

    public MovilizerMovelet getMoveletFromFile(String filePath) {
        JAXBElement<MovilizerMovelet> root;
        try {
            URL fullPath = getClass().getResource(filePath);
            if (fullPath == null)
                throw new MovilizerException(String.format("File not found for path %s", filePath));
            root = moveletUnmarshaller.unmarshal(new StreamSource(new File(fullPath.toURI())),
                    MovilizerMovelet.class);
            return root.getValue();
        } catch (URISyntaxException e) {
            throw new MovilizerException(String.format("Malformed path %s", filePath));
        } catch (JAXBException e) {
            throw new MovilizerException(String.format("Error parsing Movelet in file %s", filePath));
        }
    }

    public MovilizerMovelet getMovelet(byte[] serializedMovelet) {
        JAXBElement<MovilizerMovelet> root;
        try {
            if (serializedMovelet == null)
                throw new MovilizerException("Unable to parse a MovilizerMovelet from an null string");
            root = moveletUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(
                    serializedMovelet)), MovilizerMovelet.class);
            return root.getValue();
        } catch (JAXBException e) {
            logger.error(e);
        }
        return null;
    }

    public byte[] serializeMovelet(MovilizerMovelet dataContainer) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (dataContainer != null) {
            try {
                moveletMarshaller.marshal(new JAXBElement<>(new QName("uri", "local"),
                        MovilizerMovelet.class, dataContainer), outputStream);
            } catch (JAXBException e) {
                logger.error(e);
            }
        }
        return outputStream.toByteArray();
    }

    public MovilizerUploadDataContainer getUploadDataContainer(byte[] serializedDatacontainer) {
        JAXBElement<MovilizerUploadDataContainer> root;
        try {
            if (serializedDatacontainer == null)
                throw new MovilizerException(
                        "Unable to parse a MovilizerUploadDataContainer from an null string");
            root = datacontainerUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(
                    serializedDatacontainer)), MovilizerUploadDataContainer.class);
            return root.getValue();
        } catch (JAXBException e) {
            logger.error(e);
        }
        return null;
    }

    public byte[] serializeUploadDataContainer(MovilizerUploadDataContainer dataContainer) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (dataContainer != null) {
            try {
                datacontainerMarshaller.marshal(new JAXBElement<>(new QName("uri", "local"),
                        MovilizerUploadDataContainer.class, dataContainer), outputStream);
            } catch (JAXBException e) {
                logger.error(e);
            }
        }
        return outputStream.toByteArray();
    }

    public MovilizerMasterdataDelete getMasterdataDelete(byte[] serializedMasterdataDelete) {
        JAXBElement<MovilizerMasterdataDelete> root;
        try {
            if (serializedMasterdataDelete == null)
                throw new MovilizerException(
                        "Unable to parse a MovilizerMasterdataDelete from an null string");
            root = masterdataDeleteUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(
                    serializedMasterdataDelete)), MovilizerMasterdataDelete.class);
            return root.getValue();
        } catch (JAXBException e) {
            logger.error(e);
        }
        return null;
    }

    public byte[] serializeMasterdataDelete(MovilizerMasterdataDelete masterdataDelete) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (masterdataDelete != null) {
            try {
                masterdataDeleteMarshaller.marshal(new JAXBElement<>(new QName("uri", "local"),
                        MovilizerMasterdataDelete.class, masterdataDelete), outputStream);
            } catch (JAXBException e) {
                logger.error(e);
            }
        }
        return outputStream.toByteArray();
    }

    public MovilizerMasterdataUpdate getMasterdataUpdate(byte[] serializedMasterdataUpdate) {
        JAXBElement<MovilizerMasterdataUpdate> root;
        try {
            if (serializedMasterdataUpdate == null)
                throw new MovilizerException(
                        "Unable to parse a MovilizerMasterdataUpdate from an null string");
            root = masterdataUpdateUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(
                    serializedMasterdataUpdate)), MovilizerMasterdataUpdate.class);
            return root.getValue();
        } catch (JAXBException e) {
            logger.error(e);
        }
        return null;
    }

    public byte[] serializeMasterdataUpdate(MovilizerMasterdataUpdate masterdataUpdate) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (masterdataUpdate != null) {
            try {
                masterdataUpdateMarshaller.marshal(new JAXBElement<>(new QName("uri", "local"),
                        MovilizerMasterdataUpdate.class, masterdataUpdate), outputStream);
            } catch (JAXBException e) {
                logger.error(e);
            }
        }
        return outputStream.toByteArray();
    }

    public MovilizerMasterdataReference getMasterdataReference(byte[] serializedMasterdataReference) {
        JAXBElement<MovilizerMasterdataReference> root;
        try {
            if (serializedMasterdataReference == null)
                throw new MovilizerException(
                        "Unable to parse a MovilizerMasterdataReference from an null string");
            root = masterdataReferenceUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(
                    serializedMasterdataReference)), MovilizerMasterdataReference.class);
            return root.getValue();
        } catch (JAXBException e) {
            logger.error(e);
        }
        return null;
    }

    public byte[] serializeMasterdataReference(MovilizerMasterdataReference masterdataReference) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (masterdataReference != null) {
            try {
                masterdataReferenceMarshaller.marshal(new JAXBElement<>(new QName("uri", "local"),
                        MovilizerMasterdataReference.class, masterdataReference), outputStream);
            } catch (JAXBException e) {
                logger.error(e);
            }
        }
        return outputStream.toByteArray();
    }
}
