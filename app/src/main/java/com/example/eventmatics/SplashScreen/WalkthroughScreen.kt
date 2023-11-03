package com.example.eventmatics.SplashScreen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Html
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.example.eventmatics.MainActivity
import com.example.eventmatics.R
import com.google.android.material.button.MaterialButton

class WalkthroughScreen : AppCompatActivity() {

    lateinit var viewPager: ViewPager
    lateinit var mdotlayout:LinearLayout
    lateinit var skipbut:MaterialButton
    lateinit var backbtn:MaterialButton
    lateinit var nextbtn:MaterialButton
    private var dots: Array<TextView?> = arrayOfNulls(4)
    lateinit var viewpageradapter:ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        backbtn = findViewById(R.id.backbtn);
        nextbtn = findViewById(R.id.nextbtn);
        skipbut = findViewById(R.id.skipButton);
        viewPager = findViewById(R.id.slideViewPager)
        mdotlayout = findViewById(R.id.indicator_layout)

        if (isFirstLaunch(this)) { setFirstLaunchFlag(this, false) }
        backbtn.setOnClickListener { if(getItem(0)>0){ viewPager.setCurrentItem(getItem(-1),true) } }
        nextbtn.setOnClickListener { if(getItem(0)<4){ viewPager.setCurrentItem(getItem(1),true) }
            else{ Intent(this,MainActivity::class.java).also { startActivity(it)
                    finish()
                } } }
        skipbut.setOnClickListener { Intent(this,MainActivity::class.java).also { startActivity(it)
            }
        }
        viewpageradapter = ViewPagerAdapter(this)
        viewPager.adapter = viewpageradapter
        setUpIndicator(0)
        viewPager.addOnPageChangeListener(viewListener)
    }
    private fun isFirstLaunch(context: Context): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getBoolean("isFirstLaunch", true)
    }
    private fun setFirstLaunchFlag(context: Context, isFirstLaunch: Boolean) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit().putBoolean("isFirstLaunch", isFirstLaunch).apply()
    }
    fun setUpIndicator(pos:Int){
        dots = arrayOfNulls(4)
        mdotlayout.removeAllViews()

        for(i in dots.indices){
            dots[i]= TextView(this)
            dots[i]!!.text = Html.fromHtml("&#8226")
            dots[i]!!.textSize = 35f
            dots[i]!!.setTextColor(getColor(R.color.Red))
            mdotlayout.addView(dots[i])
        }
        dots[pos]!!.setTextColor(getColor(R.color.Indigo))
    }
    private val viewListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            setUpIndicator(position)
            if (position > 0) { backbtn.visibility = View.VISIBLE }
            else { backbtn.visibility = View.INVISIBLE } }
        override fun onPageScrollStateChanged(state: Int) {}
    }
    fun getItem(item:Int):Int{
        return viewPager.currentItem+item
    }
}