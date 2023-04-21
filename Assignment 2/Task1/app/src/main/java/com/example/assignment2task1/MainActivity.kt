package com.example.assignment2task1

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private var player1_dice1 = 0
    private var player1_dice2 = 0
    private var player1_score = 0

    private var player2_dice1 = 0
    private var player2_dice2 = 0
    private var player2_score = 0

    private var player1Title: TextView? = null
    private var player2Title: TextView? = null
    private var player1Score: TextView? = null
    private var player2Score: TextView? = null
    private var player1LeftDice: ImageView? = null
    private var player1RightDice: ImageView? = null
    private var player2LeftDice: ImageView? = null
    private var player2RightDice: ImageView? = null
    private var player1NumberThrown: TextView? = null
    private var player2NumberThrown: TextView? = null
    private var player1RollButton: Button? = null
    private var player2RollButton: Button? = null

    private var playerTurn = true


    private fun init() {
        player1Score = findViewById(R.id.player_1_score)
        player2Score = findViewById(R.id.player_2_score)
    
        player1Title = findViewById(R.id.player_1)
        player2Title = findViewById(R.id.player_2)

        player1LeftDice = findViewById(R.id.player1_left_dice)
        player1RightDice = findViewById(R.id.player1_right_dice)
        player2LeftDice = findViewById(R.id.player2_left_dice)
        player2RightDice = findViewById(R.id.player2_right_dice)

        player1NumberThrown = findViewById(R.id.player_1_number_thrown)
        player2NumberThrown = findViewById(R.id.player_2_number_thrown)

        player1RollButton = findViewById(R.id.player1_roll_button)
        player2RollButton = findViewById(R.id.player2_roll_button)
        player1RollButton?.setOnClickListener(this)
        player1RollButton?.setOnClickListener(this)



    }

    private fun changeImage(ImageElement: ImageView?, number: Int) {
        if (number == 1) ImageElement?.setImageResource(R.drawable.dice_one)
        if (number == 2) ImageElement?.setImageResource(R.drawable.dice_two)
        if (number == 3) ImageElement?.setImageResource(R.drawable.dice_three)
        if (number == 4) ImageElement?.setImageResource(R.drawable.dice_four)
        if (number == 5) ImageElement?.setImageResource(R.drawable.dice_five)
        if (number == 6) ImageElement?.setImageResource(R.drawable.dice_six)
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(p0: View?) {
        var randomNumber1 = (1..6).random()
        var randomNumber2 = (1..6).random()

        // Player 1 turn
        if (playerTurn) {
            var result = randomNumber1 + randomNumber2
            player1Title?.setTextColor(Color.RED)
            player1_dice1 = randomNumber1
            player1_dice2 = randomNumber2
            player1_score = player1_score!! + result
            player1Score?.text = "Score: ${player1_score}"
            player1NumberThrown?.text = "Number thrown: $result"

            changeImage(player1LeftDice, randomNumber1)
            changeImage(player1RightDice, randomNumber2)

            if (result == 7) {
                playerTurn = false
                player2RollButton?.isEnabled = true
                player1RollButton?.isEnabled = false
                player1Title?.setTextColor(Color.BLACK)
                player2Title?.setTextColor(Color.RED)
            }
        }

        // Player 2 turn
        else {
            var result = randomNumber1 + randomNumber2
            player2_dice1 = randomNumber1
            player2_dice2 = randomNumber2
            player2_score = player2_score!! + result
            player2Score?.text = "Score: ${player2_score}"
            player2NumberThrown?.text = "Number thrown: ${result}"


            changeImage(player2LeftDice, randomNumber1)
            changeImage(player2RightDice, randomNumber2)

            if (result == 7) {
                playerTurn = true
                player2RollButton?.isEnabled = false
                player1RollButton?.isEnabled = true
                player2Title?.setTextColor(Color.BLACK)
                player1Title?.setTextColor(Color.RED)
            }
        }

        if (player1_score!! >= 70 ) {
            val dialogBuilder = AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage("Player 1 Wins")
                .setPositiveButton("Yes") {_,_ ->}

            val alert = dialogBuilder.create()
            alert.show()

            player2RollButton?.isEnabled = false
            player1RollButton?.isEnabled = false
        }

        if (player2_score!! >= 70 ) {
            val dialogBuilder = AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage("Player 2 Wins")
                .setPositiveButton("Yes") {_,_ ->}

            val alert = dialogBuilder.create()
            alert.show()

            player2RollButton?.isEnabled = false
            player1RollButton?.isEnabled = false
        }
    }


}