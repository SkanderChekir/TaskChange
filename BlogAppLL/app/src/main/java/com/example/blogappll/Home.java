package com.example.blogappll;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.blogappll.Database.ReclamationDatabase;
import com.example.blogappll.Entity.Reclamation;

import java.util.List;

public class Home extends Fragment {

    private static final String TAG = "HomeFragment";
    private ReclamationDatabase db;
    private RecyclerView recyclerView;
    private ReclamationAdapter adapter;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Home fragment is created.");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Room database
        db = Room.databaseBuilder(getContext(), ReclamationDatabase.class, "reclamation_database")
                .fallbackToDestructiveMigration()
                .build();

        // Load Reclamations from the database and update UI
        loadReclamations();

        // Find the "Plus" button and set the click listener
        ImageButton buttonPlus = view.findViewById(R.id.button_plus);
        buttonPlus.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Publish.class);
            startActivity(intent);
        });

        return view;
    }

    private void loadReclamations() {
        Log.d(TAG, "Fetching Reclamations from the database...");

        new Thread(() -> {
            try {
                List<Reclamation> Reclamations = db.reclamationDao().getAllReclamations();
                Log.d(TAG, "Reclamations loaded: " + Reclamations.size());

                getActivity().runOnUiThread(() -> {
                    if (adapter == null) {
                        // Initialize adapter and handle item click for updating
                        adapter = new ReclamationAdapter(getActivity(), Reclamations, db, reclamation -> {
                            // Create an intent to open Publish in update mode
                            Intent intent = new Intent(getActivity(), Publish.class);
                            intent.putExtra("bReclamation_id", reclamation.id);  // Pass Reclamation ID for updating
                            startActivity(intent);
                        });
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.updateReclamationList(Reclamations);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error fetching Reclamations", e);
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Home fragment resumed.");
        loadReclamations();
    }
}
