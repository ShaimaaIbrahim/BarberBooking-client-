package com.google.barberbookingapp.UI.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.barberbookingapp.Model.ActivitesInterfaces.IrecyclerItemSelectedListner;
import com.google.barberbookingapp.Model.Common.Common;
import com.google.barberbookingapp.Model.EventBus.EnableNextButton;
import com.google.barberbookingapp.Model.entities.Salon;
import com.google.barberbookingapp.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllSalonAdapter extends RecyclerView.Adapter<AllSalonAdapter.viewHolder> {

    private List<Salon>salonList;
    private Context context;
    private List<CardView> cardViewList;
   // private LocalBroadcastManager localBroadcastManager;


    public AllSalonAdapter(Context context , List<Salon> salonList) {
        this.context=context;
        this.salonList=salonList;
        cardViewList=new ArrayList<>();
       // localBroadcastManager= LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public AllSalonAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.salon_list_item , parent , false);

        return new AllSalonAdapter.viewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AllSalonAdapter.viewHolder holder, int i) {

         Salon salon = salonList.get(i);

         holder.txt_name.setText(salon.getName());
         holder.txt_address.setText(salon.getAddress());

         if (!cardViewList.contains(holder.cardView)){
             cardViewList.add(holder.cardView);
         }

         holder.setIrecyclerItemSelectedListner(new IrecyclerItemSelectedListner() {
             @Override
             public void onItemSelectedListner(View view, int position) {

                 for (CardView cardView : cardViewList)
                     cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));


                 holder.cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_dark));
                 //send BradCast to enable next button
                 Intent intent = new Intent(Common.KEY_ENABLE_NEXT);

                 intent.putExtra(Common.KEY_SALON, salonList.get(position));
                 intent.putExtra(Common.STEP , 1);

                 EventBus.getDefault().postSticky(new EnableNextButton(1 , salonList.get(position)));

                // localBroadcastManager.sendBroadcast(intent);

             }
         });
    }


    @Override
    public int getItemCount() {
        return salonList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private IrecyclerItemSelectedListner irecyclerItemSelectedListner;

        private TextView txt_name;
        private TextView txt_address;
        private CardView cardView;

        public viewHolder setIrecyclerItemSelectedListner(IrecyclerItemSelectedListner irecyclerItemSelectedListner) {
            this.irecyclerItemSelectedListner = irecyclerItemSelectedListner;
            return this;
        }

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_address=itemView.findViewById(R.id.txt_address);
            cardView=itemView.findViewById(R.id.salon_card_view);

            itemView.setOnClickListener(this::onClick);
        }


        @Override
        public void onClick(View v) {

             irecyclerItemSelectedListner.onItemSelectedListner(v , getAdapterPosition());
        }
    }
}
