package com.gabrielbandeira.desafio_mobile.presenter;

import com.gabrielbandeira.desafio_mobile.view.MainActivity;
import com.gabrielbandeira.desafio_mobile.model.CardStatement;
import com.gabrielbandeira.desafio_mobile.model.CardUsage;
import com.gabrielbandeira.desafio_mobile.model.Profile;
import com.gabrielbandeira.desafio_mobile.model.Resume;
import com.gabrielbandeira.desafio_mobile.service.APIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIPresenter {
    public MainActivity mainView;
    private APIService apiService;

    public APIPresenter(MainActivity view) {
        this.mainView = view;

        if (this.apiService == null) {
            this.apiService = new APIService();
        }
    }

    public Object getCardStatement(int month,int year,int page) {
        apiService
            .getAPI()
            .getCardStatement(month,year,page)
            .enqueue(new Callback<CardStatement>() {
                @Override
                public void onResponse(Call<CardStatement> call, Response<CardStatement> response) {
                    CardStatement cardStatement = response.body();
                    if (cardStatement != null) {
                        mainView.cardStatementReady(cardStatement);
                    }
                }

                @Override
                public void onFailure(Call<CardStatement> call, Throwable t) {
                    try {
                        throw new InterruptedException("Something went wrong!");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        return this;
    }

    public Object getCardUsage() {
        apiService
                .getAPI()
                .getCardUsage()
                .enqueue(new Callback<List<CardUsage>>() {
                    @Override
                    public void onResponse(Call<List<CardUsage>> call, Response<List<CardUsage>> response) {
                        List<CardUsage> cardUsage = response.body();
                        if (cardUsage != null) {
                            mainView.cardUsageReady(cardUsage);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CardUsage>> call, Throwable t) {
                        try {
                            throw new InterruptedException("Something went wrong!");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
        return this;
    }

    public Object getProfile() {
        apiService
                .getAPI()
                .getProfile()
                .enqueue(new Callback<Profile>() {
                    @Override
                    public void onResponse(Call<Profile> call, Response<Profile> response) {
                        Profile profile = response.body();
                        if(profile!=null){
                            mainView.profileReady(profile);
                        }
                    }

                    @Override
                    public void onFailure(Call<Profile> call, Throwable t) {
                        try {
                            throw new InterruptedException("Something went wrong!");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
        return this;
    }

    public Object getResume() {
        apiService
                .getAPI()
                .getResume()
                .enqueue(new Callback<Resume>() {
                    @Override
                    public void onResponse(Call<Resume> call, Response<Resume> response) {
                        Resume resume = response.body();
                        if(resume!=null){
                            mainView.resumeReady(resume);
                        }
                    }

                    @Override
                    public void onFailure(Call<Resume> call, Throwable t) {
                        try {
                            throw new InterruptedException("Something went wrong!");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
        return this;
    }

    public APIService getApiService() {
        return apiService;
    }

    public void setApiService(APIService apiService) {
        this.apiService = apiService;
    }
}
