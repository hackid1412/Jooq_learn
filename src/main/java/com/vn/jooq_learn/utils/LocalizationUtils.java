package com.vn.jooq_learn.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;


import java.util.Locale;

@Component
public class LocalizationUtils {
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    public LocalizationUtils(MessageSource messageSource, LocaleResolver localeResolver) {
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
    }

    public String getLocalizedMessage(String messageKey, Object... params) {//spread operator
        HttpServletRequest request = WebUtils.getCurrentRequest();
        Locale locale = localeResolver.resolveLocale(request);
        return messageSource.getMessage(messageKey, params, locale);
    }
}