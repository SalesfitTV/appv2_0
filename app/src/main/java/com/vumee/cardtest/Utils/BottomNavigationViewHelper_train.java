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


public class BottomNavigationViewHelper_train {

    private static final String TAG = "BottomNavigationViewHel";

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewExT){
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView");
        bottomNavigationViewExT.enableAnimation(false);
        bottomNavigationViewExT.enableItemShiftingMode(false);
        bottomNavigationViewExT.enableShiftingMode(false);
        bottomNavigationViewExT.setTextVisibility(false);
        bottomNavigationViewExT.setIconSizeAt(0,40,40);
        bottomNavigationViewExT.setIconSizeAt(1,40,40);
        bottomNavigationViewExT.setIconSizeAt(2,40,40);
        bottomNavigationViewExT.setIconsMarginTop(0);

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

                        break;

                    case R.id.ic_circle:
                        Intent intent3 = new Intent(context, viewtally.class);//ACTIVITY_NUM = 2
                        context.startActivity(intent3);
                        break;



                }


                return false;
            }
        });
    }

}
