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
        // 无需再定义一个新的enterTransition. 直接修改默认的enterTransition持续时间
        window.enterTransition?.duration = resources.getInteger(R.integer.anim_duration_long).toLong()
    }

    private fun setupLayout(sample: Sample) {
        // fragment1 的 Transition
        val slideTransition = Slide(Gravity.START)
        slideTransition.duration = resources.getInteger(R.integer.anim_duration_long).toLong()
        // 创建一个 Fragment, 并定义一些 transition
        val sharedElementFragment1 = SharedElementFragment1.newInstance(sample)
        sharedElementFragment1.reenterTransition = slideTransition
        sharedElementFragment1.exitTransition = slideTransition
        sharedElementFragment1.sharedElementEnterTransition = ChangeBounds()
        supportFragmentManager.beginTransaction()
                .replace(R.id.sample2_content, sharedElementFragment1)
                .commit()
    }
}