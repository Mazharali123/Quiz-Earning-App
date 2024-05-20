package com.example.quizapplication.Fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quizapplication.R
import com.example.quizapplication.databinding.FragmentProfileBinding
import com.example.quizapplication.model.user
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database


class ProfileFragment : Fragment() {

   val binding by lazy {
       FragmentProfileBinding.inflate(layoutInflater)
   }

    var isExpand =true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         binding.imageButton.setOnClickListener {

             if (isExpand){

                 binding.constraintLayoutProfile.visibility =View.VISIBLE
                 binding.imageButton.setImageResource(R.drawable.arrowup)
             }else{

                 binding.constraintLayoutProfile.visibility = View.GONE
                 binding.imageButton.setImageResource(R.drawable.downarrow)

             }
             isExpand =!isExpand
         }

        Firebase.database.reference.child("Users").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(
               object : ValueEventListener{
                   override fun onDataChange(snapshot: DataSnapshot) {

                       var user_snap = snapshot.getValue<user>()

                       binding.nameUp.text = user_snap?.name
                       binding.name.text = user_snap?.name
                       binding.password.text = user_snap?.password
                       binding.email.text = user_snap?.email
                       binding.age.text = user_snap?.age.toString()


                   }

                   override fun onCancelled(error: DatabaseError) {
                       TODO("Not yet implemented")
                   }

               }
            )

        return binding.root
    }

    companion object {
    }
}