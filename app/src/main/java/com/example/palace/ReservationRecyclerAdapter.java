package com.example.palace;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.bumptech.glide.Glide;

public class ReservationRecyclerAdapter extends RecyclerView.Adapter<ReservationRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ListItem> mReservationList = new ArrayList<ListItem>();

    @NonNull
    // @NotNull
    @Override
    public ReservationRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull /*@NotNull*/ ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recycler_item, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull /*@NotNull*/ ReservationRecyclerAdapter.ViewHolder holder, int position) {
        ListItem reservationItem = mReservationList.get(position);
        holder.onBind(reservationItem);

    }

    public void setDonationList(Context context, ArrayList<ListItem> list){
        this.context = context;
        this.mReservationList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mReservationList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView DateTv;
        ImageView PlaceImg;
        TextView PlaceNTv;
        TextView LocationTv;
        TextView GroupNTv;
        TextView ReserCountTv;
        ImageView ListBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            DateTv = (TextView) itemView.findViewById(R.id.DateTv);
            PlaceImg = (ImageView) itemView.findViewById(R.id.PlaceImg);
            PlaceNTv = (TextView) itemView.findViewById(R.id.PlaceNTv);
            LocationTv = (TextView) itemView.findViewById(R.id.LocationTv);
            GroupNTv = (TextView) itemView.findViewById(R.id.GroupNTv);
            ReserCountTv = (TextView) itemView.findViewById(R.id.ReserCountTv);
            ListBtn = (ImageView) itemView.findViewById(R.id.ListBtn);
        }

        void onBind(ListItem item){

            DateTv.setText(item.getDateTv());
            PlaceNTv.setText(item.getPlaceNTv());
            LocationTv.setText(item.getLocationTv());
            GroupNTv.setText(item.getGroupNTv());
            ReserCountTv.setText(item.getReserCountTv());
            Glide.with(context).load(item.getPlaceImg()).into(PlaceImg);
            Glide.with(context).load(item.getListBtn()).into(ListBtn);
        }
    }

}


