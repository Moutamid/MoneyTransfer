package com.moutamid.moneytransfer.adapters;

import static com.moutamid.moneytransfer.utilis.Constants.getCurrencyCode;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.moutamid.moneytransfer.R;
import com.moutamid.moneytransfer.activities.BidEditActivity;
import com.moutamid.moneytransfer.activities.ConversationActivity;
import com.moutamid.moneytransfer.models.BidModel;
import com.moutamid.moneytransfer.models.ChatModel;
import com.moutamid.moneytransfer.models.Rating;
import com.moutamid.moneytransfer.models.UserModel;
import com.moutamid.moneytransfer.utilis.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyBidAdapter extends RecyclerView.Adapter<MyBidAdapter.BidVH> {

    Context context;
    ArrayList<BidModel> list;

    ArrayList<BidModel> allList;

    public MyBidAdapter(Context context, ArrayList<BidModel> list) {
        this.context = context;
        this.list = list;
        this.allList = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public BidVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BidVH(LayoutInflater.from(context).inflate(R.layout.my_bid_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BidVH holder, int position) {
        BidModel model = list.get(holder.getAbsoluteAdapterPosition());

        holder.from.setText(model.getMyCountry().replace("_", " "));
        holder.to.setText(model.getBidCountry().replace("_", " "));
        String money = (getCurrencyCode(model.getBidCountry().replace("_", " ")) + " " + model.getPrice_ioc()) + " = " + (getCurrencyCode(model.getMyCountry().replace("_", " ")) + " " + model.getPrice());
        holder.money.setText(money);

        String date = new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault()).format(model.getTimestamp());
        holder.date.setText(date);

        holder.accept.setOnClickListener(v -> {
            context.startActivity(new Intent(context, BidEditActivity.class).putExtra("ID", model.getID()));
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BidVH extends RecyclerView.ViewHolder {
        TextView money, to, from, date;
        Button accept;

        public BidVH(@NonNull View itemView) {
            super(itemView);
            money = itemView.findViewById(R.id.money);
            date = itemView.findViewById(R.id.date);
            from = itemView.findViewById(R.id.from);
            to = itemView.findViewById(R.id.to);
            accept = itemView.findViewById(R.id.accept);
        }
    }

}
