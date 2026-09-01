package com.yorker.fanzania.views.shared;

import android.content.Context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    public Validation(Context context) {
        Context context1 = context;
    }

    public Integer Emailvalidation(String email) {

        if (email != null && !email.trim().isEmpty()) {
            if (isValidEmail(email)) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 2;
        }
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[A-Za-z0-9\\+]+([\\.|\\_|\\-][A-Za-z0-9]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean PasswordValidation(String str_password) {
        boolean value = false;

        value = str_password.length() >= 6 && isValidPassword(str_password);

        return value;
    }

    private boolean isValidPassword(final String password) {
//        Pattern pattern;
//        Matcher matcher;
//        final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#!^?$%&\"]).{6,40})";
//        final String PASSWORD_PATTERN = "(8,40)";
//        pattern = Pattern.compile(PASSWORD_PATTERN);
//        matcher = pattern.matcher(password);

//        System.out.println("Matcher result " + matcher.matches());

        return password.length() >= 8;
    }

    public Boolean StringChecking(String str_text) {
        Boolean value = false;
        value = str_text != null && !str_text.trim().isEmpty();
        return value;
    }

}
