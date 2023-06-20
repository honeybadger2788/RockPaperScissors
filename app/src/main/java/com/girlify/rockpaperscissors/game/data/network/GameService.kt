package com.girlify.rockpaperscissors.game.data.network

import android.util.Log
import com.girlify.rockpaperscissors.game.data.response.GameModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GameService @Inject constructor(
    firebaseClient: FirebaseClient
) {
    private val gameRef = firebaseClient.db.child("games")
    private lateinit var listener: ValueEventListener

    fun setGame(gameId: String) {
        gameRef.child(gameId)
    }

    fun gameListener(gameId: String): Flow<GameModel?> {
        val gameDataFlow = MutableStateFlow<GameModel?>(null)
        listener = gameRef.child(gameId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val gameModel = dataSnapshot.getValue(GameModel::class.java)
                gameDataFlow.value = gameModel
                Log.i("NOE", "Escuchando $gameId...")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("NOE", "loadPost:onCancelled", databaseError.toException())
            }
        })
        return gameDataFlow
    }

    suspend fun setPlayer(gameId: String, player: Int, username: String) {
        gameRef.child(gameId).updateChildren(
            mapOf(
                if (player == 1) "player1" to username else "player2" to username
            )
        ).await()
    }

    suspend fun makeMove(gameId: String, player: Int, playerMove: String) {
        gameRef.child(gameId).updateChildren(
            mapOf(
                if (player == 1) "player1Choice" to playerMove else "player2Choice" to playerMove
            )
        ).await()
    }

    suspend fun restartGame(gameId: String) {
        gameRef.child(gameId).updateChildren(
            mapOf(
                "player1Choice" to "",
                "player2Choice" to ""
            )
        ).await()
    }

    suspend fun deleteGame(gameId: String) {
        gameRef.child(gameId).removeValue().await()
    }

    suspend fun getGame(gameId: String): Boolean {
        val response = gameRef.child(gameId).get().await()
        return response.value != null
    }

    fun removeListener(gameId: String) {
        gameRef.child(gameId).removeEventListener(listener)
        Log.i("NOE", "Remove listener...")
    }

    suspend fun endGame(gameId: String) {
        gameRef.child(gameId).updateChildren(mapOf(
            "endGame" to true
        )).await()
    }
}