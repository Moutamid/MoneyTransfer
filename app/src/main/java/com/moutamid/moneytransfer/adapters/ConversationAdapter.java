package com.moutamid.moneytransfer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.moneytransfer.R;
import com.moutamid.moneytransfer.models.ConversationModel;
import com.moutamid.moneytransfer.utilis.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationVH> {
    Context context;
    ArrayList<ConversationModel> list;

    public static final int CHAT_ITEM_LEFT = 1;
    public static final int CHAT_ITEM_RIGHT = 2;

    public ConversationAdapter(Context context, ArrayList<ConversationModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ConversationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == CHAT_ITEM_RIGHT) {
            view = LayoutInflater.from(context).inflate(R.layout.chat_row_right, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.chat_row_left, parent, false);
        }
        return new ConversationVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationVH holder, int position) {
        ConversationModel model = list.get(holder.getAbsoluteAdapterPosition());
        holder.text.setText(model.getMessage());
        String time = new SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(model.getTimestamps());
        holder.date.setText(time);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getSenderID().equals(Constants.auth().getCurrentUser().getUid()) ? CHAT_ITEM_RIGHT : CHAT_ITEM_LEFT;
    }

    public class ConversationVH extends RecyclerView.ViewHolder{
        TextView text, date;
        public ConversationVH(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            date = itemView.findViewById(R.id.date);
        }
    }

}
