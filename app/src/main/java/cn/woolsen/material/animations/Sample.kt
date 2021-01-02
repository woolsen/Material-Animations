package cn.woolsen.material.animations

import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import java.io.Serializable

class Sample(@ColorInt val color: Int, val name: String) : Serializable {

    companion object {
        @JvmStatic
        @BindingAdapter("colorTint")
        fun setColorTint(view: ImageView, @ColorInt color: Int) {
            view.setColorFilter(color)
        }
    }
}