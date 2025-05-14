package com.example.filmworld.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmworld.R;
import com.example.filmworld.adapters.TVShowAdapter;
import com.example.filmworld.databinding.FragmentSearchBinding;
import com.example.filmworld.listeners.TVShowsListener;
import com.example.filmworld.models.TVShow;
import com.example.filmworld.viewmodels.SearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchFragment extends Fragment implements TVShowsListener {

    private FragmentSearchBinding binding;
    private SearchViewModel viewModel;
    private List<TVShow> tvShows = new ArrayList<>();
    private TVShowAdapter tvShowAdapter;
    private int currentPage = 1;
    private int totalAvailablePage = 1;
    private Timer timer;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        doInitialization();
        return binding.getRoot();
    }

    private void doInitialization() {
        // חזרה למסך הראשי
        binding.imageBack.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_searchFragment_to_mainPageFragment)
        );

        binding.tvShowRecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        tvShowAdapter = new TVShowAdapter(tvShows, this);
        binding.tvShowRecyclerView.setAdapter(tvShowAdapter);

        binding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().isEmpty()) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                currentPage = 1;
                                totalAvailablePage = 1;
                                tvShows.clear();
                                tvShowAdapter.notifyDataSetChanged();
                                searchTVShow(s.toString());
                            });
                        }
                    }, 800);
                } else {
                    tvShows.clear();
                    tvShowAdapter.notifyDataSetChanged();
                }
            }
        });

        binding.tvShowRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!binding.tvShowRecyclerView.canScrollVertically(1)) {
                    if (!binding.inputSearch.getText().toString().isEmpty()) {
                        if (currentPage < totalAvailablePage) {
                            currentPage += 1;
                            searchTVShow(binding.inputSearch.getText().toString());
                        }
                    }
                }
            }
        });

        binding.inputSearch.requestFocus();
    }

    private void searchTVShow(String query) {
        toggleLoading();
        viewModel.searchTVShow(query, currentPage).observe(getViewLifecycleOwner(), tvShowResponse -> {
            toggleLoading();
            if (tvShowResponse != null && tvShowResponse.getTvShows() != null) {
                int oldCount = tvShows.size();
                tvShows.addAll(tvShowResponse.getTvShows());
                tvShowAdapter.notifyItemRangeInserted(oldCount, tvShows.size());
                totalAvailablePage = tvShowResponse.getTotalPages();
            }
        });
    }

    private void toggleLoading() {
        if (currentPage == 1) {
            binding.setIsLoading(binding.getIsLoading() == null || !binding.getIsLoading());
        } else {
            binding.setIsLoadingMore(binding.getIsLoadingMore() == null || !binding.getIsLoadingMore());
        }
    }

    @Override
    public void onShowTvShowClicked(TVShow tvShow) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("tvshow", tvShow);
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_searchFragment_to_TVShowDetailsFragment, bundle);
    }
}
