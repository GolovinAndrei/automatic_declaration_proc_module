package by.btslogistics.autorelease.service.releasedocument.steps;

import by.btslogistics.autorelease.service.dto.RegistrationDto;
import by.btslogistics.autorelease.service.save.messageslogstage.MessagesLogStageSaveService;
import by.btslogistics.autorelease.web.rest.proxyfeign.RegistrationSvxRestProxy;
import by.btslogistics.commons.dao.dto.subsequent.SubsequentDto;
//import by.btslogistics.xsdclasses.goodsdeclaration.v1_2_0.eec.m.ca.complexdataobjects.v1_5_2.DeclarationGoodsItemDetailsType;
//import by.btslogistics.xsdclasses.goodsdeclaration.v1_2_0.eec.m.ca.complexdataobjects.v1_5_2.PresentedDocDetailsType;
import eec.m.ca.complexdataobjects.v1_5_7.DeclarationGoodsItemDetailsType;
import eec.m.ca.complexdataobjects.v1_5_7.PresentedDocDetailsType;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static by.btslogistics.commons.dao.enums.Stage.SVX_STAGE_WRITEOFF_FROM_UVR;

/**
 * Этап автоматического списания товара через модуль SVX
 * Если в автовыпущенном документе в представленных документах присутствует УВ, то для нее производится автоматическое списание массы, указанной в автовыпущенном документе.
 */
public class SvxStep extends RegistrationDocument {

    private static final Logger LOGGER = LoggerFactory.getLogger(SvxStep.class);

    private final RegistrationSvxRestProxy svxRestProxy;

    private final MessagesLogStageSaveService messagesLogStageSaveService;

    private static final String UVR_DOC_CODE = "09019";

    public SvxStep(RegistrationSvxRestProxy svxRestProxy, MessagesLogStageSaveService messagesLogStageSaveService) {
        this.svxRestProxy = svxRestProxy;
        this.messagesLogStageSaveService = messagesLogStageSaveService;
    }

