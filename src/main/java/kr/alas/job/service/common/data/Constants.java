package kr.alas.job.service.common.data;

import java.text.SimpleDateFormat;

public class Constants {
    public static final String HEADER_AUTHORIZATION = "Authorization";

    public static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 2; // 30 min

    public static final String TIME_STAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // Response
    public static final String RESPONSE_LIST = "list";
    public static final String RESPONSE_TOTAL = "total";


    public static final int ADMIT_EXPIRATION = 90; //인정휴가 만료일
}
