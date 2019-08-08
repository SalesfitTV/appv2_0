package com.vumee.cardtest;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

public class pick_from_contcts_dialog extends AppCompatDialogFragment {
    private TextView title;
    private Button newnumber_btn,contactsbtn;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_sent_to_another, null);

        builder.setView(view);

        newnumber_btn = view.findViewById(R.id.pick_cont_button);
        newnumber_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), pick_from_contacts.class);
                startActivity(intent);


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
