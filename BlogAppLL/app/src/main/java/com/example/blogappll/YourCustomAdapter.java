package com.example.blogappll;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogappll.Database.ReclamationDatabase;
import com.example.blogappll.Entity.Reclamation;

import java.util.List;

public class YourCustomAdapter extends RecyclerView.Adapter<YourCustomAdapter.ViewHolder> {

    private List<Reclamation> reclamationList;
    private ReclamationDatabase db;  // Database instance
    private Context context;  // Context for UI updates

    // Constructor
    public YourCustomAdapter(Context context, List<Reclamation> reclamationList, ReclamationDatabase db) {
        this.reclamationList = reclamationList;
        this.context = context;
        this.db = db;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reclamation reclamation = reclamationList.get(position);

        // Set text fields based on Reclamation attributes
        holder.description.setText(reclamation.description);
        holder.status.setText(reclamation.status);
        holder.type.setText(reclamation.type);

        // Delete button functionality
        holder.deleteButton.setOnClickListener(v -> {
            new Thread(() -> {
                // Delete Reclamation from database
                db.reclamationDao().delete(reclamation);
                reclamationList.remove(position);

                // Update RecyclerView on main thread
                ((Activity) context).runOnUiThread(() -> {
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, reclamationList.size());
                });
            }).start();
        });
    }

    @Override
    public int getItemCount() {
        return reclamationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView description, status, type;  // TextViews for Reclamation attributes
        ImageButton deleteButton;  // Delete button

        public ViewHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.b_desc);  // Set for description
            status = itemView.findViewById(R.id.b_status);  // Set for status
            type = itemView.findViewById(R.id.b_type);  // Set for type
            deleteButton = itemView.findViewById(R.id.delete_button);  // Delete button
        }
    }
}
