package com.URLShortener.worker.domain;

import org.json.simple.JSONObject;

public class Metric {


    boolean valid;
    String shortedUrl;
    String url;
    int clicks;

    public Metric(JSONObject o) {
        this.valid = o.get("valid").toString().equals("true");
        this.shortedUrl = o.get("shortedUrl").toString();
        this.url = o.get("url").toString();
        this.clicks = Integer.parseInt(o.get("clicks").toString());
    }

    public Metric(String url, String shortedUrl, int clicks, boolean valid) {
        this.valid = valid;
        this.shortedUrl = shortedUrl;
        this.url = url;
        this.clicks = clicks;
    }

    @Override
    public String toString() {
        JSONObject obj = new JSONObject();
        obj.put("valid", this.valid);
        obj.put("shortedUrl", this.shortedUrl);
        obj.put("url", this.url);
        obj.put("clicks", this.clicks);

        return obj.toString();
    }
}
