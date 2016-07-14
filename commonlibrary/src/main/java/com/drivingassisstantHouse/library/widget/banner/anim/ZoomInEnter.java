package com.drivingassisstantHouse.library.widget.banner.anim;

import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

public class ZoomInEnter extends BaseAnimator {
    public ZoomInEnter() {
        this.duration = 200;
    }

    public void setAnimation(View view) {
        this.animatorSet.playTogether(new Animator[]{
                ObjectAnimator.ofFloat(view, "scaleX", new float[]{1.0F, 1.5F}),
                ObjectAnimator.ofFloat(view, "scaleY", new float[]{1.0F, 1.5F})});
    }
}
