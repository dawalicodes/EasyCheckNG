package com.dawalisoftwares.easycheck.banks

import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.interfaces.Bank

class KeyStoneBank : Bank {
    override fun getName(): String {
        return Bank.KEYSTONE_BANK
    }

    override fun getCode(): String {
        return "*7111#"
    }

    override fun getLogo(): Int {
        return R.drawable.keystone_bank
    }
}