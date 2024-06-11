package com.example.quiz

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.quiz.data.AppDatabase
import com.example.quiz.data.DatabaseProvider
import com.example.quiz.data.Leaderboard
import com.example.quiz.ui.theme.QuizTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionActivity : ComponentActivity() {
    private lateinit var db: AppDatabase
    private var id_quiz: Int = 0
    private lateinit var userName: String
    private var listaquestion: MutableList<Pergunta> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        id_quiz = intent.getIntExtra("id_quiz", 0)
        userName = intent.getStringExtra("user_name").toString()
        lifecycleScope.launch(Dispatchers.IO) {
            db = DatabaseProvider.getDatabase(this@QuestionActivity)
            val lista = db.questionDAO().findById(id_quiz.toLong())
            for (pergunta in lista){
                val values = Pergunta(pergunta.id,pergunta.id_quiz,pergunta.img,pergunta.pergunta,pergunta.resposta_certa, mutableListOf())
                pergunta.resposta_certa.let { values.answers.add(it) }
                pergunta.resposta_errada?.let { values.answers.add(it) }
                for (it in db.questionDAO().allNames()) {
                    if (it.resposta_certa != pergunta.resposta_certa && it.resposta_errada != pergunta.resposta_errada && it.resposta_errada !in values.answers) {
                        values.answers.add(it.resposta_errada!!)
                    }
                    if (values.answers.size >= 4) {
                        break
                    }
                }

                values.answers.shuffle()
                listaquestion.add(values)
        }


        }
        listaquestion.shuffle()
        setContent {
            QuizTheme {
                // Remember the current question index
                val currentQuestionIndex = remember { mutableIntStateOf(0) }
                // Remember whether the user's answer is correct
                val isAnswerCorrect = remember { mutableStateOf(false) }
                // Remember whether the user has answered the question
                val hasAnswered = remember { mutableStateOf(false) }

                var points by remember { mutableIntStateOf(0) }

                // Display the current question
                if (!hasAnswered.value) {
                    Pergunta(msg = listaquestion[currentQuestionIndex.intValue], onAnswerClicked = { isCorrect ->
                        // Update the isAnswerCorrect and hasAnswered states
                        isAnswerCorrect.value = isCorrect
                        hasAnswered.value = true
                        if (isCorrect){
                            points += 1
                        }
                    })
                } else {
                    // Display Acertou or Errou based on the user's answer
                    if (isAnswerCorrect.value) {
                        Acertou(onContinueClicked = {
                            navigateToNextOrLeaderboard(currentQuestionIndex, hasAnswered, points)
                        })
                    } else {
                        Errou(onContinueClicked = {
                            navigateToNextOrLeaderboard(currentQuestionIndex, hasAnswered, points)
                        })
                    }
                }
            }
        }
    }
    private fun navigateToNextOrLeaderboard(currentQuestionIndex: MutableIntState, hasAnswered: MutableState<Boolean>, points: Int) {
        if (currentQuestionIndex.intValue + 1 >= listaquestion.size) {
            lifecycleScope.launch(Dispatchers.IO) {
                db.leaderboardDAO().inserirRegistro(Leaderboard(0, id_quiz, userName, points))
                val leaders: MutableList<Leaderboard> = db.leaderboardDAO().allNames().toMutableList()
                leaders.sortByDescending { it.pontuacao }
                withContext(Dispatchers.Main) {
                    setContent {
                        QuizTheme {
                            Leaderboard(
                                leaderboard = leaders
                            )
                        }
                    }
                }
            }
        } else {
            // Go to the next question
            currentQuestionIndex.intValue = (currentQuestionIndex.intValue + 1) % listaquestion.size
            hasAnswered.value = false
        }
    }
}




data class Pergunta(
    val id: Int,
    val id_quiz: Int,
    val img: Int,
    val pergunta: String,
    val resposta_certa: String,
    val answers: MutableList<String>
)


