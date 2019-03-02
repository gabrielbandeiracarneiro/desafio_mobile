package com.gabrielbandeira.desafio_mobile.service;

import com.gabrielbandeira.desafio_mobile.model.CardUsage;
import com.gabrielbandeira.desafio_mobile.model.Resume;
import com.gabrielbandeira.desafio_mobile.model.Profile;
import com.gabrielbandeira.desafio_mobile.model.CardStatement;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("/dev/resume")
    Call<Resume> getResume();

    @GET("/dev/card-usage")
    Call<List<CardUsage>> getCardUsage();

    @GET("/dev/users/profile")
    Call<Profile> getProfile();

    @GET("/dev/card-statement?")
    Call<CardStatement> getCardStatement(@Query("month") int month, @Query("year") int year, @Query("page") int page);

}