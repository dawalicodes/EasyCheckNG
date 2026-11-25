package com.dawalisoftwares.easycheck.database

import kotlinx.coroutines.flow.Flow

class ItemRepository(
    private val dao: ItemDao,
    private val quickActionDao: QuickActionDao
) {
    val allItems: Flow<List<ItemEntity>> = dao.getAllItems()
    val quickActionItems: Flow<List<ItemEntity>> = quickActionDao.getQuickActionItems()

    suspend fun addItem(item: ItemEntity) {
        dao.insertItem(item)
    }

    suspend fun addQuickAction(quickAction: QuickActionEntity) {
        quickActionDao.insertQuickAction(quickAction)
    }

    suspend fun updateQuickAction(quickAction: QuickActionEntity) {
        quickActionDao.updateQuickAction(quickAction)
    }

    suspend fun deleteQuickAction(quickAction: QuickActionEntity) {
        quickActionDao.deleteQuickAction(quickAction)
    }

    suspend fun updateQuickActionItemByPosition(position: Int, itemName: String) {
        quickActionDao.updateQuickActionItemByPosition(position, itemName)
    }

    suspend fun updateAllQuickActionItems(itemNamesByPosition: List<Pair<Int, String>>) {
        val quickActions = itemNamesByPosition.map { (position, itemName) ->
            QuickActionEntity(itemName = itemName, position = position)
        }
        quickActionDao.replaceAllQuickActions(quickActions)
    }
}