@Composable
fun Pergunta(msg: Pergunta,modifier: Modifier = Modifier ,onAnswerClicked: (Boolean) -> Unit) {
    Column(modifier = modifier.fillMaxSize().padding(4.dp)) {
        Text(text = msg.pergunta,modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp))
        Spacer(modifier = Modifier
            .height(4.dp))
        DisplayImage(msg.img,modifier = Modifier
            .fillMaxSize()
            .weight(1f))
        Spacer(modifier = Modifier.height(2.dp))
        LazyVerticalGrid(
            GridCells.Fixed(1),
            modifier = Modifier
                .padding(4.dp)
                .weight(1f)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center){
            items(msg.answers.size) { index ->
                Button(onClick = {
                    // Pass whether the user's answer is correct to the onAnswerClicked function
                    onAnswerClicked(msg.answers[index] == msg.resposta_certa)
                },modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                    contentPadding = PaddingValues(0.dp))
                {
                    Text(text = msg.answers[index],modifier = Modifier.padding(16.dp), fontSize = 32.sp)
                }

            }
        }

    }
}

@Composable
fun Acertou(onContinueClicked: () -> Unit,modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize()
        .background(Color.Green),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Acertou",style = MaterialTheme.typography.titleLarge, fontSize = 40.sp,modifier = Modifier.padding(16.dp))
        Spacer(modifier =Modifier.height(20.dp))
        Button(onClick = { onContinueClicked() }) {
            Text(text = "proxima pergunta")
        }
    }
}

@Composable
fun Errou(onContinueClicked: () -> Unit,modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize()
        .background(Color.Red),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Errou",modifier = Modifier
            .padding(16.dp), style = MaterialTheme.typography.titleLarge,fontSize = 40.sp)
        Spacer(modifier =Modifier.height(20.dp))
        Button(onClick = { onContinueClicked() }) {
            Text(text = "proxima pergunta")
        }
    }
}

@Composable
fun Leaderboard(leaderboard: List<Leaderboard>, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val returnIntent = Intent(context, MainActivity::class.java)
    Column(modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Leaderboard", modifier = Modifier
            .padding(16.dp), style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier
            .weight(2f)
            .padding(16.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .then(
                Modifier
                    .background(color = Color.White)
            )) {
            for (entry in leaderboard) {
                Text("${entry.nome}: ${entry.pontuacao}", modifier = Modifier
                    .padding(4.dp).fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(10.dp)), fontSize = 20.sp,textAlign = TextAlign.Center)
            }
        }
        ElevatedButton(onClick = { context.startActivity(returnIntent) },modifier = Modifier
            .padding(16.dp)
            .weight(1f)) {
            Text(text = "Voltar ao menu principal")
        }
    }
}

@Composable
fun DisplayImage(imageid: Int , modifier: Modifier = Modifier) {
    val image = painterResource(id = imageid)
    Image(painter = image, contentDescription = "My Image", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun PerguntaPreview() {
    QuizTheme {
        Pergunta(msg = Pergunta(
            id = 1,
            id_quiz = 1,
            img = R.drawable.ic_launcher_background,
            pergunta = "What is the capital of France?",
            resposta_certa = "Paris",
            answers = mutableListOf("Paris", "London", "Berlin", "Madrid")
        ), onAnswerClicked = {})
    }
}
@Preview(showBackground = true)
@Composable
fun AcertouPreview() {
    QuizTheme {
        Acertou(onContinueClicked = {})
    }
}
@Preview(showBackground = true)
@Composable
fun ErrouPreview() {
    QuizTheme {
        Errou(onContinueClicked = {})
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderboardPreview() {
    QuizTheme {
        Leaderboard(
            leaderboard = listOf(
                Leaderboard(1,1 ,"Alice" , 10),
                Leaderboard(2,1 ,"Bob", 8),
                Leaderboard(3,1 ,"Charlie", 6)
            )
        )
    }
}

