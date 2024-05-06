package com.moutamid.moneytransfer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;
import com.moutamid.moneytransfer.R;
import com.moutamid.moneytransfer.models.CurrenciesModel;
import com.moutamid.moneytransfer.models.UserModel;
import com.moutamid.moneytransfer.utilis.Constants;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
        holder.amount.setText(amount.getSign() + amount.getAmount());
        holder.code.setText(amount.getSymbol());
        holder.countryName.setText(amount.getName());
        holder.flag.setImageResource(amount.getIcon());
        UserModel userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);
        String calc = "1" + Constants.getCurrencyCode(userModel.getCountry()) + " = " + (1 * Constants.parseDoubleWithLocale(amount.getAmount())) + amount.getSign();
        holder.calculation.setText(calc);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CurrencyVH extends RecyclerView.ViewHolder{
        TextView amount, code, countryName, calculation;
        CircleImageView flag;
        public CurrencyVH(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount);
            code = itemView.findViewById(R.id.code);
            countryName = itemView.findViewById(R.id.countryName);
            flag = itemView.findViewById(R.id.flag);
            calculation = itemView.findViewById(R.id.calculation);
        }
    }

}
