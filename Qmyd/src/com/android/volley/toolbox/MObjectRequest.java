///*
// * Copyright (C) 2011 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.android.volley.toolbox;
//
//import com.ak.qmyd.User;
//import com.alibaba.fastjson.JSON;
//import com.android.volley.NetworkResponse;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.Response.ErrorListener;
//import com.android.volley.Response.Listener;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import java.io.UnsupportedEncodingException;
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//
///**
// * A canned request for retrieving the response body at a given URL as a String.
// */
//public class MObjectRequest<T> extends Request<T> {
//    private final Listener<T> mListener;
//
//    /**
//     * Creates a new request with the given method.
//     *
//     * @param method the request {@link Method} to use
//     * @param url URL to fetch the string at
//     * @param listener Listener to receive the String response
//     * @param errorListener Error listener, or null to ignore errors
//     */
//    public MObjectRequest(int method, String url, Listener<T> listener,
//            ErrorListener errorListener) {
//        super(method, url, errorListener);
//        mListener = listener;
//    }
//
//    /**
//     * Creates a new GET request.
//     *
//     * @param url URL to fetch the string at
//     * @param listener Listener to receive the String response
//     * @param errorListener Error listener, or null to ignore errors
//     */
//    public MObjectRequest(String url, Listener<T> listener, ErrorListener errorListener) {
//        this(Method.GET, url, listener, errorListener);
//    }
//
//    @Override
//    protected void deliverResponse(T response) {
//        mListener.onResponse(response);
//    }
//
//    @Override
//    protected Response<T> parseNetworkResponse(NetworkResponse response) {
//        String parsed;
//        try {
//            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//        } catch (UnsupportedEncodingException e) {
//            parsed = new String(response.data);
//        }
//        Gson gson=new Gson();
//        
//       User mu= gson.fromJson(parsed,User.class);
//        
//        Type type = new TypeToken<T>() {}.getType();
//        T u= gson.fromJson(parsed, type);//’‚¿Ô¥Ìµƒ
//        
//        JSON.parseObject("", type);
//        
//        return null;
//       // return Response.success(u, HttpHeaderParser.parseCacheHeaders(response));
//    }
//}
