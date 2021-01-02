package cn.woolsen.material.animations

import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var samples: List<Sample>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupWindowAnimations()
        setupSamples()
        setupToolbar()
        setupLayout()
    }

    private fun setupWindowAnimations() {
        // Re-enter transition is executed when returning to this activity
        val slideTransition = Slide()
        slideTransition.slideEdge = Gravity.START
        slideTransition.duration = resources.getInteger(R.integer.anim_duration_long).toLong()
        window.reenterTransition = slideTransition
        window.exitTransition = slideTransition
    }

    private fun setupSamples() {
        samples = listOf(
                Sample(ContextCompat.getColor(this, R.color.sample_red), "Transitions"),
                Sample(ContextCompat.getColor(this, R.color.sample_blue), "Shared Elements"),
                Sample(ContextCompat.getColor(this, R.color.sample_green), "View animations"),
                Sample(ContextCompat.getColor(this, R.color.sample_yellow), "Circular Reveal Animation")
        )
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupLayout() {
        val recyclerView = findViewById<RecyclerView>(R.id.sample_list)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val samplesRecyclerAdapter = SamplesRecyclerAdapter(this, samples)
        recyclerView.adapter = samplesRecyclerAdapter
    }
}