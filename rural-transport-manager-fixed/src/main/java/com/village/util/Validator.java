package com.village.util;

import java.util.regex.Pattern;

public class Validator {
    private static final Pattern PHONE = Pattern.compile("^[0-9]{6,13}$");
    private static final Pattern PLATE = Pattern.compile("^[A-Z]{2}-[0-9]{2}-[A-Z]{1,2}-[0-9]{1,4}$");

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE.matcher(phone).matches();
    }

    public static boolean isValidPlate(String plate) {
        return plate != null && PLATE.matcher(plate).matches();
    }
}
