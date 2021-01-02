package cn.woolsen.material.animations

import android.os.Bundle
import android.transition.*
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cn.woolsen.material.animations.databinding.ActivityAnimations2Binding
import java.util.*

class AnimationsActivity2 : BaseDetailActivity() {
    private val viewsToAnimate: MutableList<View> = ArrayList()

    private lateinit var scene0: Scene
    private lateinit var scene1: Scene
    private lateinit var scene2: Scene
    private lateinit var scene3: Scene
    private lateinit var scene4: Scene

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindData()
        setupLayout()
        setupToolbar()
        setupWindowAnimations()
    }

    private fun bindData() {
        val binding: ActivityAnimations2Binding = DataBindingUtil.setContentView(
                this, R.layout.activity_animations2)
        val sample = intent.extras!!.getSerializable(EXTRA_SAMPLE) as Sample?
        binding.animationsSample = sample
    }

    private fun setupWindowAnimations() {
        window.enterTransition = TransitionInflater.from(this).inflateTransition(
                R.transition.slide_from_bottom)
        window.enterTransition.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {}
            override fun onTransitionCancel(transition: Transition) {}
            override fun onTransitionPause(transition: Transition) {}
            override fun onTransitionResume(transition: Transition) {}
            override fun onTransitionEnd(transition: Transition) {
                window.enterTransition.removeListener(this)
                TransitionManager.go(scene0)
            }
        })
    }

    private fun setupLayout() {
        val activityRoot = findViewById<ViewGroup>(R.id.buttons_group)
        val sceneRoot = findViewById<ViewGroup>(R.id.scene_root)
        scene0 = Scene.getSceneForLayout(sceneRoot, R.layout.activity_animations_scene0, this)
        scene0.setEnterAction {
            for (i in viewsToAnimate.indices) {
                val child = viewsToAnimate[i]
                child.animate()
                        .setStartDelay((i * DELAY).toLong())
                        .scaleX(1f)
                        .scaleY(1f)
            }
        }
        scene0.setExitAction {
            TransitionManager.beginDelayedTransition(activityRoot)
            val title = scene0.sceneRoot.findViewById<View>(R.id.scene0_title)
            title.scaleX = 0f
            title.scaleY = 0f
        }
        scene1 = Scene.getSceneForLayout(sceneRoot, R.layout.activity_animations_scene1, this)
        scene2 = Scene.getSceneForLayout(sceneRoot, R.layout.activity_animations_scene2, this)
        scene3 = Scene.getSceneForLayout(sceneRoot, R.layout.activity_animations_scene3, this)
        scene4 = Scene.getSceneForLayout(sceneRoot, R.layout.activity_animations_scene4, this)
        val button1 = findViewById<View>(R.id.sample3_button1)
        button1.setOnClickListener { TransitionManager.go(scene1, ChangeBounds()) }
        val button2 = findViewById<View>(R.id.sample3_button2)
        button2.setOnClickListener {
            val transition = TransitionInflater.from(this)
                    .inflateTransition(R.transition.slide_and_changebounds)
            TransitionManager.go(scene2, transition)
        }
        val button3 = findViewById<View>(R.id.sample3_button3)
        button3.setOnClickListener {
            val transition = TransitionInflater.from(this)
                    .inflateTransition(R.transition.slide_and_changebounds_sequential)
            TransitionManager.go(scene3, transition)
        }
        val button4 = findViewById<View>(R.id.sample3_button4)
        button4.setOnClickListener {
            val transition = TransitionInflater.from(this)
                    .inflateTransition(R.transition.slide_and_changebounds_sequential_with_interpolators)
            TransitionManager.go(scene4, transition)
        }
        viewsToAnimate.add(button1)
        viewsToAnimate.add(button2)
        viewsToAnimate.add(button3)
        viewsToAnimate.add(button4)
    }

    companion object {
        private const val DELAY = 100
    }
}