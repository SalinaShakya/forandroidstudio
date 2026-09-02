package com.example.myview.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.myview.data.FavoriteManager

class FavoriteScreenViewModel: ViewModel(){

    // 1. Link to the real data
    val items = FavoriteManager.favorites

    // 2. Manage Selection state (Safe from rotation!)
    val selectedItems = mutableStateListOf<Int>()
    // this mutableStateListOf<Int> ensures when a tick is done it protects the tick on rotation

    // 3. Logic for the Checkboxes
    fun toggleSelection(id: Int) {
        if (selectedItems.contains(id)) selectedItems.remove(id)
        else selectedItems.add(id)
    }

    fun selectAll(allIds: List<Int>) {
        selectedItems.clear()
        selectedItems.addAll(allIds)
    }

    // 4. Logic for the "Delete All" button
    fun clearAll() {
        // Logic to talk to FavoriteManager
        items.toList().forEach { item ->
            FavoriteManager.removeFavorite(item)
        }
        selectedItems.clear()
    }

    fun deleteSelected() {
        val itemsToDelete = items.filter { selectedItems.contains(it.id) }
        itemsToDelete.forEach { item ->
            FavoriteManager.removeFavorite(item)
        }
        selectedItems.clear()
    }
}