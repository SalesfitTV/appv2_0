package com.vumee.cardtest.Utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.vumee.cardtest.MainActivity;
import com.vumee.cardtest.R;
import com.vumee.cardtest.train;
import com.vumee.cardtest.viewtally;


public class BottomNavigationViewHelper_tally {

    private static final String TAG = "BottomNavigationViewHel";

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewExA){
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView");
        bottomNavigationViewExA.enableAnimation(false);
        bottomNavigationViewExA.enableItemShiftingMode(false);
        bottomNavigationViewExA.enableShiftingMode(false);
        bottomNavigationViewExA.setTextVisibility(false);
        bottomNavigationViewExA.setIconSizeAt(0,40,40);
        bottomNavigationViewExA.setIconSizeAt(1,40,40);
        bottomNavigationViewExA.setIconSizeAt(2,40,40);
        bottomNavigationViewExA.setIconsMarginTop(0);

    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.ic_house:
                        Intent intent1 = new Intent(context, MainActivity.class);//ACTIVITY_NUM = 0
                        context.startActivity(intent1);
                        break;

                    case R.id.ic_search:
                        Intent intent2  = new Intent(context, train.class);//ACTIVITY_NUM = 1
                        context.startActivity(intent2);
                        break;

                    case R.id.ic_circle:

                        break;



                }


                return false;
            }
        });
    }

}
