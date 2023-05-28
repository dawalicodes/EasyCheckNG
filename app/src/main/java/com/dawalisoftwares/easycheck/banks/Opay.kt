package com.dawalisoftwares.easycheck.banks

import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.interfaces.Bank

class Opay : Bank {
    override fun getName(): String {
        return Bank.OPAY
    }

    override fun getCode(): String {
        return "*955#"
    }

    override fun getLogo(): Int {
        return R.drawable.airtel
    }
}