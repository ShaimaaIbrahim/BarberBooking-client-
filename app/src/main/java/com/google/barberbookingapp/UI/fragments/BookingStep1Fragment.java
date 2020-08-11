package com.google.barberbookingapp.UI.fragments;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.barberbookingapp.Model.ActivitesInterfaces.AllSalonLoadListner;
import com.google.barberbookingapp.Model.ActivitesInterfaces.IBrabchLoadListner;
import com.google.barberbookingapp.Model.Common.Common;
import com.google.barberbookingapp.Model.Common.SpaceItemDecoration;
import com.google.barberbookingapp.Model.entities.Salon;
import com.google.barberbookingapp.R;
import com.google.barberbookingapp.UI.adapters.AllSalonAdapter;
import com.google.barberbookingapp.databinding.FragmentBookingStepOneBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import dmax.dialog.SpotsDialog;

/**
 * created by shaimaa salama
 */
public class BookingStep1Fragment extends Fragment  implements AllSalonLoadListner , IBrabchLoadListner  {

    //vars
    private CollectionReference allSalonRef;
    private CollectionReference allBranchRef;
    private AllSalonLoadListner allSalonLoadListner;
    private IBrabchLoadListner iBrabchLoadListner;
    private AlertDialog dialog;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allSalonLoadListner = this;
         iBrabchLoadListner=this;


         dialog = new SpotsDialog.Builder().setContext(getActivity()).setCancelable(false).build();
    }

    private  static BookingStep1Fragment instance;
    FragmentBookingStepOneBinding  binding;
    public BookingStep1Fragment() {

    }

public static BookingStep1Fragment getInstance(){
        if (instance==null){
            instance=new BookingStep1Fragment();
        }
        return instance;
}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         binding = DataBindingUtil.inflate(inflater , R.layout.fragment_booking_step_one , container , false );

         initView();
         loadAllSalons();

       return binding.getRoot();
    }

    private void loadAllSalons() {

        allSalonRef= FirebaseFirestore.getInstance().collection("AllSalon");

        allSalonRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                List<String > list = new ArrayList<>();

                if (task.isSuccessful()){

                    list.add("Please Choose City");

                    for (QueryDocumentSnapshot snapshot : task.getResult()){

                              list.add(snapshot.getId().toString());

                          }
                          allSalonLoadListner.onAllSalonLoadSuccess(list); } }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                allSalonLoadListner.onAllSalonLoadFailed(e.getMessage());
            }
        });
    }


    @Override
    public void onAllSalonLoadSuccess(List<String> areaNameSalons) {
        binding.spinnerView.setItems(areaNameSalons);
        binding.spinnerView.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                if (position > 0) {
                    BookingStep1Fragment.this.loadBranchOfCity(item.toString());
                } else {
                    binding.recyclerSalons.setVisibility(View.GONE);
                }
            }
        });

    }

    private void loadBranchOfCity(String cityName) {
        //cityName = salonId
        if (!dialog.isShowing()) {
            dialog.show();
        }

        Common.city=cityName;

        allBranchRef = FirebaseFirestore.getInstance().collection("AllSalon").
                document(cityName).collection("Branch");

        allBranchRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                   List<Salon> salonList = new ArrayList<>();

                  if (task.isSuccessful()){

                  for (QueryDocumentSnapshot snapshot : task.getResult()){

                      Salon salon =snapshot.toObject(Salon.class);
                      salon.setSalonId(snapshot.getId());

                      salonList.add(salon);
                  }
                  iBrabchLoadListner.onBranchLoadSuccess(salonList); }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               iBrabchLoadListner.onBranchLoadFailed(e.getMessage());
            }
        });

    }

    @Override
    public void onAllSalonLoadFailed(String message) {
        Toast.makeText(getActivity() , message , Toast.LENGTH_LONG).show();
    }


    @Override
    public void onBranchLoadSuccess(List<Salon> salonList) {

            binding.recyclerSalons.setAdapter(new AllSalonAdapter(getContext() , salonList));
            binding.recyclerSalons.setVisibility(View.VISIBLE);
            dialog.dismiss();
    }

    @Override
    public void onBranchLoadFailed(String message) {
        Toast.makeText(getActivity() , message , Toast.LENGTH_LONG).show();
        dialog.dismiss();

    }

    private void initView(){

        binding.recyclerSalons.setHasFixedSize(true);
        binding.recyclerSalons.setLayoutManager(new GridLayoutManager(getActivity() , 2));
        binding.recyclerSalons.addItemDecoration(new SpaceItemDecoration(4));
    }


}
