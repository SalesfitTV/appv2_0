package com.vumee.cardtest;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class type_number_in extends AppCompatActivity {

    private EditText txtMobile;
    private EditText venueName;
    private EditText customerName;

    private Button mAddToDB;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String textmessagefinal = "";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "AddToDatabase";
    //Boolean variable to mark if the transaction is safe
    private boolean isTransactionSafe;
    //Boolean variable to mark if there is any transaction pending
    private boolean isTransactionPending;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_number_in);


        final Button homebutton = this.findViewById(R.id.homebutton);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Intent intent = new Intent(type_number_in.this.getApplicationContext(), MainActivity.class);
                type_number_in.this.startActivity(intent);
            }
        });

        final Button backbutton = this.findViewById(R.id.back);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Intent intent = new Intent(type_number_in.this.getApplicationContext(), MainActivity.class);
                type_number_in.this.startActivity(intent);
            }
        });

        ImageView homebackround = findViewById(R.id.homebacround);
        homebackround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(type_number_in.this.getApplicationContext(), MainActivity.class);
                type_number_in.this.startActivity(intent);
            }
        });

        ImageView backbackground = findViewById(R.id.backbackground);
        backbackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent intent = new Intent(type_number_in.this.getApplicationContext(), MainActivity.class);
                type_number_in.this.startActivity(intent);

            }
        });

        this.txtMobile = this.findViewById(R.id.mblTxt);
        this.venueName = this.findViewById(R.id.venuefield);
        this.customerName = this.findViewById(R.id.custname);
        final GlobalClass globalClass = (GlobalClass) this.getApplicationContext();
        final String finalurl = globalClass.getLplink();
        final String repid = globalClass.getRepID();
        Button btnSms = this.findViewById(R.id.btnSend);
        /*
         * Code you want to run on the thread goes here
         */
        this.mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        this.myRef = mFirebaseDatabase.getReference();
        this.mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(type_number_in.TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d(type_number_in.TAG, "onAuthStateChanged:signed_out");

                }
                // ...
            }
        };
        // Read from the database

