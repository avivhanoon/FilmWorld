package com.example.filmworld.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.filmworld.R;
import com.example.filmworld.adapters.WatchlistAdapter;
import com.example.filmworld.databinding.FragmentWatchlistBinding;
import com.example.filmworld.listeners.WatchlistListener;
import com.example.filmworld.models.TVShow;
import com.example.filmworld.utilities.TempDataHolder;
import com.example.filmworld.viewmodels.WatchlistViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WatchlistFragment extends Fragment implements WatchlistListener {
    private FragmentWatchlistBinding binding;
    private WatchlistViewModel viewModel;
    private View view;
    private WatchlistAdapter watchlistAdapter;
    private List<TVShow> watchlist;
    public WatchlistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_watchlist, container, false);
        view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        doInitialization();
        loadWatchlist();
    }

    private void doInitialization() {
        viewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        watchlist = new ArrayList<>();
    }

    private void loadWatchlist() {
        binding.setIsLoading(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.loadWatchlist().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShows -> {
                    binding.setIsLoading(false);
                    if (watchlist.size()>0) {
                        watchlist.clear();
                    }
                    watchlist.addAll(tvShows);
                    watchlistAdapter = new WatchlistAdapter(watchlist,this);
                    binding.tvShowFavoriteRecyclerView.setAdapter(watchlistAdapter);
                    binding.tvShowFavoriteRecyclerView.setVisibility(View.VISIBLE);
                    compositeDisposable.dispose();
                }));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (TempDataHolder.IS_WATCHLIST_UPDATED) {
            loadWatchlist();
            TempDataHolder.IS_WATCHLIST_UPDATED = false;
        }
    }

    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("tvshow", tvShow); // <-- This will pass the entire object
        Navigation.findNavController(view).navigate(R.id.action_watchlistFragment2_to_TVShowDetailsFragment , bundle);

    }

    @Override
    public void removeTVShowFromWtachlist(TVShow tvShow, int position) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.removeTVShowFromWatchlist(tvShow)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
            watchlist.remove(position);
            watchlistAdapter.notifyItemRemoved(position);
            watchlistAdapter.notifyItemRangeChanged(position, watchlistAdapter.getItemCount());
            compositeDisposable.dispose();
        }));
    }
}