package com.dawalisoftwares.easycheck.banks

import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.interfaces.Bank

class HeritageBank : Bank {
    override fun getName(): String {
        return Bank.HERITAGE_BANK
    }

    override fun getCode(): String {
        return "*745#"
    }

    override fun getLogo(): Int {
        return R.drawable.heritage_bank
    }
}