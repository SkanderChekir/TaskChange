package com.example.user_module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.user_module.Database.RateComDatabase;
import com.example.user_module.entity.RateCom;

import java.util.ArrayList;
import java.util.List;

public class RateComAdapter extends RecyclerView.Adapter<RateComAdapter.RateComViewHolder> {

    private List<RateCom> rateComList;
    private List<RateCom> filteredRateComList; // To store the filtered results
    private RateComDatabase db;
    private Context context;
    private OnUpdateClickListener onUpdateClickListener;

    // Interface for handling update button clicks
    public interface OnUpdateClickListener {
        void onUpdateClick(RateCom rateCom);
    }

    // Constructor
    public RateComAdapter(Context context, List<RateCom> rateComList, RateComDatabase db, OnUpdateClickListener onUpdateClickListener) {
        this.context = context;
        this.rateComList = rateComList;
        this.filteredRateComList = new ArrayList<>(rateComList); // Initialize filtered list
        this.db = db;
        this.onUpdateClickListener = onUpdateClickListener;
    }

    @NonNull
    @Override
    public RateComViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row3, parent, false);
        return new RateComViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RateComViewHolder holder, int position) {
        // Get the current RateCom object
        RateCom rateCom = filteredRateComList.get(position);

        // Set the details in the UI components
        holder.title.setText(rateCom.rate);
        holder.description.setText(rateCom.commentaire);
        holder.author.setText(rateCom.author);

        // Load image with Glide
        if (rateCom.imageUri != null && !rateCom.imageUri.isEmpty()) {
            Uri imageUri = Uri.parse(rateCom.imageUri);
            Glide.with(context)
                    .load(imageUri)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.rate);
        }

        // Set a click listener on the entire item to open RateComDetails activity
        holder.itemView.setOnClickListener(v -> {
            Log.d("RateComAdapter", "Item clicked: " + rateCom.rate);
            Intent intent = new Intent(holder.itemView.getContext(), RateComDetails.class);
            intent.putExtra("rate", rateCom.rate);
            intent.putExtra("Commentaire", rateCom.commentaire);
            intent.putExtra("author", rateCom.author);
            intent.putExtra("imageUri", rateCom.imageUri);
            holder.itemView.getContext().startActivity(intent);
        });

        // Handle the delete button click
        holder.deleteButton.setOnClickListener(v -> {
            Log.d("RateComAdapter", "Delete button clicked for: " + rateCom.rate);
            deleteRateCom(rateCom, position);
        });

        // Handle the update button click
        holder.updateButton.setOnClickListener(v -> onUpdateClickListener.onUpdateClick(rateCom));
        Log.d("RateComAdapter", "Updating RateCom with ID: " + rateCom.id);
    }

    // Helper method to delete a RateCom from the database and update the RecyclerView
    private void deleteRateCom(RateCom rateCom, int position) {
        new Thread(() -> {
            // Delete the RateCom from the database
            db.rateComDao().delete(rateCom);
            rateComList.remove(position);
            filteredRateComList.remove(position); // Remove from the filtered list as well

            // Update the RecyclerView on the main thread
            ((Activity) context).runOnUiThread(() -> {
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, filteredRateComList.size());
            });
        }).start();
    }

    // Update the list of RateComs (for filtering or reloading data)
    public void updateRateComList(List<RateCom> newRateComList) {
        this.rateComList = new ArrayList<>(newRateComList);
        this.filteredRateComList = new ArrayList<>(newRateComList);
        notifyDataSetChanged();
    }

    // Filter the RateCom list based on the search query
    public void filter(String query) {
        query = query.toLowerCase().trim();
        if (query.isEmpty()) {
            filteredRateComList = new ArrayList<>(rateComList); // No filter applied
        } else {
            List<RateCom> filteredList = new ArrayList<>();
            for (RateCom rateCom : rateComList) {
                if (rateCom.rate.toLowerCase().contains(query) ||
                        rateCom.commentaire.toLowerCase().contains(query) ||
                        rateCom.author.toLowerCase().contains(query)) {
                    filteredList.add(rateCom);
                }
            }
            filteredRateComList = filteredList; // Update the filtered list
        }
        notifyDataSetChanged(); // Refresh the RecyclerView
    }

    @Override
    public int getItemCount() {
        return filteredRateComList.size();
    }

    // ViewHolder for the RateCom items
    public static class RateComViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, author;
        ImageView image;
        ImageButton deleteButton, updateButton;

        public RateComViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.b_title);
            description = itemView.findViewById(R.id.btn_desc);
            author = itemView.findViewById(R.id.btn_auth);
            image = itemView.findViewById(R.id.image_tmb);
            deleteButton = itemView.findViewById(R.id.delete_button);
            updateButton = itemView.findViewById(R.id.update_button);
        }
    }
}
