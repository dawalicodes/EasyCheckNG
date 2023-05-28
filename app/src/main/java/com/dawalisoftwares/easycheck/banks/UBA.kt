package com.dawalisoftwares.easycheck.banks

import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.interfaces.Bank

class UBA : Bank {
    override fun getName(): String {
        return Bank.UBA_BANK
    }

    override fun getCode(): String {
        return "*919#"
    }

    override fun getLogo(): Int {
        return R.drawable.uba_bank
    }
}