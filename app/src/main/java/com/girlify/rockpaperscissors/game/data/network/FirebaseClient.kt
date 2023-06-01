package com.girlify.rockpaperscissors.game.data.network

import android.util.Log
import com.girlify.rockpaperscissors.game.data.response.GameModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow


class FirebaseClient {
    private val db = Firebase.database.reference
    private val gameRef = db.child("games")

    fun setGame(gameId: String, player1: String) {
        val game = GameModel(player1)
        gameRef.child(gameId).setValue(game)
    }

    fun gameListener(gameId: String): Flow<GameModel?>  {
        val gameDataFlow = MutableStateFlow<GameModel?>(null)
        gameRef.child(gameId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val gameModel = dataSnapshot.getValue(GameModel::class.java)
                gameDataFlow.value = gameModel
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("NOE", "loadPost:onCancelled", databaseError.toException())
            }
        })
        return gameDataFlow
    }

    fun updateGame(gameId: String, player2: String) {
        gameRef.child(gameId).updateChildren(mapOf("player2" to player2))
    }

    fun makeMove(gameId: String, player: Int, playerMove: String) {
        gameRef.child(gameId).updateChildren(
            mapOf(
                if (player == 1) "player1Choice" to playerMove else "player2Choice" to playerMove
            )
        )
    }

    fun deleteGame(gameId: String) {
        gameRef.child(gameId).removeValue()
    }
    /*fun gameListener(gameId: String): Flow<GameModel> = flow {
        emit(getGame(gameId))
        while (true) {
            delay(1000) // Actualiza cada 1 segundo
            emit(getGame(gameId))
        }
    }

    private suspend fun getGame(gameId: String): GameModel {
        val response = gameRef.document(gameId).get().await()
        val data = response.data
        val playerMove = data?.get("player1") as? String ?: ""
        val opponentMove = data?.get("player2") as? String ?: ""

        return GameModel(gameId, playerMove, opponentMove)
    }

    suspend fun makeMove(gameId: String, player: Int,playerMove: String) {
        gameRef.document(gameId).set(
            mapOf(
                if (player == 1) "player1" to playerMove else "player2" to playerMove
            ),
            SetOptions.merge()
        ).await()
    }*/
}