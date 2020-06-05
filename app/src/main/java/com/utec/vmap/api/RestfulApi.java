package com.utec.vmap.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.util.HashMap;
import java.util.Map;

public class RestfulApi {
    private RequestQueue queue;
    public RestfulApi(Context context)
    {
        queue = Volley.newRequestQueue(context);
    }
    public void get(String url, ApiCallback action)
    {
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        action.OnSuccess(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        action.OnError(error.getMessage());
                    }
                }
        );
        queue.add(getRequest);
    }
    public void post(String url, HashMap<String, String> data, ApiCallback action)
    {
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        action.OnSuccess(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        action.OnError(error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                return data;
            }
        };
        queue.add(postRequest);
    }
}
