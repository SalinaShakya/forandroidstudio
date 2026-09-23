package com.example.myview.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ShippingAddressDao {
    @Query("SELECT * FROM shipping_addresses")
    fun getAllAddresses(): Flow<List<ShippingAddressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: ShippingAddressEntity)

    @Query("DELETE FROM shipping_addresses WHERE id = :id")
    suspend fun deleteById(id: String)
}
