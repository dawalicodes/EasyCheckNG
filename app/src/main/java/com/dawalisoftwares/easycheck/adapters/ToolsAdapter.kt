package com.dawalisoftwares.easycheck.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.TooltipCompat
import androidx.camera.view.TransformExperimental
import com.dawalisoftwares.easycheck.INFO
import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.ScanCardActivity
import com.dawalisoftwares.easycheck.USSDCodes

@TransformExperimental class ToolsAdapter(val mContext: Context, tool: MutableList<Tool>) : ArrayAdapter<Tool>(mContext, R.layout.tool_face,tool) {
    private lateinit var view: View
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView != null) {
            view = convertView.rootView
        }else {
            view = LayoutInflater.from(mContext).inflate(R.layout.tool_face,parent,false)
            val image: ImageView = view.findViewById(R.id.tool_image)
            val title: TextView = view.findViewById(R.id.tools_text)
            image.setImageResource(getItem(position)?.icon ?: R.drawable.stanbic_bank)
            title.text = getItem(position)?.name ?: ""
            TooltipCompat.setTooltipText(view, getItem(position)?.desc ?: "")
            view.setOnClickListener {  when (getItem(position)?.name) {
                INFO.BEE -> Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show()
                INFO.USSD_CODES -> context.startActivity(Intent(context, USSDCodes::class.java))
                INFO.SCAN_CARD -> context.startActivity(Intent(context, ScanCardActivity::class.java))
                INFO.ABOUT -> {}
            } }
        }
        return view;
    }
}

data class Tool(val name: String, val icon: Int, val desc: String) {

}
