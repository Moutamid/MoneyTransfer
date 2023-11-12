package com.moutamid.moneytransfer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.moneytransfer.R;
import com.moutamid.moneytransfer.adapters.BidAdapter;
import com.moutamid.moneytransfer.databinding.ActivityOthersBidBinding;
import com.moutamid.moneytransfer.models.BidModel;
import com.moutamid.moneytransfer.models.UserModel;
import com.moutamid.moneytransfer.utilis.Constants;

import java.util.ArrayList;

public class OthersBidActivity extends AppCompatActivity {
    ActivityOthersBidBinding binding;
    ArrayList<BidModel> list;
    UserModel stashUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOthersBidBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.title.setText("Find Your Bid");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        Constants.initDialog(this);

        binding.bidRC.setLayoutManager(new LinearLayoutManager(this));
        binding.bidRC.setHasFixedSize(false);

        list = new ArrayList<>();
        stashUser = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);

        getAll();

        binding.search.setOnClickListener(v -> {
            if (binding.max.getEditText().getText().toString().isEmpty() && binding.min.getEditText().getText().toString().isEmpty()) {
                getAll();
            } else {
                if (!binding.max.getEditText().getText().toString().isEmpty()) {
                    Constants.showDialog();
                    Constants.databaseReference().child(Constants.BIDS).get()
                            .addOnSuccessListener(snapshot -> {
                                Constants.dismissDialog();
                                if (snapshot.exists()) {
                                    list.clear();
                                    for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                                        BidModel bidModel = datasnapshot.getValue(BidModel.class);
                                        String mn = binding.min.getEditText().getText().toString().isEmpty() ? "0.0" : binding.min.getEditText().getText().toString();
                                        String mx = binding.max.getEditText().getText().toString().isEmpty() ? "0.0" : binding.max.getEditText().getText().toString();
                                        double min = Double.parseDouble(mn);
                                        double max = Double.parseDouble(mx);
                                        if (!stashUser.getID().equals(bidModel.getUserID()) &&
                                                bidModel.getBidCountry().equals(stashUser.getCountry()) &&
                                                (bidModel.getPrice_ioc() >= min && bidModel.getPrice_ioc() <= max)
                                        ) {
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
                                    BidAdapter adapter = new BidAdapter(OthersBidActivity.this, list);
                                    binding.bidRC.setAdapter(adapter);
                                }
                            }).addOnFailureListener(e -> {
                                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                Constants.dismissDialog();
                            });
                } else {
                    Toast.makeText(this, "Max range is required", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getAll() {
        Constants.showDialog();
        Constants.databaseReference().child(Constants.BIDS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                        BidModel bidModel = datasnapshot.getValue(BidModel.class);
                        if (!stashUser.getID().equals(bidModel.getUserID()) && bidModel.getBidCountry().equals(stashUser.getCountry()))
                            list.add(bidModel);
                    }

                    if (list.size() > 0) {
                        binding.bidRC.setVisibility(View.VISIBLE);
                        binding.noLayout.setVisibility(View.GONE);
                    } else {
                        binding.bidRC.setVisibility(View.GONE);
                        binding.noLayout.setVisibility(View.VISIBLE);
                    }
                    BidAdapter adapter = new BidAdapter(OthersBidActivity.this, list);
                    binding.bidRC.setAdapter(adapter);
                }
                Constants.dismissDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Constants.dismissDialog();
                Toast.makeText(OthersBidActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}