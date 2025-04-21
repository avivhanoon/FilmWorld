package com.example.filmworld.fragments;

import static android.content.Intent.getIntent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.filmworld.R;
import com.example.filmworld.databinding.FragmentTVShowDetailsBinding;
import com.example.filmworld.viewmodels.TVShowDetailsViewModel;

public class TVShowDetailsFragment extends Fragment {
    private FragmentTVShowDetailsBinding binding;
    private TVShowDetailsViewModel tvShowDetailsViewModel;
    public TVShowDetailsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTVShowDetailsBinding.inflate(inflater, container, false);
        doInitialization();
        return binding.getRoot();
    }
    private void doInitialization() {
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
        getTVShowDetails();
    }
    private void getTVShowDetails() {
        binding.setIsLoading(true);
        String tvShowId = "";
        Bundle args = getArguments();
        if (args != null) {
            tvShowId = String.valueOf(args.getInt("id", -1));
        }
        tvShowDetailsViewModel.getTVShowDetails(tvShowId).observe(this, tvShowDetailsResponse -> {
            binding.setIsLoading(false);
            //TODO: FINISH
        });
    }
}