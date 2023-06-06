package com.girlify.rockpaperscissors.game.data.network

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FirebaseClient @Inject constructor() {
    val db = Firebase.database.reference
}