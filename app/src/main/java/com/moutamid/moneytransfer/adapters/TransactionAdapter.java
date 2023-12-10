package com.moutamid.moneytransfer.adapters;

import static com.moutamid.moneytransfer.utilis.Constants.getCurrencyCode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.moneytransfer.R;
import com.moutamid.moneytransfer.models.Rating;
import com.moutamid.moneytransfer.models.TransactionModel;
import com.moutamid.moneytransfer.models.UserModel;
import com.moutamid.moneytransfer.utilis.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionVH> {

    Context context;
    ArrayList<TransactionModel> list;

    public TransactionAdapter(Context context, ArrayList<TransactionModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TransactionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionVH(LayoutInflater.from(context).inflate(R.layout.transaction_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionVH holder, int position) {
        TransactionModel model = list.get(holder.getAbsoluteAdapterPosition());
        UserModel userModel = model.getReceiver();
        UserModel stash = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);

        holder.name.setText(model.getTransactionName());
        holder.money.setText(getCurrencyCode(stash.getCountry()) + " " + model.getAmount_ioc());
        Glide.with(context).load(userModel.getImage()).placeholder(R.drawable.profile_icon).into(holder.profile);

        String date = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(model.getTimestamp());
        holder.date.setText(date);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TransactionVH extends RecyclerView.ViewHolder{
        TextView name, money,date;
        ImageView profile;
        public TransactionVH(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.img_nav_logo);
            money = itemView.findViewById(R.id.money);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
        }
    }
}
