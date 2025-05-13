package com.example.filmworld.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmworld.R;
import com.example.filmworld.databinding.LayoutContainerTvBinding;
import com.example.filmworld.listeners.WatchlistListener;
import com.example.filmworld.models.TVShow;

import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.TVShowViewHolder> {

    private final List<TVShow> tvShows;
    private LayoutInflater layoutInflater;
    private WatchlistListener watchlistListener;
    public WatchlistAdapter(List<TVShow> tvShows, WatchlistListener watchlistListener) {
        this.tvShows = tvShows;
        this.watchlistListener = watchlistListener ;
    }

    @NonNull
    @Override
    public TVShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LayoutContainerTvBinding tvShowBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.layout_container_tv, parent, false
        );
        return new TVShowViewHolder(tvShowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowViewHolder holder, int position) {
        holder.bindTVShow(tvShows.get(position));
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

     class TVShowViewHolder extends RecyclerView.ViewHolder {

        private final LayoutContainerTvBinding layoutContainerTvBinding;
        public TVShowViewHolder(LayoutContainerTvBinding layoutContainerTvBinding) {
            super(layoutContainerTvBinding.getRoot());
            this.layoutContainerTvBinding = layoutContainerTvBinding;

        }
        public void bindTVShow (TVShow tvShow){
            layoutContainerTvBinding.setTvShow(tvShow);
            layoutContainerTvBinding.executePendingBindings();
            layoutContainerTvBinding.getRoot().setOnClickListener(v -> watchlistListener.onTVShowClicked(tvShow));
            layoutContainerTvBinding.imageDelete.setOnClickListener(v -> watchlistListener.removeTVShowFromWtachlist(tvShow, getAdapterPosition()));
            layoutContainerTvBinding.imageDelete.setVisibility(View.VISIBLE);
        }
    }
}
