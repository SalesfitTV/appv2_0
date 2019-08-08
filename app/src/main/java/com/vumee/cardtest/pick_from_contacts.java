package com.vumee.cardtest;
import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class pick_from_contacts extends AppCompatActivity {

    private final int REQUEST_CODE = 99;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private String web;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private static final String TAG = "AddToDatabase";
    private TextView venueName,custName,custMobile;
    private EditText textcopy;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Boolean variable to mark if the transaction is safe
    private boolean isTransactionSafe;
    //Boolean variable to mark if there is any transaction pending
    private boolean isTransactionPending;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_from_contacts);
        this.setContentView(R.layout.activity_pick_from_contacts);

        this.venueName = this.findViewById(R.id.venuefield);
        this.custName = this.findViewById(R.id.namefield);
        this.custMobile = this.findViewById(R.id.mobilefield);
        this.textcopy = this.findViewById(R.id.copy);
        String venue;

        Button sharebutton = findViewById(R.id.other_share);
        sharebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String venue = venueName.getText().toString();
                String name = custName.getText().toString();
                String mobile = custMobile.getText().toString();
                String mobile1 = mobile.replaceAll(" ","");
                String copyshare = textcopy.getText().toString();

                GlobalClass globalClass = (GlobalClass) pick_from_contacts.this.getApplicationContext();
                String reptag = globalClass.getRepID();
                String finalurlfordb = globalClass.getLplink();


                if (!venue.equals("") && !name.equals("") && !mobile.equals("")) {
                    Intent myIntent = new Intent(Intent.ACTION_SEND);
                    myIntent.setType("text/plain");
                    String shareBody = copyshare+"\n"+finalurlfordb+"?utm_source="+mobile1+reptag;
                    myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(myIntent, "Share Using"));
                    savetodb2();
                }else {
                    Toast.makeText(pick_from_contacts.this, "Please ensure all fields are filled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Button homebutton = this.findViewById(R.id.homebutton);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Intent intent = new Intent(pick_from_contacts.this.getApplicationContext(), MainActivity.class);
                pick_from_contacts.this.startActivity(intent);
            }
        });
        final Button backbutton = this.findViewById(R.id.back);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Intent intent = new Intent(pick_from_contacts.this.getApplicationContext(), MainActivity.class);
                pick_from_contacts.this.startActivity(intent);
            }
        });
        ImageView homebackround = findViewById(R.id.homebacround);
        homebackround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(pick_from_contacts.this.getApplicationContext(), MainActivity.class);
                pick_from_contacts.this.startActivity(intent);
            }
        });

        ImageView backbackground = findViewById(R.id.backbackground);
        backbackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent intent = new Intent(pick_from_contacts.this.getApplicationContext(), MainActivity.class);
                pick_from_contacts.this.startActivity(intent);

            }
        });

        final TextView name = this.findViewById(R.id.namefield);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // user BoD suggests using Intent.ACTION_PICK instead of .ACTION_GET_CONTENT to avoid the chooser
                final Intent intent = new Intent(Intent.ACTION_PICK);
                // BoD con't: CONTENT_TYPE instead of CONTENT_ITEM_TYPE
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                pick_from_contacts.this.startActivityForResult(intent, 1);
            }
        });

        final TextView mobile = this.findViewById(R.id.mobilefield);
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // user BoD suggests using Intent.ACTION_PICK instead of .ACTION_GET_CONTENT to avoid the chooser
                final Intent intent = new Intent(Intent.ACTION_PICK);
                // BoD con't: CONTENT_TYPE instead of CONTENT_ITEM_TYPE
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                pick_from_contacts.this.startActivityForResult(intent, 1);
            }
        });
        //firebsecode
        /*
         * Code you want to run on the thread goes here
         */
        this.mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        this.myRef = mFirebaseDatabase.getReference();
        // User is signed in
        // User is signed out
        // ...
        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(pick_from_contacts.TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(pick_from_contacts.TAG, "onAuthStateChanged:signed_out");

                }// ...
            }
        };
        // Read from the database
        this.myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
            }
            @Override
            public void onCancelled(final DatabaseError error) {
                // Failed to read value
                Log.w(pick_from_contacts.TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void savetodb2() {
        final String namemob;
        GlobalClass globalClass = (GlobalClass) pick_from_contacts.this.getApplicationContext();
        String finalurlfordb = globalClass.getLplink();
        String venue = venueName.getText().toString();
        String name = custName.getText().toString();
        String mobile = custMobile.getText().toString();

        namemob = ";"+venue+";"+mobile+";"+name+";"+finalurlfordb+";share";



        Map<String, Object> data = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

        String currentDateandTime = sdf.format(new Date());


        data.put(currentDateandTime, namemob);


        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

        String currentDate = sdf2.format(new Date());

        DocumentReference sheetsupload = db.collection("allusers").document("useractivity").collection(globalClass.getUser_id()).document(currentDate);
        sheetsupload.set(data, SetOptions.merge());
    }

    @Override
    public void onActivityResult(final int reqCode, final int resultCode, final Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (data != null) {
            final Uri uri = data.getData();

            if (uri != null) {
                Cursor c = null;
                try {
                    c = this.getContentResolver().query(uri, new String[]{
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                                    ContactsContract.CommonDataKinds.Phone.TYPE,
                                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                            null, null, null);
                    final ContentResolver mContentResolver = getContentResolver();

                    if (c != null && c.moveToFirst()) {
                        final String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        final String name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        final String contactId = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                        final String rawContactId = this.getRawContactId(contactId);
                        final String companyName = this.getCompanyName(rawContactId);
                        final int type = c.getInt(1);

                        this.custMobile.setText(number);
                        this.custName.setText(name);
                        this.venueName.setText(companyName);
                    }
                } finally {
                    if (c != null) {
                        c.close();
                    }
                }
            }
        }

        final TextView phoneNumber = this.findViewById(R.id.mobilefield);
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkPermission()) {
                Log.e("permission", "Permission already granted.");
            } else {
                this.requestPermission();
            }
        }
        final EditText smsText = this.findViewById(R.id.copy);
        Button sendSMS = this.findViewById(R.id.sendSMS);
        sendSMS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                final String mobileforcopy = phoneNumber.getText().toString();
                final String mob = mobileforcopy.replaceAll(" ", "");
                final GlobalClass globalClass = (GlobalClass) pick_from_contacts.this.getApplicationContext();
                String reptag2 = globalClass.getRepID();
                final String finalurlfordb = globalClass.getLplink();
                final String urlcollected = finalurlfordb;
                final String copy =smsText.getText().toString();
                final String sms = copy+"\n"+urlcollected+"?utm_source="+mob+reptag2;
                final String phoneNum = phoneNumber.getText().toString();
                final String venue = venueName.getText().toString();

                if (!TextUtils.isEmpty(sms) && !TextUtils.isEmpty(phoneNum) && !TextUtils.isEmpty(venue)) {
                    if (pick_from_contacts.this.checkPermission()) {
                        final String SENT = "SMS_SENT";
                        final PendingIntent sentPI = PendingIntent.getBroadcast(pick_from_contacts.this, 0, new Intent(SENT), 0);
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
                                                Toast.makeText(pick_from_contacts.this.getBaseContext(), "SMS sent successfully", Toast.LENGTH_LONG).show();
                                                OpenDialog();

                                                break;
                                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                                Toast.makeText(pick_from_contacts.this.getBaseContext(), "SMS sent failed", Toast.LENGTH_LONG).show();
                                                break;
                                            case SmsManager.RESULT_ERROR_NO_SERVICE:
                                                Toast.makeText(pick_from_contacts.this.getBaseContext(), "SMS sent failed: no service", Toast.LENGTH_LONG).show();
                                                break;
                                            case SmsManager.RESULT_ERROR_NULL_PDU:
                                                Toast.makeText(pick_from_contacts.this.getBaseContext(), "SMS sent failed", Toast.LENGTH_LONG).show();
                                                break;
                                            case SmsManager.RESULT_ERROR_RADIO_OFF:
                                                Toast.makeText(pick_from_contacts.this.getBaseContext(), "SMS sent failed", Toast.LENGTH_LONG).show();
                                                break;
                                        }
                                    }
                                }, new IntentFilter(SENT));
                        final SmsManager smsManager = SmsManager.getDefault();
                        ArrayList<String> parts = smsManager.divideMessage(sms);
                        ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>(Collections.singleton(sentPI));
                        ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();
                        smsManager.sendMultipartTextMessage(phoneNum,null, parts,sentIntents,deliveryIntents);
                        pick_from_contacts.this.savetodb();
                    } else {
                        Toast.makeText(pick_from_contacts.this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(pick_from_contacts.this.getBaseContext(), "Please ensure all fields are filled", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void OpenDialog() {

        if(isTransactionSafe) {
            pick_from_contcts_dialog pick = new pick_from_contcts_dialog();
            pick.show(getSupportFragmentManager(), "pick from contacts dialog");
        }
        else {
/*
If any transaction is not done because the activity is in background. We set the isTransactionPending variable to true so that we can pick this up when we come back to foreground
*/
            isTransactionPending = true;
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
            OpenDialog();
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

    private void savetodb() {
        final String namemob;
        GlobalClass globalClass = (GlobalClass) pick_from_contacts.this.getApplicationContext();
        String UID = globalClass.getUser_id();
        String finalurlfordb = globalClass.getLplink();
        String venue = venueName.getText().toString();
        String name = custName.getText().toString();
        String mobile = custMobile.getText().toString();

        namemob = ";"+venue+";"+mobile+";"+name+";"+finalurlfordb+";sms";






        Map<String, Object> data = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

        String currentDateandTime = sdf.format(new Date());



        data.put(currentDateandTime, namemob);


        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

        String currentDate = sdf2.format(new Date());

        DocumentReference sheetsupload = db.collection("allusers").document("useractivity").collection(globalClass.getUser_id()).document(currentDate);
        sheetsupload.set(data, SetOptions.merge());
    }

    private String getCompanyName(final String rawContactId) {
        try {
            final String orgWhere = ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
            final String[] orgWhereParams = {rawContactId,
                    ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
            final Cursor cursor = this.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                    null, orgWhere, orgWhereParams, null);

            if (cursor == null) return null;
            String Vname = null;
            if (cursor.moveToFirst()) {
                Vname = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY));

            }
            cursor.close();
            return Vname;
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getRawContactId(final String contactId) {
        final String[] projection = {BaseColumns._ID};
        final String selection = ContactsContract.RawContacts.CONTACT_ID + "=?";
        final String[] selectionArgs = {contactId};
        final Cursor c = this.getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, projection, selection, selectionArgs, null);
        if (c == null) return null;
        int rawContactId = -1;
        if (c.moveToFirst()) {
            rawContactId = c.getInt(c.getColumnIndex(BaseColumns._ID));
        }
        c.close();
        return String.valueOf(rawContactId);
    }


    private boolean checkPermission() {
        final int result = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, pick_from_contacts.PERMISSION_REQUEST_CODE);

    }



    @Override
    public void onRequestPermissionsResult(final int requestCode, final String[] permissions, final int[] grantResults) {
        switch (requestCode) {
            case pick_from_contacts.PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this,
                            "Permission accepted", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this,
                            "Permission denied", Toast.LENGTH_LONG).show();
                    final Button sendSMS = this.findViewById(R.id.sendSMS);
                    sendSMS.setEnabled(false);

                }
                break;
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Write your code here
        final Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        this.startActivity(intent);

    }
}
