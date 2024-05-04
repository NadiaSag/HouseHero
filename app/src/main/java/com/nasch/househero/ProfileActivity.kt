package com.nasch.househero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nasch.househero.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userId = getUserId() // Obtener el ID del usuario actual
        userId?.let {
            FirebaseDatabase.getInstance().getReference("Profesionales").child(
                it
            )
        }?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userName = snapshot.child("userName").value.toString()
                    val userSurname = snapshot.child("userSurname").value.toString()
                    val selectedRole = snapshot.child("selectedRole").value.toString()

                    // Actualizar la interfaz de usuario con los datos del usuario
                    updateUI(userName, userSurname, selectedRole)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el error
            }
        })

    }
    private fun updateUI(userName: String, userSurname: String, selectedRole: String) {
        // Actualizar el TextView dentro del CardView con los datos del usuario
        binding.cvInfo.findViewById<TextView>(R.id.tvUserInfo).text = "Hola, $userName $userSurname. Rol: $selectedRole"

    }
    private fun getUserId(): String? {

        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid
    }
}




