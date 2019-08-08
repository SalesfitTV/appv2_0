package com.vumee.cardtest;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class save_to_contcts_dialog extends AppCompatDialogFragment {
    private TextView title;
    private Button newnumber_btn,contactsbtn;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_save_to_contct_dialog, null);

        builder.setView(view);

        newnumber_btn = view.findViewById(R.id.pick_cont_button);
        newnumber_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = getArguments();
                String CustomerName = bundle.getString("name","");
                String MobileNumber = bundle.getString("number","");
                String VenueName = bundle.getString("venue","");

                final Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                contactIntent
                        .putExtra(ContactsContract.Intents.Insert.NAME, CustomerName)
                        .putExtra(ContactsContract.Intents.Insert.PHONE, MobileNumber)
                        .putExtra(ContactsContract.Intents.Insert.COMPANY, VenueName);

                save_to_contcts_dialog.this.startActivityForResult(contactIntent, 1);

            }
        });

        contactsbtn = view.findViewById(R.id.new_num_btn);
        contactsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        title = view.findViewById(R.id.textView3);
        return builder.create();

    }


}
