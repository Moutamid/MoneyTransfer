package com.moutamid.moneytransfer.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.moneytransfer.MainActivity;
import com.moutamid.moneytransfer.R;
import com.moutamid.moneytransfer.adapters.ChatAdapter;
import com.moutamid.moneytransfer.databinding.FragmentChatBinding;
import com.moutamid.moneytransfer.models.ChatModel;
import com.moutamid.moneytransfer.utilis.Constants;

import java.util.ArrayList;

public class ChatFragment extends Fragment {
    FragmentChatBinding binding;
    ArrayList<ChatModel> list;
    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(getLayoutInflater(), container, false);

        binding.back.setOnClickListener(v -> {
            MainActivity activity = (MainActivity) getActivity();
            if (activity!=null){
                activity.onBackClick();
            }
            // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).disallowAddToBackStack().commit();
           // getActivity().getSupportFragmentManager().popBackStack("Chat", FragmentManager.POP_BACK_STACK_INCLUSIVE); // ("Chat", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        });

        binding.chatRC.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.chatRC.setHasFixedSize(false);


        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        Constants.setLocale(requireContext(), Stash.getString(Constants.LANGUAGE, "en"));
        getChats();
    }

    private void getChats() {
//        Constants.showDialog();
        list = new ArrayList<>();
        Constants.databaseReference().child(Constants.CHATS).child(Constants.auth().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                                list.add(chatModel);
                            }

                            ChatAdapter adapter = new ChatAdapter(binding.getRoot().getContext(), list);
                            binding.chatRC.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(binding.getRoot().getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}