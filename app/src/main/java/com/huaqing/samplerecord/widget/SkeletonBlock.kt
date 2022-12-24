package com.huaqing.samplerecord.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.LinearLayout
import com.huaqing.samplerecord.R
import java.util.*

class SkeletonBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    companion object {
        var duration = 500L
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SkeletonBlock)
        val d = typedArray.getInteger(R.styleable.SkeletonBlock_sb_duration, 0)
        val orientation =
            typedArray.getInt(R.styleable.SkeletonBlock_sb_orientation, LinearLayout.HORIZONTAL)
        typedArray.recycle()
        val random = Random().nextBoolean()


        // any 不为null 时才会调用let 函数
//        random.let {
//            it.text = "Kotlin"
//            it.textSize = 16f
//        }
//        apply函数返回的是调用对象本身
//        random.apply {
//            // 最后返回的是any对象，而不是2
//            1+1
//        }
//        这个函数的作用和let函数是类似的，其中it就是返回的调用对象本身，其一般的结构格式就是
//        any.also {
//            //it代表的就是any todo是any的属性方法
//            1+1 //返回的是any对象，而不是2
//        }
//    run函数接受一个lambda 函数为参数，传入this并以闭包形式返回，返回值是最后的计算结果
//           mTextView?.run{
//                   text = "Kotlin"
//                  textSize = 16f
//           }

        post {
            var v = Random().nextFloat()
            if (v < 0.3) v = 0.3f else if (v > 0.8) v = 0.8f
            val animation: Animation
            animation = if (orientation == LinearLayout.HORIZONTAL) {
                ScaleAnimation(
                    if (random) 1f else v, if (random) v else 1f,
                    1f, 1f, Animation.RELATIVE_TO_SELF,
                    0f, Animation.RELATIVE_TO_SELF, 0f
                )
            } else {
                ScaleAnimation(
                    1f, 1f, if (random) 1f else v, if (random) v else 1f,
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f
                )
            }
            animation.setDuration(if (d == 0) duration else d.toLong())
            animation.setRepeatMode(Animation.REVERSE)
            animation.setRepeatCount(Animation.INFINITE)
            startAnimation(animation)
        }
    }
}