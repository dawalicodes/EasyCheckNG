package com.dawalisoftwares.easycheck.banks

import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.interfaces.Bank

class ZenithBank : Bank() {
    override fun getName(): String {
        return Bank.ZENITH_BANK
    }

    override fun getCode(): String {
        return "*966#"
    }

    override fun getLogo(): Int {
        return R.drawable.zenith_bank
    }
}