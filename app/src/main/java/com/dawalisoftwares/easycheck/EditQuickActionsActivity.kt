package com.dawalisoftwares.easycheck

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dawalisoftwares.easycheck.database.DatabaseViewModel
import com.dawalisoftwares.easycheck.database.ItemEntity
import com.dawalisoftwares.easycheck.databinding.ActivityEditQuickActionsBinding
import com.dawalisoftwares.easycheck.databinding.ItemEditQuickActionChoiceBinding
import com.dawalisoftwares.easycheck.databinding.ItemQuickActionsBinding

class EditQuickActionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditQuickActionsBinding
    private lateinit var viewModel: DatabaseViewModel

    private val selectedSlots = ArrayList<ItemEntity?>(listOf(null, null, null, null))
    private val allItems = ArrayList<ItemEntity>()
    private lateinit var choiceAdapter: AvailableItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditQuickActionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[DatabaseViewModel::class.java]

        setupRecyclerView()
        setupSlotListeners()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {
            saveQuickActions()
        }

        // Observe quick actions database table
        viewModel.quickActions.observe(this) { quickActions ->
            if (quickActions.isNotEmpty() && selectedSlots.all { it == null }) {
                for (i in 0 until minOf(4, quickActions.size)) {
                    selectedSlots[i] = quickActions[i]
                }
                updateSlotsUI()
                choiceAdapter.notifyDataSetChanged()
            }
        }

        // Observe all items database table
        viewModel.items.observe(this) { items ->
            allItems.clear()
            allItems.addAll(items)
            choiceAdapter.notifyDataSetChanged()
        }
    }

    private fun setupRecyclerView() {
        choiceAdapter = AvailableItemsAdapter()
        binding.availableItemsRecycler.layoutManager = LinearLayoutManager(this)
        binding.availableItemsRecycler.adapter = choiceAdapter
    }

    private fun setupSlotListeners() {
        val slotViews = listOf(
            binding.slot1.root,
            binding.slot2.root,
            binding.slot3.root,
            binding.slot4.root
        )

        slotViews.forEachIndexed { index, slotView ->
            slotView.tag = index
            slotView.setOnDragListener(SlotDragListener(index))
            slotView.setOnClickListener {
                if (selectedSlots[index] != null) {
                    selectedSlots[index] = null
                    updateSlotsUI()
                    choiceAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun updateSlotsUI() {
        val slotBindings = listOf(
            ItemQuickActionsBinding.bind(binding.slot1.root),
            ItemQuickActionsBinding.bind(binding.slot2.root),
            ItemQuickActionsBinding.bind(binding.slot3.root),
            ItemQuickActionsBinding.bind(binding.slot4.root)
        )

        for (i in 0 until 4) {
            val slotBinding = slotBindings[i]
            val item = selectedSlots[i]

            val (cardColor, iconBackground, iconTint, strokeColor) = when (i) {
                0 -> listOf("#FFFAEB", "#F0ECFF", "#6741D9", "#F7EED7").map { it.toColorInt() }
                1 -> listOf("#EFF6FF", "#F0ECFF", "#6741D9", "#DCEBFF").map { it.toColorInt() }
                2 -> listOf("#EDF9F2", "#F0ECFF", "#6741D9", "#DDF3E6").map { it.toColorInt() }
                3 -> listOf("#F5F1FF", "#F0ECFF", "#6741D9", "#EAE2FF").map { it.toColorInt() }
                else -> listOf(Color.WHITE, "#F0ECFF".toColorInt(), "#6741D9".toColorInt(), "#EAEAF2".toColorInt())
            }

            if (item != null) {
                slotBinding.actionTitle.text = item.name
                slotBinding.actionDescription.text = item.description
                slotBinding.actionIcon.setImageResource(item.icon)

                slotBinding.actionCard.setCardBackgroundColor(cardColor)
                slotBinding.actionCard.strokeColor = strokeColor
                slotBinding.iconHolder.setCardBackgroundColor(item.iconBackgroundColor.toColorInt())
                slotBinding.actionIcon.setColorFilter(item.iconColor.toColorInt())
            } else {
                slotBinding.actionTitle.text = "Slot ${i + 1} (Empty)"
                slotBinding.actionDescription.text = "Drag or tap item to add"
                slotBinding.actionIcon.setImageResource(R.drawable.ic_add)

                slotBinding.actionCard.setCardBackgroundColor(Color.parseColor("#F8FAFC"))
                slotBinding.actionCard.strokeColor = Color.parseColor("#E2E8F0")
                slotBinding.iconHolder.setCardBackgroundColor(Color.parseColor("#F1F5F9"))
                slotBinding.actionIcon.setColorFilter(Color.parseColor("#94A3B8"))
            }
        }
    }

    private fun assignItemToSlot(item: ItemEntity, slotIndex: Int) {
        val existingIndex = selectedSlots.indexOfFirst { it?.name == item.name }
        if (existingIndex != -1 && existingIndex != slotIndex) {
            selectedSlots[existingIndex] = selectedSlots[slotIndex]
        }
        selectedSlots[slotIndex] = item
        updateSlotsUI()
        choiceAdapter.notifyDataSetChanged()
    }

    private fun saveQuickActions() {
        val nonNullSlots = selectedSlots.filterNotNull()
        if (nonNullSlots.size < 4) {
            Toast.makeText(this, "Please assign items to all 4 quick action slots", Toast.LENGTH_SHORT).show()
            return
        }

        val itemNamesByPosition = selectedSlots.mapIndexedNotNull { index, itemEntity ->
            if (itemEntity != null) {
                Pair(index + 1, itemEntity.name)
            } else null
        }

        viewModel.updateQuickActions(itemNamesByPosition)
        Toast.makeText(this, "Quick actions updated!", Toast.LENGTH_SHORT).show()
        finish()
    }

    private inner class SlotDragListener(private val slotIndex: Int) : View.OnDragListener {
        override fun onDrag(v: View, event: DragEvent): Boolean {
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    return event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    v.animate().scaleX(1.04f).scaleY(1.04f).setDuration(100).start()
                    return true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()
                    return true
                }
                DragEvent.ACTION_DROP -> {
                    v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()
                    val itemName = event.clipData?.getItemAt(0)?.text?.toString() ?: return false
                    val item = allItems.find { it.name == itemName } ?: return false
                    assignItemToSlot(item, slotIndex)
                    return true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()
                    return true
                }
            }
            return false
        }
    }

    private inner class AvailableItemsAdapter : RecyclerView.Adapter<AvailableItemsAdapter.ViewHolder>() {

        inner class ViewHolder(val binding: ItemEditQuickActionChoiceBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemEditQuickActionChoiceBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ViewHolder(binding)
        }

        override fun getItemCount(): Int = allItems.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = allItems[position]
            with(holder.binding) {
                actionTitle.text = item.name
                actionDescription.text = item.description
                actionIcon.setImageResource(item.icon)
                iconHolder.setCardBackgroundColor(item.iconBackgroundColor.toColorInt())
                actionIcon.setColorFilter(item.iconColor.toColorInt())

                val slotIndex = selectedSlots.indexOfFirst { it?.name == item.name }
                if (slotIndex != -1) {
                    statusBadge.visibility = View.VISIBLE
                    statusBadge.text = "Slot ${slotIndex + 1}"
                    choiceCard.strokeColor = Color.parseColor("#5035E8")
                    choiceCard.setCardBackgroundColor(Color.parseColor("#F5F3FF"))
                } else {
                    statusBadge.visibility = View.GONE
                    choiceCard.strokeColor = Color.parseColor("#EAEAF2")
                    choiceCard.setCardBackgroundColor(Color.WHITE)
                }

                root.setOnLongClickListener { v ->
                    val clipData = ClipData.newPlainText("itemName", item.name)
                    val shadowBuilder = View.DragShadowBuilder(v)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        v.startDragAndDrop(clipData, shadowBuilder, item, 0)
                    } else {
                        @Suppress("DEPRECATION")
                        v.startDrag(clipData, shadowBuilder, item, 0)
                    }
                    true
                }

                root.setOnClickListener {
                    val emptyIndex = selectedSlots.indexOfFirst { it == null }
                    if (emptyIndex != -1) {
                        assignItemToSlot(item, emptyIndex)
                    } else if (slotIndex == -1) {
                        assignItemToSlot(item, 0)
                    }
                }
            }
        }
    }
}
