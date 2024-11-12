package com.example.user_module;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.user_module.Database.RateComDatabase;
import com.example.user_module.entity.RateCom;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.pdf.PdfDocument;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Home1 extends Fragment {

    private static final String TAG = "HomeFragment";
    private RateComDatabase db;
    private RecyclerView recyclerView;
    private RateComAdapter adapter;
    private EditText searchEditText;
    private TextView titleTextView;
    private List<RateCom> allRateComs; // Store the full list
    private List<RateCom> filteredRateComs; // Store the filtered list

    public Home1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Home fragment is created.");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homeratecom, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Room database
        db = Room.databaseBuilder(getContext(), RateComDatabase.class, "ratecom_database")
                .fallbackToDestructiveMigration()
                .build();

        // Initialize UI elements
        titleTextView = view.findViewById(R.id.textView3);
        searchEditText = view.findViewById(R.id.searchEditText);

        // Find the "Search" button and set the click listener
        ImageButton buttonSearch = view.findViewById(R.id.button_search);
        buttonSearch.setOnClickListener(v -> {
            toggleSearchMode();
        });

        // Find the "Plus" button and set the click listener
        ImageButton buttonPlus = view.findViewById(R.id.button_plus);
        buttonPlus.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Publish1.class);
            startActivity(intent);
        });

        // Find the "Export" button and set the click listener for PDF export
        ImageButton buttonExport = view.findViewById(R.id.button_export);
        buttonExport.setOnClickListener(v -> {
            exportToPDF();
        });

        // Add TextWatcher for real-time search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Call filter on the adapter to filter the list as the user types
                adapter.filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Load data from the database and update the UI
        loadRateCom();

        return view;
    }

    private void toggleSearchMode() {
        // Toggle visibility of the title text and search field
        if (searchEditText.getVisibility() == View.GONE) {
            titleTextView.setVisibility(View.GONE);
            searchEditText.setVisibility(View.VISIBLE);
            searchEditText.requestFocus();
        } else {
            titleTextView.setVisibility(View.VISIBLE);
            searchEditText.setVisibility(View.GONE);
        }
    }

    private void loadRateCom() {
        Log.d(TAG, "Fetching RateComment from the database...");

        new Thread(() -> {
            try {
                // Fetch all RateCom entries
                allRateComs = db.rateComDao().getAllRateCom();
                filteredRateComs = new ArrayList<>(allRateComs); // Initialize filtered list
                Log.d(TAG, "RateCom loaded: " + allRateComs.size());

                getActivity().runOnUiThread(() -> {
                    if (adapter == null) {
                        // Initialize the adapter and handle clicks for updating
                        adapter = new RateComAdapter(getActivity(), filteredRateComs, db, rateCom -> {
                            Intent intent = new Intent(getActivity(), Publish1.class);
                            intent.putExtra("id", rateCom.id);  // Pass ID to update
                            startActivity(intent);
                        });
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.updateRateComList(filteredRateComs);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error fetching RateCom", e);
            }
        }).start();
    }

    private void exportToPDF() {
        // Fetch all RateCom entries from the database (this will give you the full list, not just the filtered ones)
        new Thread(() -> {
            try {
                List<RateCom> allRateComs = db.rateComDao().getAllRateCom(); // Fetch all RateComs from the database

                // Create a new PDF document
                PdfDocument pdfDocument = new PdfDocument();
                Paint paint = new Paint();
                int pageWidth = 600;
                int pageHeight = 800;

                // Create page layout
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
                PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                Canvas canvas = page.getCanvas();
                canvas.drawColor(Color.WHITE);

                // Set paint properties for text
                paint.setColor(Color.BLACK);
                paint.setTextSize(20);
                float yPosition = 40f; // Start at the top of the page

                // Add title to the PDF
                canvas.drawText("Rate and Comment Export", 20f, yPosition, paint);
                yPosition += 40f; // Move down for the data

                // Add the RateCom data to the PDF
                for (RateCom rateCom : allRateComs) {
                    String text = "Rate: " + rateCom.rate + ", Comment: " + rateCom.commentaire + ", Author: " + rateCom.author;
                    canvas.drawText(text, 20f, yPosition, paint);
                    yPosition += 30f; // Move to the next line

                    // If content goes beyond the page height, start a new page
                    if (yPosition > pageHeight - 100) {
                        pdfDocument.finishPage(page);  // Finish the current page
                        page = pdfDocument.startPage(pageInfo); // Start a new page
                        canvas = page.getCanvas();  // Get the new page canvas
                        canvas.drawColor(Color.WHITE); // Clear canvas for the new page
                        yPosition = 40f; // Reset yPosition for the new page
                    }
                }

                // Finish the last page
                pdfDocument.finishPage(page);

                // Save the PDF file to external storage
                File outputDir = new File(getContext().getExternalFilesDir(null), "RateCommentPDFs");
                if (!outputDir.exists()) {
                    outputDir.mkdirs(); // Create the folder if it doesn't exist
                }

                File pdfFile = new File(outputDir, "RateCommentExport.pdf");
                try (FileOutputStream fileOutputStream = new FileOutputStream(pdfFile)) {
                    pdfDocument.writeTo(fileOutputStream);
                    Log.d(TAG, "PDF Exported: " + pdfFile.getAbsolutePath());
                    Toast.makeText(getActivity(), "PDF Exported: " + pdfFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                  //  Log.e(TAG, "Error exporting PDF", e);
                   // Toast.makeText(getActivity(), "Error exporting PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {
                    pdfDocument.close(); // Close the PDF document
                }

            } catch (Exception e) {
                Log.e(TAG, "Error fetching RateCom data", e);
                getActivity().runOnUiThread(() -> {Toast.makeText(getActivity(), " exporting PDF", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }






    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Home fragment resumed.");
        loadRateCom();
    }
}
