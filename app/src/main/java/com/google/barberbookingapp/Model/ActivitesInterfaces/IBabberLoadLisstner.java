package com.google.barberbookingapp.Model.ActivitesInterfaces;

import com.google.barberbookingapp.Model.entities.Banner;

import java.util.List;

public interface IBabberLoadLisstner {

    void onBannerLoadSucceded(List<Banner> bannerList);
    void onBannerLoadFailed(String message);
}
