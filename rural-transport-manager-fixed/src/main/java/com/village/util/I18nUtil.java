package com.village.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18nUtil {
    private static final ResourceBundle en = ResourceBundle.getBundle("messages", Locale.forLanguageTag("en"));
    private static final ResourceBundle ta = ResourceBundle.getBundle("messages", Locale.forLanguageTag("ta"));

    public static String get(String key, Locale locale) {
        if (locale == null) locale = Locale.forLanguageTag("en");
        try {
            if ("ta".equals(locale.getLanguage())) return ta.getString(key);
            return en.getString(key);
        } catch (Exception e) {
            return key;
        }
    }
}
