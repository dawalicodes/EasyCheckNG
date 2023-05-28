package com.dawalisoftwares.easycheck.banks

import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.interfaces.Bank

class FidelityBank : Bank {
    override fun getName(): String {
        return Bank.FIDELITY_BANK
    }

    override fun getCode(): String {
        return "*770#"
    }

    override fun getLogo(): Int {
        return R.drawable.fidelity_bank
    }
}