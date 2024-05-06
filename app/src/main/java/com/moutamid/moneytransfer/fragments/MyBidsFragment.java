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
import com.moutamid.moneytransfer.activities.OthersBidActivity;
import com.moutamid.moneytransfer.adapters.BidAdapter;
import com.moutamid.moneytransfer.adapters.MyBidAdapter;
import com.moutamid.moneytransfer.databinding.FragmentMyBidsBinding;
import com.moutamid.moneytransfer.models.BidModel;
import com.moutamid.moneytransfer.utilis.Constants;

import java.util.ArrayList;

public class MyBidsFragment extends Fragment {
    FragmentMyBidsBinding binding;
    ArrayList<BidModel> list;

    public MyBidsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyBidsBinding.inflate(getLayoutInflater(), container, false);
        Stash.put("BACK", 1);
        list = new ArrayList<>();

        binding.back.setOnClickListener(v -> {
            MainActivity activity = (MainActivity) getActivity();
            if (activity!=null){
                activity.onBackClick();
            }
           // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).disallowAddToBackStack().commit();
        });

        binding.bidRC.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.bidRC.setHasFixedSize(false);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Constants.setLocale(requireContext(), Stash.getString(Constants.LANGUAGE, "en"));
        getList();
    }

    private void getList() {
       // Constants.showDialog();
        Constants.databaseReference().child(Constants.BIDS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            list.clear();
                            for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                                BidModel bidModel = datasnapshot.getValue(BidModel.class);
                                if (Constants.auth().getCurrentUser().getUid().equals(bidModel.getUserID())) {
                                    list.add(bidModel);
                                }
                            }

                            if (list.size() > 0) {
                                binding.bidRC.setVisibility(View.VISIBLE);
                                binding.noLayout.setVisibility(View.GONE);
                            } else {
                                binding.bidRC.setVisibility(View.GONE);
                                binding.noLayout.setVisibility(View.VISIBLE);
                            }
                            MyBidAdapter adapter = new MyBidAdapter(binding.getRoot().getContext(), list);
                            binding.bidRC.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError e) {
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}