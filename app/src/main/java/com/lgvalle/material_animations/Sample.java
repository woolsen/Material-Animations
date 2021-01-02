package com.lgvalle.material_animations;

import androidx.annotation.ColorInt;
import androidx.databinding.BindingAdapter;
import androidx.annotation.ColorRes;
import androidx.core.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

import java.io.Serializable;

public class Sample implements Serializable {

    final int color;
    private final String name;

    public Sample(@ColorInt int color, String name) {
        this.color = color;
        this.name = name;
    }

    @BindingAdapter("bind:colorTint")
    public static void setColorTint(ImageView view, @ColorInt int color) {
        DrawableCompat.setTint(view.getDrawable(), color);
        //view.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }


}
