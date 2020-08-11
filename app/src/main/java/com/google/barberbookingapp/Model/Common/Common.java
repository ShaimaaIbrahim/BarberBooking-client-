package com.google.barberbookingapp.Model.Common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.barberbookingapp.Model.entities.Barber;
import com.google.barberbookingapp.Model.entities.BookingInformation;
import com.google.barberbookingapp.Model.entities.Salon;
import com.google.barberbookingapp.Model.entities.User;
import com.google.barberbookingapp.R;
import com.google.barberbookingapp.UI.activities.HomeActivity;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.Document;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.constraintlayout.widget.Barrier;
import io.paperdb.Paper;

public  class Common {


    public static final String KEY_ENABLE_NEXT = "ENABLE_NEXT";
    public static final String KEY_SALON = "SALON_SAVE";

    public static final String BARBERS_LIST ="BARBERS" ;
    public static final String KEY_BARBER_LOAD_DONE =  "LOAD_DONE";
    public static final String KEY_DISPLAY_TIME_SLOT ="TIME_SLOT" ;
    public static final String STEP = "STEP" ;
    public static final String KEY_BARBER_SELECTED = "BARBER_SELECTED";
    public static final int TIME_SLOT_TOTAL = 20;
    public static final Object DISPLAY_TAG = "DISPLAY_TAG ";
    public static final String KEY_SLOT_OF_TIME = "INDEX_SLOT_OF_TIME";
    public static final String KEY_CONFIRM_BOOKING = "KEY_CONFIRM_BOOKING";
    public static final String EVENT_URI_CASHE = "URI_CASHE";
    public static final String LOGGED_KEY = "LOGGED_KEY";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";
    public static final String RATING_INFORMATION_KEY = "rating_information";

    public static String IS_LOGIN;
    public static User currentUser ;
    public static Salon currentSalon;
    public static Barber currentBarber;
    public static int step =0 ;
    public static String city;
    public static int currentTimeSlot = -1;
    public static Calendar bookingDate = Calendar.getInstance();
    public static SimpleDateFormat simpleFormatDate = new SimpleDateFormat("dd_MM_yyy") ;
    public static BookingInformation currentBooking;
    public static String currentBookingId;

    public static final String RATING_STATE_KEY = "rating_state";
    public static final String RATING_SALON_ID = "rating_salon";
    public static final String RATING_SALON_NAME = "rating_salon_name";
    public static final String RATING_BARBER_ID = "rating_barber_id" ;


    public Common() {

    }

    public static Barber getCurrentBarber() {
        return currentBarber;
    }

    public static void setCurrentBarber(Barber currentBarber) {
        Common.currentBarber = currentBarber;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Common.currentUser = currentUser;
    }

    public static String getIsLogin() {
        return IS_LOGIN;
    }

    public static void setIsLogin(String isLogin) {
        IS_LOGIN = isLogin;
    }

    public static String ConvertTimeSlotsToString(int position) {
        switch (position){
            case 0 :
                return "9:00_9:30";
            case 1 :
                return "9:30_10:00";
            case 2 :
                return "10:00_10:30";

            case 3 :
                return "10:30_11:00";
            case 4:
                return "11:00_11:30";
            case 5 :
                return "11:30_12:00";
            case 6 :
                return "12:00_12:30";
            case 7 :
                return "12:30_13:00";
            case 8 :
                return "13:00_13:30";

            case 9 :
                return "13:30_14:00";
            case 10 :
                return "14:00_14:30";
            case 11 :
                return "14:30_15:00";
            case 12 :
                return "15:00_15:30";
            case 13 :
                return "15:30_16:00";
            case 14 :
                return "16:00_16:30";

            case 15 :
                return "16:30_17:00";
            case 16 :
                return "17:00_17:30";
            case 17 :
                return "17:30_18:00";
            case 18 :
                return "18:00_18:30";
            case 19:
                return "18:30_19:00";

           default:return "Closed";
        }
    }





    public static String convertTimestampToStringKey(Timestamp timestamp) {

        Date date =  timestamp.toDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyy");

        return simpleDateFormat.format(date);
    }

    public static String formatShoppingItemName(String name) {
        return name.length() > 13 ? new StringBuilder(name.substring(0 ,10)).append("....")
                .toString() : name ;
    }


    public static void showRatingDialog(Context context , String state_name, String salon_id, String salon_name, String barber_id) {

        DocumentReference BarberNeedRateRef = FirebaseFirestore.getInstance()
                .collection("AllSalon").document(state_name).collection("Branch").document(salon_id)
                .collection("Barber").document(barber_id);

        BarberNeedRateRef.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context , e.getMessage() , Toast.LENGTH_LONG).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){

                    Barber barberRate = task.getResult().toObject(Barber.class);
                    barberRate.setBarberId(task.getResult().getId());

                    //create view for dialoge
                    View view = LayoutInflater.from(context).inflate(R.layout.layout_rating_dialoge , null);

                    TextView txt_salon_name = (TextView) view.findViewById(R.id.salon_name);
                    TextView txt_barber_name = (TextView) view.findViewById(R.id.barber_name);
                    AppCompatRatingBar ratingBar = (AppCompatRatingBar) view.findViewById(R.id.rating) ;


                    txt_barber_name.setText(barberRate.getName());
                    txt_salon_name.setText(salon_name);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context)
                            .setView(view)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Double original_rating = barberRate.getRating();
                                    Long ratingTimes = barberRate.getRatingTimes();
                                    float userRating = ratingBar.getRating();

                                    Double finalRating = (original_rating + userRating);

                                    Map<String , Object> data_update = new HashMap<>();
                                    data_update.put("rating" , finalRating);
                                    data_update.put("ratingTimes" , ++ratingTimes);


                                    BarberNeedRateRef.update(data_update).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                           Toast.makeText(context , e.getMessage() , Toast.LENGTH_LONG).show();
                                        }
                                    }).addOnCompleteListener(task1 -> {
                                  if (task1.isSuccessful()){

                                      Toast.makeText(context , "Thank You For Rating!!" , Toast.LENGTH_LONG).show();
//delete key
                                      Paper.init(context);
                                      Paper.book().delete(Common.RATING_INFORMATION_KEY);
                                  }
                                    }); }

                            }).setNegativeButton("Skip", (dialog, which) -> dialog.dismiss()).setNeutralButton("Never", (dialog, which) -> {

                                //delete key
                                Paper.init(context);
                                Paper.book().delete(Common.RATING_INFORMATION_KEY);
                            });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } }});
    }
}
