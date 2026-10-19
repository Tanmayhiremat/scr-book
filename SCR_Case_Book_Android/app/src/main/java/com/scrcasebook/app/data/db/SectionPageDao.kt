package com.scrcasebook.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SectionPageDao {
    @Query("SELECT * FROM section_pages WHERE patientId = :patientId AND sectionKey = :sectionKey ORDER BY pageIndex ASC")
    fun getPagesFlow(patientId: String, sectionKey: String): Flow<List<SectionPageEntity>>

    @Query("SELECT * FROM section_pages WHERE patientId = :patientId AND sectionKey = :sectionKey ORDER BY pageIndex ASC")
    suspend fun getPagesList(patientId: String, sectionKey: String): List<SectionPageEntity>

    @Query("SELECT * FROM section_pages WHERE patientId = :patientId AND sectionKey = :sectionKey AND pageIndex = :pageIndex LIMIT 1")
    suspend fun getPage(patientId: String, sectionKey: String, pageIndex: Int): SectionPageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPage(page: SectionPageEntity)

    @Query("DELETE FROM section_pages WHERE patientId = :patientId AND sectionKey = :sectionKey AND pageIndex = :pageIndex")
    suspend fun deletePage(patientId: String, sectionKey: String, pageIndex: Int)

    @Query("SELECT COUNT(*) FROM section_pages WHERE patientId = :patientId AND sectionKey = :sectionKey AND LENGTH(strokesJson) > 5")
    suspend fun countNonEmptyPages(patientId: String, sectionKey: String): Int

    @Query("SELECT DISTINCT sectionKey FROM section_pages WHERE patientId = :patientId AND LENGTH(strokesJson) > 5")
    suspend fun getSectionsWithInk(patientId: String): List<String>

    @Query("SELECT DISTINCT sectionKey FROM section_pages WHERE patientId = :patientId AND LENGTH(strokesJson) > 5")
    fun getSectionsWithInkFlow(patientId: String): Flow<List<String>>

    @Query("SELECT * FROM section_pages WHERE patientId = :patientId")
    suspend fun getAllPagesForPatient(patientId: String): List<SectionPageEntity>
}
