package com.example.filmworld.listeners;

import com.example.filmworld.models.TVShow;

public interface WatchlistListener {
    void onTVShowClicked (TVShow tvShow);

    void removeTVShowFromWtachlist(TVShow tvShow, int position);
}
