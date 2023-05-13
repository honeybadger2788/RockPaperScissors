package com.girlify.rockpaperscissors.game.data.network

import com.girlify.rockpaperscissors.game.data.response.GameModel
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseClient {
    private val db = Firebase.firestore
    private val gameRef = db.collection("games")

    fun gameListener(gameId: String): Flow<GameModel> = flow {
        emit(getGame(gameId))
        while (true) {
            delay(1000) // Actualiza cada 1 segundo
            emit(getGame(gameId))
        }
    }

    private suspend fun getGame(gameId: String): GameModel {
        val response = gameRef.document(gameId).get().await()
        val data = response.data
        val playerMove = data?.get("playerMove") as? String ?: ""
        val opponentMove = data?.get("opponentMove") as? String ?: ""
        val result = data?.get("result") as? String ?: ""

        return GameModel(gameId, playerMove, opponentMove, result)
    }

    suspend fun makeMove(gameId: String, player: Int,playerMove: String) {
        gameRef.document(gameId).set(
            mapOf(
                if (player == 1) "playerMove" to playerMove else "opponentMove" to playerMove
            ),
            SetOptions.merge()
        ).await()
    }
}