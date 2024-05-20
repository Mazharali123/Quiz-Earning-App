package com.example.quizapplication.Fragment


import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quizapplication.databinding.FragmentSpinBinding
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
import java.util.Random


class SpinFragment : Fragment() {

    private var _binding: FragmentSpinBinding? = null
    private val binding get() = _binding!!
    private val itemTitles = arrayOf("100", "Try Again", "500", "Try Again", "200", "Try Again")
    private lateinit var timer: CountDownTimer
    var currentChance = 0L
    var currentCoins = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSpinBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.historyCoin.setOnClickListener {
            var bottomSheetDialogFragment: BottomSheetDialogFragment = WithDrawFragment()
            bottomSheetDialogFragment.show(requireActivity().supportFragmentManager, "TEST")
        }

        binding.historyCoinText.setOnClickListener {
            var bottomSheetDialogFragment: BottomSheetDialogFragment = WithDrawFragment()
            bottomSheetDialogFragment.show(requireActivity().supportFragmentManager, "TEST")
        }

        Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(
                object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (snapshot.exists()){
                            currentChance = snapshot.getValue() as Long
                            binding.chanceLeft.text = (snapshot.getValue() as Long).toString()

                        }else{

                            var temp = 0
                            binding.chanceLeft.text = temp.toString()
                            binding.spinBnt.isEnabled = false
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                }
            )

        Firebase.database.reference.child("playerCoin").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(
                object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (snapshot.exists()){
                            currentCoins = snapshot.getValue() as Long
                            binding.historyCoinText.text = currentCoins.toString()

                        }

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                }
            )

        return view
    }

    private fun showResult(itemTitles: String , spin :Int) {
        if(spin%2==0){

            var winCoins = itemTitles.toInt()
            Firebase.database.reference.child("playerCoin").child(Firebase.auth.currentUser!!.uid).setValue(winCoins+currentCoins)
            binding.historyCoinText.text = (currentCoins+winCoins).toString()


            var historyModelClass =
                historyModel(winCoins.toString(), System.currentTimeMillis().toString(),false)
            Firebase.database.reference.child("playerCoinHistory")
                .child(Firebase.auth.currentUser!!.uid)
                .push()
                .setValue(historyModelClass)


        }
        Toast.makeText(requireContext(), itemTitles, Toast.LENGTH_SHORT).show()

        currentChance = currentChance -1
        Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid).setValue(currentChance)
        binding.spinBnt.isEnabled = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spinBnt.setOnClickListener {
            binding.spinBnt.isEnabled = false //disable button while wheel is running

           if (currentChance > 0){

               val spin = Random().nextInt(6)  //generate random value b/w 0 and 5
               val degrees = 60f * spin   // calculate the rotation degrees based in random value

               timer = object : CountDownTimer(5000, 50) {
                   var rotation = 0f
                   override fun onTick(p0: Long) {
                       rotation += 5f //rotate wheel
                       if (rotation >= degrees) {
                           rotation = degrees
                           timer.cancel()
                           showResult(itemTitles[spin] , spin)
                       }
                       binding.wheel.rotation = rotation
                   }

                   override fun onFinish() {
                       TODO("Not yet implemented")
                   }
               }.start()
           }else{
               Toast.makeText(activity, "out of spin chance", Toast.LENGTH_SHORT).show()
           }
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
