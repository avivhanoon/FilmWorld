package com.example.filmworld.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.filmworld.R;
import com.example.filmworld.viewmodels.MostPopularTVShowViewModel;

import java.util.zip.Inflater;

public class MainPageFragment extends Fragment {
    private MostPopularTVShowViewModel viewModel;
    public View view;
    public MainPageFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_page, container, false);
        viewModel = new ViewModelProvider(this).get(MostPopularTVShowViewModel.class);
        return view;
    }

    private void getMostPopularTVShows() {
        viewModel.getMostPopularTVShows(0).observe(this, mostPopularTVShowResponse -> {
            //TODO: FINISH
                }
        );
    }
}