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
import android.widget.Button;
import android.widget.ImageButton;
import com.example.user_module.Database.CardDatabase;
import com.example.user_module.entity.Card;
import java.util.List;

public class Home4 extends Fragment {

    private static final String TAG = "HomeFragment";
    private CardDatabase db;
    private RecyclerView recyclerView;
    private CardAdapter adapter;

    public Home4() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Home fragment is created.");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home4, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Room database
        db = Room.databaseBuilder(getContext(), CardDatabase.class, "card_database")
                .fallbackToDestructiveMigration()
                .build();

        // Load cards from the database and update UI
        loadCards();

        // Set up the button for going to the front activity
        Button goToFrontButton = view.findViewById(R.id.goToFrontButton);
        goToFrontButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserCreditsActivity.class);
                startActivity(intent);
            }
        });

        // Handle the "Plus" button click
        ImageButton buttonPlus = view.findViewById(R.id.button_plus);
        buttonPlus.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Publish4.class);
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
                        // Initializing adapter and setting up click listener for updating cards
                        adapter = new CardAdapter(getActivity(), cards, db, card -> {
                            // Intent to open Publish in update mode
                            Intent intent = new Intent(getActivity(), Publish4.class);
                            intent.putExtra("card_id", card.id);  // Pass card ID for updating
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
