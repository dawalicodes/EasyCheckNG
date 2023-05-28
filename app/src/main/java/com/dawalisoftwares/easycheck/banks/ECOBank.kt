package com.dawalisoftwares.easycheck.banks

import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.interfaces.Bank

class ECOBank : Bank {
    override fun getName(): String {
        return Bank.ECO_BANK
    }

    override fun getCode(): String {
        return "*326#"
    }

    override fun getLogo(): Int {
        return R.drawable.eco_bank
    }
}