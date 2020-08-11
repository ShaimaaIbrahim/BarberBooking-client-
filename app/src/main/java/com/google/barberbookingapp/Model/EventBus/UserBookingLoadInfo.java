package com.google.barberbookingapp.Model.EventBus;

import com.google.barberbookingapp.Model.entities.BookingInformation;

import java.util.List;

public class UserBookingLoadInfo {

    private boolean b ;
    private String message;
    private List<BookingInformation > bookingInformationList;

    public void setB(boolean b) {
        this.b = b;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setBookingInformationList(List<BookingInformation> bookingInformationList) {
        this.bookingInformationList = bookingInformationList;
    }

    public UserBookingLoadInfo(boolean b, String message) {

        this.b=b;
        this.message=message;
    }

    public boolean isB() {
        return b;
    }

    public String getMessage() {
        return message;
    }

    public List<BookingInformation> getBookingInformationList() {
        return bookingInformationList;
    }

    public UserBookingLoadInfo(boolean b, List<BookingInformation> bookingInformationList) {
        this.b = b;
        this.bookingInformationList = bookingInformationList;
    }
}
