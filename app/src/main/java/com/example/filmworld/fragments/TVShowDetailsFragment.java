package com.example.filmworld.fragments;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.filmworld.R;
import com.example.filmworld.adapters.ImageSliderAdapter;
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
        loadBasicTvShowDetails();
        getTVShowDetails();
        binding.imageViewBackButton.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigate(R.id.action_TVShowDetailsFragment_to_mainPageFragment));
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
            if (tvShowDetailsResponse.getTvShowDetails()!=null) {
                if (tvShowDetailsResponse.getTvShowDetails().getPictures()!= null) {
                    loadImageSlider(tvShowDetailsResponse.getTvShowDetails().getPictures());
                }
                binding.setTvShowImageURL(
                        tvShowDetailsResponse.getTvShowDetails().getImagePath()
                );
                binding.imageTvShow.setVisibility(View.VISIBLE);
            }
        });
    }
    private void loadImageSlider(String[] sliderImages) {
        binding.sliderViewPager.setOffscreenPageLimit(1);
        binding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderImages));
        binding.sliderViewPager.setVisibility(View.VISIBLE);
        binding.viewEdge.setVisibility(View.VISIBLE);
        setUpSliderIndicatrs(sliderImages.length);
        binding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setUpCurrentIndicators(position);
            }
        });
    }
    private void setUpSliderIndicatrs(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0,0,0,0);
        for (int i=0; i <indicators.length; i++) {
            indicators[i] = new ImageView(getActivity().getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.background_slider_indicator_inactive));
            indicators[i].setLayoutParams(layoutParams);
            binding.layoutSliderIndicators.addView(indicators[i]);
        }
        binding.layoutSliderIndicators.setVisibility(View.VISIBLE);
        setUpCurrentIndicators(0);
    }
    private void setUpCurrentIndicators(int position) {
        int childCount = binding.layoutSliderIndicators.getChildCount();
        for(int i = 0; i < childCount; i++){

            ImageView imageView = (ImageView) binding.layoutSliderIndicators.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.background_slider_indicator));
            }
            else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.background_slider_indicator_inactive));
            }
        }
    }
    private void loadBasicTvShowDetails() {
        Bundle args = getArguments();
        if (args != null) {
            // Set the text views with data from the bundle
            binding.setTvShowName(args.getString("name"));
            binding.setNetworkCountry(args.getString("network") + " (" + args.getString("country") + ")");
            binding.setStatus(args.getString("status"));
            binding.setStartDate(args.getString("startDate"));

            // Make text views visible
            binding.textTvName.setVisibility(View.VISIBLE);
            binding.textNetworkCountry.setVisibility(View.VISIBLE);
            binding.textStatus.setVisibility(View.VISIBLE);
            binding.textStartedAt.setVisibility(View.VISIBLE);
        }
    }
}