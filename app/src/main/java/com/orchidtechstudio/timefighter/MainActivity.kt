package com.orchidtechstudio.timefighter

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    internal lateinit var gameScoreTextView: TextView
    internal lateinit var timeLeftTextView: TextView
    internal lateinit var tapMeButton: Button
    internal var score = 0
    internal var gameStarted = false
    internal lateinit var countDownTimer: CountDownTimer
    internal val initialCountDown: Long = 10000
    internal val countDownInterval: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        resetGame()
        clickTapMeButton()

    }

    private fun clickTapMeButton() {
        tapMeButton.setOnClickListener { view ->
            if (!gameStarted) {
                startGame()
            }
            incrementScore()
        }
    }

    private fun startGame() {
        gameStarted = true
        countDownTimer.start()
    }
    private fun resetGame() {
        score = 0
        gameScoreTextView.text = getString(R.string.your_score, score.toString())
        val initialTimeLeft = initialCountDown / countDownInterval
        timeLeftTextView.text = getString(R.string.time_left, initialTimeLeft.toString())

        setCountDownTimer()

        gameStarted = false
    }

    private fun setCountDownTimer() {
        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                val timeLeft = millisUntilFinished / countDownInterval
                timeLeftTextView.text = getString(R.string.time_left, timeLeft.toString())
            }

            override fun onFinish() {
                endGame()
            }
        }
    }

    private fun endGame() {

        showEndGameMessage()
        /*Toast.makeText(this@MainActivity, getString(R.string.game_finished, score.toString()), Toast.LENGTH_LONG)
        resetGame()*/
    }

    private fun showEndGameMessage() {
        val builder = AlertDialog.Builder(this)
        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            resetGame()
        }
        with(builder)
        {
            setTitle(R.string.game_finished)
            setMessage(getString(R.string.your_score, score.toString()))
            setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
            show()
        }
    }

    private fun incrementScore() {
        score += 1
        gameScoreTextView.text = getString(R.string.your_score, score.toString())
    }

    private fun bindViews() {
        gameScoreTextView = findViewById<TextView>(R.id.tv_your_score)
        timeLeftTextView = findViewById<TextView>(R.id.tv_time_left)
        tapMeButton = findViewById<Button>(R.id.btn_tap_me)
    }

}
