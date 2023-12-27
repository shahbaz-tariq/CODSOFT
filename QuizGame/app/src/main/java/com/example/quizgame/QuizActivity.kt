package com.example.quizgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quizgame.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private var currentQuestionIndex = 0
    private var score = 0
    private lateinit var category : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        category = intent.getStringExtra("CATEGORY") ?: "GK"

        val questions = when (category) {
            "GK" -> QuestionsData().GkQuestions
            "CS" -> QuestionsData().CSQuestions
            "Programming" -> QuestionsData().ProgrammingQuestions
            else -> throw IllegalArgumentException("Invalid category: $category")
        }

        displayQuestion(questions)

        binding.btnSubmit.setOnClickListener {
            if (binding.btnSubmit.text == "Play Again") {
                playAgain()
            } else {
                submitAnswer(questions)
            }
        }
    }

    private fun displayQuestion(questions: List<Question>) {

            val currentQuestion = questions[currentQuestionIndex].options
            binding.tvQuestion.text = questions[currentQuestionIndex].question
            binding.rb1.text = currentQuestion[0]
            binding.rb2.text = currentQuestion[1]
            binding.rb3.text = currentQuestion[2]
            binding.rb4.text = currentQuestion[3]

            binding.rgMain.clearCheck()
            binding.tvFeedback.text = ""
    }

    private fun submitAnswer(questions: List<Question>) {

        val selectedOption = binding.rgMain.checkedRadioButtonId

        if (selectedOption == -1) {
            binding.tvFeedback.text = "Please select an option."
            return
        }

        val selectedOptionIndex = binding.rgMain.indexOfChild(findViewById(selectedOption))
        val correctAnswer = questions[currentQuestionIndex].correctAnswerIndex

        if (selectedOptionIndex == correctAnswer) {
            binding.tvFeedback.text = "Correct!"
            score++
        } else {
            binding.tvFeedback.text =
                "Incorrect. Correct Answer is: ${questions[currentQuestionIndex].options[correctAnswer]}"
        }

        if (currentQuestionIndex == questions.lastIndex) {
            binding.tvFeedback.text =
                "Quiz Completed! Your score is $score out of ${questions.size}"
            binding.btnSubmit.text = "Play Again"
        } else {
            currentQuestionIndex++
            displayQuestion(questions)
        }
    }

    private fun playAgain() {
        currentQuestionIndex = 0
        score = 0
        binding.btnSubmit.text = "Submit"

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}