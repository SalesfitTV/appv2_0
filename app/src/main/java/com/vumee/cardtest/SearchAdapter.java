package com.vumee.cardtest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    View view;

    public static String searchurl="";

    public static String getsearchurl() {

        return String.valueOf(searchurl);
    }
    public static void setsearchurl(String searchurl) {
        SearchAdapter.searchurl = searchurl;
    }
    Context context;
    ArrayList<String> fullNameList;
    ArrayList<String> userNameList;
    ArrayList<String> lpLinkList;
    ArrayList<String> profilePicList;

    class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView full_name, user_name, lp_link;

        public SearchViewHolder(View itemView) {
            super(itemView);

            full_name = (TextView) itemView.findViewById(R.id.full_name);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            lp_link = (TextView) itemView.findViewById(R.id.lp_link);
        }
    }

    public SearchAdapter(Context context, ArrayList<String> lpLinkList, ArrayList<String> fullNameList, ArrayList<String> userNameList, ArrayList<String> profilePicList) {
        this.context = context;
        this.fullNameList = fullNameList;
        this.userNameList = userNameList;
        this.lpLinkList = lpLinkList;
        this.profilePicList = profilePicList;
    }

    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_list_items, parent, false);
        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchViewHolder holder, final int position) {
        holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        holder.full_name.setText(fullNameList.get(position));

        holder.user_name.setText(userNameList.get(position));
        SearchAdapter.setsearchurl(userNameList.get(position));


        holder.full_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, searchwebview.class);

                context.startActivity(intent);
            }
        });
        holder.user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, searchwebview.class);
                context.startActivity(intent);
            }
        });
        holder.lp_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, searchwebview.class);
                context.startActivity(intent);
            }
        });







    }

    @Override
    public int getItemCount() {
        return fullNameList.size();
    }
}
