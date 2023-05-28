package com.dawalisoftwares.easycheck.banks

import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.interfaces.Bank

class UnionBank : Bank {
    override fun getName(): String {
        return Bank.UNION_BANK
    }

    override fun getCode(): String {
        return "*826#"
    }

    override fun getLogo(): Int {
        return R.drawable.union_bank
    }
}