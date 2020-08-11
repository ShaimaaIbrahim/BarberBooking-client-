package com.google.barberbookingapp.UI.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.barberbookingapp.Model.entities.Banner;
import com.google.barberbookingapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LookBookAdapter extends RecyclerView.Adapter<LookBookAdapter.viewHolder> {

private List<Banner> lookBoolList;
private Context context;

public LookBookAdapter (List<Banner> lookBoolList , Context context){
    this.lookBoolList=lookBoolList;
    this.context=context;
}

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.look_book_layout , parent , false);
        return new LookBookAdapter.viewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
       Banner banner = lookBoolList.get(position);
        Picasso.get().load(banner.getImage()).into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return lookBoolList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

    private ImageView imageView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image_look_book);

        }
    }
}
