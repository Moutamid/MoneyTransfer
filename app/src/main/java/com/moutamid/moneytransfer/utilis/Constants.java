package com.moutamid.moneytransfer.utilis;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.moutamid.moneytransfer.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constants {

    static Dialog dialog;
    public static final String DATEFORMATE = "dd/MM/yyyy";
    public static final String USER = "USERS";
    public static final String BIDS = "BIDS";
    public static final String CHAT_ITEM = "CHAT_ITEM";
    public static final String TRANSACTIONS = "TRANSACTIONS";
    public static final String ONGOING_BIDS = "ONGOING_BIDS";
    public static final String Values = "CountriesRate";
    public static final String CHATS = "CHATS";
    public static final String CONVERSATION = "CONVERSATION";
    public static final String STASH_USER = "STASH_USER";
    public static final String CountriesCodes = "eg,ae,sa,qa,ma,sd,om,it,ru,sy,ps";

    // Currencies Code
    public static final String Egypt = "EGP";
    public static final String UAE = "AED";
    public static final String Saudi_Arabia = "SAR";
    public static final String Qatar = "QAR";
    public static final String Morocco = "MAP";
    public static final String Sudan = "SDG";
    public static final String Oman = "OMR";
    public static final String Italy = "EUR";
    public static final String Russia = "RUB";
    public static final String Syria = "SYP";
    public static final String Palestine = "ILS";

    //Countries
    public static final String EGYPT= "Egypt";
    public static final String ITALY = "Italy";
    public static final String United_Arab_Emirates = "United_Arab_Emirates";
    public static final String SAUDI_ARABIA = "Saudi_Arabia";
    public static final String QATAR = "Qatar";
    public static final String MOROCCO = "Morocco";
    public static final String SUDAN = "Sudan";
    public static final String OMAN = "Oman";
    public static final String RUSSIA = "Russia";
    public static final String SYRIA = "Syria";
    public static final String PALESTINE = "Palestine";

    public static String getNameCode(String myCountry) {
        // "eg,ae,sa,qa,ma,sd,om,it,ru,sy,ps"
        switch (myCountry) {
            case "Egypt":
                return "eg";
            case "United Arab Emirates":
                return "ae";
            case "Saudi Arabia":
                return "sa";
            case "Qatar":
                return "qa";
            case "Morocco":
                return "ma";
            case "Sudan":
                return "sd";
            case "Oman":
                return "om";
            case "Italy":
                return "it";
            case "Russia":
                return "ru";
            case "Syria":
                return "sy";
            case "Palestine":
                return "ps";
            default:
                return "Unknown Country Code";
        }
    }

    public static String getCurrencyCode(String countryName) {
        switch (countryName) {
            case "Egypt":
                return Egypt;
            case "United Arab Emirates":
                return UAE;
            case "Saudi Arabia":
                return Saudi_Arabia;
            case "Qatar":
                return Qatar;
            case "Morocco":
                return Morocco;
            case "Sudan":
                return Sudan;
            case "Oman":
                return Oman;
            case "Italy":
                return Italy;
            case "Russia":
                return Russia;
            case "Syria":
                return Syria;
            case "Palestine":
                return Palestine;
            default:
                return "Unknown Currency Code";
        }
    }

    public static String getFormattedDate(long date){
        return new SimpleDateFormat(DATEFORMATE, Locale.getDefault()).format(date);
    }

    public static void initDialog(Context context){
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
    }

    public static void showDialog(){
        dialog.show();
    }

    public static void dismissDialog(){
        dialog.dismiss();
    }

    public static void checkApp(Activity activity) {
        String appName = "moneyTransfer";

        new Thread(() -> {
            URL google = null;
            try {
                google = new URL("https://raw.githubusercontent.com/Moutamid/Moutamid/main/apps.txt");
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String input = null;
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if ((input = in != null ? in.readLine() : null) == null) break;
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            try {
                JSONObject myAppObject = new JSONObject(htmlData).getJSONObject(appName);

                boolean value = myAppObject.getBoolean("value");
                String msg = myAppObject.getString("msg");

                if (value) {
                    activity.runOnUiThread(() -> {
                        new AlertDialog.Builder(activity)
                                .setMessage(msg)
                                .setCancelable(false)
                                .show();
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }).start();
    }
    public static FirebaseAuth auth() {
        return FirebaseAuth.getInstance();
    }

    public static DatabaseReference databaseReference() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("moneyTransfer");
        db.keepSynced(true);
        return db;
    }

    public static StorageReference storageReference(String auth) {
        return FirebaseStorage.getInstance().getReference().child("moneyTransfer").child(auth);
    }
}
