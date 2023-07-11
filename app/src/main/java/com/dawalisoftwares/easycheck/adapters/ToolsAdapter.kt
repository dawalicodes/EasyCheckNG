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
import android.widget.Toast
import androidx.appcompat.widget.TooltipCompat
import androidx.camera.view.TransformExperimental
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dawalisoftwares.easycheck.*

@TransformExperimental
class ToolsAdapter(val mContext: Context, tool: MutableList<Tool>) :
    ArrayAdapter<Tool>(mContext, R.layout.tool_face, tool) {
    private lateinit var view: View
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView != null) {
            view = convertView.rootView
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.tool_face, parent, false)
            val image: ImageView = view.findViewById(R.id.tool_image)
            val title: TextView = view.findViewById(R.id.tools_text)
            image.setImageResource(getItem(position)?.icon ?: R.drawable.stanbic_bank)
            title.text = getItem(position)?.name ?: ""
            TooltipCompat.setTooltipText(view, getItem(position)?.desc ?: "")
            view.setOnClickListener {
                when (getItem(position)?.name) {
                    INFO.BEE -> context.startActivity(Intent(context,Bee::class.java))
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

data class Tool(val name: String, val icon: Int, val desc: String) {

}
