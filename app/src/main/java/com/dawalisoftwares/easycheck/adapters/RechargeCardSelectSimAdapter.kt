package com.dawalisoftwares.easycheck.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.Sim

class RechargeCardSelectSimAdapter(
    val mContext: Context,
    private val resource: Int,
    private val sims: List<Sim>
) : ArrayAdapter<Sim>(mContext, resource, sims) {
    lateinit var view: View
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resource,parent,false)
            val image = view.findViewById<ImageView>(R.id.sim_image)
            val name = view.findViewById<TextView>(R.id.sim_name)
            image.setImageResource(getItem(position)?.simImage ?: R.drawable.ic_baseline_sim_card_24)
            name.text = getItem(position)?.name ?: "sim ${position + 1}"
            return view
        }
        view = convertView
        return view
    }
}