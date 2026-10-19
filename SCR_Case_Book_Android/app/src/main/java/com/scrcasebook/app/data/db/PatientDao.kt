package com.scrcasebook.app.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Query("SELECT * FROM patients ORDER BY updated DESC")
    fun getAllPatientsFlow(): Flow<List<PatientEntity>>

    @Query("SELECT * FROM patients ORDER BY updated DESC")
    suspend fun getAllPatientsList(): List<PatientEntity>

    @Query("SELECT * FROM patients WHERE id = :id LIMIT 1")
    suspend fun getPatientById(id: String): PatientEntity?

    @Query("SELECT * FROM patients WHERE name LIKE '%' || :query || '%' ORDER BY updated DESC")
    fun searchPatientsFlow(query: String): Flow<List<PatientEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatient(patient: PatientEntity)

    @Update
    suspend fun updatePatient(patient: PatientEntity)

    @Delete
    suspend fun deletePatient(patient: PatientEntity)

    // Follow-ups
    @Query("SELECT * FROM follow_ups WHERE patientId = :patientId ORDER BY date DESC, id DESC")
    fun getFollowUpsFlow(patientId: String): Flow<List<FollowUpEntity>>

    @Query("SELECT * FROM follow_ups WHERE patientId = :patientId ORDER BY date DESC, id DESC")
    suspend fun getFollowUpsList(patientId: String): List<FollowUpEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollowUp(followUp: FollowUpEntity)

    @Update
    suspend fun updateFollowUp(followUp: FollowUpEntity)

    @Delete
    suspend fun deleteFollowUp(followUp: FollowUpEntity)

    @Query("SELECT COUNT(*) FROM follow_ups WHERE patientId = :patientId")
    suspend fun getFollowUpCount(patientId: String): Int
}
