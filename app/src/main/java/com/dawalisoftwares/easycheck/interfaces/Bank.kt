package com.dawalisoftwares.easycheck.interfaces

import com.dawalisoftwares.easycheck.banks.*
import java.util.*

abstract class Bank {
    companion object {
        const val ACCESS_BANK = "Access Bank"
        const val ECO_BANK = "ECO Bank"
        const val FCMB_BANK = "FCMB"
        const val FIRST_BANK = "First Bank"
        const val FIDELITY_BANK = "Fidelity"
        const val GT_BANK = "GTB"
        const val HERITAGE_BANK = "Heritage"
        const val KEYSTONE_BANK = "Keystone"
        const val OPAY = "Opay"
        const val PALMPAY = "Palmpay"
        const val POLARIS_BANK = "Polaris"
        const val STANBIC_IBTC_BANK = "Stanbic"
        const val STERLING_BANK = "Sterling"
        const val UBA_BANK = "UBA"
        const val UNION_BANK = "Union"
        const val UNITY_BANK = "Unity"
        const val WEMA_BANK = "Wema"
        const val ZENITH_BANK = "Zenith"

        fun getAllBanks() : List<Bank> {
            return listOf(
                AccessBank(),
                ECOBank(),
                FCMB(),
                FirstBank(),
                FidelityBank(),
                GTBank(),
                HeritageBank(),
                KeyStoneBank(),
                Opay(),
                PalmPay(),
                PolarisBank(),
                StanbicBank(),
                SterlingBank(),
                UBA(),
                UnionBank(),
                UnityBank(),
                WemaBank(),
                ZenithBank()
            )
        }
    }

    abstract fun getName() : String
    abstract fun getCode() : String
    abstract fun getLogo() : Int


}