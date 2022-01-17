package com.university.finalwork;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.university.finalwork.content.OrderContent;

import java.util.List;

public class MyOrdersRecyclerViewAdapter extends RecyclerView.Adapter<MyOrdersRecyclerViewAdapter.ViewHolder> {

    private final List<OrderContent.OrderItem> mValues;

    public MyOrdersRecyclerViewAdapter(List<OrderContent.OrderItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mOrderUser.setText(mValues.get(position).mOrderUser);
        holder.mOrderAddress.setText(mValues.get(position).mOrderAddress);
        holder.mOrderItem.setText(mValues.get(position).mOrderItem);
        holder.mOrderQuantity.setText(mValues.get(position).mOrderQuantity);
        holder.mOrderFinalPrice.setText(mValues.get(position).mOrderFinalPrice);

        holder.mOrderMarkDelivered.setOnClickListener(v -> {

            DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders");
            orderRef.child(holder.mItem.id).child("delivered").setValue(true);
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mOrderUser;
        public final TextView mOrderFinalPrice;
        public final TextView mOrderAddress;
        public final TextView mOrderItem;
        public final TextView mOrderQuantity;
        public final Button mOrderMarkDelivered;
        public OrderContent.OrderItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mOrderUser = (TextView) view.findViewById(R.id.order_user);
            mOrderFinalPrice = (TextView) view.findViewById(R.id.order_final_price);
            mOrderAddress = (TextView) view.findViewById(R.id.order_address);
            mOrderItem = (TextView) view.findViewById(R.id.order_item);
            mOrderQuantity = (TextView) view.findViewById(R.id.order_quantity);
            mOrderMarkDelivered = (Button) view.findViewById(R.id.order_mark_delivered);
        }
    }
}