package com.dawalisoftwares.easycheck.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
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
import java.time.temporal.TemporalAmount

class BankAirtimeReveal(private val sims: List<Sim>, private val banks: List<Bank>) : EasyReveal() {
    private lateinit var pickContact: OnPickContact
    private lateinit var context: Context
    private lateinit var amount: MaterialAutoCompleteTextView
    private lateinit var phone: TextInputEditText
    private lateinit var bankList: Spinner

    companion object {
        val RECHARGE_KEY = "RECHARGE"
    }

    override fun onCreate(context: Context) {
        this.context = context
        pickContact = context as OnPickContact
    }
    override fun onCreateView(viewGroup: ViewGroup, layoutInflater: LayoutInflater): View {
        val view = layoutInflater.inflate(R.layout.airtime_from_bank, viewGroup, false)
        val simHolder = view.findViewById<ConstraintLayout>(R.id.sim_place_holder)
        bankList = view.findViewById(R.id.bank_spinner)
        amount = view.findViewById(R.id.amount_selected)
        phone = view.findViewById(R.id.recharge_phone)
        val phoneLayout: TextInputLayout = view.findViewById(R.id.recharge_phone_layout)
        phoneLayout.setEndIconOnClickListener { pickContact.onPick() }
        val ussdViewModel: USSDViewModel = ViewModelProvider(context as ViewModelStoreOwner)[USSDViewModel::class.java]
        ussdViewModel.phoneNumber.observe(context as LifecycleOwner) { phoneNumber ->
            phone.setText(phoneNumber)
        }
        bankList.adapter = BankGridAdapter(view.context,R.layout.bank_list, banks,null)

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
        if (amount.text.isNotBlank() && phone.text!!.isNotBlank()) {
            val bank: Bank = bankList.selectedItem as Bank
            val bundle = Bundle()
            bundle.putString(RECHARGE_KEY,bank.getCode().removeSuffix("#").plus("*").plus(amount.text.toString()).plus("*").plus(phone.text!!.toString()).plus("#"))
            sims[simIndex].network?.loadAirtimeFromBank(sims[simIndex],bundle)
        }else {
            Toast.makeText(context,"fill in all fields",Toast.LENGTH_SHORT).show()
        }
    }
}