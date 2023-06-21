package com.girlify.rockpaperscissors.game.data.network

import android.util.Log
import com.girlify.rockpaperscissors.R
import com.girlify.rockpaperscissors.game.data.response.GameModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GameService @Inject constructor(
    firebaseClient: FirebaseClient
) {
    private val gameRef = firebaseClient.db.child("games")

    fun setGame(gameId: String) {
        gameRef.child(gameId).setValue(GameModel())
    }

    fun gameListener(gameId: String): Flow<GameModel?> = callbackFlow {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val gameModel = dataSnapshot.getValue(GameModel::class.java)
                try {
                    trySend(gameModel).isSuccess
                } catch (e: Exception) {
                    Log.w(R.string.app_name.toString(), "Error offering game model", e)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(R.string.app_name.toString(), "loadPost:onCancelled", databaseError.toException())
            }
        }
        gameRef.child(gameId).addValueEventListener(valueEventListener)

        awaitClose { gameRef.child(gameId).removeEventListener(valueEventListener) }
    }

    suspend fun setPlayer(gameId: String, player: Int, username: String) {
        val updateKey = if (player == 1) "player1" else "player2"
        gameRef.child(gameId).child(updateKey).setValue(username).await()
    }

    suspend fun makeMove(gameId: String, player: Int, playerMove: String) {
        val updateKey = if (player == 1) "player1Choice" else "player2Choice"
        gameRef.child(gameId).child(updateKey).setValue(playerMove).await()
    }

    suspend fun restartGame(gameId: String) {
        gameRef.child(gameId).child("player1Choice").setValue("").await()
        gameRef.child(gameId).child("player2Choice").setValue("").await()
    }

    suspend fun deleteGame(gameId: String) {
        gameRef.child(gameId).removeValue().await()
    }

    suspend fun getGame(gameId: String): Boolean {
        val response = gameRef.child(gameId).get().await()
        return response.value != null
    }

    suspend fun endGame(gameId: String) {
        gameRef.child(gameId).child("endGame").setValue(true).await()
    }
}