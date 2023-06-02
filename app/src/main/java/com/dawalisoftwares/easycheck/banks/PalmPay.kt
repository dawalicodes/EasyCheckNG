package com.dawalisoftwares.easycheck.banks

import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.interfaces.Bank

class PalmPay : Bank() {
    override fun getName(): String {
        return Bank.PALMPAY
    }

    override fun getCode(): String {
        return "*652#"
    }

    override fun getLogo(): Int {
        return R.drawable.palmpay
    }
}