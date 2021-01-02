package cn.woolsen.material.animations

import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionInflater
import android.transition.Visibility
import android.view.Gravity
import android.view.View
import androidx.databinding.DataBindingUtil
import cn.woolsen.material.animations.databinding.ActivityTransition3Binding

class TransitionActivity3 : BaseDetailActivity() {

    private var type = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindData()
        setupWindowAnimations()
        setupLayout()
        setupToolbar()
    }

    private fun bindData() {
        val binding: ActivityTransition3Binding = DataBindingUtil
                .setContentView(this, R.layout.activity_transition3)
        val extras = intent.extras ?: return
        val sample = extras.getSerializable(EXTRA_SAMPLE) as? Sample ?: return
        type = extras.getInt(EXTRA_TYPE)
        binding.transition3Sample = sample
    }

    private fun setupWindowAnimations() {
        val transition = if (type == TYPE_PROGRAMMATICALLY) {
            buildEnterTransition()
        } else {
            TransitionInflater.from(this)
                    .inflateTransition(R.transition.slide_from_bottom)
        }
        window.enterTransition = transition
    }

    private fun setupLayout() {
        findViewById<View>(R.id.exit_button).setOnClickListener { finishAfterTransition() }
    }

    private fun buildEnterTransition(): Visibility {
        val enterTransition = Slide()
        enterTransition.duration = resources.getInteger(R.integer.anim_duration_long).toLong()
        enterTransition.slideEdge = Gravity.START
        return enterTransition
    }
}