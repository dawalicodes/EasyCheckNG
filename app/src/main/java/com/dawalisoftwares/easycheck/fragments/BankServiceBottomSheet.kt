package com.dawalisoftwares.easycheck.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import com.dawalisoftwares.easycheck.Bank
import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.Sim
import com.dawalisoftwares.easycheck.adapters.BankGridAdapter
import com.dawalisoftwares.easycheck.interfaces.OnBankSelectedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class BankServiceBottomSheet(val sim: Sim) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.sheet, container, false)
        val gridview = view.findViewById<GridView>(R.id.bank_grid_view)
        val banks = mutableListOf<Bank>(
            Bank(Bank.ACCESS_BANK, Bank.getCode(Bank.ACCESS_BANK), R.drawable.access_bank),
            Bank(Bank.ECO_BANK,Bank.getCode(Bank.ECO_BANK),R.drawable.eco_bank),
            Bank(Bank.FCMB_BANK, Bank.getCode(Bank.FCMB_BANK), R.drawable.fcmb),
            Bank(Bank.FIRST_BANK, Bank.getCode(Bank.FIRST_BANK), R.drawable.first_bank),
            Bank(Bank.FIDELITY_BANK, Bank.getCode(Bank.FIDELITY_BANK), R.drawable.fidelity_bank),
            Bank(Bank.GT_BANK,Bank.getCode(Bank.GT_BANK),R.drawable.gt_bank),
            Bank(Bank.HERITAGE_BANK,Bank.getCode(Bank.HERITAGE_BANK),R.drawable.heritage_bank),
            Bank(Bank.KEYSTONE_BANK,Bank.getCode(Bank.KEYSTONE_BANK),R.drawable.keystone_bank),
            Bank(Bank.OPAY,Bank.getCode(Bank.OPAY),R.drawable.access_bank),
            Bank(Bank.PALMPAY,Bank.getCode(Bank.PALMPAY),R.drawable.stanbic_bank),
            Bank(Bank.POLARIS_BANK,Bank.getCode(Bank.POLARIS_BANK),R.drawable.polaris),
            Bank(Bank.STANBIC_IBTC_BANK,Bank.getCode(Bank.STANBIC_IBTC_BANK),R.drawable.stanbic_bank),
            Bank(Bank.STERLING_BANK,Bank.getCode(Bank.STERLING_BANK),R.drawable.sterling_bank),
            Bank(Bank.UBA_BANK,Bank.getCode(Bank.UBA_BANK),R.drawable.uba_bank),
            Bank(Bank.UNION_BANK,Bank.getCode(Bank.UNION_BANK),R.drawable.union_bank),
            Bank(Bank.UNITY_BANK,Bank.getCode(Bank.UNITY_BANK),R.drawable.unity_bank),
            Bank(Bank.WEMA_BANK,Bank.getCode(Bank.WEMA_BANK),R.drawable.wema_bank),
            Bank(Bank.ZENITH_BANK,Bank.getCode(Bank.ZENITH_BANK),R.drawable.zenith_bank)
        )
        gridview.adapter = BankGridAdapter(view.context, R.layout.bank_face, banks,
            object : OnBankSelectedListener {
                override fun bankSelected(bank: Bank) {
                    if (sim.network != null) {
                        sim.network!!.placeCall(sim, Uri.fromParts("tel",bank.code,null))
                    } else {
                        MaterialAlertDialogBuilder(gridview.context).setTitle("Ops").setMessage("This mobile network is not supported")
                            .setPositiveButton(android.R.string.ok) { d, _,  -> d.cancel()}.create().show()
                    }
                }
            })

        return view
    }

}
