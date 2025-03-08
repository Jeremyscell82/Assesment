package com.lloydsbyte.redditassesment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.Coil
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.lloydsbyte.redditassesment.database.AppDatabase
import com.lloydsbyte.redditassesment.network.CharacterResponseModel
import com.lloydsbyte.redditassesment.ui.theme.RedditAssesmentTheme
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        enableEdgeToEdge()
        setContent {
            RedditAssesmentTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        innerPadding = innerPadding
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(innerPadding: PaddingValues) {

    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context.applicationContext)
    val viewModel: MainViewModel = viewModel(factory = ViewModelFactory(database))
    var itemSelected by remember {
        mutableStateOf(false)
    }

    val characters by viewModel.getCharactersFlow()
        .collectAsState(initial = emptyList<CharacterResponseModel.CharacterModel>())

    LaunchedEffect(key1 = Unit) {
        viewModel.initApp(context)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(characters) { character ->
                CardForCharacters(character) {
                    Timber.d("JL_ something is off: $character")
                    viewModel.characterSelected = character
                    itemSelected = true
                }

            }
        }
    }


    if (itemSelected && viewModel.characterSelected != null) {
        BottomSheetInfo(viewModel.characterSelected!!) {
            itemSelected = false
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RedditAssesmentTheme {
//        Greeting("Android")
        val characterMock = CharacterResponseModel.CharacterModel(
            dbKey = 0L,
            id = 0,
            name = "Name Here",
            status = "Alive",
            species = "Human",
            imageUrl = ""
        )
//        CardForCharacters() {

//        }

        BottomSheetInfo(characterMock) { }
    }
}

@Composable
fun CardForCharacters(
    character: CharacterResponseModel.CharacterModel,
    onClick: (CharacterResponseModel.CharacterModel) -> Unit
) {

    Card(
        modifier = Modifier
            .padding(12.dp, 4.dp)
            .width(600.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 0.dp
        ),
        onClick = {
            onClick(character)
        }
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f),
            model = character.imageUrl,
            contentDescription = "Image of character: ${character.name}"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetInfo(
    character: CharacterResponseModel.CharacterModel,
    onClick: (Boolean) -> Unit
) {

    val modalBottomSheetState = rememberModalBottomSheetState()
    val haptics = LocalHapticFeedback.current


    ModalBottomSheet(
        modifier = Modifier
            .width(600.dp)
            .wrapContentHeight(),
        onDismissRequest = {
            onClick(false)
        },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        shape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp),
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(.9f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize(.7f)
                        .aspectRatio(1f),
                    model = character.imageUrl,
                    contentDescription = "Image of character: ${character.name}"
                )
                Spacer(modifier = Modifier.width(12.dp))

                Column {

                    Text(
                        text = character.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = character.species,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = character.status,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = .2f)
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = .6f)
                ),
                shape = RoundedCornerShape(12.dp),
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClick(true)
                }
            ) {

                Text(
                    text = "Close",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.End,
                    modifier = Modifier.padding(24.dp, 8.dp)
                )

            }

            Spacer(modifier = Modifier.height(24.dp))

        }
    }
}