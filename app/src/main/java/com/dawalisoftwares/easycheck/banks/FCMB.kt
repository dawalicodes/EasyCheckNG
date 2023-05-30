package com.dawalisoftwares.easycheck.banks

import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.interfaces.Bank

class FCMB : Bank() {
    override fun getName(): String {
        return Bank.FCMB_BANK
    }

    override fun getCode(): String {
        return "*329#"
    }

    override fun getLogo(): Int {
        return R.drawable.fcmb
    }
}