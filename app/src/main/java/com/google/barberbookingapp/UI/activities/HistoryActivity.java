package com.google.barberbookingapp.UI.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.barberbookingapp.Model.Common.Common;
import com.google.barberbookingapp.Model.EventBus.UserBookingLoadInfo;
import com.google.barberbookingapp.Model.entities.BookingInformation;
import com.google.barberbookingapp.R;
import com.google.barberbookingapp.UI.adapters.MyHistoryAdapter;
import com.google.barberbookingapp.databinding.ActivityHistoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class HistoryActivity extends AppCompatActivity {

    private ActivityHistoryBinding binding ;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            binding = DataBindingUtil.setContentView(this , R.layout.activity_history);

            alertDialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

            EventBus.getDefault().register(this);

            loadUserBookingInformation();

            binding.recyclerViewHistory.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

            binding.recyclerViewHistory.setLayoutManager(linearLayoutManager);
            binding.recyclerViewHistory.addItemDecoration(new DividerItemDecoration(this , linearLayoutManager.getOrientation()));

    }

    private void loadUserBookingInformation() {

        CollectionReference reference = FirebaseFirestore.getInstance().collection("User")
                .document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .collection("Booking");

        reference.whereEqualTo("done" , true ).
                orderBy("timestamp" , Query.Direction.DESCENDING).get()
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                EventBus.getDefault().postSticky(new UserBookingLoadInfo(false , e.getMessage()));

            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){

                    List<BookingInformation> bookingInformationList = new ArrayList<>();

                    for (DocumentSnapshot sh : Objects.requireNonNull(task.getResult())){

                        BookingInformation bookingInformation = sh.toObject(BookingInformation.class);

                        bookingInformationList.add(bookingInformation);
                    }
                    EventBus.getDefault().postSticky(new UserBookingLoadInfo(true , bookingInformationList));
                }
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    //to load data inside
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void displayData(UserBookingLoadInfo userBookingLoadInfo){
        if (userBookingLoadInfo.isB()){

       binding.recyclerViewHistory.setAdapter(new MyHistoryAdapter(this , userBookingLoadInfo.getBookingInformationList()));
       binding.txtHistory.setText(new StringBuilder("History ( ")
       .append(userBookingLoadInfo.getBookingInformationList().size())
       .append(" )"));

        }
        else {
            Toast.makeText(this , userBookingLoadInfo.getMessage() , Toast.LENGTH_LONG).show();
        }
        alertDialog.dismiss();
    }
}