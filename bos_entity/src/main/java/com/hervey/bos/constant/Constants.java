package com.hervey.bos.constant;

/**
 * Created on 2017/12/10.
 *
 * @author hervey
 */
public class Constants {


    public static final String BOS_MANAGEMENT_HOST = "http://localhost:8080";
    public static final String CRM_MANAGEMENT_HOST = "http://localhost:8080";
    public static final String BOS_FORE_HOST = "http://localhost:8080";
    public static final String BOS_SMS_HOST = "http://localhost:8080";
    public static final String BOS_MAIL_HOST = "http://localhost:8080";

    private static final String BOS_MANAGEMENT_CONTEXT = "/bos";
    private static final String CRM_MANAGEMENT_CONTEXT = "/crm";
    private static final String BOS_FORE_CONTEXT = "/bos_fore";
    private static final String BOS_SMS_CONTEXT = "/bos_sms";
    private static final String BOS_MAIL_CONTEXT = "/bos_mail";

    public static final String BOS_MANAGEMENT_URL = BOS_MANAGEMENT_HOST
            + BOS_MANAGEMENT_CONTEXT;
    public static final String CRM_MANAGEMENT_URL = CRM_MANAGEMENT_HOST
            + CRM_MANAGEMENT_CONTEXT;
    public static final String BOS_FORE_URL = BOS_FORE_HOST + BOS_FORE_CONTEXT;
    public static final String BOS_SMS_URL = BOS_SMS_HOST + BOS_SMS_CONTEXT;
    public static final String BOS_MAIL_URL = BOS_MAIL_HOST + BOS_MAIL_CONTEXT;

}