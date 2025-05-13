package com.example.filmworld.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.filmworld.R;
import com.example.filmworld.adapters.EpisodeAdapter;
import com.example.filmworld.adapters.ImageSliderAdapter;
import com.example.filmworld.databinding.FragmentTVShowDetailsBinding;
import com.example.filmworld.databinding.LayoutEpisodesBottomSheetBinding;
import com.example.filmworld.models.TVShow;
import com.example.filmworld.utilities.TempDataHolder;
import com.example.filmworld.viewmodels.TVShowDetailsViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.Locale;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TVShowDetailsFragment extends Fragment {
    private FragmentTVShowDetailsBinding binding;
    private TVShowDetailsViewModel tvShowDetailsViewModel;
    private BottomSheetDialog episodeBottomSheetDialog;
    private LayoutEpisodesBottomSheetBinding layoutEpisodesBottomSheetBinding;
    private static final String TAG = "TVShowDetailsFragment";
    private TVShow tvShow;
    private Boolean isTVShowAvailabeInWatchlist = false;

    public TVShowDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTVShowDetailsBinding.inflate(inflater, container, false);
        doInitialization();
        return binding.getRoot();
    }

    private void doInitialization() {
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
        if (getArguments() != null && getArguments().containsKey("tvshow")) {
            tvShow = (TVShow) getArguments().getSerializable("tvshow");
        }
        checkTVShowInWatchlist();
        if (tvShow != null) {
            getTVShowDetails();
        } else {
            Log.e(TAG, "tvShow object is null!");
            Toast.makeText(getContext(), "Unable to load TV Show details", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkTVShowInWatchlist() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(tvShowDetailsViewModel.getTVShowFromWatchlist(String.valueOf(tvShow.getId()))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShow -> {
                    isTVShowAvailabeInWatchlist = true;
                    binding.imageWatchList.setImageResource(R.drawable.ic_check);
                    compositeDisposable.dispose();
                }, throwable -> {
                    Log.e(TAG, "Error checking watchlist", throwable);
                    compositeDisposable.dispose();
                }));
    }

    private void getTVShowDetails() {
        binding.setIsLoading(true);
        String tvShowId = String.valueOf(tvShow.getId());

        tvShowDetailsViewModel.getTVShowDetails(tvShowId).observe(getViewLifecycleOwner(), tvShowDetailsResponse -> {
            binding.setIsLoading(false);

            if (tvShowDetailsResponse == null) {
                Log.e(TAG, "Received null TVShowDetailsResponse");
                Toast.makeText(getContext(), "Error loading show details", Toast.LENGTH_SHORT).show();
                return;
            }

            if (tvShowDetailsResponse.getTvShowDetails() != null) {
                if (tvShowDetailsResponse.getTvShowDetails().getPictures() != null) {
                    loadImageSlider(tvShowDetailsResponse.getTvShowDetails().getPictures());
                }

                binding.setTvShowImageURL(tvShowDetailsResponse.getTvShowDetails().getImagePath());
                binding.imageTvShow.setVisibility(View.VISIBLE);

                String description = tvShowDetailsResponse.getTvShowDetails().getDescription();
                if (description != null && !description.trim().isEmpty()) {
                    binding.descriptionTextView.setText(HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else {
                    binding.descriptionTextView.setText("No description available.");
                }

                binding.descriptionTextView.setVisibility(View.VISIBLE);
                binding.textReadMore.setVisibility(View.VISIBLE);

                binding.textReadMore.setOnClickListener(v -> {
                    if (binding.textReadMore.getText().toString().equals("Read More...")) {
                        binding.descriptionTextView.setMaxLines(Integer.MAX_VALUE);
                        binding.descriptionTextView.setEllipsize(null);
                        binding.textReadMore.setText("Read Less...");
                    } else {
                        binding.descriptionTextView.setMaxLines(4);
                        binding.descriptionTextView.setEllipsize(TextUtils.TruncateAt.END);
                        binding.textReadMore.setText("Read More...");
                    }
                });

                binding.setRating(String.format(Locale.getDefault(), "%.2f", Double.parseDouble(tvShowDetailsResponse.getTvShowDetails().getRating())));
                binding.setGenre(tvShowDetailsResponse.getTvShowDetails().getGenres() != null ? tvShowDetailsResponse.getTvShowDetails().getGenres()[0] : "N/A");
                binding.setRuntime(tvShowDetailsResponse.getTvShowDetails().getRunTime() + " Min");

                binding.viewOivider1.setVisibility(View.VISIBLE);
                binding.layoutMisc.setVisibility(View.VISIBLE);
                binding.viewDivider2.setVisibility(View.VISIBLE);
                binding.dot1.setVisibility(View.VISIBLE);
                binding.dot2.setVisibility(View.VISIBLE);
                binding.ratingImageView.setVisibility(View.VISIBLE);

                binding.buttonWebsite.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(tvShowDetailsResponse.getTvShowDetails().getUrl()));
                    startActivity(intent);
                });

                binding.buttonWebsite.setVisibility(View.VISIBLE);
                binding.buttonEpisodes.setVisibility(View.VISIBLE);
                binding.buttonEpisodes.setOnClickListener(v -> {
                    if (episodeBottomSheetDialog == null) {
                        episodeBottomSheetDialog = new BottomSheetDialog(requireContext());
                        layoutEpisodesBottomSheetBinding = DataBindingUtil.inflate(
                                LayoutInflater.from(requireContext()),
                                R.layout.layout_episodes_bottom_sheet,
                                binding.getRoot().findViewById(R.id.episodeContainer),
                                false
                        );
                        episodeBottomSheetDialog.setContentView(layoutEpisodesBottomSheetBinding.getRoot());
                        layoutEpisodesBottomSheetBinding.episodesRecyclerView.setAdapter(
                                new EpisodeAdapter(tvShowDetailsResponse.getTvShowDetails().getEpisodes())
                        );

                        layoutEpisodesBottomSheetBinding.textTitle.setText(
                                String.format("Episodes | %s", tvShow != null && tvShow.getName() != null ? tvShow.getName() : "N/A")
                        );
                        layoutEpisodesBottomSheetBinding.imageClose.setOnClickListener(v1 -> episodeBottomSheetDialog.dismiss());
                    }
                    episodeBottomSheetDialog.show();
                });
            }
        });

        binding.imageWatchList.setOnClickListener(v -> {
            CompositeDisposable compositeDisposable = new CompositeDisposable();
            if (isTVShowAvailabeInWatchlist) {
                compositeDisposable.add(tvShowDetailsViewModel.removeTVShowFromWtachlist(tvShow)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            isTVShowAvailabeInWatchlist = false;
                            TempDataHolder.IS_WATCHLIST_UPDATED = true;
                            binding.imageWatchList.setImageResource(R.drawable.ic_favorites);
                            Toast.makeText(getContext(), "Removed From Favorites!", Toast.LENGTH_SHORT).show();
                            compositeDisposable.dispose();
                        }, throwable -> {
                            Log.e(TAG, "Failed to remove from watchlist", throwable);
                            compositeDisposable.dispose();
                        }));
            } else {
                compositeDisposable.add(tvShowDetailsViewModel.addToWatchlist(tvShow)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            TempDataHolder.IS_WATCHLIST_UPDATED = true;
                            binding.imageWatchList.setImageResource(R.drawable.ic_check);
                            Toast.makeText(getContext(), "Added to watchlist", Toast.LENGTH_SHORT).show();
                            compositeDisposable.dispose();
                        }, throwable -> {
                            throwable.printStackTrace();
                            Toast.makeText(getContext(), "Failed to add to watchlist", Toast.LENGTH_SHORT).show();
                            compositeDisposable.dispose();
                        }));
            }
        });

        binding.imageWatchList.setVisibility(View.VISIBLE);
        loadBasicTvShowDetails();
    }

    private void loadImageSlider(String[] sliderImages) {
        binding.sliderViewPager.setOffscreenPageLimit(1);
        binding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderImages));
        binding.sliderViewPager.setVisibility(View.VISIBLE);
        binding.viewEdge.setVisibility(View.VISIBLE);
        setUpSliderIndicators(sliderImages.length);

        binding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setUpCurrentIndicators(position);
            }
        });
    }

    private void setUpSliderIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(requireContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.background_slider_indicator_inactive));
            indicators[i].setLayoutParams(layoutParams);
            binding.layoutSliderIndicators.addView(indicators[i]);
        }
        binding.layoutSliderIndicators.setVisibility(View.VISIBLE);
        setUpCurrentIndicators(0);
    }

    private void setUpCurrentIndicators(int position) {
        int childCount = binding.layoutSliderIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) binding.layoutSliderIndicators.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.background_slider_indicator));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.background_slider_indicator_inactive));
            }
        }
    }

    private void loadBasicTvShowDetails() {
        if (tvShow != null) {
            binding.setTvShowName(tvShow.getName());
            binding.setNetworkCountry(tvShow.getNetwork() + " (" + tvShow.getCountry() + ")");
            binding.setStatus(tvShow.getStatus());
            binding.setStartDate(tvShow.getStartDate());
            binding.textTvName.setVisibility(View.VISIBLE);
            binding.textNetworkCountry.setVisibility(View.VISIBLE);
            binding.textStatus.setVisibility(View.VISIBLE);
            binding.textStartedAt.setVisibility(View.VISIBLE);
        }
    }
}
