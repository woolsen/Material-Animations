package cn.woolsen.material.animations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment

class SharedElementFragment2 : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.activity_sharedelement_fragment2, container, false)
        val sample = arguments!!.getSerializable(EXTRA_SAMPLE) as Sample
        val squareBlue = view.findViewById<ImageView>(R.id.square_blue)
        DrawableCompat.setTint(squareBlue.drawable, sample.color)
        return view
    }

    companion object {
        private const val EXTRA_SAMPLE = "sample"

        @JvmStatic
        fun newInstance(sample: Sample): SharedElementFragment2 {
            return SharedElementFragment2().apply {
                arguments = Bundle().apply {
                    putSerializable(EXTRA_SAMPLE, sample)
                }
            }
        }
    }
}