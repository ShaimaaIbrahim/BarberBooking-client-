package com.google.barberbookingapp.UI.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.barberbookingapp.Model.ActivitesInterfaces.IrecyclerItemSelectedListner;
import com.google.barberbookingapp.Model.Common.Common;
import com.google.barberbookingapp.Model.EventBus.EnableNextButton;
import com.google.barberbookingapp.Model.entities.Barber;
import com.google.barberbookingapp.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllBarberAdapter extends RecyclerView.Adapter<AllBarberAdapter.viewHolder> {

    private Context context;
    private List<Barber> barberList;
    private List<CardView> cardViewList;
  //  private LocalBroadcastManager localBroadcastManager;


    public AllBarberAdapter(Context context, List<Barber> barberList) {
        this.context = context;
        this.barberList = barberList;
       // localBroadcastManager= LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    @NonNull
    @Override
    public AllBarberAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_barber , parent , false);
        return new AllBarberAdapter.viewHolder(view);
    }



    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull AllBarberAdapter.viewHolder holder, int position) {

          Barber barber = barberList.get(position);

          if (barber.getRatingTimes()!=null){
              holder.ratingBar.setRating(barber.getRating().floatValue() / barber.getRatingTimes());

          }else {
              holder.ratingBar.setRating(0);

          }

          holder.textView.setText(barber.getName());

          if (!cardViewList.contains(holder.cardView)){
              cardViewList.add(holder.cardView);
          }


          holder.setIrecyclerItemSelectedListner(new IrecyclerItemSelectedListner() {
              @Override
              public void onItemSelectedListner(View view, int position) {
                  for ( CardView cardView : cardViewList){
                      cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
                  }

                  holder.cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_dark
                  ));

                  Intent intent = new Intent(Common.KEY_ENABLE_NEXT);
                  intent.putExtra(Common.KEY_BARBER_SELECTED , barberList.get(position));
                  intent.putExtra(Common.STEP , 2);

                  EventBus.getDefault().postSticky(new EnableNextButton(barberList.get(position) , 2));
              }

          });


    }


    @Override
    public int getItemCount() {
        return barberList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        private IrecyclerItemSelectedListner irecyclerItemSelectedListner;

        private TextView textView;
        private RatingBar ratingBar;
        private CardView cardView;

        public viewHolder setIrecyclerItemSelectedListner(IrecyclerItemSelectedListner irecyclerItemSelectedListner) {
            this.irecyclerItemSelectedListner = irecyclerItemSelectedListner;
            return this;
        }

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            textView=itemView.findViewById(R.id.txt_barber_name);
            ratingBar=itemView.findViewById(R.id.rating_barber);
            cardView=itemView.findViewById(R.id.card_view);
           itemView.setOnClickListener(this::onClick);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
             irecyclerItemSelectedListner.onItemSelectedListner(v , getAdapterPosition());
        }
    }
}
