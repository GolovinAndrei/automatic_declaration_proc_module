package by.btslogistics.autorelease.web.rest.proxyfeign;

import by.btslogistics.commons.dto.nsi.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static by.btslogistics.autorelease.web.constant.AutoReleaseConstants.*;

@FeignClient(name = "${helper.service.name}", url = "${helper.service.path}")
public interface HelperRestProxy {

    @GetMapping(URL_TO_GET_SIGN_FROM_NSI_PR_UNP)
    Iterable<NsiPrUnpDto> getSignByUnp(@RequestParam("unp") String unp);

    @GetMapping(URL_TO_GET_UNP_FOR_OIS_CHECK)
    List<NsiReestrIsLicoDto> getUnpByReshParams(@PathVariable("id") int idResh,
                                                @PathVariable("year") int yearResh,
                                                @PathVariable("type") int typeResh,
                                                @PathVariable("lico") int lico);

    @GetMapping(URL_TO_GET_UNP_OSOB_PEREM)
    Iterable<NsiUnpOsobPeremDto> getUnpOsobPeremByIdObj (@PathVariable ("idObj") String idObj);

    @GetMapping(URL_TO_GET_NSI_CATALOG_BY_SPEC)
    List<NsiCatalogDto> getNsiCatalogsBySpec (@PathVariable ("specObj") String specObj);

    @GetMapping(URL_TO_GET_NSI_CATALOG_BY_ID)
    NsiCatalogDto getNsiCatalogById(@PathVariable ("id") String id);

    @GetMapping(URL_TO_GET_FULL_TNVED_SP)
    Optional<NsiTnvedSpDto> getFullTnVed (@PathVariable ("tnved") String tnved, @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") LocalDateTime dateTime);

    @GetMapping(URL_TO_GET_RECORD_BY_IDOBJ_AND_IDSP)
    Optional<NsiTarifDto> getActualByListIdsObjAndIdSpIn(@PathVariable ("idsObj") List<String> idsObj,
                                                         @PathVariable ("idSp") String idSp,
                                                         @PathVariable ("dateTime") @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") LocalDateTime dateTime);

    @GetMapping(URL_TO_GET_ALL_MAGS)
    Iterable<NsiReestrMagDto> getAllMags ();

    @GetMapping(URL_TO_GET_NAL_PLAT_BY_UNN)
    Boolean getNsiNalPlatByUnp(@PathVariable String unn, @PathVariable String status);

    @GetMapping(URL_TO_GET_REESTR_UO_WITH_UNN)
    NsiReestrUoByDto getNsiReestrUoByByUnn(@PathVariable("unn")String unn);

    @GetMapping(URL_TO_GET_REESTR_TS)
    NsiReestrTsDto getReestrTsRecordByNum(@RequestParam ("number") String numReestr);

    @GetMapping(URL_TO_CHECK_NOM_ZTK)
    Boolean checkNomZtk (@RequestParam String nomZtk,
                         @RequestParam  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime dateTime);

    @GetMapping(URL_TO_GET_LIST_OF_SPEC_FOR_PTO)
    Iterable <String> getListOfSpecForPto (@PathVariable ("code") String code);

    @GetMapping(URL_TO_CHECK_TNVED_AND_IDOBJ_IN_NSI_NO_TARIF)
    Boolean checkTnVedAndIdObjInNoTarif (@PathVariable ("idObj") String idObj,
                                                         @PathVariable ("tnVed")String tnVed,
                                                         @PathVariable ("dateTime")@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime dateTime);

    @GetMapping(URL_TO_CHECK_UNP_IN_NSI_REESTR_FIRM)
    Iterable<NsiReestrFirmDto> getUnpFromReestrFirm (@RequestParam String unp);

    @GetMapping(PATH_CHEK_UNP_KCEZ)
    Boolean getUnpKcez (@PathVariable String unp);

    @GetMapping(PATH_CHECK_NSI_TAR_OKSMT)
    Boolean checkTarOksmt(@PathVariable String codeUsl,
                          @PathVariable String codeOksmt,
                          @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime dateTime);


}
