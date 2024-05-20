package com.example.quizapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.quizapplication.Fragment.WithDrawFragment
import com.example.quizapplication.model.Question
import com.example.quizapplication.model.user
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class QuizActivity : AppCompatActivity() {

    private val binding by lazy {
        com.example.quizapplication.databinding.ActivityQuizBinding.inflate(layoutInflater)
    }

    private lateinit var questionList: ArrayList<Question>
    private var currentQuestion = 0
    private var score = 0
    var currentChance: Long? = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        /* Setting number of coin to history coin text  */
        Firebase.database.reference.child("playerCoin").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (snapshot.exists()) {
                            var currentCoins = snapshot.getValue() as Long
                            binding.historyCoinText.text = currentCoins.toString()

                        }

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                }
            )
        /* End Setting number of coin to history coin text  */


        /* Setting number of chance to chance left  */
        Firebase.auth.currentUser?.let {
            Firebase.database.reference.child("PlayChance").child(it.uid)
        }?.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        currentChance = snapshot.value as Long
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )
        /* End Setting number of chance to chance left  */



        /* Setting images sending to quiz  */

        questionList = ArrayList<Question>()

        var image = intent.getIntExtra("category_image", 0)
        var catText = intent.getStringExtra("questionType")
        binding.categoryImage.setImageResource(image)

        /* End Setting images sending to quiz  */


        /* Creating node in cloud fire store and collection for question  */
        Firebase.firestore.collection("Questions")
            .document(catText.toString())
            .collection("question1").get().addOnSuccessListener { questionData ->

                questionList.clear()
                for (data in questionData.documents) {
                    var question: Question? = data.toObject(Question::class.java)
                    questionList.add(question!!)
                }

                if (questionList.size > 0) {

                    binding.questionDes.text = questionList.get(currentQuestion).question
                    binding.option1.text = questionList.get(currentQuestion).option1
                    binding.option2.text = questionList.get(currentQuestion).option2
                    binding.option3.text = questionList.get(currentQuestion).option3
                    binding.option4.text = questionList.get(currentQuestion).option4
                }

            }
        /*End Creating node in cloud fire store and collection for question  */


        /* image to open withdrawal  from down*/
        binding.historyCoin.setOnClickListener {
            val bottomSheetDialog: BottomSheetDialogFragment = WithDrawFragment()
            bottomSheetDialog.show(this@QuizActivity.supportFragmentManager, "TEST")
        }
        /*End Button to open withdrawal  from down*/

        /* textview to open withdrawal  from down*/
        binding.historyCoinText.setOnClickListener {
            val bottomSheetDialogFragment: BottomSheetDialogFragment = WithDrawFragment()
            bottomSheetDialogFragment.show(this@QuizActivity.supportFragmentManager, "TEST")
        }
        /*End textview to open withdrawal  from down*/

        /*Fetching name from database and setting it to user name*/
        Firebase.database.reference.child("Users").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val snap_data = snapshot.getValue<user>()

                        binding.hName.text = snap_data?.name
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }

            )
        /*End Fetching name from database and setting it to user name*/


        /*Click listener on option of quiz */
        binding.option1.setOnClickListener {
            nextQuestionAndUpdateScore(binding.option1.text.toString())
        }

        binding.option2.setOnClickListener {
            nextQuestionAndUpdateScore(binding.option2.text.toString())
        }

        binding.option3.setOnClickListener {
            nextQuestionAndUpdateScore(binding.option3.text.toString())
        }

        binding.option4.setOnClickListener {
            nextQuestionAndUpdateScore(binding.option4.text.toString())
        }
        /*End Click listener on option of quiz */

    }

    /*function checking each question answer */
    private fun nextQuestionAndUpdateScore(s: String) {

        if (s.equals(questionList.get(currentQuestion).ans)) {
            score += 10
            Toast.makeText(this, score.toString(), Toast.LENGTH_SHORT).show()
        }

        currentQuestion++

        if (currentQuestion >= questionList.size) {

            val totalPossibleScore = questionList.size * 10
            val passingScore = totalPossibleScore * 0.8  // Example: 80% passing score

            if (score >= passingScore) {

                Firebase.database.reference.child("PlayChance")
                    .child(Firebase.auth.currentUser!!.uid).setValue(
                        currentChance?.plus(1)
                    )

                binding.winner.visibility = View.VISIBLE

            } else {
                binding.sorry.visibility = View.VISIBLE
            }

        } else {

            binding.questionDes.text = questionList.get(currentQuestion).question
            binding.option1.text = questionList.get(currentQuestion).option1
            binding.option2.text = questionList.get(currentQuestion).option2
            binding.option3.text = questionList.get(currentQuestion).option3
            binding.option4.text = questionList.get(currentQuestion).option4

        }
    }
}