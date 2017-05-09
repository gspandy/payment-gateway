package com.joker.microservice.regtest;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/5/9.
 */
public class Regex {

    @Test
    public void URL() {
        Pattern pattern = Pattern.compile("((ht|f)tps?):\\/\\/[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-\\.,@?^=%&:\\/~\\+#]*[\\w\\-\\@?^=%&\\/~\\+#])?");
        Matcher matcher = pattern.matcher("http://blog.csdssn.net/cclovett/article/details/12448843.html");
        System.out.println(matcher.matches());

    }
}
