package com.dawalisoftwares.easycheck.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import com.dawalisoftwares.easycheck.INFO
import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.interfaces.EasyReveal
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class NetworkLookUpReveal : EasyReveal() {
    override fun onCreate(context: Context) {}
    override fun onCreateView(
        viewGroup: ViewGroup,
        layoutInflater: LayoutInflater,
    ): View {
        val view = layoutInflater.inflate(R.layout.network_look_up, viewGroup, false)
        val simLogoHolder: View = view.findViewById(R.id.sim_logo_holder)
        val simLogo: ImageView = view.findViewById(R.id.sim_logo)
        val title: TextView = view.findViewById(R.id.title)
        val input: TextInputEditText = view.findViewById(R.id.phone_lookup)
        val inputLayout: TextInputLayout = view.findViewById(R.id.phone_layout)
        val res = view.resources

        input.doOnTextChanged { text, _, _, _ ->
            val inputStr = if ((text.isNullOrEmpty()) || (text.length < 4)) text?.toString() ?: "" else text.substring(0..3)
            when (inputStr) {
                in res.getStringArray(R.array.mtn_prefix) -> {
                    title.text = INFO.NETWORK_NAME_MTN
                    simLogoHolder.setBackgroundResource(R.drawable.bg_sim_mtn)
                    simLogo.setImageResource(R.drawable.logo_mtn)
                    inputLayout.endIconDrawable = ResourcesCompat.getDrawable(res, R.drawable.ic_check_circle, view.context.theme)
                }
                in res.getStringArray(R.array.airtel_prefix) -> {
                    title.text = INFO.NETWORK_NAME_AIRTEL
                    simLogoHolder.setBackgroundResource(R.drawable.bg_sim_airtel)
                    simLogo.setImageResource(R.drawable.logo_airtel)
                    inputLayout.endIconDrawable = ResourcesCompat.getDrawable(res, R.drawable.ic_check_circle, view.context.theme)
                }
                in res.getStringArray(R.array.mobile_prefix) -> {
                    title.text = INFO.NETWORK_NAME_MOBILE
                    simLogoHolder.setBackgroundResource(R.drawable.bg_sim_9mobile)
                    simLogo.setImageResource(R.drawable.logo_9mobile)
                    inputLayout.endIconDrawable = ResourcesCompat.getDrawable(res, R.drawable.ic_check_circle, view.context.theme)
                }
                in res.getStringArray(R.array.glo_prefix) -> {
                    title.text = INFO.NETWORK_NAME_GLO
                    simLogoHolder.setBackgroundResource(R.drawable.bg_sim_glo)
                    simLogo.setImageResource(R.drawable.logo_glo)
                    inputLayout.endIconDrawable = ResourcesCompat.getDrawable(res, R.drawable.ic_check_circle, view.context.theme)
                }
                else -> {
                    title.text = "Find a phone network"
                    simLogoHolder.setBackgroundResource(R.drawable.bg_sim_mtn)
                    simLogo.setImageResource(R.drawable.ic_baseline_dialpad_24)
                    inputLayout.endIconDrawable = null
                }
            }
        }

        return view
    }
}