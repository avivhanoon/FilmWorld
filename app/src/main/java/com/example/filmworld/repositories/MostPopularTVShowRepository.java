package com.example.filmworld.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.filmworld.network.ApiClient;
import com.example.filmworld.network.ApiService;
import com.example.filmworld.response.TVShowResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostPopularTVShowRepository {
    private ApiService apiService;
    public MostPopularTVShowRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TVShowResponse> getMostPopularTVShows( int page) {
        MutableLiveData<TVShowResponse> data = new MutableLiveData<>();
        apiService.getMostPopularTVShows(page).enqueue(new Callback<TVShowResponse>() {
            @Override
            public void onResponse(@NonNull Call<TVShowResponse> call,@NonNull Response<TVShowResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TVShowResponse> call,@NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
