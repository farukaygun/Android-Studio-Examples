package com.farukaygun.kotlincatchthekenny

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.farukaygun.kotlincatchthekenny.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var score = 0
    var imageArray = ArrayList<ImageView>()
    var handler = Handler(Looper.getMainLooper())
    var runnable = Runnable {  }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        imageArray.add(binding.imageView1)
        imageArray.add(binding.imageView2)
        imageArray.add(binding.imageView3)
        imageArray.add(binding.imageView4)
        imageArray.add(binding.imageView5)
        imageArray.add(binding.imageView6)
        imageArray.add(binding.imageView7)
        imageArray.add(binding.imageView8)
        imageArray.add(binding.imageView9)

        hideImages()

        object : CountDownTimer(15000, 1000) {
            override fun onTick(p0: Long) {
                binding.textView.text = "Time: ${p0 / 1000}"
            }

            override fun onFinish() {
                binding.textView.text = "Time: 0"

                handler.removeCallbacks(runnable)

                for (image in imageArray) {
                    image.visibility = View.INVISIBLE
                }

                val alert = AlertDialog.Builder(this@MainActivity)
                    .setTitle("Game Over")
                    .setMessage("Restart the Game?")
                    .setPositiveButton("YES") {AlertDialog, which ->
                        val intent = intent
                        finish()
                        startActivity(intent)
                    }
                    .setNegativeButton("NO") {AlertDialog, which ->
                        Toast.makeText(this@MainActivity, "Game Over", Toast.LENGTH_LONG).show()
                    }
                    .show()
            }
        }.start()
    }

    fun IncreaseScore(view : View) {
        score++
        binding.textView2.text = "Score: $score"
    }

    fun hideImages() {

        runnable = object : Runnable {
            override fun run() {
                for (image in imageArray) {
                    image.visibility = View.INVISIBLE
                }

                val randomIndex = Random.nextInt(9)
                imageArray[randomIndex].visibility = View.VISIBLE

                handler.postDelayed(runnable,750)
            }

        }

        handler.post(runnable)

    }
}