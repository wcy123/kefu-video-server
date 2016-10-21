package com.easemob.weichat.conf;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.commons.lang.time.DateFormatUtils;

public class XdfVisitorCsvConfig {

private static final ResourceBundle rb= ResourceBundle.getBundle("i18n/XdfVisitor",Locale.getDefault());
    private static final String FILE_NAME_PREFIX = rb.getString("visitorcsv.filename");
    private static final String FILE_DATE_FORMAT = rb.getString("visitorcsv.dateformat");
    
    private static final List<String>  COLUMN_NAME;
    private static final List<String> FIELD_NAME ;
    
    private static final String VISITOR_MALE=rb.getString("visitorcsv.sex.male");
    private static final String VISITOR_FEMALE=rb.getString("visitorcsv.sex.female");

    private static final String NONE=rb.getString("visitor.none");
    
    static {
        COLUMN_NAME = Arrays.asList(rb.getString("visitorcsv.columnname").split(","));
        FIELD_NAME = Arrays.asList(rb.getString("visitorcsv.fieldname").split(","));
    }
    
    private XdfVisitorCsvConfig(){
    }
    
    public static List<String> getColumnName() {
        return COLUMN_NAME;
    }

    public static List<String> getFieldName() {
        return FIELD_NAME;
    }

    public static String getCsvFileName(Date start, Date end) {
    	if (start == null && end == null) {
    		return String.format("%s_%s.csv", FILE_NAME_PREFIX, "全部");
    	}
    	String startStr = "";
    	String endStr = "";
    	if (start != null) {
    		startStr = DateFormatUtils.format(start, FILE_DATE_FORMAT);
    	}
    	if (end != null) {
    		endStr = DateFormatUtils.format(end, FILE_DATE_FORMAT);
    	}
        return String.format("%s_%s_%s.csv", FILE_NAME_PREFIX, startStr, endStr);
    }
	
    public static String getVisitorSex(Object obj){
    	if("1".equals(obj.toString()))
    		return VISITOR_MALE;
    	else if("2".equals(obj.toString()))
    		return VISITOR_FEMALE;
    	return NONE;
    }
    
}
