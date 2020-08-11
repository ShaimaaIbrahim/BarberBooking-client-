package com.google.barberbookingapp.UI.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import dmax.dialog.SpotsDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.barberbookingapp.Model.ActivitesInterfaces.ItimeSlotLoadListner;
import com.google.barberbookingapp.Model.Common.Common;
import com.google.barberbookingapp.Model.Common.SpaceItemDecoration;
import com.google.barberbookingapp.Model.EventBus.displayTimeSlotEvent;
import com.google.barberbookingapp.Model.entities.TimeSlot;
import com.google.barberbookingapp.R;
import com.google.barberbookingapp.UI.adapters.AllTimeSlotAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingStep3Fragment extends Fragment implements ItimeSlotLoadListner{

    private static BookingStep3Fragment instance;
    private DocumentReference barberDoc;
    private ItimeSlotLoadListner itimeSlotLoadListner;
    private AlertDialog alertDialog;
    private SimpleDateFormat simpleDateFormat;
 //   private LocalBroadcastManager localBroadcastManager;

    private RecyclerView recyclerView;


   /* private BroadcastReceiver displayTimeSlots = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE , 0);
            loadAvailableTimeSlotsForEachBarber(Common.currentBarber.getBarberId() ,

                    simpleDateFormat.format(calendar.getTime()));


        }
    };*/

//============================== EVENT BUS ==============
@Override
public void onStop() {
    EventBus.getDefault().unregister(this);
    super.onStop();
}

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Subscribe (sticky = true , threadMode = ThreadMode.MAIN)
    public void displayTimeSlots(displayTimeSlotEvent event){

    if (event.isDisplay()){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE , 0);
        loadAvailableTimeSlotsForEachBarber(Common.currentBarber.getBarberId() ,

                simpleDateFormat.format(calendar.getTime()));
    }
}
    //==================================

    public BookingStep3Fragment() {
        // Required empty public constructor
    }
    public static BookingStep3Fragment getInstance(){
        if (instance==null){
            instance=new BookingStep3Fragment();
        }
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itimeSlotLoadListner=this;
        alertDialog = new SpotsDialog.Builder().setContext(getContext()).build();


        //todo
      /*  localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(displayTimeSlots ,
        new IntentFilter(Common.KEY_DISPLAY_TIME_SLOT));*/

        simpleDateFormat = new SimpleDateFormat("dd_MM_yyy");


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
     //   localBroadcastManager.unregisterReceiver(displayTimeSlots);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      //   binding = DataBindingUtil.inflate(inflater , R.layout.fragment_booking_step_three ,container , false);

           View itemView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_booking_step_three , container , false);

            recyclerView=itemView.findViewById(R.id.recycler_time_slot);


         initView(itemView);

         return itemView;
    }

    private void initView(View view) {

        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext() , 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new SpaceItemDecoration(9));

        //todo set up horizontal calendar
        //startDate
        Calendar start_date = Calendar.getInstance();
        start_date.add(Calendar.DATE , 0);
         //EndDate
        Calendar end_date = Calendar.getInstance();
        end_date.add(Calendar.DATE , 2);

        //setUpCalendar
        HorizontalCalendar horizontalCalendarView = new HorizontalCalendar.Builder(view , R.id.calendar_view)
                .range(start_date , end_date).datesNumberOnScreen(1)
                .mode(HorizontalCalendar.Mode.DAYS).defaultSelectedDate(start_date)
                .build();

                  horizontalCalendarView.show();

        horizontalCalendarView.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                if (Common.bookingDate.getTimeInMillis() != date.getTimeInMillis()){
                    Common.bookingDate = date;
                    loadAvailableTimeSlotsForEachBarber(Common.currentBarber.getBarberId() , simpleDateFormat.format(date.getTime()));
                }
            }
        });

    }


    private void loadAvailableTimeSlotsForEachBarber(String barberId, String bookDate) {
        alertDialog.show();

        //AllSalon/Abbas/Branch/UN96EJMbUvlSahBebY0A/Barber/FmA2bwFwgJ4VTjnJHRK7
        barberDoc = FirebaseFirestore.getInstance().collection("AllSalon")
                .document(Common.city).collection("Branch").document(Common.currentSalon.getSalonId())
                .collection("Barber").document(Common.currentBarber.getBarberId());

        barberDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                  if (task.isSuccessful()){
         DocumentSnapshot snapshot = task.getResult();
         if (snapshot.exists()){

           CollectionReference date =   FirebaseFirestore.getInstance().collection("AllSalon")
                   .document(Common.city).collection("Branch").document(Common.currentSalon.getSalonId())
                   .collection("Barber").document(Common.currentBarber.getBarberId()).collection(bookDate);

           date.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
               @Override
               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                   if (task.isSuccessful()){

                      QuerySnapshot querySnapshot = task.getResult();

                       assert querySnapshot != null;
                       if (querySnapshot.isEmpty()){
                          itimeSlotLoadListner.LoadTimeSlotEmpty();
                      }
                      else {

                     List<TimeSlot> timeSlotList = new ArrayList<>();

                     for (DocumentSnapshot documentSnapshot : task.getResult()){

                         timeSlotList.add(documentSnapshot.toObject(TimeSlot.class));
                         itimeSlotLoadListner.LoadTimeSlotSuccess(timeSlotList);

                     } } } }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
           itimeSlotLoadListner.LoadTimeSlotFailed(e.getMessage());
               }
           });


         } }}
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    @Override
    public void LoadTimeSlotSuccess(List<TimeSlot> timeSlotList) {

        AllTimeSlotAdapter allTimeSlotAdapter = new AllTimeSlotAdapter(getContext() , timeSlotList);
        recyclerView.setAdapter(allTimeSlotAdapter);
        alertDialog.dismiss();
    }

    @Override
    public void LoadTimeSlotFailed(String message) {
        Toast.makeText(getActivity() , message , Toast.LENGTH_LONG).show();
    }

    @Override
    public void LoadTimeSlotEmpty() {
        AllTimeSlotAdapter allTimeSlotAdapter = new AllTimeSlotAdapter(getContext());
           recyclerView.setAdapter(allTimeSlotAdapter);
           alertDialog.dismiss();
    }
}
