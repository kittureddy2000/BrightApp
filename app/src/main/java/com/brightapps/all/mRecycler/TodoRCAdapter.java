package com.brightapps.all.mRecycler;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.brightapps.all.R;
import com.brightapps.all.mDatabase.MainDbAdapter;
import com.brightapps.all.mDatabase.TodoObject;

import java.util.ArrayList;

public class TodoRCAdapter extends RecyclerView.Adapter<TodoRCAdapter.MyViewHolder>  {

    private ArrayList<TodoObject> todos;

    Context context;
    private ClickListner clickListner;

    public TodoRCAdapter(Context context, ArrayList<TodoObject> todos) {
        this.todos = todos;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_cards_layout, parent, false);

        //view.setOnClickListener(MainActivity.myOnClickListener);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewTitle = holder.textViewTitle;
        TextView textViewDescription = holder.textViewDescription;
        TextView textViewDate = holder.textViewDate;
        //Log.d("KITDBG-ROWID", String.valueOf(todos.get(listPosition).getRow_id()));
        textViewTitle.setText(todos.get(listPosition).getTitle());
//        textViewDescription.setText(todos.get(listPosition).getDescription());
  //      textViewDate.setText(todos.get(listPosition).getReminder_Date());

    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public void setClickListener(ClickListner clickListener){
        this.clickListner = clickListener;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewDate;
        TextView textViewTime;


        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            //this.textViewDescription = (TextView) itemView.findViewById(R.id.textViewDesc);
            //this.textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
        }

        @Override
        public void onClick(View v) {
            if(clickListner != null){
                clickListner.itemClicked(v,getAdapterPosition());
            }

        }
    }



    public interface ClickListner{
        public void itemClicked(View view,int position);
    }

}
