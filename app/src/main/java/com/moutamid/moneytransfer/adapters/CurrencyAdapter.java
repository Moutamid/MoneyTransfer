package com.moutamid.moneytransfer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.moneytransfer.R;

import java.util.ArrayList;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyVH> {

    Context context;
    ArrayList<String> list;

    public CurrencyAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CurrencyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CurrencyVH(LayoutInflater.from(context).inflate(R.layout.currency_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyVH holder, int position) {
        String amount = list.get(holder.getAbsoluteAdapterPosition());
        holder.amount.setText(amount);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CurrencyVH extends RecyclerView.ViewHolder{
        TextView amount;
        public CurrencyVH(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount);
        }
    }

}
