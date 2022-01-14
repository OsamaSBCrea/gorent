package com.example.gorent.util.network;

import android.util.Log;

import com.example.gorent.util.shared.SharedPreferencesKey;
import com.example.gorent.util.shared.SharedPreferencesManager;
import com.example.gorent.util.shared.keys.SharedKeys;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final SharedPreferencesManager sharedPreferencesManager;

    public AuthInterceptor(SharedPreferencesManager sharedPreferencesManager) {
        this.sharedPreferencesManager = sharedPreferencesManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String authorizationToken = sharedPreferencesManager.get(SharedPreferencesKey.AUTH_TOKEN, "");
        Response response;
        if (authorizationToken.isEmpty()) {
            response = chain.proceed(chain.request());
        } else {
            Request request = chain.request()
                    .newBuilder()
                    .addHeader(SharedKeys.AUTHORIZATION_HEADER, "Bearer " + authorizationToken)
                    .build();
            response = chain.proceed(request);
        }
        return response;
    }
}
