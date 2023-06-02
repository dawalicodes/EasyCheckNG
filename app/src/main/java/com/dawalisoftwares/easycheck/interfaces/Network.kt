package com.dawalisoftwares.easycheck.interfaces

import android.net.Uri
import android.os.Bundle
import com.dawalisoftwares.easycheck.Sim

interface Network {
    fun checkAirtimeBalance(sim: Sim?, bundle: Bundle?)
    fun checkDataBalance(sim: Sim?, bundle: Bundle?)
    fun checkPhoneNumber(sim: Sim?, bundle: Bundle?)
    fun checkTariffPlan(sim: Sim?, bundle: Bundle?)
    fun buyData(sim: Sim?, bundle: Bundle?)
    fun loadAirtime(sim: Sim?, pin: String)
    fun borrowAirtime(sim: Sim?, bundle: Bundle?)
    fun borrowData(sim: Sim?, bundle: Bundle?)
    fun sendPleaseCallMe(sim: Sim?, bundle: Bundle?)
    fun transferAirtime(sim: Sim?, bundle: Bundle?)
    fun transferData(sim: Sim?, bundle: Bundle?)
    fun cancelAutoRenew(sim: Sim?, bundle: Bundle?)
    fun callCustomerCare(sim: Sim?, bundle: Bundle?)
    fun selfService(sim: Sim?, bundle: Bundle?)
    fun loadAirtimeFromBank(sim: Sim?, bundle: Bundle?)
    fun nationalNumber(sim: Sim?, bundle: Bundle?)
    fun getNetworkName():String
    fun privateNumber(sim: Sim?, bundle: Bundle?)
    fun placeCall(sim: Sim?, uri: Uri)
}