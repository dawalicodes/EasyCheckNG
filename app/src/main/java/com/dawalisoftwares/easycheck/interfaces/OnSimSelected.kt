package com.dawalisoftwares.easycheck.interfaces

import android.os.Bundle
import com.dawalisoftwares.easycheck.Sim

interface OnSimSelected {
    fun onSimSelected(sim: Sim, bundle: Bundle?,action: String)
}