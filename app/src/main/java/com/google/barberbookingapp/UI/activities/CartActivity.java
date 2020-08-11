package com.google.barberbookingapp.UI.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.barberbookingapp.Model.ActivitesInterfaces.onUpdateSuccess;
import com.google.barberbookingapp.Model.Common.Common;
import com.google.barberbookingapp.Model.mvp.CartModel;
import com.google.barberbookingapp.Model.mvp.CartPresenter;
import com.google.barberbookingapp.Model.mvp.CartView;
import com.google.barberbookingapp.R;
import com.google.barberbookingapp.Repository.RoomDb.CartItem;
import com.google.barberbookingapp.UI.adapters.AllCartAdapter;
import com.google.barberbookingapp.databinding.ActivityCartBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CartActivity extends AppCompatActivity implements CartView , onUpdateSuccess {

    private ActivityCartBinding binding ;
    private CartPresenter cartPresenter ;
    private onUpdateSuccess onUpdateSuccess;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this , R.layout.activity_cart);

        cartPresenter = new CartModel(this);

        onUpdateSuccess=this;

        cartPresenter.getAllItemsFromCart(this , Common.currentUser.getPhoneNumber());

       cartPresenter.sumPrice(Common.currentUser.getPhoneNumber() , this);


       binding.btnClearCart.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           String phoneNumber=  FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().toString();

             cartPresenter.ClearCart(phoneNumber , CartActivity.this);
             //update items
             cartPresenter.getAllItemsFromCart(CartActivity.this ,phoneNumber);
             //update price
               cartPresenter.sumPrice(phoneNumber , CartActivity.this);

           }
       });

    }

    @Override
    public void ShowAllCartItems(List<CartItem> cartItemList) {

        binding.recyclerCart.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerCart.addItemDecoration(new DividerItemDecoration(this , linearLayoutManager.getOrientation()));
        binding.recyclerCart.setLayoutManager(linearLayoutManager);

        AllCartAdapter allCartAdapter = new AllCartAdapter(this , cartItemList ,  this);

        binding.recyclerCart.setAdapter(allCartAdapter);

    }

    @Override
    public void ShowAllCartItemCount(int count) {

    }

    @Override
    public void showProductById(CartItem cartItem) {

    }


    @Override
    public void showSumPrice(long sumPrice) {

          binding.txtTotalPrice.setText(new StringBuilder("$").append(sumPrice));
    }

    @Override
    public void onUpdateSuccess() {

         cartPresenter.sumPrice(Common.currentUser.getPhoneNumber() , this);
    }
}
