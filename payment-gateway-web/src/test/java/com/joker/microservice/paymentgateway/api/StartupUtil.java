package com.joker.microservice.paymentgateway.api;

/**
 * @Author zhangjian
 * @Date 2017/3/22
 * @Copyright:
 * @Describe:
 */
public class StartupUtil {
    public static char SOMETHING = 90;

    public StartupUtil() {
    }

    public static String encryption(String plain, char mKey) {
        char[] buf = plain.toCharArray();
        char chX = ROR(mKey);

        for(int i = 0; i < buf.length; ++i) {
            buf[i] = (char)(buf[i] ^ chX ^ mKey);
            chX = ROR(buf[i]);
        }

        return new String(buf);
    }

    public static String decryption(String notplain, char mKey) {
        char[] buf = notplain.toCharArray();

        for(int i = buf.length - 1; i >= 0; --i) {
            char chX;
            if(i == 0) {
                chX = mKey;
            } else {
                chX = buf[i - 1];
            }

            chX = ROR(chX);
            buf[i] = (char)(buf[i] ^ chX ^ mKey);
        }

        return new String(buf);
    }

    protected static char RCL(char mValue) {
        mValue = (char)(mValue << 1);
        return mValue;
    }

    protected static char ROR(char mValue) {
        mValue = (char)(mValue >> 1);
        return mValue;
    }

    public static String numToString(String num) {
        StringBuffer str = new StringBuffer();
        char[] chAr = num.toCharArray();

        for(int i = 0; i < chAr.length; i += 3) {
            str.append((char)Integer.parseInt(chAr[i] + "" + chAr[i + 1] + "" + chAr[i + 2]));
        }

        return str.toString();
    }

    public static String stringToNum(String str) {
        StringBuffer ret = new StringBuffer();
        char[] chAr = str.toCharArray();

        for(int i = 0; i < chAr.length; ++i) {
            ret.append(BasicUtility.formatNumber((double)chAr[i], 3));
        }

        return ret.toString();
    }

    public static void main(String[] arg) {
        System.out.println(stringToNum(encryption("aisweb", SOMETHING)));
        System.out.println(stringToNum(encryption("web_321", SOMETHING)));
        System.out.println(stringToNum(encryption("starhub", SOMETHING)));
//004044045062045057036-018034056046042061055037042126085070-004044045062045057036
//spens: starhub  eqshgrsdb106 starhub
//pfingo talk 34:starhub eqshgrsdb106 grs
//pfingo talk 175:starhub eqshgrsdb106 starhub
        System.out.println(decryption(numToString("004044045062045057036"), SOMETHING));
        System.out.println(decryption(numToString("018034056046042061055037042126085070"), SOMETHING));
        System.out.println(decryption(numToString("004044045062045057036"), SOMETHING));

    }
}
