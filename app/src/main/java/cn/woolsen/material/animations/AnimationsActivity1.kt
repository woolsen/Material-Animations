package cn.woolsen.material.animations

import android.content.Intent
import android.os.Bundle
import android.transition.Fade
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import cn.woolsen.material.animations.databinding.ActivityAnimations1Binding

class AnimationsActivity1 : BaseDetailActivity() {

    private lateinit var square: ImageView
    private lateinit var viewRoot: ViewGroup

    private var sizeChanged = false
    private var savedWidth = 0
    private var positionChanged = false

    private lateinit var sample: Sample

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindData()
        setupWindowAnimations()
        setupLayout()
        setupToolbar()
    }

    private fun setupWindowAnimations() {
        window.reenterTransition = Fade()
    }

    private fun bindData() {
        val binding: ActivityAnimations1Binding = DataBindingUtil.setContentView(this, R.layout.activity_animations1)
        sample = intent.extras!!.getSerializable(EXTRA_SAMPLE) as Sample
        binding.animationsSample = sample
    }

    private fun setupLayout() {
        square = findViewById(R.id.square_green)
        viewRoot = findViewById(R.id.sample3_root)
        findViewById<View>(R.id.sample3_button1).setOnClickListener { changeLayout() }
        findViewById<View>(R.id.sample3_button2).setOnClickListener { changePosition() }
        findViewById<View>(R.id.sample3_button3).setOnClickListener {
            val i = Intent(this, AnimationsActivity2::class.java)
            i.putExtra(EXTRA_SAMPLE, sample)
            transitionTo(i)
        }
    }

    private fun changeLayout() {
        TransitionManager.beginDelayedTransition(viewRoot)
        val params = square.layoutParams
        if (sizeChanged) {
            params.width = savedWidth
        } else {
            savedWidth = params.width
            params.width = 200
        }
        sizeChanged = !sizeChanged
        square.layoutParams = params
    }

    private fun changePosition() {
        TransitionManager.beginDelayedTransition(viewRoot)
        val lp = square.layoutParams as LinearLayout.LayoutParams
        if (positionChanged) {
            lp.gravity = Gravity.CENTER
        } else {
            lp.gravity = Gravity.START
        }
        positionChanged = !positionChanged
        square.layoutParams = lp
    }
}