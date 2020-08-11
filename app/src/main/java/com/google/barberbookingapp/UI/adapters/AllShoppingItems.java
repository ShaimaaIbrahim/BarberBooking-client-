package com.google.barberbookingapp.UI.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.barberbookingapp.Model.ActivitesInterfaces.onItemCartClickListner;
import com.google.barberbookingapp.Model.Common.Common;
import com.google.barberbookingapp.Model.entities.ShoppingItems;
import com.google.barberbookingapp.Model.mvp.CartModel;
import com.google.barberbookingapp.Model.mvp.CartPresenter;
import com.google.barberbookingapp.R;
import com.google.barberbookingapp.Repository.RoomDb.CartItem;
import com.squareup.picasso.Picasso;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AllShoppingItems extends RecyclerView.Adapter<AllShoppingItems.viewHolder> {

    private List<ShoppingItems > shoppingItemsList ;
    private Context context;
    private CartPresenter cartPresenter;
    public AllShoppingItems() {

    }

    public AllShoppingItems(List<ShoppingItems> shoppingItemsList, Context context) {
        this.shoppingItemsList = shoppingItemsList;
        this.context = context;

        cartPresenter = new CartModel();

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_shopping_item , parent , false);

        return new AllShoppingItems.viewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        ShoppingItems items = shoppingItemsList.get(position);

        holder.name_shopping_item.setText(Common.formatShoppingItemName(items.getName()));

        Picasso.get().load(items.getImage()).into(holder.image_shopping_item);

        holder.price_shooping_item.setText(new StringBuilder(" $ ").append(items.getPrice()));

        holder.setCliclListner(new onItemCartClickListner() {
            @Override
            public void onClickListner(View v, int position) {

                CartItem cartItem = new CartItem();

                cartItem.setProductId(items.getId());
                cartItem.setProductName(items.getName());
                cartItem.setProductImage(items.getImage());
                cartItem.setProductPrice(items.getPrice());
                cartItem.setProductQuantity(1);
                cartItem.setUserPhone(Common.currentUser.getPhoneNumber());

                Toast.makeText(context, " Add to Cart ", Toast.LENGTH_LONG).show();

                cartPresenter.insert(context ,cartItem);
            }
        });

    }


    @Override
    public int getItemCount() {
        return shoppingItemsList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

             private onItemCartClickListner cliclListner;
              private TextView name_shopping_item;
              private ImageView image_shopping_item;
              private TextView price_shooping_item;

        public void setCliclListner(onItemCartClickListner cliclListner) {
            this.cliclListner = cliclListner;

        }

        public viewHolder(@NonNull View itemView) {

            super(itemView);

            itemView.setOnClickListener(this::onClick);

            name_shopping_item=itemView.findViewById(R.id.name_shopping_item);
            image_shopping_item=itemView.findViewById(R.id.img_shopping_item);
            price_shooping_item=itemView.findViewById(R.id.price_shopping_item);

        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
           cliclListner.onClickListner(v , getAdapterPosition());
        }
    }
}