    @Override
    public RegistrationDto doStep(RegistrationDto dto) {
        LOGGER.debug("Start auto write-off in SVX");

        if (dto.getGoodsDeclarationTypeNew() != null) {

            List<by.btslogistics.xsdclasses.eec.v1_4_0.eec.m.ca.complexdataobjects.v1_5_8.DeclarationGoodsItemDetailsType> declarationGoodsItemDetails =
                    dto.getGoodsDeclarationTypeNew().getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails();

            Map<String, SubsequentDto> map = new HashMap<>();

            for (by.btslogistics.xsdclasses.eec.v1_4_0.eec.m.ca.complexdataobjects.v1_5_8.DeclarationGoodsItemDetailsType declarationGoodsItemDetail : declarationGoodsItemDetails) {
                if (declarationGoodsItemDetail.getPresentedDocDetails() != null && !declarationGoodsItemDetail.getPresentedDocDetails().isEmpty()) {


                    List<by.btslogistics.xsdclasses.eec.v1_4_0.eec.m.ca.complexdataobjects.v1_5_8.PresentedDocDetailsType> presentedDocDetails = declarationGoodsItemDetail.getPresentedDocDetails();
                    for (by.btslogistics.xsdclasses.eec.v1_4_0.eec.m.ca.complexdataobjects.v1_5_8.PresentedDocDetailsType presentedDocDetail : presentedDocDetails) {
                        if (presentedDocDetail.getDocKindCode() != null
                                && UVR_DOC_CODE.equals(presentedDocDetail.getDocKindCode().getValue())) {

                            String registrationNumberUvr = presentedDocDetail.getDocId();

                            Double gross = null;
                            Double net = null;

                            if (declarationGoodsItemDetail.getUnifiedGrossMassMeasure() != null) {
                                BigDecimal grossValue = declarationGoodsItemDetail.getUnifiedGrossMassMeasure().getValue();
                                gross = grossValue.doubleValue();

                            }
                            if (declarationGoodsItemDetail.getUnifiedNetMassMeasure() != null) {
                                BigDecimal netValue = declarationGoodsItemDetail.getUnifiedNetMassMeasure().getValue();
                                net = netValue.doubleValue();
                            }

                            if (!map.isEmpty() && map.containsKey(registrationNumberUvr)) {
                                SubsequentDto subsequentDto = map.get(registrationNumberUvr);
                                if (gross != null)
                                    subsequentDto.setGrossWeight(subsequentDto.getGrossWeight() + gross);
                                if (net != null)
                                     subsequentDto.setNetWeight(subsequentDto.getNetWeight() + net);
                            } else {
                                SubsequentDto subsequentDto = new SubsequentDto();
                                subsequentDto.setRegNumberNotify(registrationNumberUvr);
                                subsequentDto.setGrossWeight(gross);
                                subsequentDto.setNetWeight(net);
                                subsequentDto.setReleaseDate(dto.getMessageLog().getDateReg());
                                subsequentDto.setRegNumber(dto.getMessageLog().getRegNumber());
                                map.put(registrationNumberUvr, subsequentDto);
                            }

                        }
                    }
                }
            }
            if (map.size() != 0) {
                map.forEach((k, v) -> {
                    try {
                        svxRestProxy.autoWriteOffGoods(v);
                        LOGGER.debug("Written off the weight of {} for the UV № {}", v.getGrossWeight(), k);
                    } catch (FeignException e) {
                        e.printStackTrace();
                    }
                });
            }
        } else {
            List<DeclarationGoodsItemDetailsType> declarationGoodsItemDetails = dto.getGoodsDeclarationTypeOld().getDeclarationGoodsShipmentDetails().getDeclarationGoodsItemDetails();

            Map<String, SubsequentDto> map = new HashMap<>();

            for (DeclarationGoodsItemDetailsType declarationGoodsItemDetail : declarationGoodsItemDetails) {
                if (declarationGoodsItemDetail.getPresentedDocDetails() != null && !declarationGoodsItemDetail.getPresentedDocDetails().isEmpty()) {


                    List<PresentedDocDetailsType> presentedDocDetails = declarationGoodsItemDetail.getPresentedDocDetails();
                    for (PresentedDocDetailsType presentedDocDetail : presentedDocDetails) {
                        if (presentedDocDetail.getDocKindCode() != null
                                && UVR_DOC_CODE.equals(presentedDocDetail.getDocKindCode().getValue())) {

                            String registrationNumberUvr = presentedDocDetail.getDocId();

                            Double gross = null;
                            Double net = null;

                            if (declarationGoodsItemDetail.getUnifiedGrossMassMeasure() != null) {
                                BigDecimal grossValue = declarationGoodsItemDetail.getUnifiedGrossMassMeasure().getValue();
                                gross = grossValue.doubleValue();

                            }
                            if (declarationGoodsItemDetail.getUnifiedNetMassMeasure() != null) {
                                BigDecimal netValue = declarationGoodsItemDetail.getUnifiedNetMassMeasure().getValue();
                                net = netValue.doubleValue();
                            }

                            if (!map.isEmpty() && map.containsKey(registrationNumberUvr)) {
                                SubsequentDto subsequentDto = map.get(registrationNumberUvr);
                                if (gross != null)
                                    subsequentDto.setGrossWeight(subsequentDto.getGrossWeight() + gross);
                                if (net != null)
                                    subsequentDto.setNetWeight(subsequentDto.getNetWeight() + net);
                            } else {
                                SubsequentDto subsequentDto = new SubsequentDto();
                                subsequentDto.setRegNumberNotify(registrationNumberUvr);
                                subsequentDto.setGrossWeight(gross);
                                subsequentDto.setNetWeight(net);
                                subsequentDto.setReleaseDate(dto.getMessageLog().getDateReg());
                                subsequentDto.setRegNumber(dto.getMessageLog().getRegNumber());
                                map.put(registrationNumberUvr, subsequentDto);
                            }

                        }
                    }
                }
            }
            if (map.size() != 0) {
                map.forEach((k, v) -> {
                    try {
                        svxRestProxy.autoWriteOffGoods(v);
                        LOGGER.debug("Written off the weight of {} for the UV № {}", v.getGrossWeight(), k);
                    } catch (FeignException e) {
                        e.printStackTrace();
                    }
                });
            }

        }
        messagesLogStageSaveService.addStageRec(SVX_STAGE_WRITEOFF_FROM_UVR, dto.getMessageLog().getId());
        return nextStep(dto);
    }

}
