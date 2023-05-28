package com.dawalisoftwares.easycheck.banks

import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.interfaces.Bank

class SterlingBank : Bank {
    override fun getName(): String {
        return Bank.STERLING_BANK
    }

    override fun getCode(): String {
        return "*822#"
    }

    override fun getLogo(): Int {
        return R.drawable.sterling_bank
    }
}