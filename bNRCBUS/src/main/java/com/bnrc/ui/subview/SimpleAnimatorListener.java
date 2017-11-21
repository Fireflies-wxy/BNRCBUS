package com.bnrc.ui.subview;

import android.animation.Animator;

/**
 * Created by frank on 15/9/28.
 */
public class SimpleAnimatorListener implements Animator.AnimatorListener {
    private boolean mWasCanceled;

    public SimpleAnimatorListener() {
    }

    public void onAnimationCancel(Animator animator) {
        this.mWasCanceled = true;
    }

    public void onAnimationEnd(Animator animator) {
        if(!this.mWasCanceled) {
            this.onAnimationComplete(animator);
        }

    }

    public void onAnimationRepeat(Animator animator) {
    }

    public void onAnimationStart(Animator animator) {
        this.mWasCanceled = false;
    }

    public void onAnimationComplete(Animator animator) {
    }

    public boolean wasCanceled() {
        return this.mWasCanceled;
    }
}