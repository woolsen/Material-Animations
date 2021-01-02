package cn.woolsen.material.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import cn.woolsen.material.animations.databinding.ActivityRevealBinding
import kotlin.math.hypot

class RevealActivity : BaseDetailActivity(), OnTouchListener {

    private lateinit var interpolator: Interpolator

    private lateinit var bgViewGroup: RelativeLayout
    private lateinit var toolbar: Toolbar
    private lateinit var body: TextView
    private lateinit var btnRed: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindData()
        setupWindowAnimations()
        setupLayout()
        setupToolbar()
    }

    private fun bindData() {
        val binding: ActivityRevealBinding = DataBindingUtil.setContentView(this, R.layout.activity_reveal)
        val sample = intent.extras!!.getSerializable(EXTRA_SAMPLE) as Sample?
        binding.reveal1Sample = sample
    }

    private fun setupWindowAnimations() {
        interpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in)
        setupEnterAnimations()
        setupExitAnimations()
    }

    private fun setupEnterAnimations() {
        val transition = TransitionInflater.from(this).inflateTransition(R.transition.changebounds_with_arcmotion)
        window.sharedElementEnterTransition = transition
        transition.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {}
            override fun onTransitionEnd(transition: Transition) {
                // Removing listener here is very important because shared element transition is executed again backwards on exit. If we don't remove the listener this code will be triggered again.
                transition.removeListener(this)
                hideTarget()
                animateRevealShow(toolbar)
                animateButtonsIn()
            }

            override fun onTransitionCancel(transition: Transition) {}
            override fun onTransitionPause(transition: Transition) {}
            override fun onTransitionResume(transition: Transition) {}
        })
    }

    private fun setupExitAnimations() {
        val returnTransition = Fade()
        window.returnTransition = returnTransition
        returnTransition.duration = resources.getInteger(R.integer.anim_duration_medium).toLong()
        returnTransition.startDelay = resources.getInteger(R.integer.anim_duration_medium).toLong()
        returnTransition.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {
                transition.removeListener(this)
                animateButtonsOut()
                animateRevealHide(bgViewGroup)
            }

            override fun onTransitionEnd(transition: Transition) {}
            override fun onTransitionCancel(transition: Transition) {}
            override fun onTransitionPause(transition: Transition) {}
            override fun onTransitionResume(transition: Transition) {}
        })
    }

    private fun setupLayout() {
        bgViewGroup = findViewById(R.id.reveal_root)
        toolbar = findViewById(R.id.toolbar)
        body = findViewById(R.id.sample_body)
        val btnGreen = findViewById<View>(R.id.square_green)
        btnGreen.setOnClickListener { revealGreen() }
        btnRed = findViewById(R.id.square_red)
        btnRed.setOnClickListener { revealRed() }
        val btnBlue = findViewById<View>(R.id.square_blue)
        btnBlue.setOnClickListener { revealBlue() }
        findViewById<View>(R.id.square_yellow).setOnTouchListener(this)
    }

    private fun revealBlue() {
        animateButtonsOut()
        val anim = animateRevealColorFromCoordinates(bgViewGroup, R.color.sample_blue, bgViewGroup.width / 2, 0)
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                animateButtonsIn()
            }
        })
        body.setText(R.string.reveal_body4)
        body.setTextColor(ContextCompat.getColor(this, R.color.theme_blue_background))
    }

    private fun revealRed() {
        val originalParams = btnRed.layoutParams
        val transition = TransitionInflater.from(this).inflateTransition(R.transition.changebounds_with_arcmotion)
        transition.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {}
            override fun onTransitionEnd(transition: Transition) {
                animateRevealColor(bgViewGroup, R.color.sample_red)
                body.setText(R.string.reveal_body3)
                body.setTextColor(ContextCompat.getColor(this@RevealActivity, R.color.theme_red_background))
                btnRed.layoutParams = originalParams
            }

            override fun onTransitionCancel(transition: Transition) {}
            override fun onTransitionPause(transition: Transition) {}
            override fun onTransitionResume(transition: Transition) {}
        })
        TransitionManager.beginDelayedTransition(bgViewGroup, transition)
        val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
        btnRed.layoutParams = layoutParams
    }

    private fun revealYellow(x: Float, y: Float) {
        animateRevealColorFromCoordinates(bgViewGroup, R.color.sample_yellow, x.toInt(), y.toInt())
        body.setText(R.string.reveal_body1)
        body.setTextColor(ContextCompat.getColor(this, R.color.theme_yellow_background))
    }

    private fun revealGreen() {
        animateRevealColor(bgViewGroup, R.color.sample_green)
        body.setText(R.string.reveal_body2)
        body.setTextColor(ContextCompat.getColor(this, R.color.theme_green_background))
    }

    private fun hideTarget() {
        findViewById<View>(R.id.shared_target).visibility = View.GONE
    }

    private fun animateButtonsIn() {
        for (i in 0 until bgViewGroup.childCount) {
            val child = bgViewGroup.getChildAt(i)
            child.animate()
                    .setStartDelay((100 + i * DELAY).toLong())
                    .setInterpolator(interpolator)
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
        }
    }

    private fun animateButtonsOut() {
        for (i in 0 until bgViewGroup.childCount) {
            val child = bgViewGroup.getChildAt(i)
            child.animate()
                    .setStartDelay(i.toLong())
                    .setInterpolator(interpolator)
                    .alpha(0f)
                    .scaleX(0f)
                    .scaleY(0f)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        if (motionEvent.action == MotionEvent.ACTION_DOWN) {
            if (view.id == R.id.square_yellow) {
                revealYellow(motionEvent.rawX, motionEvent.rawY)
            }
        }
        return false
    }

    private fun animateRevealShow(viewRoot: View) {
        val cx = (viewRoot.left + viewRoot.right) / 2
        val cy = (viewRoot.top + viewRoot.bottom) / 2
        val finalRadius = viewRoot.width.coerceAtLeast(viewRoot.height)
        val anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, 0f, finalRadius.toFloat())
        viewRoot.visibility = View.VISIBLE
        anim.duration = resources.getInteger(R.integer.anim_duration_long).toLong()
        anim.interpolator = AccelerateInterpolator()
        anim.start()
    }

    private fun animateRevealColor(viewRoot: ViewGroup, @ColorRes color: Int) {
        val cx = (viewRoot.left + viewRoot.right) / 2
        val cy = (viewRoot.top + viewRoot.bottom) / 2
        animateRevealColorFromCoordinates(viewRoot, color, cx, cy)
    }

    private fun animateRevealColorFromCoordinates(viewRoot: ViewGroup, @ColorRes color: Int, x: Int, y: Int): Animator {
        val finalRadius = hypot(viewRoot.width.toDouble(), viewRoot.height.toDouble()).toFloat()
        val anim = ViewAnimationUtils.createCircularReveal(viewRoot, x, y, 0f, finalRadius)
        viewRoot.setBackgroundColor(ContextCompat.getColor(this, color))
        anim.duration = resources.getInteger(R.integer.anim_duration_long).toLong()
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.start()
        return anim
    }

    private fun animateRevealHide(viewRoot: View) {
        val cx = (viewRoot.left + viewRoot.right) / 2
        val cy = (viewRoot.top + viewRoot.bottom) / 2
        val initialRadius = viewRoot.width
        val anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, initialRadius.toFloat(), 0f)
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                viewRoot.visibility = View.INVISIBLE
            }
        })
        anim.duration = resources.getInteger(R.integer.anim_duration_medium).toLong()
        anim.start()
    }

    companion object {
        private const val DELAY = 100
    }
}