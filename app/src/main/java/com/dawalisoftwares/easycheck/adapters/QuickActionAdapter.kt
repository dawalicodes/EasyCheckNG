package com.dawalisoftwares.easycheck.adapters

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.TooltipCompat
import android.graphics.Color
import androidx.camera.view.TransformExperimental
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dawalisoftwares.easycheck.AboutApp
import com.dawalisoftwares.easycheck.INFO
import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.ScanCardActivity
import com.dawalisoftwares.easycheck.USSDCodes
import com.dawalisoftwares.easycheck.databinding.ItemQuickActionsBinding
import androidx.core.graphics.toColorInt

@TransformExperimental

class QuickActionAdapter(
    private val mContext: Context,
    tool: MutableList<Action>
) : ArrayAdapter<Action>(mContext, R.layout.item_quick_actions, tool) {

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val binding = if (convertView == null) {
            ItemQuickActionsBinding.inflate(
                LayoutInflater.from(mContext),
                parent,
                false
            )
        } else {
            convertView.tag as ItemQuickActionsBinding
        }

        val action = getItem(position)

        binding.root.tag = binding

        binding.actionIcon.setImageResource(
            action?.icon ?: R.drawable.baseline_account_balance_wallet_24
        )

        binding.actionTitle.text = action?.name ?: ""
        binding.actionDescription.text = action?.desc ?: ""

        val (cardColor, iconBackground, iconTint, strokeColor) = when (action?.name) {
            INFO.CHECK_AIRTIME_BALANCE -> listOf(
                "#FFFAEB", "#FFF0BF", "#F59A00", "#F7EED7"
            ).map { it.toColorInt() }

            INFO.CHECK_DATA_BALANCE -> listOf(
                "#EFF6FF", "#CDE3FF", "#1673E6", "#DCEBFF"
            ).map { it.toColorInt() }

            INFO.CHECK_PHONE_NUMBER -> listOf(
                "#EDF9F2", "#C8F0D8", "#159B5A", "#DDF3E6"
            ).map { it.toColorInt() }

            INFO.CHECK_TARIFF_PLAN -> listOf(
                "#F5F1FF", "#DED0FF", "#6335D5", "#EAE2FF"
            ).map { it.toColorInt() }

            else -> listOf(
                Color.WHITE,
                "#F0ECFF".toColorInt(),
                "#6741D9".toColorInt(),
                "#EAEAF2".toColorInt()
            )
        }

        binding.actionCard.setCardBackgroundColor(cardColor)
        binding.actionCard.strokeColor = strokeColor
        binding.iconHolder.setCardBackgroundColor(iconBackground)
        binding.actionIcon.setColorFilter(iconTint)

        // Always update the click listener for recycled views.
        binding.root.setOnClickListener {
            when (action?.name) {
                INFO.USSD_CODES -> {
                    mContext.startActivity(
                        Intent(mContext, USSDCodes::class.java)
                    )
                }

                INFO.SCAN_CARD -> {
                    if (
                        ContextCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        mContext.startActivity(
                            Intent(mContext, ScanCardActivity::class.java)
                        )
                    } else if (mContext is Activity) {
                        ActivityCompat.requestPermissions(
                            mContext,
                            arrayOf(Manifest.permission.CAMERA),
                            INFO.CAMERA_REQUEST_CODE
                        )
                    }
                }

                INFO.ABOUT -> {
                    mContext.startActivity(
                        Intent(mContext, AboutApp::class.java)
                    )
                }
            }
        }

        TooltipCompat.setTooltipText(
            binding.root,
            action?.desc ?: ""
        )

        return binding.root
    }
}

data class Action(
    val name: String,
    val icon: Int,
    val desc: String
)
