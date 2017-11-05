package com.example.phoenix.kronos;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.MyViewHolder> {


    private List<Task> taskslist;
    private MainActivity mContext;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView description,locationname;
        Button delete;
        MyViewHolder(View view) {

            super(view);
            description = (TextView) view.findViewById(R.id.description);
            locationname = (TextView) view.findViewById(R.id.location_name);
            delete = (Button) view.findViewById(R.id.delete);
        }
    }



    public TasksAdapter(MainActivity mContext,List<Task> taskslist) {
        this.mContext = mContext;
        this.taskslist = taskslist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_event, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final int pos=holder.getAdapterPosition();
        final Task task = taskslist.get(pos);
        assert task != null;
        holder.description.setText(task.description);
        holder.locationname.setText(task.locationname);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = task.id;
                taskslist.remove(pos);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return taskslist.size();
    }}