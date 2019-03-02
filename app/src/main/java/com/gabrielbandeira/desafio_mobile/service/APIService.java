package com.gabrielbandeira.desafio_mobile.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIService {
    private Retrofit retrofit = null;

    public APIInterface getAPI() {
        String BASE_URL = "https://2hm1e5siv9.execute-api.us-east-1.amazonaws.com";

        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(APIInterface.class);
    }
}
