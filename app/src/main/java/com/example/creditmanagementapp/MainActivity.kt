package com.example.creditmanagementapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    private var userCredits: Int = 100 // Example starting credits
    private lateinit var tvCredits: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvCredits = findViewById(R.id.tvCredits) // Initialize tvCredits here
        val btnBuyCredits = findViewById<Button>(R.id.btnBuyCredits)
        val btnRequestTask = findViewById<Button>(R.id.btnRequestTask)
        val btnConfirmTask = findViewById<Button>(R.id.btnConfirmTask)
        val btnRedeemCard = findViewById<Button>(R.id.btnRedeemCard)

        // Update Credits TextView
        updateCreditsText()

        // Action to Buy Credits
        btnBuyCredits.setOnClickListener {
            userCredits += 50 // Example: Bought 50 credits
            updateCreditsText()
        }

        // Action to Request a Task
        btnRequestTask.setOnClickListener {
            userCredits -= 20 // Example: Task costs 20 credits
            updateCreditsText()
        }

        // Action to Confirm Task Completion
        btnConfirmTask.setOnClickListener {
            userCredits += 20 // Example: Second user receives 20 credits
            updateCreditsText()
        }

        // Action to Redeem Prepaid Card
        btnRedeemCard.setOnClickListener {
            showRedeemCardDialog()
        }
    }

    // Function to update the credits display
    private fun updateCreditsText() {
        tvCredits.text = "Credits: $userCredits"
    }

    // Define a list of prepaid card codes and their associated credits
    private val prepaidCards = mutableMapOf(
        "CARD123" to 100,
        "CARD456" to 200,
        "CARD789" to 300
    )

    // Function to redeem prepaid card
    fun redeemPrepaidCard(cardCode: String): Boolean {
        val credits = prepaidCards[cardCode]
        return if (credits != null) {
            userCredits += credits
            prepaidCards.remove(cardCode) // Remove the card after use
            true // Success
        } else {
            false // Card not found
        }
    }

    // Function to show redeem card dialog
    private fun showRedeemCardDialog() {
        // Inflate the custom layout for the dialog
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_redeem_code, null)
        val etCardCode = dialogView.findViewById<EditText>(R.id.etCardCode)

        // Create the dialog
        val dialog = AlertDialog.Builder(this)
            .setTitle("Redeem Prepaid Card")
            .setView(dialogView)
            .setPositiveButton("Submit") { _, _ ->
                val cardCode = etCardCode.text.toString().trim()

                if (redeemPrepaidCard(cardCode)) {
                    // Success
                    updateCreditsText()
                    showMessage("Code redeemed successfully!")
                } else {
                    // Failure
                    showMessage("Please try again, this code is invalid.")
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    // Function to show a simple message
    private fun showMessage(message: String) {
        val alert = AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .create()
        alert.show()
    }
}
