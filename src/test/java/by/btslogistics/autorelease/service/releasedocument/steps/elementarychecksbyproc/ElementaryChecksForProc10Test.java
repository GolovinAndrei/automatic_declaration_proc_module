package by.btslogistics.autorelease.service.releasedocument.steps.elementarychecksbyproc;

import by.btslogistics.autorelease.service.dto.RegistrationDto;
import by.btslogistics.autorelease.service.dto.messageslog.MessageLogDto;
import by.btslogistics.autorelease.service.save.commonreport.CommonReportSaveService;

import by.btslogistics.xsdclasses.goodsdeclaration.v1_2_0.eec.m.ca.complexdataobjects.v1_5_2.CACountryDetailsType;
import by.btslogistics.xsdclasses.goodsdeclaration.v1_2_0.eec.m.ca.complexdataobjects.v1_5_2.DeclarationGoodsShipmentDetailsType;
import by.btslogistics.xsdclasses.goodsdeclaration.v1_2_0.eec.m.ca.simpledataobjects.v1_5_2.CACountryCodeType;
import by.btslogistics.xsdclasses.goodsdeclaration.v1_2_0.eec.r._036.goodsdeclaration.v1_2_0.GoodsDeclarationType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ElementaryChecksForProc10Test {

    @Mock
    private CommonReportSaveService commonReportSaveService;

    @InjectMocks
    private ElementaryChecksForProc10 elementaryChecksForProc10;

    private RegistrationDto dto;

    @BeforeAll
    public void globalSetUp() {
        elementaryChecksForProc10 = new ElementaryChecksForProc10(commonReportSaveService);
    }

   /* @Before
    public void setUp(){
        dto = new RegistrationDto(new MessageLogDto());
        CACountryCodeType caCountryCodeType = new CACountryCodeType();
        CACountryDetailsType caCountryDetailsType = new CACountryDetailsType();
        caCountryDetailsType.setCACountryCode(caCountryCodeType);
        DeclarationGoodsShipmentDetailsType declarationGoodsShipmentDetailsType = new DeclarationGoodsShipmentDetailsType();
        declarationGoodsShipmentDetailsType.setDestinationCountryDetails(caCountryDetailsType);
        GoodsDeclarationType goodsDeclarationType = new GoodsDeclarationType();
        goodsDeclarationType.setDeclarationGoodsShipmentDetails(declarationGoodsShipmentDetailsType);
        dto.setGoodsDeclarationType(goodsDeclarationType);
    }*/

    /*@Test
    public void positiveResultOfElemetaryChecksForProc10() {
        dto.getGoodsDeclarationType().getDeclarationGoodsShipmentDetails().getDestinationCountryDetails().getCACountryCode().setValue("CA");
        Assert.assertTrue(elementaryChecksForProc10.doStep(dto).isElementaryChecksPassed());
    }

    @Test
    public void negativeResultOFElementaryChecksForProc10(){
        dto.getGoodsDeclarationType().getDeclarationGoodsShipmentDetails().getDestinationCountryDetails().getCACountryCode().setValue("BY");
        Assert.assertFalse(elementaryChecksForProc10.doStep(dto).isElementaryChecksPassed());
    }*/
}