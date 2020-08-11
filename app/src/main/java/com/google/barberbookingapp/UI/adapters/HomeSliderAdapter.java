package com.google.barberbookingapp.UI.adapters;

import com.google.barberbookingapp.Model.entities.Banner;
import java.util.List;
import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class HomeSliderAdapter extends SliderAdapter {

    private List<Banner> bannerList;

    public HomeSliderAdapter(List<Banner> bannerList) {
        this.bannerList = bannerList;
    }

    @Override
    public int getItemCount() {
        return bannerList.size() ;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {

            imageSlideViewHolder.bindImageSlide(bannerList.get(position).getImage());
    }
}
