package com.example.filmworld.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.databinding.DataBindingUtil;

import com.example.filmworld.R;
import com.example.filmworld.databinding.LayoutContainerTvBinding;
import com.example.filmworld.listeners.TVShowsListener;
import com.example.filmworld.models.TVShow;

import java.util.List;

public class TVShowAdapter extends RecyclerView.Adapter<TVShowAdapter.TVShowViewHolder> {

    private final List<TVShow> tvShows;
    private LayoutInflater layoutInflater;
    private TVShowsListener tvShowsListener;
    public TVShowAdapter(List<TVShow> tvShows, TVShowsListener tvShowsListener) {
        this.tvShows = tvShows;
        this.tvShowsListener = tvShowsListener;
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
            layoutContainerTvBinding.getRoot().setOnClickListener(v -> tvShowsListener.onShowTvShowClicked(tvShow));
        }
    }
}
