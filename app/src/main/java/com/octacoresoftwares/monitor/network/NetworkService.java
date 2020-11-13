package com.octacoresoftwares.monitor.network;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    public static NetworkApi createService(){
        return getRetrofitService().create(NetworkApi.class);
    }

    private static Retrofit getRetrofitService(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient())
                .build();
    }

    @NonNull
    private static OkHttpClient getClient(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chain -> {
                    Request original = chain.request();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("key", AUTH_KEY); // <-- this is the important line

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                })
                .build();
    }

    private static final String BASE_URL = "https://api.qa.arca-payments.network/";
    private static final String AUTH_KEY = "2os5C0TTSz4D9GgvqRmlEG3eVnVaOEKI";
}
