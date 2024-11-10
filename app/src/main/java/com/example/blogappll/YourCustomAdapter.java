package com.example.blogappll;

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

import com.example.blogappll.Database.EvenementDatabase;
import com.example.blogappll.Entity.Evenement;

import java.util.List;

public class YourCustomAdapter extends RecyclerView.Adapter<YourCustomAdapter.ViewHolder> {

    private List<Evenement> EvenementList;
    private EvenementDatabase db;  // Add EvenementDatabase instance
    private Context context;  // Add a Context field

    // Single constructor
    public YourCustomAdapter(Context context, List<Evenement> EvenementList, EvenementDatabase db) {
        this.EvenementList = EvenementList;
        this.context = context;  // Initialize the Context
        this.db = db;  // Initialize the database instance
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Evenement Evenement = EvenementList.get(position);
        holder.title.setText(Evenement.title);
        holder.description.setText(Evenement.description);
        holder.lieu.setText(Evenement.lieu);

        holder.deleteButton.setOnClickListener(v -> {
            // Perform deletion on a background thread
            new Thread(() -> {
                db.EvenementDao().delete(Evenement); // Delete from the database

                // Update the list and notify the adapter on the main thread
                EvenementList.remove(position);
                ((Activity) context).runOnUiThread(() -> {  // Use context to run on the UI thread
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, EvenementList.size());
                });
            }).start();
        });
    }


    @Override
    public int getItemCount() {
        return EvenementList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, lieu;
        ImageButton deleteButton;  // Add the delete button reference
        ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.b_title);
            description = itemView.findViewById(R.id.btn_desc);
            lieu = itemView.findViewById(R.id.btn_auth);

            deleteButton = itemView.findViewById(R.id.delete_button);  // Bind the delete button
        }
    }
}
