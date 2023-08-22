package by.btslogistics.autorelease.web.constant;

public interface AutoReleaseConstants {
    /**
     * Root
     */
    String ROOT_AUTO_RELEASE = "api/autorelease";

    /**
     * Receiving Document
     */
    String RECEIVE_DOC = "receivedt";

    /**
     * To ARTD
     */
    String URL_SEND_TO_ARTD = "api/get/employees";

    /**
     * ZIO
     */
    String URL_GET_CERTIFICATE_REPORT = "zio/check/certificateControl/{messagesLogId}";

    String URL_TO_GET_ALL_OBJID_FROM_ZIO = "zio/get/docGoodsListId/{goodNumber}/{codeTnVed}/{dateTime}";

    /**
     * FLK
     */
    String URL_TO_READ_FLK_RESULT_TO_RECEIVE_SERVICE = "api/read/flkResult/report/short";

    /**
     * Payments
     */
    String URL_TO_GET_PAYMENT_REPORT = "api/payments/report";

    /**
     * To SVX
     */
    String URL_FOR_SVX_WRITEOFF = "api/writeoff/external";

    /**
     * Risks
     */
    String URL_TO_GET_RISK_PROTOCOL = "api/profile/check/document";

    /**
     * NSI_PR_UNP
     */
    String URL_TO_GET_SIGN_FROM_NSI_PR_UNP = "api/read/nsiPrUnp";

    /**
     * NSI_REESTR_IS_LICO
     */
    String URL_TO_GET_UNP_FOR_OIS_CHECK = "api/read/nsiReestrIsLico/{id}/{year}/{type}/{lico}";

    /**
     * NSI_UNP_OSOB_PEREM
     */
    String URL_TO_GET_UNP_OSOB_PEREM = "api/read/nsiUnpOsobPerem/{idObj}";

    /**
     * NSI_CATALOG
     */
    String URL_TO_GET_NSI_CATALOG_BY_SPEC = "api/read/nsiCatalog/spec/{specObj}";

    String URL_TO_GET_NSI_CATALOG_BY_ID = "api/read/nsiCatalog/{id}";

    /**
     * NSI_TNVED_SP
     */
    String URL_TO_GET_FULL_TNVED_SP = "api/read/nsiTnvedSp/single/{tnved}/{dateTime}";

    /**
     * NSI_TARIF
     */
    String URL_TO_GET_RECORD_BY_IDOBJ_AND_IDSP = "api/read/nsiTarifsIds/{idsObj}/{idSp}/{dateTime}";

    /**
     * NSI_RESTR_MAG
     */
    String URL_TO_GET_ALL_MAGS = "api/read/nsiReestrMag";

    /**
     * NAL_PLAT
     */
    String URL_TO_GET_NAL_PLAT_BY_UNN = "api/read/nsiUnpPlat/check/unn/{unn}/{status}";

    /**
     * NSI_REESTR_UO_BY
     */
    String URL_TO_GET_REESTR_UO_WITH_UNN = "api/read/nsiReestrUoBy/{unn}";

    /**
     * NSI_REESTR_TS
     */
    String URL_TO_GET_REESTR_TS = "api/read/nsiReestrTs/num";

    /**
     * KSTP
     */
    String URL_TO_REMOVED_CONTROL_KSTP = "api/removed/control/start";

    /**
     * NSI_ZTK
     */
    String URL_TO_CHECK_NOM_ZTK = "api/read/nsiZtk/nomZtk/date";

    /**
     * NSI_PTO_SPEC_BR (NSI_PTO_BR)
     */
    String URL_TO_GET_LIST_OF_SPEC_FOR_PTO = "api/read/nsiPtoBr/spec/{code}";

    /**
     * NSI_NO_TARIF
     */
    String URL_TO_CHECK_TNVED_AND_IDOBJ_IN_NSI_NO_TARIF = "api/read/nsiNoTarif/check/grAndSgrAndPozAndSp/{idObj}/{tnVed}/{dateTime}";

    /**
     * NSI_REESTR_FIRM
     */
    String URL_TO_CHECK_UNP_IN_NSI_REESTR_FIRM = "api/read/nsiReestrFirm/unp";

    /**
     * UNP_KCEZ
     */
    String PATH_CHEK_UNP_KCEZ = "api/read/unpKcez/check/unp/{unp}";

    /**
     * NSI_TS/TAR_OKSMT
     */
    String PATH_CHECK_NSI_TAR_OKSMT = "api/read/nsiTarOksmt/check/{codeUsl}/{codeOksmt}/{dateTime}";
}
