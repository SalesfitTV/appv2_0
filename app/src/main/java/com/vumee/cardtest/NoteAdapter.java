package com.vumee.cardtest;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteHolder> {

private OnItemClickListner listner;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options) {
        super(options);

    }




    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull Note model) {

        holder.textViewTitle.setText(model.getTitle());
        holder.textViewDescription.setText(model.getDescription());

        Picasso.get()
                .load(model.getImage())
                .error(R.drawable.ic_close)
                .into(holder.textViewThumbnail);



    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,
                parent, false);
        return new NoteHolder(v);
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewPriority;
        ImageView textViewThumbnail;
        TextView textViewlplink;
        Button buttonsharebutton;

        public NoteHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);
            textViewThumbnail = itemView.findViewById(R.id.text_view_thumbnail);
            textViewlplink = itemView.findViewById(R.id.text_view_lplink);
            buttonsharebutton = itemView.findViewById(R.id.sharebtn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listner != null){
                        listner.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });

            buttonsharebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listner != null){
                        listner.onShareClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });

        }
    }

    public  interface OnItemClickListner {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
        void onShareClick(DocumentSnapshot documentSnapshot, int position);
    }
    public  void setOnItemClickListener(OnItemClickListner listener) {
        this.listner = listener;
    }
}
