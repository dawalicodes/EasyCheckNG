package com.dawalisoftwares.easycheck.banks

import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.interfaces.Bank

class PolarisBank : Bank {
    override fun getName(): String {
        return Bank.POLARIS_BANK
    }

    override fun getCode(): String {
        return "*833#"
    }

    override fun getLogo(): Int {
        return R.drawable.polaris
    }
}