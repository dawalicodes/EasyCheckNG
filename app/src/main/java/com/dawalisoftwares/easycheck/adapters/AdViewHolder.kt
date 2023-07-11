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
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class AdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val adIcon: ImageView
    val adTitle: MaterialTextView
    val adBody: MaterialTextView
    val adRating: RatingBar
    val adAction: MaterialButton
    val adClose: ImageButton

    init {
        adIcon = itemView.findViewById(R.id.ad_icon)
        adTitle = itemView.findViewById(R.id.ad_title)
        adBody = itemView.findViewById(R.id.ad_body)
        adRating = itemView.findViewById(R.id.ad_rating)
        adAction = itemView.findViewById(R.id.ad_action)
        adClose = itemView.findViewById(R.id.ad_close)
        val sharedPreferences: SharedPreferences = itemView.context.getSharedPreferences(INFO.AD_PREF, Context.MODE_PRIVATE)

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