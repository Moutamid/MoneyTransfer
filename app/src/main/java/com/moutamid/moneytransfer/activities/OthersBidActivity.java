package com.moutamid.moneytransfer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
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
import java.util.Comparator;

public class OthersBidActivity extends AppCompatActivity {
    ActivityOthersBidBinding binding;
    ArrayList<BidModel> list;
    UserModel stashUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOthersBidBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.setLocale(getBaseContext(), Stash.getString(Constants.LANGUAGE, "en"));

        binding.toolbar.title.setText(getString(R.string.find_your_bid));
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.bidRC.setLayoutManager(new LinearLayoutManager(this));
        binding.bidRC.setHasFixedSize(false);

        list = new ArrayList<>();
        stashUser = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);

        getAll();

        binding.filter.setOnClickListener(v -> {
            showPopupMenu(v);
        });

/*
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
                                    adapter = new BidAdapter(OthersBidActivity.this, list);
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
*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.initDialog(this);
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(OthersBidActivity.this, v);
        popupMenu.getMenuInflater().inflate(R.menu.sort_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.egypt) {
                adapter.getFilter().filter("egypt");
                return true;
            } else if (itemId == R.id.uae) {
                adapter.getFilter().filter("United_Arab_Emirates");
                return true;
            } else if (itemId == R.id.Saudi_Arabia) {
                adapter.getFilter().filter("Saudi Arabia");
                return true;
            } else if (itemId == R.id.Qatar) {
                adapter.getFilter().filter("Qatar");
                return true;
            } else if (itemId == R.id.Morocco) {
                adapter.getFilter().filter("Morocco");
                return true;
            } else if (itemId == R.id.Sudan) {
                adapter.getFilter().filter("Sudan");
                return true;
            } else if (itemId == R.id.Oman) {
                adapter.getFilter().filter("Oman");
                return true;
            } else if (itemId == R.id.Italy) {
                adapter.getFilter().filter("Italy");
                return true;
            } else if (itemId == R.id.Russia) {
                adapter.getFilter().filter("Russia");
                return true;
            } else if (itemId == R.id.Syria) {
                adapter.getFilter().filter("Syria");
                return true;
            } else if (itemId == R.id.Palestine) {
                adapter.getFilter().filter("Palestine");
                return true;
            } else if (itemId == R.id.rollback) {
                adapter.getFilter().filter("");
                return true;
            }
            return false;
        });
        popupMenu.show();
    }
    BidAdapter adapter;
    private void getAll() {
        Constants.showDialog();
        Constants.databaseReference().child(Constants.BIDS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                        BidModel bidModel = datasnapshot.getValue(BidModel.class);
                        list.add(bidModel);
//                        if (!stashUser.getID().equals(bidModel.getUserID()) && bidModel.getBidCountry().equals(stashUser.getCountry()))
//                            list.add(bidModel);
                    }

                    if (list.size() > 0) {
                        binding.bidRC.setVisibility(View.VISIBLE);
                        binding.noLayout.setVisibility(View.GONE);
                    } else {
                        binding.bidRC.setVisibility(View.GONE);
                        binding.noLayout.setVisibility(View.VISIBLE);
                    }
                    adapter = new BidAdapter(OthersBidActivity.this, list);
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