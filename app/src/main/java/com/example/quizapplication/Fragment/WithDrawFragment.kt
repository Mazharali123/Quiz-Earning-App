package com.example.quizapplication.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.quizapplication.databinding.FragmentWithDrawBinding
import com.example.quizapplication.model.historyModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class WithDrawFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentWithDrawBinding
    var currentCoin = 0L


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWithDrawBinding.inflate(inflater, container, false)

        Firebase.database.reference.child("playerCoin").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            currentCoin = snapshot.getValue() as Long
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )
        binding.transfer.setOnClickListener {
            if (binding.amount.text.toString().toDouble() <= currentCoin) {

                Firebase.database.reference.child("playerCoin")
                    .child(Firebase.auth.currentUser!!.uid)
                    .setValue(currentCoin - binding.amount.text.toString().toDouble())


                var historyModelClass = historyModel(
                    binding.amount.text.toString(),
                    System.currentTimeMillis().toString(),
                    true
                )
                Firebase.database.reference.child("playerCoinHistory")
                    .child(Firebase.auth.currentUser!!.uid).push().setValue(historyModelClass)


            } else {
                Toast.makeText(activity, "out of money", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    companion object {
    }
}
