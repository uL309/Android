package com.example.quiz

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.quiz.data.AppDatabase
import com.example.quiz.data.DatabaseProvider
import com.example.quiz.data.Leaderboard
import com.example.quiz.data.Quiz
import com.example.quiz.data.QuizDAO
import com.example.quiz.ui.theme.QuizTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        lifecycleScope.launch(Dispatchers.IO) {
            db = DatabaseProvider.getDatabase(this@MainActivity)
        }
        val intent = Intent(this, QuestionActivity::class.java)
        setContent {
            QuizTheme {
                MyApp(intent,db)
            }
        }
    }
}




@Composable
fun MyApp(intent: Intent,db: AppDatabase,modifier: Modifier = Modifier) {

    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }
    var userName by rememberSaveable { mutableStateOf("") }

    Surface(modifier) {
        if (shouldShowOnboarding) {
            OnboardingScreen(onContinueClicked = { inputText ->
                userName = inputText
                shouldShowOnboarding = false
            })
        } else {
            Questions(intent,db,userName)
        }
    }
}

@Composable
fun OnboardingScreen(
    onContinueClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by rememberSaveable { mutableStateOf("") }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bem Vindo ao App Quiz!", fontSize = 30.sp, modifier = Modifier.padding(16.dp))
        Spacer(modifier = Modifier.height(24.dp))
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Entre com seu nome") },
            modifier = Modifier.padding(vertical = 24.dp)
        )
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = { onContinueClicked(inputText) }
        ) {
            Text("Continuar")
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Questions(intent: Intent, db: AppDatabase, userName: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var quizes by remember { mutableStateOf(listOf<Quiz>()) }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var leaders by remember { mutableStateOf(listOf<Leaderboard>()) }
    val extraPadding by animateDpAsState(
        if (expanded) 24.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = ""
    )
    val coroutineScope = rememberCoroutineScope()
    coroutineScope.launch {
        quizes = withContext(Dispatchers.IO) { db.quizDAO().allNames() }
        leaders = withContext(Dispatchers.IO) { db.leaderboardDAO().allNames().toMutableList() }
    }
    Surface {
        Spacer(modifier = modifier.height(16.dp))
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (quiz in quizes) {
                Column(modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .background(Color.Gray, RoundedCornerShape(20.dp))
                            .clickable { expanded = !expanded }
                            .clipToBounds()
                            .padding(if (extraPadding < 0.dp) 0.dp else extraPadding)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = quiz.nome, modifier = Modifier
                                .padding(16.dp)
                                .weight(1f)
                        )
                        Button(onClick = {
                            intent.putExtra("id_quiz", quiz.id)
                            intent.putExtra("user_name", userName)
                            context.startActivity(intent)
                        }, modifier = Modifier.padding(4.dp)) {
                            Text("Start Quiz")
                        }
                    }
                    if (expanded) { // Add this condition
                        leaderboard(leaders) // Add the leaderboard composable here
                    }
                }
            }
        }
    }

}
@Composable
fun leaderboard(leaderboard: List<Leaderboard>){
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.DarkGray, RoundedCornerShape(20.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(modifier =Modifier.padding(8.dp), text = "Leaderboard")
        for (entry in leaderboard) {
            Text("${entry.nome}: ${entry.pontuacao}", modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(10.dp)), fontSize = 20.sp,textAlign = TextAlign.Center)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPreview() {
    QuizTheme {
        OnboardingScreen(onContinueClicked = {})
    }
}

@Preview
@Composable
fun MyAppPreview() {
    QuizTheme {
        MyApp(intent = Intent(),db = DatabaseProvider.getDatabase(LocalContext.current),Modifier.fillMaxSize())
    }
}

@Preview(showBackground = true)
@Composable
fun QuestionsPreview() {
    QuizTheme {
        Questions(intent = Intent(),db = DatabaseProvider.getDatabase(LocalContext.current),userName = "Teste",Modifier.fillMaxSize())
    }
}

@Preview(showBackground = true)
@Composable
fun leaderboardPreview() {
    QuizTheme {
        leaderboard(listOf(Leaderboard(0,0,"lucas",100),Leaderboard(0,0,"lucas",10)))
    }
}