package com.dawalisoftwares.easycheck

import android.telecom.TelecomManager

object Telecom {
    var telecomManager: TelecomManager? = null
        get() = if (field != null) field else throw IllegalStateException("telecomManager not initialized")
}