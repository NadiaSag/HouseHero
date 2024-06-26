package com.nasch.househero

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import com.nasch.househero.databinding.ActivityCreateProfileBinding
import com.nasch.househero.dataclasses.Clientes
import com.nasch.househero.dataclasses.Profesionales

class CreateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateProfileBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa Firebase

        val rolesArray = resources.getStringArray(R.array.roles)
        val rolesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, rolesArray)
        rolesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRoles.adapter = rolesAdapter

        binding.btNext.setOnClickListener{
            // Obtiene los valores del EditText y el Spinner
            val userName = binding.etUserName.text.toString()
            val userSurname = binding.etUserSurname.text.toString()
            val selectedRole = binding.spinnerRoles.selectedItem.toString()
            val email = binding.etEmail.text.toString()
            val phoneNumber = binding.etNumber.text.toString()

            // Guarda los valores en la base de datos Firebase
            saveUserInfo(userName, userSurname, selectedRole, email, phoneNumber)
        }
    }

    private fun saveUserInfo(
        userName: String,
        userSurname: String,
        selectedRole: String,
        email: String,
        phoneNumber: String
    ) {
        if (userName.isEmpty()){Toast.makeText(
            baseContext,
            "Escribe nombre.",
            Toast.LENGTH_SHORT
        ).show()}

        if (userSurname.isEmpty()){Toast.makeText(
            baseContext,
            "Escribe apelllido.",
            Toast.LENGTH_SHORT
        ).show()}

        if (selectedRole.isEmpty()){
            Toast.makeText(
            baseContext,
            "Escribe rol.",
            Toast.LENGTH_SHORT
        ).show()
        }
        if (email.isEmpty()){
            Toast.makeText(
                baseContext,
                "Escribe un email de contacto.",
                Toast.LENGTH_SHORT
            ).show()
        }
        if(selectedRole.equals("Soy profesional")){
            dbRef = FirebaseDatabase.getInstance().getReference("Profesionales")
            val userId = dbRef.push().key!!
            val user = Profesionales(userId, userName, userSurname, selectedRole, email, phoneNumber)
            dbRef.child(userId).setValue(user).addOnCompleteListener{
                Toast.makeText(
                    baseContext,
                    "User information saved.",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this, ProfessionalActivity::class.java)
                intent.putExtra("userName", userName)
                intent.putExtra("userSurname", userSurname)
                intent.putExtra("selectedRole", selectedRole)
                intent.putExtra("email", email)
                intent.putExtra("phoneNumber", phoneNumber)
                startActivity(intent)

            }.addOnFailureListener{ err ->
                Toast.makeText(
                    baseContext,
                    "An error has occured.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }else {
            dbRef = FirebaseDatabase.getInstance().getReference("Clientes")

            val userId = dbRef.push().key!!

            val user = Clientes(userId, userName, userSurname, selectedRole)
            dbRef.child(userId).setValue(user)
                .addOnCompleteListener {
                    Toast.makeText(
                        baseContext,
                        "User information saved.",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                }.addOnFailureListener { err ->
                    Toast.makeText(
                        baseContext,
                        "An error has occured.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
        }
    }

}


