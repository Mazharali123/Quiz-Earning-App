package com.example.quizapplication.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizapplication.adapter.HistoryAdapter
import com.example.quizapplication.databinding.FragmentHistoryBinding
import com.example.quizapplication.model.historyModel
import com.example.quizapplication.model.user
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import java.util.Collections


class HistoryFragment : Fragment() {

    val binding by lazy {
        FragmentHistoryBinding.inflate(layoutInflater)
    }

    lateinit var adapter :HistoryAdapter
    private val listHistory = ArrayList<historyModel>()
    private val listHistory1 = ArrayList<historyModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Firebase.database.reference.child("playerCoinHistory").child(Firebase.auth.currentUser!!.uid).addValueEventListener(
            object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        listHistory1.clear()
                        for (datasnapshot in snapshot.children){
                            var data = datasnapshot.getValue(historyModel::class.java)
                            Log.d("MyTag" , datasnapshot.toString())
                            listHistory.add(data!!)
                        }
                        Collections.reverse(listHistory)
                        Collections.addAll(listHistory)
                        adapter.notifyDataSetChanged()


                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding.historyCoin.setOnClickListener {
            val bottomSheetDialog: BottomSheetDialogFragment = WithDrawFragment()
            bottomSheetDialog.show(requireActivity().supportFragmentManager, "TEST")
        }

        binding.historyCoinText.setOnClickListener {
            val bottomSheetDialogFragment: BottomSheetDialogFragment = WithDrawFragment()
            bottomSheetDialogFragment.show(requireActivity().supportFragmentManager, "TEST")
        }


        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = HistoryAdapter(listHistory)
        binding.historyRecyclerView.adapter = adapter
        binding.historyRecyclerView.hasFixedSize()

        Firebase.database.reference.child("Users").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var snap_data = snapshot.getValue<user>()

                        binding.hName.text = snap_data?.name
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )

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


        return binding.root
    }

    companion object {
    }
}