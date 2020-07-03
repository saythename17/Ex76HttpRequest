package com.icandothisallday2020.ex76httprequest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<BoardItem> items;

    public ItemAdapter(Context context, ArrayList<BoardItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View itemView=inflater.inflate(R.layout.boardlist_item,parent,false);
        VH holder=new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh=(VH)holder;
        BoardItem item=items.get(position);
        vh.no.setText(item.no);
        vh.name.setText(item.name);
        vh.msg.setText(item.message);
        vh.date.setText(item.date);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{
        TextView no,name,msg,date;

        public VH(@NonNull View itemView) {
            super(itemView);
            no=itemView.findViewById(R.id.no);
            name=itemView.findViewById(R.id.name);
            msg=itemView.findViewById(R.id.msg);
            date=itemView.findViewById(R.id.date);
        }
    }
}
