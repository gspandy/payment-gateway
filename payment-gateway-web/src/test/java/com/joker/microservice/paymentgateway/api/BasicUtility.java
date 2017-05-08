package com.joker.microservice.paymentgateway.api;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.Date;

/**
 * @Author zhangjian
 * @Date 2017/3/22
 * @Copyright:
 * @Describe:
 */
public class BasicUtility {
    public static final String MESSAGE_INTERNAL_ERROR = "An internal error has occured.";
    public static final String PARAMETER_EXT_MISSING = " is missing.";

    private BasicUtility() {
    }

    public static final boolean isValidPhoneNumberFormat(String phone) {
        return phone != null?phone.matches("\\+\\d+|\\d+"):false;
    }

    public static final boolean isValidString(String string) {
        return string != null && !string.trim().equals("");
    }

    public static final boolean isValidString(Object obj) {
        return obj != null?!((String)obj).trim().equals(""):false;
    }

    public static final boolean isValidDate(Date date) {
        return date != null && date.getTime() > 0L;
    }

    private static String changeNull(String check) {
        return check == null?"":check;
    }

    public static final String removeReplaceCode(String string) {
        if(string != null) {
            string = string.trim();
            int length = string.length();
            if(length == 0) {
                return "";
            } else {
                StringBuffer buffer = new StringBuffer();

                for(int i = 0; i < length; ++i) {
                    char c = string.charAt(i);
                    buffer.append(c);
                    if(c == 34) {
                        buffer.append(c);
                    }
                }

                return buffer.toString();
            }
        } else {
            return null;
        }
    }

    public static final String removeWhiteSpace(String string) {
        if(string != null) {
            string = string.trim();
            int length = string.length();
            if(length == 0) {
                return "";
            } else {
                StringBuffer buffer = new StringBuffer();

                for(int i = 0; i < length; ++i) {
                    char c = string.charAt(i);
                    if(c != 32) {
                        buffer.append(c);
                    }
                }

                return buffer.toString();
            }
        } else {
            return null;
        }
    }

    public static final String removeNull(String string) {
        return string != null?string:"";
    }

    public static final String roundToDecimalPlace(double value, int dp) {
        DecimalFormat format = new DecimalFormat("0");
        format.setMaximumFractionDigits(dp);
        format.setMinimumFractionDigits(dp);
        return format.format(value);
    }

    public static final String formatNumber(double value, int num) {
        DecimalFormat format = new DecimalFormat();
        format.setMaximumIntegerDigits(num);
        format.setMinimumIntegerDigits(num);
        format.setGroupingSize(0);
        return format.format(value);
    }

    public static final String display2DecimalPt(double value) {
        DecimalFormat df = new DecimalFormat("0.00");
        StringBuffer result = new StringBuffer();
        df.format(value, result, new FieldPosition(0));
        return result.toString();
    }

    public static final String display4DecimalPt(double value) {
        DecimalFormat df = new DecimalFormat("0.0000");
        StringBuffer result = new StringBuffer();
        df.format(value, result, new FieldPosition(0));
        return result.toString();
    }

    public static final String stripGrDomain(String value) {
        return stripDomain(value, 15);
    }

    public static final String stripDomain(String value, int domainLength) {
        return isValidString(value) && value.length() >= domainLength?value.substring(0, value.length() - 15):value;
    }

    public static final String checkUserDomain(String userId, String defaultDomain) {
        if(isValidString(userId)) {
            if(userId.indexOf("@") != -1) {
                String[] splitUserId = userId.split("@");
                if(splitUserId != null && splitUserId.length > 0) {
                    if(splitUserId[splitUserId.length - 1].equals(defaultDomain)) {
                        return userId;
                    }

                    userId = userId + defaultDomain;
                }

                return userId;
            }

            userId = userId + defaultDomain;
        }

        return userId;
    }

    public static void main(String[] arg) {
    }

    public static String getFormData(Object obj) {
        String tempString = null;
        if(obj instanceof String[]) {
            String[] tmp = (String[])((String[])obj);
            tempString = tmp[0];
        } else if(obj instanceof String) {
            tempString = (String)obj;
        }

        return tempString;
    }

    public static String[] getFormDataArray(Object obj) {
        String[] array = null;
        if(obj instanceof String[]) {
            array = (String[])((String[])obj);
        } else if(obj instanceof String) {
            array = new String[]{(String)obj};
        } else {
            array = new String[0];
        }

        return array;
    }

    public static String[] commaSeparator(String input) {
        ArrayList output = new ArrayList();
        int length = input.length();
        StringBuffer sb = new StringBuffer();
        int count = 0;

        for(int temp = 0; temp < length; ++temp) {
            if(input.charAt(temp) == 44) {
                if(isValidString(sb.toString())) {
                    output.add(sb.toString());
                }

                sb = new StringBuffer();
                ++count;
            } else {
                sb.append(input.charAt(temp));
            }
        }

        if(isValidString(sb.toString())) {
            output.add(sb.toString());
        }

        String[] var7 = new String[output.size()];

        for(int i = 0; i < output.size(); ++i) {
            var7[i] = (String)output.get(i);
        }

        return var7;
    }

    public static String[] commaSeparatorRemoveWhiteSpace(String input) {
        ArrayList output = new ArrayList();
        int length = input.length();
        StringBuffer sb = new StringBuffer();
        int count = 0;

        for(int temp = 0; temp < length; ++temp) {
            if(input.charAt(temp) == 44) {
                output.add(sb.toString());
                sb = new StringBuffer();
                ++count;
            } else {
                sb.append(input.charAt(temp));
            }
        }

        output.add(sb.toString().replaceAll(" ", ""));
        String[] var7 = new String[output.size()];

        for(int i = 0; i < output.size(); ++i) {
            var7[i] = (String)output.get(i);
        }

        return var7;
    }
}
