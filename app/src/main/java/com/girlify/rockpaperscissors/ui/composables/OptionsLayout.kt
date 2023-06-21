package com.girlify.rockpaperscissors.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.girlify.rockpaperscissors.R
import com.girlify.rockpaperscissors.game.core.model.Options

@Composable
fun OptionsLayout(isEnable: Boolean, onClick: (String) -> Unit) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (boxTitle,boxRock, boxPaper, boxScissors) = createRefs()

        Box(modifier = Modifier
            .constrainAs(boxTitle){
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(boxRock.top)
            })  {
            Title()
        }

        Box(modifier = Modifier
            .constrainAs(boxRock) {
                top.linkTo(boxTitle.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(boxPaper.top)
            }) {
            PlayButton(Options.ROCK, isEnable ) {
                onClick(Options.ROCK)
            }
        }

        Box(modifier = Modifier
            .constrainAs(boxPaper) {
                top.linkTo(boxRock.bottom)
                start.linkTo(parent.start)
                end.linkTo(boxScissors.start)
                bottom.linkTo(parent.bottom)
            }){
            PlayButton(Options.PAPER, isEnable ) {
                onClick(Options.PAPER)
            }
        }

        Box(modifier = Modifier
            .constrainAs(boxScissors) {
                top.linkTo(boxRock.bottom)
                start.linkTo(boxPaper.end)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }){
            PlayButton(Options.SCISSORS, isEnable ) {
                onClick(Options.SCISSORS)
            }
        }
    }
}

@Composable
fun Title() {
    Text(
        stringResource(R.string.options_layout_title),
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun PlayButton(text: String, buttonState: Boolean, onClick: () -> Unit) {
    FilledIconButton(
        onClick = { onClick() },
        enabled = buttonState,
        modifier = Modifier.size(136.dp)
    ) {
        Icon(
            painter = when(text){
                Options.ROCK -> painterResource(id = R.drawable.rock)
                Options.PAPER -> painterResource(id = R.drawable.paper)
                else -> painterResource(id = R.drawable.scissors)
            },
            contentDescription = text
        )
    }
}