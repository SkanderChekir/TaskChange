package com.example.prepaidcards;

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

import com.example.prepaidcards.Database.CardDatabase;
import com.example.prepaidcards.Entity.Card;

import java.util.List;

public class Home extends Fragment {

    private static final String TAG = "HomeFragment";
    private CardDatabase db;
    private RecyclerView recyclerView;
    private CardAdapter adapter;

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
        db = Room.databaseBuilder(getContext(), CardDatabase.class, "card_database")
                .fallbackToDestructiveMigration()
                .build();

        // Load cards from the database and update UI
        loadCards();

        // Find the "Plus" button and set the click listener
        ImageButton buttonPlus = view.findViewById(R.id.button_plus);
        buttonPlus.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Publish.class);
            startActivity(intent);
        });

        return view;
    }

    private void loadCards() {
        Log.d(TAG, "Fetching cards from the database...");

        new Thread(() -> {
            try {
                List<Card> cards = db.cardDao().getAllCards();
                Log.d(TAG, "Cards loaded: " + cards.size());

                getActivity().runOnUiThread(() -> {
                    if (adapter == null) {
                        // Initialiser l'adaptateur et gérer les clics pour mise à jour
                        adapter = new CardAdapter(getActivity(), cards, db, card -> {
                            // Créer une intention pour ouvrir Publish en mode mise à jour
                            Intent intent = new Intent(getActivity(), Publish.class);
                            intent.putExtra("card_id", card.id);  // Passer l'ID du card pour mise à jour
                            startActivity(intent);
                        });
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.updateCardList(cards);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error fetching cards", e);
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Home fragment resumed.");
        loadCards();
    }
}
