package cn.woolsen.material.animations

import android.os.Bundle
import android.transition.Explode
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import cn.woolsen.material.animations.databinding.ActivityTransition2Binding

class TransitionActivity2 : BaseDetailActivity() {

    private var type = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindData()
        setupWindowAnimations()
        setupLayout()
        setupToolbar()
    }

    private fun bindData() {
        val binding: ActivityTransition2Binding = DataBindingUtil
                .setContentView(this, R.layout.activity_transition2)
        val extras = intent.extras ?: return
        val sample = extras.getSerializable(EXTRA_SAMPLE) as? Sample ?: return
        type = extras.getInt(EXTRA_TYPE)
        binding.transition2Sample = sample
    }

    private fun setupWindowAnimations() {
        val transition: Transition = if (type == TYPE_PROGRAMMATICALLY) {
            buildEnterTransition()
        } else {
            TransitionInflater.from(this).inflateTransition(R.transition.explode)
        }
        window.enterTransition = transition
    }

    private fun setupLayout() {
        findViewById<View>(R.id.exit_button).setOnClickListener { finishAfterTransition() }
    }

    private fun buildEnterTransition(): Transition {
        val enterTransition = Explode()
        enterTransition.duration = resources.getInteger(R.integer.anim_duration_long).toLong()
        return enterTransition
    }
}