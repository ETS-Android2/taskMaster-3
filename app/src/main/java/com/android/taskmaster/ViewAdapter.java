package com.android.taskmaster;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.TaskItem;

import java.util.List;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder>{
//    private final List<TaskItem> taskItems;
    private OnTaskItemClickListener listener;
    private final List<com.amplifyframework.datastore.generated.model.TaskItem> taskLists ;
//    public ViewAdapter(List<TaskItem> taskItems,OnTaskItemClickListener listener) {
//        this.taskItems = taskItems;
//        this.listener = listener;
//    }
    public ViewAdapter(List<com.amplifyframework.datastore.generated.model.TaskItem> taskLists, OnTaskItemClickListener listener) {
        this.taskLists = taskLists;
        this.listener = listener;
    }

    public interface OnTaskItemClickListener {
        void onTaskClicked(int position);
    }

    @NonNull
    @Override
    public ViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAdapter.ViewHolder holder, int position) {
//        TaskItem item = taskItems.get(position);
        TaskItem item = taskLists.get(position);
        holder.title.setText(item.getTitle());

    }

    @Override
    public int getItemCount() {
//        return taskItems.size();
        return taskLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView body;
        ListView[] state;


        public ViewHolder(@NonNull View itemView,OnTaskItemClickListener listener) {
            super(itemView);

        title=itemView.findViewById(R.id.task_title_name);
        body=itemView.findViewById(R.id.body);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTaskClicked(getAdapterPosition());
                }
            });
        }




    }
}
