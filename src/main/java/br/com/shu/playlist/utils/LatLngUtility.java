package br.com.shu.playlist.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LatLngUtility {

    private static Pattern latitudePattern = Pattern
            .compile("^(\\+|-)?(?:90(?:(?:(\\.|\\,)0+)?)|(?:[0-9]|[1-8][0-9])(?:(?:(\\.|\\,)[0-9]+)?))$");
    private static Pattern longitudePattern = Pattern
            .compile(
                    "^(\\+|-)?(?:180(?:(?:(\\.|\\,)0+)?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:(\\.|\\,)[0-9]+)?))$");

    public static boolean isLatitudeValid(String value) {
        Matcher m = latitudePattern.matcher(value);
        return m.matches();
    }

    public static boolean isLongitudeValid(String value) {
        Matcher m = longitudePattern.matcher(value);
        return m.matches();
    }
}
