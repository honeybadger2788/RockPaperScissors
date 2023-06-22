package com.girlify.rockpaperscissors.ui.composables

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            PlayButton(Options.ROCK ) {
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
            PlayButton(Options.PAPER ) {
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
            PlayButton(Options.SCISSORS ) {
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
fun PlayButton(text: String, onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
        modifier = Modifier.size(148.dp),
        elevation = FloatingActionButtonDefaults.elevation(16.dp),
        containerColor = MaterialTheme.colorScheme.primary,
        shape = CircleShape
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