package cn.woolsen.material.animations

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.Slide
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment

class SharedElementFragment1 : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.activity_sharedelement_fragment1, container, false)
        val sample = arguments!!.getSerializable(EXTRA_SAMPLE) as Sample
        val squareBlue = view.findViewById<ImageView>(R.id.square_blue)
        DrawableCompat.setTint(squareBlue.drawable, sample.color)
        view.findViewById<View>(R.id.sample2_button1).setOnClickListener { addNextFragment(sample, squareBlue, false) }
        view.findViewById<View>(R.id.sample2_button2).setOnClickListener { addNextFragment(sample, squareBlue, true) }
        return view
    }

    private fun addNextFragment(sample: Sample?, squareBlue: ImageView, overlap: Boolean) {
        val sharedElementFragment2 = SharedElementFragment2.newInstance(sample!!)
        val slideTransition = Slide(Gravity.END)
        slideTransition.duration = resources.getInteger(R.integer.anim_duration_medium).toLong()
        val changeBoundsTransition = ChangeBounds()
        changeBoundsTransition.duration = resources.getInteger(R.integer.anim_duration_medium).toLong()
        sharedElementFragment2.enterTransition = slideTransition
        sharedElementFragment2.allowEnterTransitionOverlap = overlap
        sharedElementFragment2.allowReturnTransitionOverlap = overlap
        sharedElementFragment2.sharedElementEnterTransition = changeBoundsTransition
        requireFragmentManager().beginTransaction()
                .replace(R.id.sample2_content, sharedElementFragment2)
                .addToBackStack(null)
                .addSharedElement(squareBlue, getString(R.string.square_blue_name))
                .commit()
    }

    companion object {
        private const val EXTRA_SAMPLE = "sample"

        @JvmStatic
        fun newInstance(sample: Sample?): SharedElementFragment1 {
            return SharedElementFragment1().apply {
                arguments = Bundle().apply {
                    putSerializable(EXTRA_SAMPLE, sample)
                }
            }
        }
    }
}