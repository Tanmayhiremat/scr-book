package com.scrcasebook.app.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "patients")
data class PatientEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val created: Long = System.currentTimeMillis(),
    val updated: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "follow_ups",
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["id"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("patientId")]
)
data class FollowUpEntity(
    @PrimaryKey
    val id: Long,
    val patientId: String,
    val date: String,
    val rx: String = "",
    val pageCount: Int = 1,
    val hasInk: Boolean = false,
    val updated: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "section_pages",
    primaryKeys = ["patientId", "sectionKey", "pageIndex"],
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["id"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("patientId"), Index(value = ["patientId", "sectionKey"])]
)
data class SectionPageEntity(
    val patientId: String,
    val sectionKey: String, // e.g. "s0" or "f169762..."
    val pageIndex: Int,
    val strokesJson: String, // JSON array of Stroke objects
    val updated: Long = System.currentTimeMillis()
)
