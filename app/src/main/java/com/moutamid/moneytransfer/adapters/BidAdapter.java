package com.moutamid.moneytransfer.adapters;

import static com.moutamid.moneytransfer.utilis.Constants.getCurrencyCode;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.moneytransfer.R;
import com.moutamid.moneytransfer.activities.AcceptBidsActivity;
import com.moutamid.moneytransfer.models.BidModel;
import com.moutamid.moneytransfer.models.ChatModel;
import com.moutamid.moneytransfer.models.Rating;
import com.moutamid.moneytransfer.models.UserModel;
import com.moutamid.moneytransfer.utilis.Constants;

import java.util.ArrayList;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class BidAdapter extends RecyclerView.Adapter<BidAdapter.BidVH> {

    Context context;
    ArrayList<BidModel> list;

    public BidAdapter(Context context, ArrayList<BidModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BidVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BidVH(LayoutInflater.from(context).inflate(R.layout.bid_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BidVH holder, int position) {
        BidModel model = list.get(holder.getAbsoluteAdapterPosition());

        holder.from.setText("From " + model.getMyCountry());
        holder.userName.setText(model.getUsername());
        Glide.with(context).load(model.getUserImage()).placeholder(R.drawable.profile_icon).into(holder.profile);

        holder.money.setText(getCurrencyCode(model.getBidCountry()) + " " + model.getPrice_ioc());

        Rating rating = model.getUserRating();
        int rr = rating.getStar1() + rating.getStar2() + rating.getStar3() + rating.getStar4() + rating.getStar5();
        double rate = rr / 5;
        if (rate > 0.0) {
            holder.star1.setImageResource(R.drawable.round_star_24);
        } else if (rate >= 1.0) {
            holder.star1.setImageResource(R.drawable.round_star_24);
            holder.star2.setImageResource(R.drawable.round_star_24);
        } else if (rate >= 2.0) {
            holder.star1.setImageResource(R.drawable.round_star_24);
            holder.star2.setImageResource(R.drawable.round_star_24);
            holder.star3.setImageResource(R.drawable.round_star_24);
        } else if (rate >= 3.0) {
            holder.star1.setImageResource(R.drawable.round_star_24);
            holder.star2.setImageResource(R.drawable.round_star_24);
            holder.star3.setImageResource(R.drawable.round_star_24);
            holder.star4.setImageResource(R.drawable.round_star_24);
        } else if (rate >= 4.0) {
            holder.star1.setImageResource(R.drawable.round_star_24);
            holder.star2.setImageResource(R.drawable.round_star_24);
            holder.star3.setImageResource(R.drawable.round_star_24);
            holder.star4.setImageResource(R.drawable.round_star_24);
            holder.star5.setImageResource(R.drawable.round_star_24);
        }

        holder.accept.setOnClickListener(v -> {
            Constants.showDialog();
            String ID = UUID.randomUUID().toString();
            ChatModel chatModel = new ChatModel(
                    ID, model.getUserID(),
                    model.getUserImage(), model.getUsername(), "Start Conversation"
            );
            UserModel stash = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);
            ChatModel other = new ChatModel(
                    ID, stash.getID(),
                    stash.getImage(), stash.getName(), "Start Conversation"
            );
            Constants.databaseReference().child(Constants.CHATS).child(Constants.auth().getCurrentUser().getUid()).child(chatModel.getID())
                    .setValue(chatModel).addOnFailureListener(e -> {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Constants.dismissDialog();
                    }).addOnSuccessListener(unused -> {
                        Constants.databaseReference().child(Constants.CHATS).child(chatModel.getUserID()).child(chatModel.getID())
                                .setValue(other).addOnFailureListener(e -> {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Constants.dismissDialog();
                                }).addOnSuccessListener(unused1 -> {
                                    Constants.dismissDialog();
                                    Toast.makeText(context, "You can now start conversation with this user", Toast.LENGTH_SHORT).show();
                                });
                    });
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BidVH extends RecyclerView.ViewHolder {
        TextView userName, money, from;
        ImageView star1, star2, star3, star4, star5;
        CircleImageView profile;
        Button accept;

        public BidVH(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            money = itemView.findViewById(R.id.money);
            from = itemView.findViewById(R.id.from);
            profile = itemView.findViewById(R.id.profile);
            accept = itemView.findViewById(R.id.accept);
            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);
            star4 = itemView.findViewById(R.id.star4);
            star5 = itemView.findViewById(R.id.star5);
        }
    }

}
