package com.ibl.tool.clapfindphone.data.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ibl.tool.clapfindphone.data.model.SoundItem


@Dao
interface SoundDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(soundItem: SoundItem)

    @Query("SELECT * FROM sound ORDER BY time_import ASC")
    fun getAllSound(): MutableList<SoundItem>

    @Query("DELETE FROM sound WHERE sound_path =:path ")
    fun delete(path: String)

    @Query("UPDATE sound SET name =:newName WHERE sound_path =:path")
    fun updateName(path: String, newName: String)
}