package com.dawalisoftwares.easycheck.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatabaseViewModel(application: Application) : AndroidViewModel(application) {
    private val db = EasyCheckDatabase.getDatabase(application)
    private val itemRepository = ItemRepository(db.itemDao(), db.quickActionDao())
    val items = itemRepository.allItems.asLiveData()
    val quickActions = itemRepository.quickActionItems.asLiveData()

    fun updateQuickActions(itemNamesByPosition: List<Pair<Int, String>>) {
        viewModelScope.launch(Dispatchers.IO) {
            itemRepository.updateAllQuickActionItems(itemNamesByPosition)
        }
    }
}
