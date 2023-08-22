package by.btslogistics.autorelease.service.releasedocument.unloaddocument.impl;

import by.btslogistics.autorelease.dao.repository.uploader.UploaderRepository;
import by.btslogistics.autorelease.service.dto.RegistrationDto;
import by.btslogistics.autorelease.service.releasedocument.unloaddocument.SendToAisUtpService;
import by.btslogistics.autorelease.service.unmarshaller.UnmarshallerService;
import by.btslogistics.commons.service.utils.ZipUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Slf4j
@Service
public class SendToAisUtpServiceImpl implements SendToAisUtpService {


    private final UploaderRepository uploaderRepository;

    private final UnmarshallerService unmarshallerService;

    private final WebClient soapWebClient;

    @Value("${url.nased}")
    private String urlNased;

    public static final String FIRST_PART_OF_WRAP = "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">\n    <Body>\n";

    public static final String LAST_PART_OF_WRAP = "    </Body>\n</Envelope>";



    @Autowired
    public SendToAisUtpServiceImpl(UploaderRepository uploaderRepository, UnmarshallerService unmarshallerService, @Qualifier("soapWebClient") WebClient soapWebClient) {
        this.uploaderRepository = uploaderRepository;
        this.unmarshallerService = unmarshallerService;
        this.soapWebClient = soapWebClient;
    }

    @Override
    public String createXml(RegistrationDto dto) {

        String xml = uploaderRepository.getXmlFromUploaderDbProcedure(dto.getMessageLog().getId());
        String messageZipB64;
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(ZipUtils.checkForUtf8BOM(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)))));

            byte[] bytes = ZipUtils.asByteArray(document, "UTF-8");
            String fileName = dto.getMessageLog().getRegNumber().replaceAll("/", "_") + "_DT.xml";
            messageZipB64 = ZipUtils.zipB64(bytes, fileName);

        } catch (SAXException | IOException | TransformerException | ParserConfigurationException e) {
            log.error("XML has not been created!", e);
            return null;
        }

        String documentId;
        if (dto.getGoodsDeclarationTypeOld() != null) {
            documentId = dto.getGoodsDeclarationTypeOld().getEDocId();
        } else {
            documentId = dto.getGoodsDeclarationTypeNew().getEDocId();
        }

        /*LoadDoc2 loadDoc2 = new LoadDoc2();
        loadDoc2.setBase64Str(messageZipB64);
        loadDoc2.setDocumentID(documentId);
        loadDoc2.setFileName(documentId + ".zip");*/

        return null; //unmarshallerService.marshal(loadDoc2, LoadDoc2.class);
    }

    @Override
    public void sendXmlToAisUtp(RegistrationDto dto) {

        String xml = createXml(dto);

        if (xml != null) {

            StringBuilder messageToSend = new StringBuilder(FIRST_PART_OF_WRAP);
            messageToSend.append(xml).append(LAST_PART_OF_WRAP);

            try {
                log.debug("Sending of document to AIS UTP: {}", messageToSend);
                String stringMono = soapWebClient
                        .post()
                        .uri(urlNased)
                        .body(BodyInserters.fromValue(messageToSend.toString()))
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

                if (stringMono == null) {
                    log.debug("Ответ - '{}'", stringMono);
                }

                log.info("Document {} hase been sanded to AIS UTP", dto.getMessageLog().getId());
            } catch (Exception e) {
                log.error("Error via sanding XML,  {}", dto.getMessageLog().getId(), e);
            }
        }
    }
}
