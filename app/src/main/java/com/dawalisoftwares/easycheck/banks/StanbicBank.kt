package com.dawalisoftwares.easycheck.banks

import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.interfaces.Bank

class StanbicBank : Bank() {
    override fun getName(): String {
        return Bank.STANBIC_IBTC_BANK
    }

    override fun getCode(): String {
        return "*909#"
    }

    override fun getLogo(): Int {
        return R.drawable.stanbic_bank
    }
}