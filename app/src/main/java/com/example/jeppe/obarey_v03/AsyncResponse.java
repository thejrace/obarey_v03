package com.example.jeppe.obarey_v03;

import org.json.JSONObject;

/**
 * Created by Jeppe on 06.03.2016.
 * Async PostExecute tan donen parametreyi almak icin
 */
public interface AsyncResponse {
    void processFinishJSON(JSONObject output);
}