// save data to fire base and send sms
        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final GlobalClass globalClass = (GlobalClass) type_number_in.this.getApplicationContext();

                final EditText txtMessage1 = type_number_in.this.findViewById(R.id.msgTxt);

                final String mobnum = type_number_in.this.txtMobile.getText().toString();

                final String copyfinal = txtMessage1.getText().toString();

                String reptag = globalClass.getRepID();

                final String finalurlfordb = globalClass.getUrlcolleccted();

                textmessagefinal = (copyfinal+"\n"+finalurl+"?utm_source="+mobnum+reptag);

                Log.d(type_number_in.TAG, "onClick: Attempting to add object to database.");

                final Date currentTime = Calendar.getInstance().getTime();
                final String datetime = currentTime.toString();
                final String VENUENAME = type_number_in.this.venueName.getText().toString();
                final String mobile = type_number_in.this.txtMobile.getText().toString();
                final String customername = type_number_in.this.customerName.getText().toString();
                final FirebaseUser userin = type_number_in.this.mAuth.getCurrentUser();

                final String urlcollected = globalClass.getLplink();
                final String mob = urlcollected+";sms";
                final String namemob;

                namemob = ";"+VENUENAME+";"+mobile+";"+customername+";"+mob;






                Map<String, Object> data = new HashMap<>();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

                String currentDateandTime = sdf.format(new Date());



                data.put(currentDateandTime, namemob);



                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

                String currentDate = sdf2.format(new Date());

                DocumentReference sheetsupload = db.collection("allusers").document("useractivity").collection(globalClass.getUser_id()).document(currentDate);
                sheetsupload.set(data, SetOptions.merge());

                if(!mobile.equals("") && !VENUENAME.equals("") && !customername.equals("")){
                    try{
                        final String SENT = "SMS_SENT";
                        final PendingIntent sentPI = PendingIntent.getBroadcast(type_number_in.this, 0, new Intent(SENT), 0);
                        // ---when the SMS has been sent---
                        registerReceiver(
                                new BroadcastReceiver()
                                {
                                    @Override
                                    public void onReceive(final Context arg0, final Intent arg1)
                                    {
                                        switch(this.getResultCode())
                                        {
                                            case Activity.RESULT_OK:
                                                Toast.makeText(type_number_in.this.getBaseContext(), "SMS sent successfully", Toast.LENGTH_LONG).show();

                                                openDialog();
                                                break;
                                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                                Toast.makeText(type_number_in.this.getBaseContext(), "SMS sent failed", Toast.LENGTH_LONG).show();
                                                break;
                                            case SmsManager.RESULT_ERROR_NO_SERVICE:
                                                Toast.makeText(type_number_in.this.getBaseContext(), "SMS sent failed: no service", Toast.LENGTH_LONG).show();
                                                break;
                                            case SmsManager.RESULT_ERROR_NULL_PDU:
                                                Toast.makeText(type_number_in.this.getBaseContext(), "SMS sent failed", Toast.LENGTH_LONG).show();
                                                break;
                                            case SmsManager.RESULT_ERROR_RADIO_OFF:
                                                Toast.makeText(type_number_in.this.getBaseContext(), "SMS sent failed", Toast.LENGTH_LONG).show();
                                                break;
                                        }
                                    }
                                }, new IntentFilter(SENT));
                        final SmsManager smgr = SmsManager.getDefault();
                        ArrayList<String> parts = smgr.divideMessage(textmessagefinal);
                        ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>(Collections.singleton(sentPI));
                        ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();
                        smgr.sendMultipartTextMessage(type_number_in.this.txtMobile.getText().toString(),null, parts,sentIntents,deliveryIntents);
                        globalClass.setCustname(customername);
                        globalClass.setCustmob(mobile);
                        globalClass.setVenuename(VENUENAME);
                    }
                    catch (final Exception e){
                        Toast.makeText(type_number_in.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(type_number_in.this, "Please ensure all fields are filled above", Toast.LENGTH_SHORT).show();
                }
            }


        });

        Button sharebutton = findViewById(R.id.sendurl);
        sharebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String venue = venueName.getText().toString();
                String name = customerName.getText().toString();
                String mobile = txtMobile.getText().toString();
                String mobile1 = mobile.replaceAll(" ","");
                EditText textcopy = type_number_in.this.findViewById(R.id.msgTxt);
                String copyshare = textcopy.getText().toString();

                GlobalClass globalClass = (GlobalClass) type_number_in.this.getApplicationContext();
                String reptag = globalClass.getRepID();
                String finalurlfordb = globalClass.getLplink();

                String uid = globalClass.getUser_id();


                Map<String, Object> data = new HashMap<>();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

                String currentDateandTime = sdf.format(new Date());



                String namemob;

                namemob = ";"+venue+";"+mobile+";"+name+";"+finalurlfordb+";share";

                data.put(currentDateandTime, namemob);




                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

                String currentDate = sdf2.format(new Date());

                DocumentReference sheetsupload = db.collection("allusers").document("useractivity").collection(globalClass.getUser_id()).document(currentDate);
                sheetsupload.set(data, SetOptions.merge());

                if (!venue.equals("") && !name.equals("") && !mobile.equals("")) {
                    Intent myIntent = new Intent(Intent.ACTION_SEND);
                    myIntent.setType("text/plain");
                    String shareBody = copyshare+"\n"+finalurlfordb+"?utm_source="+mobile1+reptag;
                    myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(myIntent, "Share Using"));

                }else {
                    Toast.makeText(type_number_in.this, "Please ensure all fields are filled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        openDialog();
    }

    private void openDialog() {
        if (isTransactionSafe) {
            save_to_contcts_dialog save = new save_to_contcts_dialog();
            Bundle bundle = new Bundle();
            bundle.putString("name", String.valueOf(customerName.getText()));
            bundle.putString("number", String.valueOf(txtMobile.getText()));
            bundle.putString("venue", String.valueOf(venueName.getText()));
            save.setArguments(bundle);
            save.show(getSupportFragmentManager(), "save to contacts dialog");

        }else {
/*
If any transaction is not done because the activity is in background. We set the isTransactionPending variable to true so that we can pick this up when we come back to foreground
*/
            isTransactionPending = true;
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        this.mAuth.addAuthStateListener(this.mAuthListener);

    }
    @Override
    public void onStop() {
        super.onStop();

        if (this.mAuthListener != null) {
            this.mAuth.removeAuthStateListener(this.mAuthListener);
        }
    }

    /*
onPostResume is called only when the activity's state is completely restored. In this we will
set our boolean variable to true. Indicating that transaction is safe now
*/
    public void onPostResume() {
        super.onPostResume();
        isTransactionSafe = true;
        /* Here after the activity is restored we check if there is any transaction pending from the last restoration
         */
        if (isTransactionPending) {
            openDialog();
        }
    }
    /*
    onPause is called just before the activity moves to background and also before onSaveInstanceState. In this
    we will mark the transaction as unsafe
    */
    public void onPause() {
        super.onPause();
        isTransactionSafe = false;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Write your code here
        final Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        this.startActivity(intent);
    }
}
