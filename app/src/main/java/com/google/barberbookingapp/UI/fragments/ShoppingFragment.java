package com.google.barberbookingapp.UI.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.barberbookingapp.Model.ActivitesInterfaces.IsShoppingDataLoadListner;
import com.google.barberbookingapp.Model.Common.SpaceItemDecoration;
import com.google.barberbookingapp.Model.entities.ShoppingItems;
import com.google.barberbookingapp.Model.mvp.CartModel;
import com.google.barberbookingapp.Model.mvp.CartPresenter;
import com.google.barberbookingapp.Model.mvp.CartView;
import com.google.barberbookingapp.R;
import com.google.barberbookingapp.Repository.RoomDb.CartItem;
import com.google.barberbookingapp.UI.adapters.AllShoppingItems;
import com.google.barberbookingapp.databinding.FragmentShoppingBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * created by shaimaa salama
 */
public class ShoppingFragment extends Fragment implements IsShoppingDataLoadListner {

    private FragmentShoppingBinding binding;
    private CollectionReference shoppingIremRef;
    private IsShoppingDataLoadListner isShoppingDataLoadListner;
    private View itemView;
    private RecyclerView recyclerView;
    private Chip wax_chip , spray_chip;
    private CartPresenter cartPresenter;

    public ShoppingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShoppingDataLoadListner=this;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_shopping , container , false);

        itemView= inflater.inflate(R.layout.fragment_shopping , container , false);





        isShoppingDataLoadListner=this;

        recyclerView=itemView.findViewById(R.id.recycler_items);
        wax_chip=itemView.findViewById(R.id.chip_wax);
        spray_chip=itemView.findViewById(R.id.chip_spray);



             // Default
             loadShoppingItemProducts("Wax");

             binding.bodyCare.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     setSelectedChip(binding.bodyCare);
                     loadShoppingItemProducts("BodyCare");
                 }
             });

             binding.hairCare.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     setSelectedChip(binding.hairCare);
                     loadShoppingItemProducts("HairCare");
                 }
             });

        binding.chipWax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            setSelectedChip(wax_chip);
            loadShoppingItemProducts("Wax");
            }
        });

        binding.chipSpray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setSelectedChip(spray_chip);
                loadShoppingItemProducts("Spray");
            }
        });

        return binding.getRoot();
    }

    private void loadShoppingItemProducts(String  itemMenu) {

   shoppingIremRef = FirebaseFirestore.getInstance().collection("Shopping")
           .document(itemMenu).collection("items");


      shoppingIremRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
       @Override
       public void onComplete(@NonNull Task<QuerySnapshot> task) {

           if (task.isSuccessful()){

               List<ShoppingItems> shoppingItemsList = new ArrayList<>();


               for (DocumentSnapshot snapshot : task.getResult()){

                    ShoppingItems shoppingItems = snapshot.toObject(ShoppingItems.class);

                     shoppingItems.setId(snapshot.getId());
                     shoppingItemsList.add(shoppingItems);
               }
               isShoppingDataLoadListner.ShoppingDataLoadSuccess(shoppingItemsList);
           }
       }
   }).addOnFailureListener(new OnFailureListener() {
       @Override
       public void onFailure(@NonNull Exception e) {
           isShoppingDataLoadListner.ShoppingDataLoadFailed(e.getMessage());
       }
   });
    }

    private void setSelectedChip(Chip chipWax) {

        for (int i = 0 ; i < binding.chipGroup.getChildCount() ; i++){

            Chip chipItem = (Chip) binding.chipGroup.getChildAt(i);

            if (chipItem.getId() != chipWax.getId()){

                chipItem.setChipBackgroundColorResource(android.R.color.darker_gray);
                chipItem.setTextColor(getResources().getColor(android.R.color.white));

            }else {

                chipItem.setChipBackgroundColorResource(android.R.color.holo_orange_dark);
                chipItem.setTextColor(getResources().getColor(android.R.color.black));

            }
        }
    }


    @Override
    public void ShoppingDataLoadSuccess(List<ShoppingItems> shoppingItemsList) {

        binding.recyclerItems.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity() , 2);
        binding.recyclerItems.setLayoutManager(layoutManager);
        binding.recyclerItems.addItemDecoration(new SpaceItemDecoration(8));
         binding.recyclerItems.setAdapter(new AllShoppingItems(shoppingItemsList , getActivity()));

    }

    @Override
    public void ShoppingDataLoadFailed(String message) {

        Toast.makeText(getActivity() , message , Toast.LENGTH_LONG).show();
    }



}
