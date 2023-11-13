package com.moutamid.moneytransfer.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.moutamid.moneytransfer.R;
import com.moutamid.moneytransfer.adapters.ConversationAdapter;
import com.moutamid.moneytransfer.databinding.ActivityConversationBinding;
import com.moutamid.moneytransfer.models.ChatModel;
import com.moutamid.moneytransfer.models.ConversationModel;
import com.moutamid.moneytransfer.models.CountriesRates;
import com.moutamid.moneytransfer.models.Rating;
import com.moutamid.moneytransfer.models.TransactionModel;
import com.moutamid.moneytransfer.models.UserModel;
import com.moutamid.moneytransfer.utilis.Constants;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ConversationActivity extends AppCompatActivity {
    ActivityConversationBinding binding;
    ArrayList<ConversationModel> list;
    ChatModel chatModel;
    ConversationAdapter adapter;
    String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConversationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatModel = (ChatModel) Stash.getObject(Constants.CHAT_ITEM, ChatModel.class);

        Constants.initDialog(this);
        list = new ArrayList<>();

        binding.ChatRC.setLayoutManager(new LinearLayoutManager(this));
        binding.ChatRC.setHasFixedSize(false);

        getChat();

        binding.toolbar.title.setText(chatModel.getName());
        binding.toolbar.popup.setVisibility(View.VISIBLE);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.popup.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(ConversationActivity.this, v);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.complete) {
                    completeTransaction();
                    return true;
                } else if (itemId == R.id.rate_user) {
                    rateUser();
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });

        binding.send.setOnClickListener(v -> {
            if (!binding.message.getText().toString().isEmpty()) {
                message = binding.message.getText().toString();
                binding.message.setText("");
                ConversationModel sender = new ConversationModel(message, Constants.auth().getCurrentUser().getUid(), new Date().getTime());

                Constants.databaseReference().child(Constants.CONVERSATION).child(Constants.auth().getCurrentUser().getUid()).child(chatModel.getID()).push().setValue(sender).addOnSuccessListener(unused -> {
                    Constants.databaseReference().child(Constants.CONVERSATION).child(chatModel.getUserID()).child(chatModel.getID()).push().setValue(sender).addOnSuccessListener(unused1 -> {

                    }).addOnFailureListener(e -> e.printStackTrace());
                }).addOnFailureListener(e -> e.printStackTrace());

            }
        });

    }

    private void getChat() {
//        Constants.showDialog();
        Constants.databaseReference().child(Constants.CONVERSATION).child(Constants.auth().getCurrentUser().getUid()).child(chatModel.getID()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    ConversationModel model = snapshot.getValue(ConversationModel.class);
                    list.add(model);
                    list.sort(Comparator.comparing(ConversationModel::getTimestamps));
                    adapter = new ConversationAdapter(ConversationActivity.this, list);
                    binding.ChatRC.setAdapter(adapter);
                    binding.ChatRC.scrollToPosition(list.size() - 1);
                    adapter.notifyItemInserted(list.size() - 1);
                } else {
                    Toast.makeText(ConversationActivity.this, "No Chat Available", Toast.LENGTH_SHORT).show();
                }
//                        Constants.dismissDialog();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {

                }
//                        Constants.dismissDialog();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                }
//                        Constants.dismissDialog();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {

                }
