package com.dawalisoftwares.easycheck.adapters

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import androidx.recyclerview.widget.RecyclerView
import com.dawalisoftwares.easycheck.INFO
import com.dawalisoftwares.easycheck.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

class AdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val adIcon: ImageView
    val adTitle: MaterialTextView
    val adBody: MaterialTextView
    val adMediaView: MediaView
    val adRating: RatingBar
    val adAction: MaterialButton
    val nativeAd: NativeAdView
    val adClose: ImageButton

    init {
        adIcon = itemView.findViewById(R.id.ad_icon)
        adTitle = itemView.findViewById(R.id.ad_title)
        adBody = itemView.findViewById(R.id.ad_body)
        adMediaView = itemView.findViewById(R.id.ad_media)
        adRating = itemView.findViewById(R.id.ad_rating)
        adAction = itemView.findViewById(R.id.ad_action)
        nativeAd = itemView.findViewById(R.id.native_ad_view)
        adClose = itemView.findViewById(R.id.ad_close)
        val sharedPreferences: SharedPreferences = itemView.context.getSharedPreferences(INFO.AD_PREF, Context.MODE_PRIVATE)

        val adLoader = AdLoader.Builder(itemView.context, "ca-app-pub-3940256099942544/2247696110")
            .forNativeAd { ad: NativeAd ->

                adClose.visibility = View.VISIBLE
                nativeAd.setNativeAd(ad)
                nativeAd.mediaView = adMediaView
                adMediaView.isClickable = false
                nativeAd.callToActionView = adAction
                nativeAd.setNativeAd(ad)
                if (ad.icon != null) {
                    adIcon.setImageDrawable(ad.icon!!.drawable)
                }
                if (ad.body != null) {
                    adBody.visibility = View.VISIBLE
                    adBody.text = ad.body
                }
                if (ad.starRating != null) {
                    adRating.visibility = View.VISIBLE
                    adRating.rating = ad.starRating!!.toFloat()
                }
                adTitle.text = ad.headline
                adAction.text = ad.callToAction

                val currentTime = Calendar.getInstance().time.time
                val savedTime = sharedPreferences.getLong(INFO.AD_PREF_KEY, 0L)
                val timeInMillis: Long = currentTime - savedTime
                val hours: Long = TimeUnit.MILLISECONDS.toHours(timeInMillis)
                if (hours > 12) {
                    Timer("waitClick",false).schedule(3000) {
                        animate()
                    }
                }
                adMediaView.isClickable = false
                adMediaView.isFocusable = false
                adMediaView.isFocusableInTouchMode = false

            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {

                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    val currentTimeNow = Calendar.getInstance().time
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putLong(INFO.AD_PREF_KEY, currentTimeNow.time)
                    editor.apply()
                }
            })
            .withNativeAdOptions(

                NativeAdOptions.Builder()
                    .build()
            ).build()
        if (isVpnConnected(itemView.context)) {
            adBody.visibility = View.VISIBLE
            adBody.text = "This app is available to residence of Nigeria only"
        }else {
            adLoader.loadAd(AdRequest.Builder().build())
        }

    }

    private fun animate() {
        (itemView.context as Activity).runOnUiThread {
            adAction.visibility = View.VISIBLE
            val animation = android.view.animation.AnimationUtils.loadAnimation(itemView.context,R.anim.zoom_in)
            adAction.startAnimation(animation)
        }

    }
    private fun isVpnConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        return networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) == true
    }
}