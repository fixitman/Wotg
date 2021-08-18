package com.fimappware.customviews

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.fimappware.customviews.databinding.CrossfadeViewLayoutBinding

class CrossFadeView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val bind : CrossfadeViewLayoutBinding =
        CrossfadeViewLayoutBinding.inflate(LayoutInflater.from(context),this)
    private var isFading = false

    fun setBackViewImage(drawable: Drawable){
        bind.backView.setImageDrawable(drawable)
    }

    fun crossfade(drawable: Drawable, duration : Int = android.R.integer.config_longAnimTime){
        if(isFading) return

        isFading = true
        bind.frontView.apply{
            setImageDrawable(drawable)
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .setDuration(duration.toLong())
                .alpha(1f)
                .setListener(null)
        }

        bind.backView.apply {
            animate()
                .setDuration(duration.toLong())
                .alpha(0f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        bind.frontView.apply{
                            alpha = 0f
                            visibility = View.GONE
                        }

                        bind.backView.apply{
                            alpha = 1f
                            visibility = View.VISIBLE
                            setImageDrawable(drawable)
                        }
                        isFading = false
                    }
                })

        }
    }
}