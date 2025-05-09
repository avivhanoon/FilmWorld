package com.example.filmworld.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.filmworld.database.TVShowDatabase;
import com.example.filmworld.models.TVShow;
import com.example.filmworld.repositories.TVShowDetailsRepository;
import com.example.filmworld.response.TVShowDetailsResponse;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class TVShowDetailsViewModel extends AndroidViewModel {

    private TVShowDatabase tvShowDatabase;
    private TVShowDetailsRepository tvShowDetailsRepository;
    public TVShowDetailsViewModel(@NonNull Application application) {
        super(application);
        tvShowDetailsRepository = new TVShowDetailsRepository();
        tvShowDatabase = TVShowDatabase.getTvShowDatabase(application);
    }
    public LiveData<TVShowDetailsResponse> getTVShowDetails(String tvShowId) {
        return tvShowDetailsRepository.getTVShowDetails(tvShowId);
    }
    public Completable addToWatchlist(TVShow tvShow) {
        return tvShowDatabase.tvShowDao().addToWatchlist(tvShow);
    }

}
