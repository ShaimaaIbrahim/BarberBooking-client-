package com.google.barberbookingapp.UI.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.barberbookingapp.Model.ActivitesInterfaces.onUpdateSuccess;
import com.google.barberbookingapp.Model.Common.Common;
import com.google.barberbookingapp.Model.mvp.CartModel;
import com.google.barberbookingapp.Model.mvp.CartPresenter;
import com.google.barberbookingapp.R;
import com.google.barberbookingapp.Repository.RoomDb.CartItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AllCartAdapter extends RecyclerView.Adapter<AllCartAdapter.viewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private CartPresenter cartPresenter;
    private onUpdateSuccess onUpdateSuccess;

    public AllCartAdapter(Context context, List<CartItem> cartItems , onUpdateSuccess onUpdateSuccess) {
        this.context = context;
        this.cartItems = cartItems;
        cartPresenter = new CartModel();
        this.onUpdateSuccess=onUpdateSuccess;

    }

    @NonNull
    @Override
    public AllCartAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_cart_item , parent , false);

        return new AllCartAdapter.viewHolder(view);
    }


    interface IimageButoonListner{
        void onImageButtonListner(View v , int pos , boolean isDecrease);
    }
    @Override
    public void onBindViewHolder(@NonNull AllCartAdapter.viewHolder holder, int position) {

        CartItem cartItem = cartItems.get(position);

        Picasso.get().load(cartItem.getProductImage()).into(holder.img_cart_item);

        holder.txt_cart_price.setText(new StringBuilder("$ ").append(String.valueOf(cartItem.getProductPrice())));

        holder.txt_cart_name.setText(Common.formatShoppingItemName(cartItem.getProductName()));

        holder.txt_item_quantity.setText(new StringBuilder(String.valueOf(cartItem.getProductQuantity())));



        holder.setIimageButoonListner(new IimageButoonListner() {
            @Override
            public void onImageButtonListner(View v, int pos, boolean isDecrease) {
                if (isDecrease){

                  if (cartItem.getProductQuantity() > 0){

                      cartItem.setProductQuantity(cartItem.getProductQuantity()-1);

                      cartPresenter.update(cartItem , context);

                      holder.txt_item_quantity.setText(new StringBuilder(String.valueOf(cartItem.getProductQuantity())));

                  }
                  else if(cartItem.getProductQuantity() ==0){
                      cartPresenter.delete(cartItem , context);
                      cartItems.remove(pos);
                      notifyItemRemoved(pos);
                  }

                }
                else {

                    if (cartItem.getProductQuantity() < 99){

                        cartItem.setProductQuantity(cartItem.getProductQuantity()+1);

                        cartPresenter.update(cartItem , context);

                        holder.txt_item_quantity.setText(new StringBuilder(String.valueOf(cartItem.getProductQuantity())));

                    }

                    onUpdateSuccess.onUpdateSuccess();
                }


            }

        }); }


    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {

        private TextView txt_cart_name , txt_cart_price , txt_item_quantity;
        private ImageView img_cart_item , img_increse , imgDecrease;
        private IimageButoonListner iimageButoonListner;


        public void setIimageButoonListner(IimageButoonListner iimageButoonListner) {
            this.iimageButoonListner = iimageButoonListner;
        }


        public viewHolder(@NonNull View itemView) {
            super(itemView);

            img_increse=itemView.findViewById(R.id.plus);
            imgDecrease =itemView.findViewById(R.id.minus);

            txt_cart_name=itemView.findViewById(R.id.txt_cart_name);
            txt_cart_price=itemView.findViewById(R.id.txt_cart_price);
            txt_item_quantity=itemView.findViewById(R.id.item_quantity);
            img_cart_item=itemView.findViewById(R.id.img_cart_item);

          img_increse.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                 iimageButoonListner.onImageButtonListner(v , getAdapterPosition() , false);
              }
          });

          imgDecrease.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  iimageButoonListner.onImageButtonListner(v , getAdapterPosition() , true);

              }
          });
        }



    }
}
