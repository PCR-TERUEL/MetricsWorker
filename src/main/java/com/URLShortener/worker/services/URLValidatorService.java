package com.URLShortener.worker.services;

import org.apache.commons.validator.routines.UrlValidator;

import java.net.HttpURLConnection;
import java.net.URL;

public class URLValidatorService {

    private final String url;

    public URLValidatorService(String url) {
        this.url = url;
    }

    public boolean isValid() {
        UrlValidator urlValidator = new UrlValidator(new String[] {"http",
                "https"});
        if (urlValidator.isValid(url)) {
            try {
                HttpURLConnection http = (HttpURLConnection)new URL(url).openConnection();
                http.setConnectTimeout(10000);
                if (http.getResponseCode() == 200) {
                    return true;
                }
            } catch (Exception e) { /* return false */ }
        }else{
            System.out.println("NO ES VALIDA");
        }

        return false;
    }

}
