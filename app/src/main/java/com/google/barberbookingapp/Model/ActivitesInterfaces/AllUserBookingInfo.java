package com.google.barberbookingapp.Model.ActivitesInterfaces;

import com.google.barberbookingapp.Model.entities.BookingInformation;

public interface AllUserBookingInfo {
    void onBookingInfoLoadSuccess(BookingInformation information , String documentId);
    void  onBookingInfoLoadEmpty();
    void  onBookingInfoLoadFailed(String message);
}
