package com.dawalisoftwares.easycheck.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.text.trimmedLength
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get
import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.Sim
import com.dawalisoftwares.easycheck.interfaces.EasyReveal
import com.dawalisoftwares.easycheck.interfaces.OnPickContact
import com.dawalisoftwares.easycheck.interfaces.OnSimSelected
import com.dawalisoftwares.easycheck.view.USSDViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class PrivateNumberReveal(private val sims: MutableList<Sim>,
                          private val action: String) : EasyReveal() {
    private lateinit var onSimSelected: OnSimSelected
    private lateinit var onPickContact: OnPickContact
    private lateinit var context: Context
    private lateinit var phoneText: TextInputEditText
    companion object {
        val phonekey: String = "PhoneKey"
    }
    override fun onCreate(context: Context?) {
        onSimSelected = context as OnSimSelected
        onPickContact = context as OnPickContact
        this.context = context
    }

    override fun onCreateView(viewGroup: ViewGroup?, layoutInflater: LayoutInflater): View {
        val view: View = layoutInflater.inflate(R.layout.private_number, viewGroup,false)
        val simHolder: LinearLayoutCompat = view.findViewById(R.id.sim_place_holder)
        val phoneLayout: TextInputLayout = view.findViewById(R.id.private_number_layout)
        phoneText = view.findViewById(R.id.private_number_text)
        phoneLayout.setEndIconOnClickListener { onPickContact.onPick() }
        val easyReveal: EasyReveal = object : SelectSimReveal(sims,action,null) {
            override fun simOneClick() {
                dialNumber(0) // dial on sim one
            }

            override fun simTwoClick() {
               dialNumber(1) //dial on sim two
            }
        }
        easyReveal.addToView(simHolder)
        val viewModel: USSDViewModel = ViewModelProvider(context as ViewModelStoreOwner)[USSDViewModel::class.java]
        viewModel.phoneNumber.observe(context as LifecycleOwner) {
            phoneText.setText(it)
        }
        return view
    }

    private fun dialNumber(simIndex: Int) {
        if (phoneText.text?.toString()!!.isNotBlank()) {
            val bundle: Bundle = Bundle()
            bundle.putString(phonekey, phoneText.text.toString())
            onSimSelected.onSimSelected(sims[simIndex],bundle,action)
        }else {
            Toast.makeText(context,"Phone Number can't be blank", Toast.LENGTH_SHORT).show()
        }
    }
}