package com.google.barberbookingapp.UI.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.barberbookingapp.Model.Common.Common;
import com.google.barberbookingapp.Model.Common.SpaceItemDecoration;
import com.google.barberbookingapp.Model.EventBus.BarberDoneEvent;
import com.google.barberbookingapp.Model.entities.Barber;
import com.google.barberbookingapp.R;
import com.google.barberbookingapp.UI.adapters.AllBarberAdapter;
import com.google.barberbookingapp.databinding.FragmentBookingStepTwoBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingStep2Fragment extends Fragment {

private static BookingStep2Fragment instance;
private List<Barber>barberList;
//private LocalBroadcastManager localBroadcastManager;
   private FragmentBookingStepTwoBinding binding;



/*private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {

        barberList=intent.getParcelableArrayListExtra(Common.BARBERS_LIST);

        AllBarberAdapter allBarberAdapter = new AllBarberAdapter(getContext() , barberList);
        binding.recyclerBarber.setAdapter(allBarberAdapter);

    }
};*/

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

    @Override
    public void onDestroy() {
        super.onDestroy();
      //  localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }

    public BookingStep2Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // todo local broadCatReciever
      /*  localBroadcastManager = LocalBroadcastManager.getInstance(getContext());

        localBroadcastManager.registerReceiver(broadcastReceiver
        , new IntentFilter(Common.KEY_BARBER_LOAD_DONE));*/
    }

    public static BookingStep2Fragment getInstance(){
        if (instance==null){
            instance=new BookingStep2Fragment();
        }
        return instance;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookingStepTwoBinding.bind(inflater.inflate(R.layout.fragment_booking_step_two, container, false));

             initView();

        return binding.getRoot();
    }

    private void initView() {
        binding.recyclerBarber.setHasFixedSize(true);
        binding.recyclerBarber.setLayoutManager(new GridLayoutManager(getActivity() , 2));
        binding.recyclerBarber.addItemDecoration(new SpaceItemDecoration(4));
    }

    @Subscribe(sticky =  true , threadMode = ThreadMode.MAIN)
    public void setBarberAdapter(BarberDoneEvent event){

        AllBarberAdapter allBarberAdapter = new AllBarberAdapter(getContext() , event.getBarberList());
        binding.recyclerBarber.setAdapter(allBarberAdapter);
    }

}
