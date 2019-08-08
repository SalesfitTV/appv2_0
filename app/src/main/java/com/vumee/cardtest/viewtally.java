package com.vumee.cardtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import static java.lang.Math.toIntExact;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.value.StringValue;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.vumee.cardtest.Utils.BottomNavigationViewHelper;
import com.vumee.cardtest.Utils.BottomNavigationViewHelper_tally;

public class viewtally extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "viewtallyactivity";
    private  static  final int ACTIVITY_NUM = 2;
    private Context mContext = viewtally.this;

    TextView monthlyviews, monthlysends,monthlyrates,totalviews,totalsends,totalrates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewtally);
        setupBottomNavigation();


        Button  logout_btn = findViewById(R.id.logout_button);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                final Intent intent = new Intent(viewtally.this.getApplicationContext(),login.class);
                viewtally.this.startActivity(intent);
            }
        });

        monthlyviews = findViewById(R.id.title_1_value);
        monthlysends = findViewById(R.id.title_2_value);
        monthlyrates = findViewById(R.id.monthly_opens_value);

        totalviews = findViewById(R.id.total_viewed_value);
        totalsends = findViewById(R.id.total_sent_value);
        totalrates = findViewById(R.id.total_opens_value);
        getviews();
    }



    private void getviews() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        String userID = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        GlobalClass globalClass = (GlobalClass) viewtally.this.getApplicationContext();

        DocumentReference CompanyDocumentRef = db.collection(globalClass.getCompanyname()).document("users").collection(globalClass.getUser_id()).document("anlytics");
        CompanyDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        int monthly_open_rate = toIntExact(document.getLong("Monthly_open_rate"));
                        int total_open_rate = toIntExact(document.getLong("total_open_rate"));
                        int monthly_watched = toIntExact(document.getLong("Monthly_watched"));
                        int monthly_send = toIntExact(document.getLong("Monthly_send"));
                        int total_send = toIntExact(document.getLong("total_send"));
                        int total_open = toIntExact(document.getLong("total_watched"));

                        monthlyrates.setText(monthly_open_rate+"%");
                        totalrates.setText(total_open_rate+"%");
                        totalsends.setText(total_send+"");
                        totalviews.setText(total_open+"");
                        monthlysends.setText(monthly_send+"");
                        monthlyviews.setText(monthly_watched+"");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void setupBottomNavigation() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewExA = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper_tally.setupBottomNavigationView(bottomNavigationViewExA);
        BottomNavigationViewHelper_tally.enableNavigation(mContext, bottomNavigationViewExA);
        Menu menu = bottomNavigationViewExA.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
