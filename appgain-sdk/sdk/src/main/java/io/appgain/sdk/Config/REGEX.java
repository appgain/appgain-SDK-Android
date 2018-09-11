package io.appgain.sdk.Config;

/**
 * Created by developers@appgain.io on 8/18/2018.
 */
public class REGEX {

        public static final String ALL_CHAR= "^[A-Z,a-z,0-9,_,.,-,\\\\,/,///,:,\\s,-]";
    public static final String NAME_LENGTH_REGX = ALL_CHAR+"{4,100}";
    public static final String DEEP_PAGE_TOPIC_LENGTH_REGX = ALL_CHAR+"{5,50}";
    public static final String SM_TITLE_LENGTH_REGX = ALL_CHAR+"{5,15}";
    public static final String SM_DESCRIPTION_LENGTH_REGX = ALL_CHAR+"{5,30}";
    public static final String SLUG_LENGTH_REGX = ALL_CHAR+"{5,100}";
    public static final String LINK_LENGTH_REGX = ALL_CHAR+"{4,500}";
    public static final String SUB_TOPIC_LENGTH_REGX = ALL_CHAR+"{4,500}";
    public static final String NO_WHITE_SPACE = "(\\S)+";
    public static final String alphanumrical = "^\\w";
    public static final String alphabatical = "^[a-z,A-Z]";
    public static final String URL = "^[A-Z,a-z,0-9]+:.*";



}
