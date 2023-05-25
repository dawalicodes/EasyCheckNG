package com.dawalisoftwares.easycheck.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.dawalisoftwares.easycheck.R

class ToolsAdapter(val mContext: Context, tool: MutableList<Tool>) : ArrayAdapter<Tool>(mContext, R.layout.tool_face,tool) {
    private lateinit var view: View
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView != null) {
            view = convertView.rootView
        }else {
            view = LayoutInflater.from(mContext).inflate(R.layout.tool_face,parent,false)
            val image: ImageView = view.findViewById(R.id.tool_image)
            val title: TextView = view.findViewById(R.id.tools_text)
            image.setImageResource(getItem(position)?.icon ?: R.drawable.stanbic_bank)
//            title.text = getItem(position)?.name ?: "Tool"
        }
        return view;
    }
}

data class Tool(val name: String, val icon: Int, val desc: String) {

}
