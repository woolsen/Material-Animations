package cn.woolsen.material.animations

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.Slide
import android.view.Gravity
import androidx.databinding.DataBindingUtil
import cn.woolsen.material.animations.databinding.ActivitySharedelementBinding

class SharedElementActivity : BaseDetailActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sample = intent.extras!!.getSerializable(EXTRA_SAMPLE) as Sample
        bindData(sample)
        setupWindowAnimations()
        setupLayout(sample)
        setupToolbar()
    }

    private fun bindData(sample: Sample) {
        val binding: ActivitySharedelementBinding = DataBindingUtil
                .setContentView(this, R.layout.activity_sharedelement)
        binding.sharedSample = sample
    }

    private fun setupWindowAnimations() {
        // We are not interested in defining a new Enter Transition. Instead we change default transition duration
        window.enterTransition.duration = resources.getInteger(R.integer.anim_duration_long).toLong()
    }

    private fun setupLayout(sample: Sample) {
        // Transition for fragment1
        val slideTransition = Slide(Gravity.START)
        slideTransition.duration = resources.getInteger(R.integer.anim_duration_long).toLong()
        // Create fragment and define some of it transitions
        val sharedElementFragment1 = SharedElementFragment1.newInstance(sample)
        sharedElementFragment1.reenterTransition = slideTransition
        sharedElementFragment1.exitTransition = slideTransition
        sharedElementFragment1.sharedElementEnterTransition = ChangeBounds()
        supportFragmentManager.beginTransaction()
                .replace(R.id.sample2_content, sharedElementFragment1)
                .commit()
    }
}