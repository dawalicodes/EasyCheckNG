package com.dawalisoftwares.easycheck.adapters

import android.Manifest
import android.R.attr.iconTint
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
import androidx.core.graphics.component1
import com.dawalisoftwares.easycheck.AboutApp
import com.dawalisoftwares.easycheck.INFO
import com.dawalisoftwares.easycheck.R
import com.dawalisoftwares.easycheck.ScanCardActivity
import com.dawalisoftwares.easycheck.USSDCodes
import com.dawalisoftwares.easycheck.databinding.ItemQuickActionsBinding
import androidx.core.graphics.toColorInt
import com.dawalisoftwares.easycheck.database.ItemEntity
import com.dawalisoftwares.easycheck.interfaces.ItemClickListener

@TransformExperimental
class QuickActionAdapter(
    private val mContext: Context,
    tool: MutableList<ItemEntity>,private val actionClickListener: ItemClickListener
) : ArrayAdapter<ItemEntity>(mContext, R.layout.item_quick_actions, tool) {

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
        binding.actionDescription.text = action?.description ?: ""

        val (cardColor, iconBackground, iconTint, strokeColor) = when (position) {
            0 -> listOf(
                "#FFFAEB", action?.iconBackgroundColor ?: "#F0ECFF", action?.iconColor ?: "#6741D9", "#F7EED7"
            ).map { it.toColorInt() }

            1 -> listOf(
                "#EFF6FF", action?.iconBackgroundColor ?: "#F0ECFF", action?.iconColor ?: "#6741D9", "#DCEBFF"
            ).map { it.toColorInt() }

            2 -> listOf(
                "#EDF9F2", action?.iconBackgroundColor ?: "#F0ECFF", action?.iconColor ?: "#6741D9", "#DDF3E6"
            ).map { it.toColorInt() }

            3 -> listOf(
                "#F5F1FF", action?.iconBackgroundColor ?: "#F0ECFF", action?.iconColor ?: "#6741D9", "#EAE2FF"
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

        binding.root.setOnClickListener {
            action?.let { action -> actionClickListener.onItemClickListener(binding.actionCard, action.name, action.description,position) }
        }

        TooltipCompat.setTooltipText(
            binding.root,
            action?.description ?: ""
        )

        return binding.root
    }
}

