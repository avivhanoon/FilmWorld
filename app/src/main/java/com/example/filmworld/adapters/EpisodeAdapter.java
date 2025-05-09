package com.example.filmworld.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmworld.R;
import com.example.filmworld.databinding.ItemContainerEpisodeBinding;
import com.example.filmworld.models.Episode;

import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>{
    private List<Episode> episode;
    private LayoutInflater layoutInflater;

    public EpisodeAdapter(List<Episode> episode) {
        this.episode = episode;
    }

    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerEpisodeBinding itemContainerEpisodeBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_container_episode, parent, false
        );
        return new EpisodeViewHolder(itemContainerEpisodeBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {
        holder.bindEpisode(episode.get(position));
    }

    @Override
    public int getItemCount() {
        return episode.size();
    }

    static class EpisodeViewHolder extends RecyclerView.ViewHolder {
        private ItemContainerEpisodeBinding itemContainerEpisodeBinding;

        public EpisodeViewHolder(ItemContainerEpisodeBinding itemContainerEpisodeBinding) {
            super(itemContainerEpisodeBinding.getRoot());
            this.itemContainerEpisodeBinding = itemContainerEpisodeBinding;
        }

        public void bindEpisode(Episode episode) {
            // Set the episode name - THIS IS THE KEY ADDITION
            itemContainerEpisodeBinding.setName(episode.getName());

            // Format season number with leading zero if needed
            String season = episode.getSeason();
            if(season.length() == 1) {
                season = "0".concat(season);
            }

            // Format episode number with leading zero if needed
            String episodeNumber = episode.getEpisode();
            if(episodeNumber.length() == 1) {
                episodeNumber = "0".concat(episodeNumber);
            }

            // Create the season and episode display format (S01E02)
            String title = "S" + season + "E" + episodeNumber;
            itemContainerEpisodeBinding.setTitle(title);

            // Set the air date
            itemContainerEpisodeBinding.setAirDate(episode.getAirDate());
        }
    }
}