package com.umsign.app.api;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.*;

public class ApiUtils {

    private ApiUtils() {
    }

    public static Map<String, List<String>> splitQuery(String query) {
        if(query == null || "".equals((query))) {
            return Collections.emptyMap();
        }

        return Pattern.compile("&").splitAsStream(query)
                .map(s -> Arrays.copyOf(s.split("="), 2))
                .collect(groupingBy(s -> decode(s[0]), mapping(s -> decode(s[1]), toList())));
    }

    public static Map<String, String> getQueryMap(String query) {
        if(query == null || "".equals((query))) {
            return Collections.emptyMap();
        }

        return Pattern.compile("&").splitAsStream(query)
                .map(s -> Arrays.copyOf(s.split("="), 2))
                .collect(toMap(s -> decode(s[0]), s -> decode(s[1])));
    }

    /*
    * splitAsStream example
    * String str = "java,scala,kotlin";
    * Pattern.compile(",")
    * .splitAsStream(str)
    * .forEach(System.out::println);
    * output > java
    * output > php
    * output > asp
    * */

    private static String decode(final String encoded) {
        try {
            return encoded == null ? null : URLDecoder.decode(encoded, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is a required encoding", e);
        }
    }
}
