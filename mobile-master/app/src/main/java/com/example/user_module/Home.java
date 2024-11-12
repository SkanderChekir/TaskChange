package com.example.user_module;

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

import com.example.user_module.Database.EvenementDatabase;
import com.example.user_module.entity.Evenement;
import com.example.user_module.EvenementAdapter;
import com.example.user_module.Publish;
import com.example.user_module.R;

import java.util.List;

public class Home extends Fragment {

    private static final String TAG = "HomeFragment";
    private EvenementDatabase db;
    private RecyclerView recyclerView;
    private EvenementAdapter adapter;

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
        db = Room.databaseBuilder(getContext(), EvenementDatabase.class, "Evenement_database")
                .fallbackToDestructiveMigration()
                .build();

        // Load Evenement from the database and update UI
        loadEvenements();

        // Find the "Plus" button and set the click listener
        ImageButton buttonPlus = view.findViewById(R.id.button_plus);
        buttonPlus.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Publish.class);
            startActivity(intent);
        });

        return view;
    }

    private void loadEvenements() {
        Log.d(TAG, "Fetching Evenement from the database...");

        new Thread(() -> {
            try {
                List<Evenement> Evenements = db.EvenementDao().getAllEvenements();
                Log.d(TAG, "Evenements loaded: " + Evenements.size());

                getActivity().runOnUiThread(() -> {
                    if (adapter == null) {
                        // Initialiser l'adaptateur et gérer les clics pour mise à jour
                        adapter = new EvenementAdapter(getActivity(), Evenements, db, Evenement -> {
                            // Créer une intention pour ouvrir Publish en mode mise à jour
                            Intent intent = new Intent(getActivity(), Publish.class);
                            intent.putExtra("bEvenement_id", Evenement.id);  // Passer l'ID du Evenement pour mise à jour
                            startActivity(intent);
                        });
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.updateEvenementList(Evenements);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error fetching Evenements", e);
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Home fragment resumed.");
        loadEvenements();
    }
}
