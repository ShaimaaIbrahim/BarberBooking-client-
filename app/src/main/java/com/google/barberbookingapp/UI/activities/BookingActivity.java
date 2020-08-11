package com.google.barberbookingapp.UI.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.barberbookingapp.Model.Common.Common;
import com.google.barberbookingapp.Model.Common.NoSwipeViewPager;
import com.google.barberbookingapp.Model.EventBus.BarberDoneEvent;
import com.google.barberbookingapp.Model.EventBus.EnableNextButton;
import com.google.barberbookingapp.Model.EventBus.confirmBookingEvent;
import com.google.barberbookingapp.Model.EventBus.displayTimeSlotEvent;
import com.google.barberbookingapp.Model.entities.Barber;
import com.google.barberbookingapp.R;
import com.google.barberbookingapp.UI.adapters.MyViewPagerAdapter;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;
import dmax.dialog.SpotsDialog;


public class BookingActivity extends AppCompatActivity {

     // private   LocalBroadcastManager localBroadcastManager;
      private Button buttonNext , buttonPrevious;
      private StepView stepView;
      private NoSwipeViewPager viewPager;
      private AlertDialog alertDialog;
      private CollectionReference barberRef;


     /*private BroadcastReceiver btnNexReciever = new BroadcastReceiver() {

       @Override
       public void onReceive(Context context , Intent intent) {

           int step = intent.getIntExtra(Common.STEP , 0);

           if (step==1){
               Common.currentSalon= intent.getParcelableExtra(Common.KEY_SALON);
           }else if (step==2){

           Common.currentBarber = intent.getParcelableExtra(Common.KEY_BARBER_SELECTED);
           }else if (step ==3){
             Common.currentTimeSlot = intent.getIntExtra(Common.KEY_SLOT_OF_TIME , -1);
           }

           buttonNext.setEnabled(true);

           setUpColorButtons();

       }
   };*/

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }

    // eventBus
     @Subscribe(sticky =true  , threadMode = ThreadMode.MAIN)
     public void enableBtnNextReciever(EnableNextButton event){

         int step = event.getNext();

         if (step==1){
             Common.currentSalon= event.getSalon();
         }else if (step==2){

             Common.currentBarber = event.getBarber();
         }else if (step ==3){
             Common.currentTimeSlot = event.getTimeSlot();
         }

         buttonNext.setEnabled(true);

         setUpColorButtons();

     }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_booking);

         viewPager=findViewById(R.id.view_pager);
         buttonNext=findViewById(R.id.button_next);
         buttonPrevious=findViewById(R.id.button_previous);
         stepView=findViewById(R.id.step_view);

         alertDialog= new SpotsDialog.Builder().setContext(this).setCancelable(false).build();



         // todo localBroadCastReciever
      /*   localBroadcastManager = LocalBroadcastManager.getInstance(this);
         localBroadcastManager.registerReceiver(btnNexReciever, new IntentFilter(Common.KEY_ENABLE_NEXT) );*/

        setUpStepView();


        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.step ==3 || Common.step > 0){
                    Common.step--;
                    viewPager.setCurrentItem(Common.step);
                    if (Common.step < 3){
                        buttonNext.setEnabled(true);
                        setUpColorButtons();
                    }
                } }

        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.step < 3 || Common.step==0) {
                    Common.step++;

                    if (Common.step == 1) {
                        if (Common.currentSalon!=null){
                            loadAllBarberBySalonId(Common.currentSalon.getSalonId());
                        }

                    }else if (Common.step==2){ // pick all times
                        if (Common.currentBarber!=null){
                            loadTimeSlotOfBarber(Common.currentBarber.getBarberId());
                        }
                    }
                    else if (Common.step==3){ // pick all times
                        if (Common.currentTimeSlot!=-1){

                            ConfirmBooking();
                        }
                    }
                    viewPager.setCurrentItem(Common.step);
                }
            }
        });


      //viewPager
      viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
      viewPager.setOffscreenPageLimit(4);  // to save state of each view when scrolling
      viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                stepView.go(position , true);

                      if(position ==0 ){
                      buttonPrevious.setEnabled(false);

                     }else {

                      buttonPrevious.setEnabled(true); }

                      buttonNext.setEnabled(false);
                      setUpColorButtons();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
    }

    private void ConfirmBooking() {

        EventBus.getDefault().postSticky(new confirmBookingEvent(true));
    }

    private void loadTimeSlotOfBarber(String barberId) {

        EventBus.getDefault().postSticky(new displayTimeSlotEvent(true));

    }

    private void loadAllBarberBySalonId(String salonId) {
        alertDialog.show();

          //AllSalon/Abbas/Branch/UN96EJMbUvlSahBebY0A/Barber
        if (!TextUtils.isEmpty(Common.city)){
            barberRef= FirebaseFirestore.getInstance().collection("AllSalon").document(Common.city)
                    .collection("Branch")
                    .document(salonId).collection("Barber");

            barberRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    List<Barber> barberList = new ArrayList<>();
              if (task.isSuccessful()){
                  for (QueryDocumentSnapshot snapshot : task.getResult()){

                       Barber barber = snapshot.toObject(Barber.class);
                       barber.setPassword("");
                       barber.setBarberId(snapshot.getId());
                       barberList.add(barber);
                  }

                   EventBus.getDefault().postSticky(new BarberDoneEvent(barberList));
                   alertDialog.dismiss();
              }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    alertDialog.dismiss();
                }
            });
        }

    }

    @SuppressLint("ResourceAsColor")
    private void setUpColorButtons() {
        if (buttonNext.isEnabled()){
          buttonNext.setBackgroundResource(R.color.colorButton);
        }else {
        buttonNext.setBackgroundResource(android.R.color.darker_gray);

        }

        if (buttonPrevious.isEnabled()){
            buttonPrevious.setBackgroundResource(R.color.colorButton);
        }else {
           buttonPrevious.setBackgroundResource(android.R.color.darker_gray);

        }

    }

    private void setUpStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Salon");
        stepList.add("Barber");
        stepList.add("Time");
        stepList.add("Confirm");

        stepView.setSteps(stepList);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //localBroadcastManager.unregisterReceiver(btnNexReciever);
    }
}
