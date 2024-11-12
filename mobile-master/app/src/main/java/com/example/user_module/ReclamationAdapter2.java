package com.example.user_module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user_module.Database.ReclamationDatabase;
import com.example.user_module.entity.Reclamation;

import java.util.List;

public class ReclamationAdapter2 extends RecyclerView.Adapter<ReclamationAdapter2.ReclamationViewHolder> {

    private List<Reclamation> reclamationList;
    private ReclamationDatabase db;
    private Context context;
    private OnUpdateClickListener onUpdateClickListener;

    // Interface pour gérer le clic sur le bouton "Update"
    public interface OnUpdateClickListener {
        void onUpdateClick(Reclamation reclamation);
    }

    // Constructeur
    public ReclamationAdapter2(Context context, List<Reclamation> reclamationList, ReclamationDatabase db, OnUpdateClickListener onUpdateClickListener) {
        this.reclamationList = reclamationList;
        this.db = db;
        this.context = context;
        this.onUpdateClickListener = onUpdateClickListener;
    }

    @NonNull
    @Override
    public ReclamationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row5, parent, false);
        return new ReclamationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReclamationViewHolder holder, int position) {
        Reclamation reclamation = reclamationList.get(position);

        holder.description.setText(reclamation.description);
        holder.status.setText(reclamation.status);
        holder.type.setText(reclamation.type);

        holder.itemView.setOnClickListener(v -> {
            Log.d("ReclamationAdapter", "Item clicked: " + reclamation.description);
            Intent intent = new Intent(holder.itemView.getContext(), ReclamationDetails2.class);
            intent.putExtra("description", reclamation.description);
            intent.putExtra("status", reclamation.status);
            intent.putExtra("type", reclamation.type);
            holder.itemView.getContext().startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v -> {
            Log.d("ReclamationAdapter", "Delete button clicked for: " + reclamation.description);
            deleteReclamation(reclamation, position);
        });

        // Update button launches PublishActivity for updating
        holder.updateButton.setOnClickListener(v -> {
            Intent updateIntent = new Intent(holder.itemView.getContext(), Publish2.class);
            updateIntent.putExtra("reclamation_id", reclamation.id); // Pass the Reclamation ID
            holder.itemView.getContext().startActivity(updateIntent);
        });
    }

    // Helper method to delete a Reclamation from the database and update the RecyclerView
    private void deleteReclamation(Reclamation reclamation, int position) {
        new Thread(() -> {
            // Delete Reclamation from database
            db.reclamationDao().delete(reclamation);
            reclamationList.remove(position);

            // Update the RecyclerView on the main thread
            ((Activity) context).runOnUiThread(() -> {
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, reclamationList.size());
            });
        }).start();
    }

    public void updateReclamationList(List<Reclamation> newReclamationList) {
        reclamationList.clear();
        reclamationList.addAll(newReclamationList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return reclamationList.size();
    }

    // ViewHolder pour les éléments de la liste
    public static class ReclamationViewHolder extends RecyclerView.ViewHolder {
        TextView description, status, type;
        ImageButton deleteButton, updateButton;

        public ReclamationViewHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.b_desc);
            status = itemView.findViewById(R.id.b_status);
            type = itemView.findViewById(R.id.b_type);

            deleteButton = itemView.findViewById(R.id.delete_button);
            updateButton = itemView.findViewById(R.id.update_button);
        }
    }
}
