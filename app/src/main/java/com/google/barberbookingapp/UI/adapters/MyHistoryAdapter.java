package com.google.barberbookingapp.UI.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.barberbookingapp.Model.entities.BookingInformation;
import com.google.barberbookingapp.R;

import java.util.List;

public class MyHistoryAdapter extends RecyclerView.Adapter<MyHistoryAdapter.viewHolder> {

    private Context context ;
    private List<BookingInformation> bookingInformationList;


    public MyHistoryAdapter(Context context , List<BookingInformation> bookingInformationList) {
        this.context = context;
        this.bookingInformationList = bookingInformationList;

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

  return new viewHolder(LayoutInflater.from(context).inflate(R.layout.layout_history , parent , false));

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        BookingInformation bookingInformation = bookingInformationList.get(position);

        holder.bookingTime.setText(bookingInformation.getTime());
        holder.barberName.setText(bookingInformation.getBarberName());
        holder.booking_date.setText(bookingInformation.getTimestamp().toString());
        holder.salon_name.setText(bookingInformation.getSalonName());
        holder.salon_address.setText(bookingInformation.getSalonAddress());

    }

    @Override
    public int getItemCount() {

        return  bookingInformationList.size() ;
    }



    public class viewHolder  extends RecyclerView.ViewHolder {

        private TextView salon_address;
        private TextView salon_name;
        private TextView bookingTime;
        private TextView barberName ;
        private TextView booking_date;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            salon_name =itemView.findViewById(R.id.salon_name);
            salon_address = itemView.findViewById(R.id.txt_salon_address);
            bookingTime = itemView.findViewById(R.id.txt_salon_time);
            barberName= itemView.findViewById(R.id.txt_barber);
            booking_date= itemView.findViewById(R.id.txt_booking_date);

        }
    }
}
