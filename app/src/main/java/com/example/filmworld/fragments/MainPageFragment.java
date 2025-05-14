package com.example.filmworld.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.filmworld.R;
import com.example.filmworld.adapters.TVShowAdapter;
import com.example.filmworld.databinding.FragmentMainPageBinding;
import com.example.filmworld.listeners.TVShowsListener;
import com.example.filmworld.models.TVShow;
import com.example.filmworld.viewmodels.MostPopularTVShowViewModel;

import java.util.ArrayList;
import java.util.List;
public class MainPageFragment extends Fragment implements TVShowsListener {
    private MostPopularTVShowViewModel viewModel;
    private RecyclerView mainPageRecyclerView;
    private FragmentMainPageBinding binding;
    public View view;
    private ProgressBar progressBar;
    private int currentPage = 1;
    private int totalAvailablePages = 1;


    private List<TVShow> tvShows = new ArrayList<>();
    private TVShowAdapter tvShowAdapter;
    public MainPageFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainPageBinding.inflate(inflater, container, false);
        view = binding.getRoot(); // שומרת על view לשימוש ב־Navigation
        mainPageRecyclerView = binding.tvShowRecyclerView;
        progressBar = binding.progressBar;
        doInitialization();
        return view;
    }

    private void doInitialization() {
        mainPageRecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularTVShowViewModel.class);
        tvShowAdapter = new TVShowAdapter(tvShows, this);
        mainPageRecyclerView.setAdapter(tvShowAdapter);
        progressBar.setVisibility(View.VISIBLE);  // Show progress bar
        mainPageRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mainPageRecyclerView.canScrollVertically(1)){
                    if (currentPage <= totalAvailablePages){
                        currentPage += 1;
                        getMostPopularTVShows();
                    }
                }
            }
        });
        binding.searchIconButton.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_mainPageFragment_to_searchFragment)
        );
        getMostPopularTVShows();
    }

    private void getMostPopularTVShows() {
        viewModel.getMostPopularTVShows(currentPage).observe(getViewLifecycleOwner(), mostPopularTVShowResponse -> {
            progressBar.setVisibility(View.GONE);
            if (mostPopularTVShowResponse != null) {
                totalAvailablePages = mostPopularTVShowResponse.getTotalPages();
                if(mostPopularTVShowResponse.getTvShows() != null){
                    int oldCount = tvShows.size();
                    tvShows.addAll(mostPopularTVShowResponse.getTvShows());
                    tvShowAdapter.notifyItemRangeInserted(oldCount, tvShows.size());
                }
            }
        });
    }

    @Override
    public void onShowTvShowClicked(TVShow tvShow) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("tvshow", tvShow);
        Navigation.findNavController(view).navigate(R.id.action_mainPageFragment_to_TVShowDetailsFragment, bundle);
    }

}