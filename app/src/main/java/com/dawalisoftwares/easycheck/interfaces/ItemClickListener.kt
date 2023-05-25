package com.dawalisoftwares.easycheck.interfaces

import android.view.View

interface ItemClickListener {
    fun onItemClickListener(view: View?, name: String, description: String?, position: Int)
}