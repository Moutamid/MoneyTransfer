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
import com.moutamid.moneytransfer.activities.AcceptBidsActivity;
import com.moutamid.moneytransfer.activities.ConversationActivity;
import com.moutamid.moneytransfer.models.BidModel;
import com.moutamid.moneytransfer.models.ChatModel;
import com.moutamid.moneytransfer.models.Rating;
import com.moutamid.moneytransfer.models.UserModel;
import com.moutamid.moneytransfer.utilis.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class BidAdapter extends RecyclerView.Adapter<BidAdapter.BidVH> implements Filterable {

    Context context;
    ArrayList<BidModel> list;

    ArrayList<BidModel> allList;

    public BidAdapter(Context context, ArrayList<BidModel> list) {
        this.context = context;
        this.list = list;
        this.allList = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public BidVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BidVH(LayoutInflater.from(context).inflate(R.layout.bid_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BidVH holder, int position) {
        BidModel model = list.get(holder.getAbsoluteAdapterPosition());

        holder.from.setText(model.getMyCountry());
        holder.to.setText(model.getBidCountry());
        holder.userName.setText(model.getUsername());
        Glide.with(context).load(model.getUserImage()).placeholder(R.drawable.profile_icon).into(holder.profile);
        String money = (getCurrencyCode(model.getBidCountry()) + " " + model.getPrice_ioc()) + " = " + (getCurrencyCode(model.getMyCountry()) + " " + model.getPrice());
        holder.money.setText(money);

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

        holder.chat.setOnClickListener(v -> {
            Constants.showDialog();
            Constants.databaseReference().child(Constants.CHATS).child(Constants.auth().getCurrentUser().getUid())
                    .get().addOnSuccessListener(snapshot -> {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                                if (chatModel.getUserID().equals(model.getUserID())){
                                    Stash.put(Constants.CHAT_ITEM, model);
                                    context.startActivity(new Intent(context, ConversationActivity.class));
                                    break;
                                }
                            }
                        } else {
                            Toast.makeText(context, "Chat not available yet", Toast.LENGTH_SHORT).show();
                        }
                        Constants.dismissDialog();
                    }).addOnFailureListener(e -> {
                        Constants.dismissDialog();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

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

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<BidModel> filterList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filterList.addAll(allList);
            } else {
                for (BidModel listModel : allList) {
                    if (listModel.getMyCountry().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filterList.add(listModel);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((Collection<? extends BidModel>) results.values);
            notifyDataSetChanged();
        }
    };

    public class BidVH extends RecyclerView.ViewHolder {
        TextView userName, money, to, from;
        ImageView star1, star2, star3, star4, star5;
        CircleImageView profile;
        Button accept;
        MaterialButton chat;

        public BidVH(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            money = itemView.findViewById(R.id.money);
            from = itemView.findViewById(R.id.from);
            to = itemView.findViewById(R.id.to);
            profile = itemView.findViewById(R.id.profile);
            accept = itemView.findViewById(R.id.accept);
            chat = itemView.findViewById(R.id.chat);
            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);
            star4 = itemView.findViewById(R.id.star4);
            star5 = itemView.findViewById(R.id.star5);
        }
    }

}
