package com.moutamid.moneytransfer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.moneytransfer.R;
import com.moutamid.moneytransfer.models.CurrenciesModel;

import java.util.ArrayList;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyVH> {

    Context context;
    ArrayList<CurrenciesModel> list;

    public CurrencyAdapter(Context context, ArrayList<CurrenciesModel> list) {
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
        CurrenciesModel amount = list.get(holder.getAbsoluteAdapterPosition());
        holder.amount.setText(amount.getAmount());
        holder.countryName.setText(amount.getName() + " Current Rate");
        holder.flag.setImageResource(amount.getIcon());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CurrencyVH extends RecyclerView.ViewHolder{
        TextView amount, countryName;
        ImageView flag;
        public CurrencyVH(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount);
            countryName = itemView.findViewById(R.id.countryName);
            flag = itemView.findViewById(R.id.flag);
        }
    }

}
