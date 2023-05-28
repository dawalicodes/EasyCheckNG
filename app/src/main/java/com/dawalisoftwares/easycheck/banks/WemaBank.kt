package com.dawalisoftwares.easycheck.banks

import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.interfaces.Bank

class WemaBank : Bank {
    override fun getName(): String {
        return Bank.WEMA_BANK
    }

    override fun getCode(): String {
        return "*945#"
    }

    override fun getLogo(): Int {
        return R.drawable.wema_bank
    }
}