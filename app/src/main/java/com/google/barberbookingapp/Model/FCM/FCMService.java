package com.google.barberbookingapp.Model.FCM;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.barberbookingapp.Model.Common.Common;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import io.paperdb.Paper;


@SuppressLint("Registered")
public class FCMService extends FirebaseMessagingService {

    public FCMService() {
    }

    // new user => every user has unique token

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        FcmCommon.updateToken(this , s);
    }

    // when recieving new message
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        if (remoteMessage.getData()!=null){

            if (remoteMessage.getData().get("update_done") != null){

                updateLastBooking();

                Map<String , String > dataRecieved = remoteMessage.getData();

                Paper.init(this);
                Paper.book().write(Common.RATING_INFORMATION_KEY , new Gson().toJson(dataRecieved));


            }
            if (remoteMessage.getData().get(Common.KEY_TITLE)!=null &&
                    remoteMessage.getData().get(Common.KEY_CONTENT)!=null){

                FcmCommon.showNotification(this , new Random().nextInt()
                        , remoteMessage.getData().get(FcmCommon.TITLE_KEY)
                        , remoteMessage.getData().get(FcmCommon.CONTENT_KEY)
                        , null);
            }
        }

    }

    private void updateLastBooking() {

        // because app maybe run on background so we can read from Paper


        CollectionReference userBooking ;

        if (Common.currentUser!=null){

            userBooking = FirebaseFirestore.getInstance().collection("User")
                    .document(Common.currentUser.getPhoneNumber()).collection("Booking");

        }else {

            Paper.init(this);
            String user =  Paper.book().read(Common.LOGGED_KEY);

            userBooking = FirebaseFirestore.getInstance().collection("User")
                    .document(user).collection("Booking");
        }

        // get current Date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE , 0);
        calendar.add(Calendar.HOUR_OF_DAY , 0);
        calendar.add(Calendar.MINUTE , 0);

        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());

        userBooking.whereGreaterThanOrEqualTo("timestamp" , timestamp)
                .whereEqualTo("done" ,false )
                .limit(1)
                .get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FCMService.this , e.getMessage() , Toast.LENGTH_LONG).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    if (task.getResult().size() > 0){

                   DocumentReference userBookingCurrentDocument = null;
                   for (DocumentSnapshot snapshot : task.getResult()){

                       userBookingCurrentDocument = userBooking.document(snapshot.getId());

                   }
                   if (userBookingCurrentDocument!=null){

                       Map<String , Object> dataUpdate = new HashMap<>();
                       dataUpdate.put("done" , true);

                       userBookingCurrentDocument.update(dataUpdate).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {

                               Toast.makeText(FCMService.this , e.getMessage() , Toast.LENGTH_LONG).show();

                           }
                       }).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {

                           }
                       });
                   }
                    }
                }
            }
        });


    }


    }

