package com.nasch.househero

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfessionalInfoActivity : AppCompatActivity() {
    private lateinit var tvProfessionalInfo: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhoneNum: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professional_info)

        val resultado = intent.getStringExtra("resultado")
        val professionalName = resultado?.split(",")?.get(0)?.trim()

        tvProfessionalInfo = findViewById(R.id.tvProfessionalInfo)
        tvEmail = findViewById(R.id.tvEmail)
        tvPhoneNum = findViewById(R.id.tvPhoneNum)

        tvProfessionalInfo.text = resultado

        professionalName?.let {
            val nameParts = it.split(" ")
            if (nameParts.size >= 2) {
                val userName = nameParts[0]
                val userSurname = nameParts[1]

                Toast.makeText(this, "$userName $userSurname", Toast.LENGTH_SHORT).show()

                fetchProfessionalDetails(userName, userSurname)
            } else {
                Log.e("ProfessionalInfoActivity", "Invalid professional name format: $professionalName")
            }
        }
    }

    private fun fetchProfessionalDetails(userName: String, userSurname: String) {
        val query = FirebaseDatabase.getInstance().getReference("Profesionales")
            .orderByChild("userName")
            .equalTo(userName)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (professionalSnapshot in snapshot.children) {
                        val fetchedSurname = professionalSnapshot.child("userSurname").value.toString()

                        if (fetchedSurname == userSurname) {
                            val email = professionalSnapshot.child("email").value.toString()
                            val phoneNum = professionalSnapshot.child("phoneNumber").value.toString()

                            tvEmail.text = "Correo electrónico: $email"
                            tvPhoneNum.text = "Teléfono de contacto $phoneNum"
                            break
                        }
                    }
                } else {
                    Log.e("ProfessionalInfoActivity", "No professional found with the name: $userName $userSurname")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ProfessionalInfoActivity", "Error fetching professional details: ${error.message}")
            }
        })
    }
}
