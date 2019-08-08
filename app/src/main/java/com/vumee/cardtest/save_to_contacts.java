package com.vumee.cardtest;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class save_to_contacts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_to_contacts);

        final Button sendagain = this.findViewById(R.id.sendanotherbtn);
        sendagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Intent intent = new Intent(save_to_contacts.this.getApplicationContext(), type_number_in.class);
                save_to_contacts.this.startActivity(intent);

            }
        });

        final Button savecontactbtn = this.findViewById(R.id.savetocontactbtn);
        savecontactbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final GlobalClass globalClass = (GlobalClass) save_to_contacts.this.getApplicationContext();
                final String namecollected = globalClass.getCustname();
                final String numbercollected = globalClass.getCustmob();
                final String venuenamecollected = globalClass.getVenuename();


                final Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                contactIntent
                        .putExtra(ContactsContract.Intents.Insert.NAME, namecollected)
                        .putExtra(ContactsContract.Intents.Insert.PHONE, numbercollected)
                        .putExtra(ContactsContract.Intents.Insert.COMPANY, venuenamecollected);

                save_to_contacts.this.startActivityForResult(contactIntent, 1);


            }

        });

        final Button homebutton = this.findViewById(R.id.homebutton);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Intent intent = new Intent(save_to_contacts.this.getApplicationContext(), MainActivity.class);
                save_to_contacts.this.startActivity(intent);
            }
        });

        final Button backbutton = this.findViewById(R.id.back);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                final Intent intent = new Intent(save_to_contacts.this.getApplicationContext(), MainActivity.class);
                save_to_contacts.this.startActivity(intent);


            }
        });

        ImageView homebackround = findViewById(R.id.homebacround);
        homebackround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(save_to_contacts.this.getApplicationContext(), MainActivity.class);
                save_to_contacts.this.startActivity(intent);
            }
        });

        ImageView backbackground = findViewById(R.id.backbackground);
        backbackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    final Intent intent = new Intent(save_to_contacts.this.getApplicationContext(), MainActivity.class);
                    save_to_contacts.this.startActivity(intent);

            }
        });
    }
}
