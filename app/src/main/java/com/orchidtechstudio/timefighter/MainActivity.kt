package com.orchidtechstudio.timefighter

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    internal lateinit var gameScoreTextView: TextView
    internal lateinit var timeLeftTextView: TextView
    internal lateinit var tapMeButton: Button
    internal var score: Long = 0
    internal var gameStarted = false
    internal lateinit var countDownTimer: CountDownTimer
    internal val initialCountDown: Long = 10000
    internal val countDownInterval: Long = 1000
    internal var timeLeftOnTimer: Long = 1000

    companion object {
        private val SCORE_KEY = "SCORE_KEY"
        private val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()

        if (savedInstanceState != null) {
            score = savedInstanceState.getLong(SCORE_KEY)
            timeLeftOnTimer = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }

        clickTapMeButton()

    }

    private fun restoreGame() {
        gameScoreTextView.text = getString(R.string.your_score, score.toString())
        val restoredTime = timeLeftOnTimer / 1000
        timeLeftTextView.text = getString(R.string.time_left, restoredTime.toString())

        countDownTimer = object : CountDownTimer(timeLeftOnTimer, countDownInterval) {
            override fun onFinish() {
                endGame()
            }

            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft = millisUntilFinished / countDownInterval
                timeLeftTextView.text = getString(R.string.time_left, timeLeft.toString())
            }

        }

        countDownTimer.start()
        gameStarted = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putLong(SCORE_KEY, score)
        outState.putLong(TIME_LEFT_KEY, timeLeftOnTimer)
    }

    private fun clickTapMeButton() {
        tapMeButton.setOnClickListener { view ->
            val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
            view.startAnimation(bounceAnimation)

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
                timeLeftOnTimer = millisUntilFinished
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
        resetGame()
        /*Toast.makeText(this@MainActivity, getString(R.string.game_finished, score.toString()), Toast.LENGTH_LONG)*/
    }

    private fun showEndGameMessage() {
        val builder = AlertDialog.Builder(this)
        with(builder)
        {
            setTitle(R.string.game_finished)
            setMessage(getString(R.string.your_score, score.toString()))
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