//                        Constants.dismissDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ConversationActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                        Constants.dismissDialog();
            }
        });
    }

    private void rateUser() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rate_user_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(true);
        dialog.show();

        Button rate = dialog.findViewById(R.id.rate_user);
        ImageView star1, star2, star3, star4, star5;
        star1 = dialog.findViewById(R.id.star1);
        star2 = dialog.findViewById(R.id.star2);
        star3 = dialog.findViewById(R.id.star3);
        star4 = dialog.findViewById(R.id.star4);
        star5 = dialog.findViewById(R.id.star5);

        AtomicInteger starCount = new AtomicInteger();

        star1.setOnClickListener(v -> {
            starCount.set(1);
            star1.setImageResource(R.drawable.round_star_24);
            star2.setImageResource(R.drawable.round_star_border_24);
            star3.setImageResource(R.drawable.round_star_border_24);
            star4.setImageResource(R.drawable.round_star_border_24);
            star5.setImageResource(R.drawable.round_star_border_24);
        });

        star2.setOnClickListener(v -> {
            starCount.set(2);
            star1.setImageResource(R.drawable.round_star_24);
            star2.setImageResource(R.drawable.round_star_24);
            star3.setImageResource(R.drawable.round_star_border_24);
            star4.setImageResource(R.drawable.round_star_border_24);
            star5.setImageResource(R.drawable.round_star_border_24);
        });


        star3.setOnClickListener(v -> {
            starCount.set(3);
            star1.setImageResource(R.drawable.round_star_24);
            star2.setImageResource(R.drawable.round_star_24);
            star3.setImageResource(R.drawable.round_star_24);
            star4.setImageResource(R.drawable.round_star_border_24);
            star5.setImageResource(R.drawable.round_star_border_24);
        });

        star4.setOnClickListener(v -> {
            starCount.set(4);
            star1.setImageResource(R.drawable.round_star_24);
            star2.setImageResource(R.drawable.round_star_24);
            star3.setImageResource(R.drawable.round_star_24);
            star4.setImageResource(R.drawable.round_star_24);
            star5.setImageResource(R.drawable.round_star_border_24);
        });


        star5.setOnClickListener(v -> {
            starCount.set(5);
            star1.setImageResource(R.drawable.round_star_24);
            star2.setImageResource(R.drawable.round_star_24);
            star3.setImageResource(R.drawable.round_star_24);
            star4.setImageResource(R.drawable.round_star_24);
            star5.setImageResource(R.drawable.round_star_24);
        });

        rate.setOnClickListener(v -> {
            dialog.dismiss();
            Constants.showDialog();
            Constants.databaseReference().child(Constants.USER).child(chatModel.getUserID()).get().addOnSuccessListener(dataSnapshot -> {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                Rating rating = userModel.getRating();
                Map<String, Object> map = new HashMap<>();
                int star = starCount.get();
                if (star == 1) {
                    map.put("star1", rating.getStar1() + 1);
                } else if (star == 2) {
                    map.put("star2", rating.getStar2() + 1);
                } else if (star == 3) {
                    map.put("star3", rating.getStar3() + 1);
                } else if (star == 4) {
                    map.put("star4", rating.getStar4() + 1);
                } else if (star == 5) {
                    map.put("star5", rating.getStar5() + 1);
                }
                Constants.databaseReference().child(Constants.USER).child(userModel.getID()).child("rating").updateChildren(map).addOnSuccessListener(unused -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, "Your rate is counted Thanks for your feedback!", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });

            }).addOnFailureListener(e -> {
                Constants.dismissDialog();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });

    }

    private void completeTransaction() {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.transaction_value);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(true);
        dialog.show();

        UserModel stash = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);
        Button complete = dialog.findViewById(R.id.complete);
        TextInputLayout amount = dialog.findViewById(R.id.amount);

        complete.setOnClickListener(v -> {
            if (!amount.getEditText().getText().toString().isEmpty()) {
                Constants.showDialog();
                String ID = UUID.randomUUID().toString();

                Constants.databaseReference().child(Constants.USER).child(chatModel.getUserID()).get().addOnSuccessListener(dataSnapshot -> {
                    UserModel model = dataSnapshot.getValue(UserModel.class);
                    long time = new Date().getTime();
                    TransactionModel myModel = new TransactionModel(
                            ID, Constants.auth().getCurrentUser().getUid(), "",
                            Double.parseDouble(amount.getEditText().getText().toString()),
                            Double.parseDouble(amount.getEditText().getText().toString()) * getCurrency(stash.getCountry()),
                            time,
                            model
                    );
                    TransactionModel receiverModel = new TransactionModel(
                            ID, model.getID(), "",
                            Double.parseDouble(amount.getEditText().getText().toString()),
                            Double.parseDouble(amount.getEditText().getText().toString()) * getCurrency(model.getCountry()),
                            time,
                            stash
                    );

                    Constants.databaseReference().child(Constants.TRANSACTIONS).child(Constants.auth().getCurrentUser().getUid())
                            .push().setValue(myModel)
                            .addOnSuccessListener(unused -> {
                                Constants.databaseReference().child(Constants.TRANSACTIONS).child(model.getID())
                                        .push().setValue(receiverModel)
                                        .addOnSuccessListener(unused22 -> {
                                            Constants.dismissDialog();
                                            Toast.makeText(this, "Transaction Completed!", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Constants.dismissDialog();
                                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Constants.dismissDialog();
                                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            });


                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });


            }
        });
    }

    private double getCurrency(String currency) {
        CountriesRates countriesRates = (CountriesRates) Stash.getObject(Constants.Values, CountriesRates.class);
        if (currency.equals(Constants.EGYPT)) {
            return countriesRates.getRates().getEgypt();
        } else if (currency.equals(Constants.ITALY)) {
            return countriesRates.getRates().getItaly();
        } else if (currency.equals(Constants.United_Arab_Emirates)) {
            return countriesRates.getRates().getUAE();
        } else if (currency.equals(Constants.SAUDI_ARABIA)) {
            return countriesRates.getRates().getSaudi_Arabia();
        } else if (currency.equals(Constants.QATAR)) {
            return countriesRates.getRates().getQatar();
        } else if (currency.equals(Constants.MOROCCO)) {
            return countriesRates.getRates().getMorocco();
        } else if (currency.equals(Constants.SUDAN)) {
            return countriesRates.getRates().getSudan();
        } else if (currency.equals(Constants.OMAN)) {
            return countriesRates.getRates().getOman();
        } else if (currency.equals(Constants.RUSSIA)) {
            return countriesRates.getRates().getRussia();
        } else if (currency.equals(Constants.SYRIA)) {
            return countriesRates.getRates().getSyria();
        } else if (currency.equals(Constants.PALESTINE)) {
            return countriesRates.getRates().getPalestine();
        }
        return 0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Stash.clear(Constants.CHAT_ITEM);
    }
}