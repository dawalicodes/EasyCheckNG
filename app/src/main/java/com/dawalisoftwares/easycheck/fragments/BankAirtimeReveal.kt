package com.dawalisoftwares.easycheck.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.dawalisoftwares.easycheck.INFO
import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.Sim
import com.dawalisoftwares.easycheck.adapters.BankGridAdapter
import com.dawalisoftwares.easycheck.interfaces.Bank
import com.dawalisoftwares.easycheck.interfaces.EasyReveal
import com.dawalisoftwares.easycheck.interfaces.OnPickContact
import com.dawalisoftwares.easycheck.view.USSDViewModel
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class BankAirtimeReveal(private val sims: List<Sim>, private val banks: List<Bank>) : EasyReveal() {
    private var pickContact: OnPickContact? = null
    private lateinit var context: Context
    private lateinit var amount: MaterialAutoCompleteTextView
    private lateinit var phone: TextInputEditText
    private lateinit var bankList: Spinner

    companion object {
        val RECHARGE_KEY = "RECHARGE"
    }

    override fun onCreate(context: Context) {
        this.context = context
        if (context is OnPickContact) {
            pickContact = context
        }
    }

    override fun onCreateView(viewGroup: ViewGroup, layoutInflater: LayoutInflater): View {
        val view = layoutInflater.inflate(R.layout.airtime_from_bank, viewGroup, false)
        val simHolder: ViewGroup = view.findViewById(R.id.sim_place_holder)
        bankList = view.findViewById(R.id.bank_spinner)
        amount = view.findViewById(R.id.amount_selected)
        phone = view.findViewById(R.id.recharge_phone)

        val phoneLayout: TextInputLayout = view.findViewById(R.id.recharge_phone_layout)
        phoneLayout.setEndIconOnClickListener {
            pickContact?.onPick()
        }

        // Quick amount selection suggestions
        val quickAmounts = arrayOf("100", "200", "500", "1000", "2000", "5000")
        val amountAdapter = ArrayAdapter(view.context, android.R.layout.simple_dropdown_item_1line, quickAmounts)
        amount.setAdapter(amountAdapter)

        if ((context is ViewModelStoreOwner) && (context is LifecycleOwner)) {
            val ussdViewModel: USSDViewModel = ViewModelProvider(context as ViewModelStoreOwner)[USSDViewModel::class.java]
            ussdViewModel.phoneNumber.observe(context as LifecycleOwner) { phoneNumber ->
                phone.setText(phoneNumber)
            }
        }

        bankList.adapter = BankGridAdapter(view.context, R.layout.bank_list, banks, null)

        val easyReveal: EasyReveal = object : SelectSimReveal(sims, INFO.LOAD_AIRTIME_FROM_BANK, Bundle()) {
            override fun simOneClick() {
                loadAirtime(0)
            }

            override fun simTwoClick() {
                loadAirtime(1)
            }
        }

        easyReveal.addToView(simHolder)

        return view
    }

    private fun loadAirtime(simIndex: Int) {
        val phoneNum = phone.text?.toString()?.trim() ?: ""
        val amountVal = amount.text?.toString()?.trim() ?: ""

        if (phoneNum.isEmpty()) {
            Toast.makeText(context, "Please enter phone number", Toast.LENGTH_SHORT).show()
            return
        }
        if (amountVal.isEmpty()) {
            Toast.makeText(context, "Please enter airtime amount", Toast.LENGTH_SHORT).show()
            return
        }

        val bank = bankList.selectedItem as? Bank
        if (bank == null) {
            Toast.makeText(context, "Please select a bank", Toast.LENGTH_SHORT).show()
            return
        }

        if (simIndex < sims.size) {
            val bundle = Bundle()
            val ussdCode = bank.getCode().removeSuffix("#") + "*" + amountVal + "*" + phoneNum + "#"
            bundle.putString(RECHARGE_KEY, ussdCode)
            sims[simIndex].network?.loadAirtimeFromBank(sims[simIndex], bundle)
        }
    }
}