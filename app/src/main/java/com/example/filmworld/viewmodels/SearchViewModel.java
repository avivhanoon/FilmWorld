package com.example.filmworld.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.filmworld.repositories.SearchTVShowRepository;
import com.example.filmworld.response.TVShowResponse;

public class SearchViewModel extends ViewModel {
    private SearchTVShowRepository searchTVShowRepository;
    public SearchViewModel() {
        searchTVShowRepository = new SearchTVShowRepository();
    }
    public LiveData<TVShowResponse> searchTVShow(String query, int page) {
        return searchTVShowRepository.searchTVShow(query, page);
    }

}
