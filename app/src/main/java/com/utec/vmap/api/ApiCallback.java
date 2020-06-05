package com.utec.vmap.api;

import org.json.JSONObject;

public interface ApiCallback {
    void OnSuccess(String obj);
    void OnError(String error);
}
