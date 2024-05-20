package com.example.quizapplication.Fragment

import android.os.Bundle
import android.text.method.TextKeyListener.clear
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quizapplication.R
import com.example.quizapplication.adapter.categoryAdapter
import com.example.quizapplication.databinding.FragmentHomeBinding
import com.example.quizapplication.model.categoryImage
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


class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    private var getArrayList = ArrayList<categoryImage>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        getArrayList.clear()
        getArrayList.add(categoryImage(R.drawable.scince1,"science"))
        getArrayList.add(categoryImage(R.drawable.english1,"english"))
        getArrayList.add(categoryImage(R.drawable.geography,"science"))
        getArrayList.add(categoryImage(R.drawable.mathmetic,"math"))


        binding.coinText.setOnClickListener {
            val bottomSheetDialog : BottomSheetDialogFragment = WithDrawFragment()
            bottomSheetDialog.show(requireActivity().supportFragmentManager,"TEST")
            //bottomSheetDialog.enterTransition
        }


        binding.coinImage.setOnClickListener {
            val bottomSheetDialog : BottomSheetDialogFragment = WithDrawFragment()
            bottomSheetDialog.show(requireActivity().supportFragmentManager,"TEST")
           // bottomSheetDialog.enterTransition
        }

        Firebase.database.reference.child("playerCoin").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(
                object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (snapshot.exists()){
                           var currentCoins = snapshot.getValue() as Long
                            binding.coinText.text = currentCoins.toString()

                        }
                    }
                    override fun onCancelled(error: DatabaseError) {

                    }
                }
            )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.categoryRecyclerView.layoutManager = GridLayoutManager(requireContext() ,2)
        var adapterObj = categoryAdapter(getArrayList,requireActivity())
        binding.categoryRecyclerView.adapter = adapterObj
        binding.categoryRecyclerView.hasFixedSize()

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
                })

    }

    companion object{

    }
}
