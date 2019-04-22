package com.mrbell.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Customadapter extends RecyclerView.Adapter<Customadapter.Selfviewholder> {


    private Context context;
    String[]name;
    ArrayList<File> mysong;

    public Customadapter(Context context, String[]name,ArrayList<File>mysong) {
        this.context = context;
        this.name=name;
        this.mysong=mysong;
    }

    @NonNull
    @Override
    public Selfviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sampleview,parent,false);
        Selfviewholder slf = new Selfviewholder(v);
        return slf;
    }

    @Override
    public void onBindViewHolder(@NonNull Selfviewholder holder, final int position) {


        holder.tv_list.setText(name[position]);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Player.class);
                intent.putExtra("pos",position);
                intent.putExtra("data",mysong);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return name.length;
    }

    public class Selfviewholder extends RecyclerView.ViewHolder{

        private TextView tv_list;
        private LinearLayout linearLayout;
        public Selfviewholder(View itemView) {
            super(itemView);
            tv_list=itemView.findViewById(R.id.tv_name);
            linearLayout=itemView.findViewById(R.id.linearlayout);
        }
    }


}
