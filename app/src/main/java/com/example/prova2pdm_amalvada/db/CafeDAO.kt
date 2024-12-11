package com.example.prova2pdm_amalvada.db

import android.util.Log
import android.widget.Toast
import com.example.prova2pdm_amalvada.models.Cafe
import com.google.firebase.Firebase
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await


class CafeDAO {
    private val db = Firebase.firestore

    suspend fun insertCafe(cafe: Cafe) {
        try {
            db.collection("cafes")
                .document(cafe.idCafe)
                .set(cafe)
                .await()
            Log.i("Database Info", "Novo Cafe adicionado com sucesso!")
        } catch (e: Exception) {
            Log.e("Database Info", "Erro ao adicionar novo Cafe", e)
        }
    }

    suspend fun updateCafe(cafe: Cafe) {
        try {
            db.collection("cafes").document(cafe.idCafe).update(
                mapOf(
                    "nomeCafe" to cafe.nomeCafe,
                    "notaCafe" to cafe.notaCafe,
                    "aroma" to cafe.aroma,
                    "acidez" to cafe.acidez,
                    "amargor" to cafe.amargor,
                    "sabor" to cafe.sabor,
                    "precoCafe" to cafe.precoCafe
                )
            ).await()
            Log.d("Database Info", "Cafe atualizado com sucesso!")
        } catch (e: Exception) {
            Log.e("Database Info", "Erro ao atualizar Cafe", e)
        }
    }

    suspend fun getAllCafes(): List<Cafe> {
        return try {
            val result = db.collection("cafes")
                .get()
                .await()
            resultFilter(result)
        } catch (e: Exception) {
            Log.e("Database Info", "Erro ao mostrar.", e)
            arrayListOf()
        }
    }

    suspend fun deleteCafe(id: String) {
        try {
            db.collection("cafes").document(id)
                .delete()
                .await()
            Log.d("Database Info", "Cafe removido com sucesso!")
        } catch (e: Exception) {
            Log.e("Database Info", "Erro ao remover Cafe!", e)
        }
    }

    private fun resultFilter(result: QuerySnapshot): List<Cafe> {
        return result.toObjects(Cafe::class.java)
    }
}