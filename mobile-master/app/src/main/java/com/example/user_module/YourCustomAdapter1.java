package com.example.user_module;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user_module.Database.RateComDatabase;
import com.example.user_module.entity.RateCom;

import java.util.List;

public class YourCustomAdapter1 extends RecyclerView.Adapter<YourCustomAdapter1.ViewHolder> {

    private List<RateCom> rateComList;
    private RateComDatabase db;  // Add BlogDatabase instance
    private Context context;  // Add a Context field

    // Single constructor
    public YourCustomAdapter1(Context context, List<RateCom> rateComList, RateComDatabase db) {
        this.rateComList = rateComList;
        this.context = context;  // Initialize the Context
        this.db = db;  // Initialize the database instance
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row3, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RateCom rateCom = rateComList.get(position);
        holder.title.setText(rateCom.rate);
        holder.description.setText(rateCom.commentaire);
        holder.author.setText(rateCom.author);

        holder.deleteButton.setOnClickListener(v -> {
            // Perform deletion on a background thread
            new Thread(() -> {
                db.rateComDao().delete(rateCom); // Delete from the database

                // Update the list and notify the adapter on the main thread
                rateComList.remove(position);
                ((Activity) context).runOnUiThread(() -> {  // Use context to run on the UI thread
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, rateComList.size());
                });
            }).start();
        });
    }


    @Override
    public int getItemCount() {
        return rateComList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, author;
        ImageButton deleteButton;  // Add the delete button reference
        ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.b_title);
            description = itemView.findViewById(R.id.btn_desc);
            author = itemView.findViewById(R.id.btn_auth);

            deleteButton = itemView.findViewById(R.id.delete_button);  // Bind the delete button
        }
    }
}
