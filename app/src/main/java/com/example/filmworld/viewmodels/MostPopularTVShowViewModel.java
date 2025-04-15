package com.example.filmworld.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.filmworld.repositories.MostPopularTVShowRepository;
import com.example.filmworld.response.TVShowResponse;

public class MostPopularTVShowViewModel extends ViewModel {
    private MostPopularTVShowRepository mostPopulatTVShowRepository;

    public MostPopularTVShowViewModel(){
        mostPopulatTVShowRepository = new MostPopularTVShowRepository();

    }

    public LiveData<TVShowResponse> getMostPopularTVShows(int page) {
        return mostPopulatTVShowRepository.getMostPopularTVShows(page);
    }
}
