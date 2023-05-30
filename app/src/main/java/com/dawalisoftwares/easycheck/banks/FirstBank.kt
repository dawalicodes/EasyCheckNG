package com.dawalisoftwares.easycheck.banks

import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.interfaces.Bank

class FirstBank : Bank() {
    override fun getName(): String {
        return Bank.FIRST_BANK
    }

    override fun getCode(): String {
        return "*894#"
    }

    override fun getLogo(): Int {
        return R.drawable.first_bank
    }
}