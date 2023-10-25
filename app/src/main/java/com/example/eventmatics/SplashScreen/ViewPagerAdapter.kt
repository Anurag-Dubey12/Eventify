package com.example.eventmatics.SplashScreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.airbnb.lottie.LottieAnimationView
import com.example.eventmatics.R

class ViewPagerAdapter(private val context: Context):PagerAdapter() {

    private val animationFiles = arrayOf(
        R.raw.welcome,
        R.raw.splash_budget,
        R.raw.splash_task,
        R.raw.splash_vendor,
        R.raw.splash_guest
    )
    private val heading= arrayOf(
        "Welcome to Event Manager",
        "Budget Management",
        "Task Management",
        "Vendor Management",
        "Guest Management"
    )
    private val description= intArrayOf(
        R.string.splash_welcome,
        R.string.splash_budget,
        R.string.splash_task,
        R.string.splash_vendor,
        R.string.splash_guest
    )
    override fun getCount(): Int {
        return heading.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view==`object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutinflater=context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutinflater.inflate(R.layout.sliderlayout, container, false)

        val slideranimation=view.findViewById<LottieAnimationView>(R.id.animation_view)
        val slideHeading = view.findViewById<TextView>(R.id.texttitle)
        val slideDescription = view.findViewById<TextView>(R.id.textdeccription)

        slideranimation.setAnimation(animationFiles[position])
        slideranimation.playAnimation()

        slideHeading.setText(heading[position])
        slideDescription.setText(description[position])
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}