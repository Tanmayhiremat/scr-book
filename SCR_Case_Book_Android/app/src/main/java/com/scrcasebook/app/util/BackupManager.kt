package com.scrcasebook.app.util

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.scrcasebook.app.data.db.FollowUpEntity
import com.scrcasebook.app.data.db.PatientEntity
import com.scrcasebook.app.data.db.ScrDatabase
import com.scrcasebook.app.data.db.SectionPageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BackupManager(private val database: ScrDatabase) {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    suspend fun generateBackupJson(): String = withContext(Dispatchers.IO) {
        val patientDao = database.patientDao()
        val sectionPageDao = database.sectionPageDao()

        val dataMap = mutableMapOf<String, Any>()
        val patientList = patientDao.getAllPatientsList()

        for (patient in patientList) {
            val followUps = patientDao.getFollowUpsList(patient.id)
            val allPages = sectionPageDao.getAllPagesForPatient(patient.id)

            val pgMap = mutableMapOf<String, Int>()
            val inkMap = mutableMapOf<String, Int>()

            for (page in allPages) {
                val currentMax = pgMap[page.sectionKey] ?: 0
                if (page.pageIndex + 1 > currentMax) {
                    pgMap[page.sectionKey] = page.pageIndex + 1
                }
                if (page.strokesJson.length > 5) {
                    inkMap[page.sectionKey] = 1
                }

                // Add strokes key: "i:patientId:sectionKey:pageIndex"
                val strokeType = object : TypeToken<List<Any>>() {}.type
                val strokesList: List<Any> = try {
                    gson.fromJson(page.strokesJson, strokeType) ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }
                dataMap["i:${patient.id}:${page.sectionKey}:${page.pageIndex}"] = strokesList
            }

            val fuList = followUps.map { fu ->
                mapOf(
                    "id" to fu.id,
                    "date" to fu.date,
                    "rx" to fu.rx
                )
            }

            val pObj = mapOf(
                "id" to patient.id,
                "name" to patient.name,
                "created" to patient.created,
                "updated" to patient.updated,
                "fu" to fuList,
                "pg" to pgMap,
                "ink" to inkMap
            )
            dataMap["p:${patient.id}"] = pObj
        }

        val root = mapOf(
            "v" to 1,
            "data" to dataMap
        )
        gson.toJson(root)
    }

    suspend fun importBackupFromJson(jsonString: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val rootType = object : TypeToken<Map<String, Any>>() {}.type
            val root: Map<String, Any> = gson.fromJson(jsonString, rootType) ?: return@withContext false
            val data = root["data"] as? Map<*, *> ?: return@withContext false

            val patientDao = database.patientDao()
            val sectionPageDao = database.sectionPageDao()

            for ((k, v) in data) {
                val key = k.toString()
                if (key.startsWith("p:")) {
                    val pMap = v as? Map<*, *> ?: continue
                    val id = pMap["id"]?.toString() ?: continue
                    val name = pMap["name"]?.toString() ?: "Unnamed"
                    val created = (pMap["created"] as? Number)?.toLong() ?: System.currentTimeMillis()
                    val updated = (pMap["updated"] as? Number)?.toLong() ?: System.currentTimeMillis()

                    val patient = PatientEntity(id = id, name = name, created = created, updated = updated)
                    patientDao.insertPatient(patient)

                    val fuList = pMap["fu"] as? List<*> ?: emptyList<Any>()
                    for (item in fuList) {
                        val fuMap = item as? Map<*, *> ?: continue
                        val fuId = (fuMap["id"] as? Number)?.toLong() ?: continue
                        val date = fuMap["date"]?.toString() ?: ""
                        val rx = fuMap["rx"]?.toString() ?: ""

                        patientDao.insertFollowUp(
                            FollowUpEntity(
                                id = fuId,
                                patientId = id,
                                date = date,
                                rx = rx
                            )
                        )
                    }
                } else if (key.startsWith("i:")) {
                    // format: i:patientId:secKey:pageIndex
                    val parts = key.split(":")
                    if (parts.size >= 4) {
                        val patientId = parts[1]
                        val sectionKey = parts[2]
                        val pageIndex = parts[3].toIntOrNull() ?: 0
                        val strokesJson = gson.toJson(v)

                        sectionPageDao.insertPage(
                            SectionPageEntity(
                                patientId = patientId,
                                sectionKey = sectionKey,
                                pageIndex = pageIndex,
                                strokesJson = strokesJson,
                                updated = System.currentTimeMillis()
                            )
                        )
                    }
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    companion object {
        fun getDefaultBackupFileName(): String {
            val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
            return "scr-backup-$dateStr.json"
        }

        fun readTextFromUri(context: Context, uri: Uri): String {
            return context.contentResolver.openInputStream(uri)?.use { stream ->
                BufferedReader(InputStreamReader(stream)).readText()
            } ?: ""
        }

        fun writeTextToUri(context: Context, uri: Uri, content: String): Boolean {
            return try {
                context.contentResolver.openOutputStream(uri)?.use { stream ->
                    OutputStreamWriter(stream).use { writer ->
                        writer.write(content)
                    }
                }
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}
