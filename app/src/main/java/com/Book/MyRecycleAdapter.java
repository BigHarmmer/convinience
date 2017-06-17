package com.Book;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.z.myproject.R;

import java.util.List;

/**
 * Created by z on 2017/5/6.
 */

public class MyRecycleAdapter  extends RecyclerView.Adapter<MyRecycleAdapter.ViewHolder>{

    private List<String>mdata;

    public MyRecycleAdapter(List<String>data){
        mdata=data;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        View myView;
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);

            myView=itemView;
            textView= (TextView) itemView.findViewById(R.id.recycle_tv);

        }
    }

    @Override
    public MyRecycleAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item,parent,false);
     final   ViewHolder holder=new ViewHolder(view);
        holder.myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                String str=mdata.get(position);
                Intent intent=new Intent(parent.getContext(),Book_Detail.class);
                Bundle bundle=new Bundle();
                bundle.putString("theme", str);
                intent.putExtras(bundle);
                parent.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MyRecycleAdapter.ViewHolder holder, int position) {

        String str=mdata.get(position);
        holder.textView.setText(str);
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }
}
