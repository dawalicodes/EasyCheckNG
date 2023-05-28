package com.dawalisoftwares.easycheck.banks

import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.interfaces.Bank

class GTBank : Bank {
    override fun getName(): String {
        return Bank.GT_BANK
    }

    override fun getCode(): String {
        return "*737#"
    }

    override fun getLogo(): Int {
        return R.drawable.gt_bank
    }
}