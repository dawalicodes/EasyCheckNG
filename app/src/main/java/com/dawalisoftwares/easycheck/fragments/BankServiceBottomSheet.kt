package com.dawalisoftwares.easycheck.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.Sim
import com.dawalisoftwares.easycheck.adapters.BankGridAdapter
import com.dawalisoftwares.easycheck.banks.*
import com.dawalisoftwares.easycheck.interfaces.Bank
import com.dawalisoftwares.easycheck.interfaces.OnBankSelectedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class BankServiceBottomSheet(val sim: Sim, val banks: List<Bank>) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bank_bottomsheet, container, false)
        val gridview = view.findViewById<GridView>(R.id.bank_grid_view)
        gridview.adapter = BankGridAdapter(view.context, R.layout.bank_face, banks,object : OnBankSelectedListener {
            override fun bankSelected(bank: Bank) {
                if (sim.network != null) {
                    sim.network!!.placeCall(sim, Uri.fromParts("tel",bank.getCode(),null))
                    dismissNow()
                } else {
                    MaterialAlertDialogBuilder(gridview.context).setTitle("Ops").setMessage("This mobile network is not supported")
                        .setPositiveButton(android.R.string.ok) { d, _,  -> d.cancel()}.create().show()
                }
            }

        })

        return view
    }

}
