package com.moutamid.moneytransfer.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.moneytransfer.R;
import com.moutamid.moneytransfer.activities.ConversationActivity;
import com.moutamid.moneytransfer.models.ChatModel;
import com.moutamid.moneytransfer.utilis.Constants;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatVH>{
    Context context;
    ArrayList<ChatModel> list;

    public ChatAdapter(Context context, ArrayList<ChatModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChatVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatVH(LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatVH holder, int position) {
        ChatModel model = list.get(holder.getAbsoluteAdapterPosition());
        holder.name.setText(model.getName());
        holder.message.setText(model.getMessage());
        Glide.with(context).load(model.getImage()).placeholder(R.drawable.profile_icon).into(holder.logo);

        holder.itemView.setOnClickListener(v -> {
            Stash.put(Constants.CHAT_ITEM, model);
            context.startActivity(new Intent(context, ConversationActivity.class));
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ChatVH extends RecyclerView.ViewHolder{
        CircleImageView logo;
        TextView name, message;
        public ChatVH(@NonNull View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.logo);
            name = itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message);
        }
    }

}
