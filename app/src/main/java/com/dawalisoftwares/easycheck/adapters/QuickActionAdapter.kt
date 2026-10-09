package com.dawalisoftwares.easycheck.adapters

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.TooltipCompat
import androidx.camera.view.TransformExperimental
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dawalisoftwares.easycheck.*
import com.dawalisoftwares.easycheck.databinding.ItemQuickActionsBinding

@TransformExperimental
class QuickActionAdapter(val mContext: Context, tool: MutableList<Action>) :
    ArrayAdapter<Action>(mContext, R.layout.item_quick_actions, tool) {
    private lateinit var view: View

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView != null) {
            view = convertView.rootView
        } else {
            val binding = ItemQuickActionsBinding.inflate(LayoutInflater.from(mContext), parent, false)
            view = binding.root
            view.tag = binding
            binding.actionIcon.setImageResource(getItem(position)?.icon ?: R.drawable.baseline_account_balance_wallet_24)
            binding.actionTitle.text = getItem(position)?.name ?: ""
            TooltipCompat.setTooltipText(view, getItem(position)?.desc ?: "")
            view.setOnClickListener {
                when (getItem(position)?.name) {
                    INFO.USSD_CODES -> context.startActivity(Intent(context, USSDCodes::class.java))
                    INFO.SCAN_CARD -> {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            context.startActivity(Intent(context, ScanCardActivity::class.java))
                        } else {
                            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.CAMERA),INFO.CAMERA_REQUEST_CODE)
                        }
                    }
                    INFO.ABOUT -> {
                        context.let { it.startActivity(Intent(it, AboutApp::class.java)) }
                    }
                }
            }
        }
        return view;
    }
}

data class Action(val name: String, val icon: Int, val desc: String) {

}
