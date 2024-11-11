package com.example.blogappll;

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
import com.example.blogappll.Publish;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.blogappll.Database.EvenementDatabase;
import com.example.blogappll.Entity.Evenement;

import java.util.List;

public class EvenementAdapter extends RecyclerView.Adapter<EvenementAdapter.EvenementViewHolder> {

    private List<Evenement> EvenementList;
    private EvenementDatabase db;
    private Context context;
    private OnUpdateClickListener onUpdateClickListener;

    // Interface pour gérer le clic sur le bouton "Update"
    public interface OnUpdateClickListener {
        void onUpdateClick(Evenement Evenement);
    }

    // Constructeur
    public EvenementAdapter(Context context, List<Evenement> EvenementList, EvenementDatabase db, OnUpdateClickListener onUpdateClickListener) {
        this.EvenementList = EvenementList;
        this.db = db;
        this.context = context;
        this.onUpdateClickListener = onUpdateClickListener;
    }

    @NonNull
    @Override
    public EvenementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row1, parent, false);
        return new EvenementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EvenementViewHolder holder, int position) {
        Evenement Evenement = EvenementList.get(position);

        holder.title.setText(Evenement.title);
        holder.description.setText(Evenement.description);
        holder.lieu.setText(Evenement.lieu);

        if (Evenement.imageUri != null && !Evenement.imageUri.isEmpty()) {
            Uri imageUri = Uri.parse(Evenement.imageUri);
            Glide.with(context)
                    .load(imageUri)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.itemView.setOnClickListener(v -> {
            Log.d("EvenementAdapter", "Item clicked: " + Evenement.title);
            Intent intent = new Intent(holder.itemView.getContext(), EvenementDetails.class);
            intent.putExtra("title", Evenement.title);
            intent.putExtra("description", Evenement.description);
            intent.putExtra("lieu", Evenement.lieu);
            intent.putExtra("imageUri", Evenement.imageUri);
            holder.itemView.getContext().startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v -> {
            Log.d("EvenementAdapter", "Delete button clicked for: " + Evenement.title);
            deleteEvenement(Evenement, position);
        });

        // Update button now launches PublishActivity for updating
        holder.updateButton.setOnClickListener(v -> {
            Intent updateIntent = new Intent(holder.itemView.getContext(), Publish.class);
            updateIntent.putExtra("Evenement_id", Evenement.id); // Pass the Evenement ID
            holder.itemView.getContext().startActivity(updateIntent);
        });
    }

    // Helper method to delete a Evenement from the database and update the RecyclerView
    private void deleteEvenement(Evenement Evenement, int position) {
        new Thread(() -> {
            // Delete Evenement from database
            db.EvenementDao().delete(Evenement);
            EvenementList.remove(position);

            // Update the RecyclerView on the main thread
            ((Activity) context).runOnUiThread(() -> {
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, EvenementList.size());
            });
        }).start();
    }


    public void updateEvenementList(List<Evenement> newEvenementList) {
        EvenementList.clear();
        EvenementList.addAll(newEvenementList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return EvenementList.size();
    }

    // ViewHolder pour les éléments de la liste
    public static class EvenementViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, lieu;
        ImageView image;
        ImageButton deleteButton, updateButton;

        public EvenementViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.b_title);
            description = itemView.findViewById(R.id.btn_desc);
            lieu = itemView.findViewById(R.id.btn_auth);
            image = itemView.findViewById(R.id.image_tmb);
            deleteButton = itemView.findViewById(R.id.delete_button);
            updateButton = itemView.findViewById(R.id.update_button);
        }
    }
}
