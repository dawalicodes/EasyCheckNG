package com.dawalisoftwares.easycheck.banks

import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.interfaces.Bank

class UnityBank : Bank() {
    override fun getName(): String {
        return Bank.UNITY_BANK
    }

    override fun getCode(): String {
        return "*7799#"
    }

    override fun getLogo(): Int {
        return R.drawable.unity_bank
    }
}