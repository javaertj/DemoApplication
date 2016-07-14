package com.drivingassisstantHouse.library.widget.banner.anim;

import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

public class NoAnimExist extends BaseAnimator {
    public NoAnimExist() {
        this.duration = 200;
    }

    public void setAnimation(View view) {
        this.animatorSet.playTogether(new Animator[]{
                ObjectAnimator.ofFloat(view, "alpha", 1, 1)});
    }
}
