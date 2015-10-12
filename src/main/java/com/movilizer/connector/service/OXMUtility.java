package com.movilizer.connector.service;

import com.movilitas.movilizer.v14.*;
import com.movilizer.connector.exception.MovilizerException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

import javax.annotation.PostConstruct;
import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;

@Component
public class OXMUtility {

	private static Log logger = LogFactory.getLog(OXMUtility.class);

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

	public String toString(JAXBElement<?> element, Class<?>... aClasses) {
		return toByteOutputStream(element, aClasses).toString();
	}

	public ByteArrayOutputStream toByteOutputStream(JAXBElement<?> element, Class<?>... aClasses) {
		try {
			JAXBContext context = JAXBContext.newInstance(aClasses);
			Marshaller marshaller = context.createMarshaller();

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			marshaller.marshal(element, outputStream);
			return outputStream;
		} catch (JAXBException e) {
			throw new OXMException("Error while marshaling Object", e);
		}
	}

	public MovilizerMasterdataReference toObject(byte[] serializedMasterdataReference, Class<?> objectClass) {
		JAXBElement<MovilizerMasterdataReference> root;
		try {
			if (serializedMasterdataReference == null)
				throw new MovilizerException("Unable to parse a MovilizerMasterdataReference from an null string");
			root = masterdataReferenceUnmarshaller.unmarshal(
					new StreamSource(new ByteArrayInputStream(serializedMasterdataReference)),
					MovilizerMasterdataReference.class);
			return root.getValue();
		} catch (JAXBException e) {
			logger.error(e);
		}
		return null;
	}

	public void toWriter(Object aObject, Writer writer, Class<?>... aClasses) {
		JAXBContext tJAXBContext;
		try {
			tJAXBContext = JAXBContext.newInstance(aClasses);

			Marshaller tMarshaller = tJAXBContext.createMarshaller();
			tMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			tMarshaller.marshal(aObject, writer);
		} catch (JAXBException aException) {
			throw new OXMException("Error on converting object to XML: " + aObject, aException);
		}
	}

	public void toFile(Object aObject, File file, Class<?>... aClasses) {
		try {
			toWriter(aObject, new FileWriter(file), aClasses);
		} catch (IOException aException) {
			throw new OXMException("Error on converting object to XML: " + aObject, aException);
		}
	}

	public <T> T stringToObject(String xmlString, Class<T> toValueType) {
		try {
			return inputStreamToObject(new ByteArrayInputStream(xmlString.getBytes("UTF-8")), toValueType);
		} catch (UnsupportedEncodingException e) {
			throw new OXMException("Error on converting XML to Object: " + xmlString, e);
		}
	}

	public <T> T inputStreamToObject(InputStream stream, Class<T> toValueType) {
		JAXBElement<T> result;
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(toValueType);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			if (stream == null)
				throw new MovilizerException("Unable to parse a EventMessage from null object");
			result = unmarshaller.unmarshal(new StreamSource(stream), toValueType);
			return result.getValue();
		} catch (JAXBException e) {
			throw new OXMException("Error on converting XML to Object", e);
		}
	}

	@PostConstruct
	public void init() throws JAXBException {
		moveletContext = JAXBContext.newInstance(MovilizerMovelet.class);
		moveletMarshaller = moveletContext.createMarshaller();
		moveletUnmarshaller = moveletContext.createUnmarshaller();
		// ValidationEventHandler validationEventHandler = new
		// javax.xml.bind.helpers.DefaultValidationEventHandler();
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
			root = moveletUnmarshaller.unmarshal(new StreamSource(new File(fullPath.toURI())), MovilizerMovelet.class);
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
			root = moveletUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(serializedMovelet)),
					MovilizerMovelet.class);
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
				moveletMarshaller.marshal(
						new JAXBElement<>(new QName("uri", "local"), MovilizerMovelet.class, dataContainer),
						outputStream);
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
				throw new MovilizerException("Unable to parse a MovilizerUploadDataContainer from an null string");
			root = datacontainerUnmarshaller.unmarshal(
					new StreamSource(new ByteArrayInputStream(serializedDatacontainer)),
					MovilizerUploadDataContainer.class);
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
				datacontainerMarshaller.marshal(
						new JAXBElement<>(new QName("uri", "local"), MovilizerUploadDataContainer.class, dataContainer),
						outputStream);
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
				throw new MovilizerException("Unable to parse a MovilizerMasterdataDelete from an null string");
			root = masterdataDeleteUnmarshaller.unmarshal(
					new StreamSource(new ByteArrayInputStream(serializedMasterdataDelete)),
					MovilizerMasterdataDelete.class);
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
				masterdataDeleteMarshaller.marshal(
						new JAXBElement<>(new QName("uri", "local"), MovilizerMasterdataDelete.class, masterdataDelete),
						outputStream);
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
				throw new MovilizerException("Unable to parse a MovilizerMasterdataUpdate from an null string");
			root = masterdataUpdateUnmarshaller.unmarshal(
					new StreamSource(new ByteArrayInputStream(serializedMasterdataUpdate)),
					MovilizerMasterdataUpdate.class);
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
				masterdataUpdateMarshaller.marshal(
						new JAXBElement<>(new QName("uri", "local"), MovilizerMasterdataUpdate.class, masterdataUpdate),
						outputStream);
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
				throw new MovilizerException("Unable to parse a MovilizerMasterdataReference from an null string");
			root = masterdataReferenceUnmarshaller.unmarshal(
					new StreamSource(new ByteArrayInputStream(serializedMasterdataReference)),
					MovilizerMasterdataReference.class);
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

	public byte[] serialize(Object serializeObject, Class serializeClass) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		if (serializeObject != null) {
			try {
				JAXBContext.newInstance(serializeClass).createMarshaller().marshal(
						new JAXBElement<>(new QName("uri", "local"), serializeClass, serializeObject), outputStream);
			} catch (JAXBException e) {
				logger.error(e);
			}
		}
		return outputStream.toByteArray();
	}

	public <T> T deserialize(byte[] serializedMasterdataDelete, Class<T> deserializeClass) {
		JAXBElement<T> root;
		try {
			if (serializedMasterdataDelete == null)
				throw new MovilizerException("Unable to parse a MovilizerMasterdataDelete from an null string");
			root = JAXBContext.newInstance(deserializeClass).createUnmarshaller().unmarshal(
					new StreamSource(new ByteArrayInputStream(serializedMasterdataDelete)), deserializeClass);
			return root.getValue();
		} catch (JAXBException e) {
			logger.error(e);
		}
		return null;
	}

	public <T> T deserialize(URL url, Class<T> deserializeClass) {
		JAXBElement<T> root;
		try {
			if (url == null)
				throw new MovilizerException(String.format("File not found for path %s", url));
			root = JAXBContext.newInstance(deserializeClass).createUnmarshaller()
					.unmarshal(new StreamSource(new File(url.toURI())), deserializeClass);
			return root.getValue();
		} catch (URISyntaxException e) {
			throw new MovilizerException(String.format("Malformed path %s", url));
		} catch (JAXBException e) {
			throw new MovilizerException(String.format("Error parsing Movelet in file %s", url));
		}
	}
}
