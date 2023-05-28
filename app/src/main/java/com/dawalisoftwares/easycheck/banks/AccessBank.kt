package com.dawalisoftwares.easycheck.banks

import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.interfaces.Bank

class AccessBank : Bank {
    override fun getName(): String {
       return Bank.ACCESS_BANK
    }

    override fun getCode(): String {
        return "*901#"
    }

    override fun getLogo(): Int {
        return R.drawable.access_bank
    }
}