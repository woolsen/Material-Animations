package cn.woolsen.material.animations

import android.content.Intent
import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.transition.Visibility
import android.view.View
import androidx.databinding.DataBindingUtil
import cn.woolsen.material.animations.databinding.ActivityTransition1Binding

class TransitionActivity1 : BaseDetailActivity() {
    private var sample: Sample? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindData()
        setupWindowAnimations()
        setupLayout()
        setupToolbar()
    }

    private fun bindData() {
        val binding: ActivityTransition1Binding = DataBindingUtil.setContentView(this, R.layout.activity_transition1)
        val extras = intent.extras ?: return
        sample = extras.getSerializable(EXTRA_SAMPLE) as? Sample ?: return
        binding.transition1Sample = sample
    }

    private fun setupWindowAnimations() {
        val enterTransition = buildEnterTransition()
        window.enterTransition = enterTransition
    }

    private fun setupLayout() {
        findViewById<View>(R.id.sample1_button1).setOnClickListener {
            val i = Intent(this@TransitionActivity1, TransitionActivity2::class.java)
            i.putExtra(EXTRA_SAMPLE, sample)
            i.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY)
            transitionTo(i)
        }
        findViewById<View>(R.id.sample1_button2).setOnClickListener {
            val i = Intent(this@TransitionActivity1, TransitionActivity2::class.java)
            i.putExtra(EXTRA_SAMPLE, sample)
            i.putExtra(EXTRA_TYPE, TYPE_XML)
            transitionTo(i)
        }
        findViewById<View>(R.id.sample1_button3).setOnClickListener {
            val i = Intent(this@TransitionActivity1, TransitionActivity3::class.java)
            i.putExtra(EXTRA_SAMPLE, sample)
            i.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY)
            transitionTo(i)
        }
        findViewById<View>(R.id.sample1_button4).setOnClickListener {
            val i = Intent(this@TransitionActivity1, TransitionActivity3::class.java)
            i.putExtra(EXTRA_SAMPLE, sample)
            i.putExtra(EXTRA_TYPE, TYPE_XML)
            transitionTo(i)
        }
        findViewById<View>(R.id.sample1_button5).setOnClickListener {
            val returnTransition = buildReturnTransition()
            window.returnTransition = returnTransition
            finishAfterTransition()
        }
        findViewById<View>(R.id.sample1_button6).setOnClickListener {
            /**
             * 如果没有定义 returnTransition, 系统会使用反转的 enterTransition
             * 在这个例子下, returnTransition 是一个反向的 Slide (定义在 buildEnterTransition)
             */
            finishAfterTransition()
        }
    }

    private fun buildEnterTransition(): Visibility {
        val enterTransition = Fade()
        enterTransition.duration = resources.getInteger(R.integer.anim_duration_long).toLong()
        // 这个 view 不受 enterTransition 的影响
        enterTransition.excludeTarget(R.id.square_red, true)
        return enterTransition
    }

    private fun buildReturnTransition(): Visibility {
        val enterTransition: Visibility = Slide()
        enterTransition.duration = resources.getInteger(R.integer.anim_duration_long).toLong()
        return enterTransition
    }
}