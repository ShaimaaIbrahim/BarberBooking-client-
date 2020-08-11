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
import com.google.barberbookingapp.Model.entities.TimeSlot;
import com.google.barberbookingapp.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllTimeSlotAdapter extends RecyclerView.Adapter<AllTimeSlotAdapter.viewHolder> {

    private Context context;
    private List<TimeSlot> timeSlotList;
  //  private LocalBroadcastManager broadcastManager;
    private List<CardView > cardViewList;

    public AllTimeSlotAdapter(Context context) {
        this.context = context;
        this.timeSlotList = new ArrayList<>();
        cardViewList= new ArrayList<>();
   //     broadcastManager = LocalBroadcastManager.getInstance(context);
    }


    public AllTimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        this.timeSlotList = timeSlotList;
        cardViewList= new ArrayList<>();
      //  broadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_time_slot, parent , false);
        return new AllTimeSlotAdapter.viewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

         holder.txt_time_slot.setText(new StringBuilder(Common.ConvertTimeSlotsToString(position)));

         if (timeSlotList.size()==0){

             holder.card_time_slot.setEnabled(true);

             holder.txt_time_slot_description.setText("Available");
             holder.txt_time_slot_description.setTextColor(context.getResources().getColor(android.R.color.black));
             holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.black));
             holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));


          //AllSalon/Makram/Branch/QqQr7PpVPYUmuhFV8jtk/Barber/atwwLQxbQHqt0moVVedj/٠١_٠٤_٢٠٢٠/0
         }else {

             for (TimeSlot slotValue : timeSlotList){

                 // convert long value to String value then parse it to integer value
                       int slot = slotValue.getSlot();

                   if (slot == position){
                   //we set tag for all slots are full
                   // so we can set bacGround for all others
                       
                       holder.card_time_slot.setEnabled(false);

                     holder.card_time_slot.setTag(Common.DISPLAY_TAG);
                     //
                     holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                     holder.txt_time_slot_description.setText("Full");
                     holder.txt_time_slot_description.setTextColor(context.getResources().getColor(android.R.color.white));
                     holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.white));
                 }
             }
         }

         if (!cardViewList.contains(holder.card_time_slot)){
             cardViewList.add(holder.card_time_slot);
         }

         holder.setIrecyclerItemSelectedListner(new IrecyclerItemSelectedListner() {
             @Override
             public void onItemSelectedListner(View view, int position) {
              for (CardView cardView : cardViewList ){

                  if (cardView.getTag()==null){

                      cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));

                  } }
                      holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_dark));
                      Intent intent = new Intent(Common.KEY_ENABLE_NEXT);

                      intent.putExtra(Common.STEP , 3);
                      intent.putExtra(Common.KEY_SLOT_OF_TIME , position);

                      EventBus.getDefault().postSticky(new EnableNextButton(3 , position));

                  //    broadcastManager.sendBroadcast(intent);

             }
         });
    }


    @Override
    public int getItemCount() {

        return Common.TIME_SLOT_TOTAL;
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private IrecyclerItemSelectedListner irecyclerItemSelectedListner;
        private CardView card_time_slot;
        private TextView txt_time_slot , txt_time_slot_description;

        public viewHolder setIrecyclerItemSelectedListner(IrecyclerItemSelectedListner irecyclerItemSelectedListner) {
            this.irecyclerItemSelectedListner = irecyclerItemSelectedListner;
            return this;
        }

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            card_time_slot=itemView.findViewById(R.id.card_time_slot);
            txt_time_slot=itemView.findViewById(R.id.txt_time_slot);
            txt_time_slot_description=itemView.findViewById(R.id.txt_time_slot_discription);

            itemView.setOnClickListener(this::onClick);
        }


        @Override
        public void onClick(View v) {
         irecyclerItemSelectedListner.onItemSelectedListner(v , getAdapterPosition());
        }
    }
}
