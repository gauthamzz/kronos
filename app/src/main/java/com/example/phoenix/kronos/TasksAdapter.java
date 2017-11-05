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

import io.realm.Realm;

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
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Task task = taskslist.get(position);
        assert task != null;
        holder.description.setText(task.nickname);
        holder.locationname.setText(task.message);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Realm realm=Realm.getDefaultInstance();
                realm.beginTransaction();
                Task t=taskslist.get(position);
                t.deleteFromRealm();
                realm.commitTransaction();
                taskslist.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return taskslist.size();
    }}