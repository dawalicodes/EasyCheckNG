package com.dawalisoftwares.easycheck.fragments

import android.content.Context
import android.content.res.Resources
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
        layoutInflater: LayoutInflater
    ): View {
        val view = layoutInflater.inflate(R.layout.network_look_up, viewGroup, false)
        val icon: ImageView = view.findViewById(R.id.icon)
        val title: TextView = view.findViewById(R.id.title)
        val input: TextInputEditText = view.findViewById(R.id.phone_lookup)
        val inputLayout: TextInputLayout = view.findViewById(R.id.phone_layout)
        val res = view.resources

        input.doOnTextChanged { text, _, _, _ ->
            icon.setImageResource(
                when (if (text!!.length < 4) text else text.substring(0..3)) {
                    in res.getStringArray(R.array.mtn_prefix) -> {
                        title.text = INFO.NETWORK_NAME_MTN
                        inputLayout.endIconDrawable = ResourcesCompat.getDrawable(res,R.drawable.ic_check_circle,view.context.theme)
                        R.drawable.mtn
                    }
                    in res.getStringArray(R.array.airtel_prefix) -> {
                        title.text = INFO.NETWORK_NAME_AIRTEL
                        inputLayout.endIconDrawable = ResourcesCompat.getDrawable(res,R.drawable.ic_check_circle,view.context.theme)
                        R.drawable.airtel
                    }
                    in res.getStringArray(R.array.mobile_prefix) -> {
                        title.text = INFO.NETWORK_NAME_MOBILE
                        inputLayout.endIconDrawable = ResourcesCompat.getDrawable(res,R.drawable.ic_check_circle,view.context.theme)
                        R.drawable.mobile
                    }
                    in res.getStringArray(R.array.glo_prefix) -> {
                        title.text = INFO.NETWORK_NAME_GLO
                        inputLayout.endIconDrawable = ResourcesCompat.getDrawable(res,R.drawable.ic_check_circle,view.context.theme)
                        R.drawable.glo
                    }
                    else -> {
                        title.text = null
                        inputLayout.endIconDrawable = null
                        R.drawable.ic_baseline_sim_card_24
                    }
                }
            )
        }

        return view
    }
}