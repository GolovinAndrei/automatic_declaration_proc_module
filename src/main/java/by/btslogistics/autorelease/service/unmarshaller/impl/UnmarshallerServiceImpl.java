package by.btslogistics.autorelease.service.unmarshaller.impl;

import by.btslogistics.autorelease.service.unmarshaller.UnmarshallerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

@Service
public class UnmarshallerServiceImpl implements UnmarshallerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnmarshallerServiceImpl.class);

    public UnmarshallerServiceImpl() {
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> T unmarshal(String xml, @SuppressWarnings("rawtypes") Class customClass) {

        JAXBContext jaxbContext = createJAXBContext(customClass);

        Unmarshaller jaxbUnmarshaller = createUnmarshaller(jaxbContext);

        @SuppressWarnings("rawtypes") JAXBElement jaxbElementSimple = getJAXBElementSimple(jaxbUnmarshaller, xml);

        return (T) jaxbElementSimple.getValue();
    }

    /**
     * вынести обработку пути к файл *.xsd в отдельный метод.
     */

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T> T unmarshalSchemaAll(String xml, Class customClass, String schemaLocation) {

        JAXBContext jaxbContext = createJAXBContext(customClass);

        Unmarshaller jaxbUnmarshaller = createUnmarshaller(jaxbContext);

        JAXBElement jaxbElementBySchema = getJAXBElementWithSchema(jaxbUnmarshaller, xml, schemaLocation);

        return (T) jaxbElementBySchema.getValue();
    }

    /**
     * Получение элемента на основе предоставленной схемы xsd
     *
     * @param jaxbUnmarshaller объект типа {@link Unmarshaller}
     * @param xmlDoc           документ xml, в виде строки
     * @param schemaLocation   путь к схеме xsd
     * @return {@link JAXBElement}
     */
    @SuppressWarnings("rawtypes")
    private JAXBElement getJAXBElementWithSchema(Unmarshaller jaxbUnmarshaller, String xmlDoc, String schemaLocation) {

        File file = getPathToLocationXsd(schemaLocation);
        final Schema schema = getSchema(file);

        jaxbUnmarshaller.setSchema(schema);

        return getJAXBElementSimple(jaxbUnmarshaller, xmlDoc);

    }


    private Schema getSchema(File file) {

        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        Schema schema = null;

        try {
            schema = schemaFactory.newSchema(file);
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return schema;
    }

    /**
     * Возникают проблемы при чтении с расположения xsd Схем
     *
     * @param schemaLocation
     * @return
     */
    private File getPathToLocationXsd(String schemaLocation) {

        ClassPathResource pathResource = new ClassPathResource(schemaLocation);

        File file = null;
        try {
            file = pathResource.getFile();
        } catch (IOException e) {
            LOGGER.error("Failed to get access to file from classpath (there is read xsd), ", e);
        }

        return file;
    }


    @SuppressWarnings("rawtypes")
    private JAXBElement getJAXBElementSimple(Unmarshaller jaxbUnmarshaller, String xmlDoc) {

        StringReader xmlDocumentIntoStringReader = new StringReader(xmlDoc);

        JAXBElement jaxbElement = null;
        try {
            jaxbElement = (JAXBElement) jaxbUnmarshaller.unmarshal(xmlDocumentIntoStringReader);
        } catch (JAXBException e) {
            LOGGER.error("Failed get JaxbElement", e);
        }

        return jaxbElement;
    }

    private Unmarshaller createUnmarshaller(JAXBContext jaxbContext) {

        Unmarshaller jaxbUnmarshaller = null;
        try {
            jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            LOGGER.error("Failed creating an Unmarshaller. ", e);
        }

        return jaxbUnmarshaller;
    }

    private Marshaller createMarshaller(JAXBContext jaxbContext) {
        Marshaller marshaller = null;
        try {
            marshaller = jaxbContext.createMarshaller();
        } catch (JAXBException e) {
            LOGGER.error("Не удалось создать Marshaller. ", e);
        }
        return marshaller;
    }

    @SuppressWarnings("rawtypes")
    private JAXBContext createJAXBContext(Class customClass) {

        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(customClass);
        } catch (JAXBException e) {
            LOGGER.error("Failed the creating JAXBContext..., from a class:  {}", customClass, e);
        }

        return jaxbContext;
    }

    @Override
    public String marshal(Object object, Class<?> customClass) {
        StringWriter stringWriter = new StringWriter();
        JAXBContext context = createJAXBContext(customClass);
        Marshaller marshaller = createMarshaller(context);
        try {
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        } catch (PropertyException e) {
            LOGGER.error("PropertyException. Не удалось установить настройки в marshaller", e);
        }


        try {
            marshaller.marshal(object, stringWriter);
        } catch (JAXBException e) {
            LOGGER.error("JAXBException. Не удалось преобразовать документ", e);
        }

        return stringWriter.toString();
    }
}
