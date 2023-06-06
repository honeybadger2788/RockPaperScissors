package com.girlify.rockpaperscissors.ui.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.girlify.rockpaperscissors.R
import com.girlify.rockpaperscissors.game.core.model.Options

@Composable
fun ResultAnimation(result: String) {
    val res: Int = when(result){
        Options.WIN_MESSAGE -> R.raw.win
        Options.LOST_MESSAGE -> R.raw.lost
        else -> R.raw.draw
    }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(res))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    Text(
        text = result,
        fontSize = 54.sp,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
    LottieAnimation(
        composition = composition,
        progress = progress,
        modifier = Modifier.size(256.dp)
    )
}