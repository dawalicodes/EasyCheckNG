package com.dawalisoftwares.easycheck.interfaces

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.telecom.TelecomManager
import com.dawalisoftwares.easycheck.Sim
import com.dawalisoftwares.easycheck.Telecom
import com.dawalisoftwares.easycheck.Telecom.telecomManager

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
    @SuppressLint("MissingPermission")
    fun placeCall(sim: Sim?, uri: Uri) {
        val bundle = Bundle()
        if (!Telecom.telecomManager?.callCapablePhoneAccounts.isNullOrEmpty()) {
            try {
                bundle.putParcelable(
                    TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE,
                    Telecom.telecomManager?.callCapablePhoneAccounts?.get(sim?.simSlotIndex ?: 0)
                )
                Telecom.telecomManager?.placeCall(uri, bundle)
            } catch (error: IndexOutOfBoundsException) {
                bundle.putParcelable(
                    TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE,
                    telecomManager?.callCapablePhoneAccounts?.first()
                )
                telecomManager?.placeCall(uri, bundle)
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }
}