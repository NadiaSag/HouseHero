package com.nasch.househero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nasch.househero.databinding.ActivitySearchServiceBinding

class SearchServiceActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySearchServiceBinding
    private val selectedServices: MutableSet<String> = mutableSetOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupCheckBoxListeners()

        binding.btSave.setOnClickListener {
            buscarUsuarios()
            Toast.makeText(
                baseContext,
                "User information saved.",
                Toast.LENGTH_SHORT
            ).show()
        }
        val userId = getUserId() // Obtener el ID del usuario actual
        userId?.let {
            FirebaseDatabase.getInstance().getReference("Clientes").child(
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
        binding.cvInfo.findViewById<TextView>(R.id.tvGreetings).text = "Hola, $userName $userSurname"
    }
    private fun getUserId(): String? {

        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid
    }

    private fun setupCheckBoxListeners() {
        // Obtener referencias a todos los CheckBox
        val checkBoxes = arrayOf(
            binding.cbFontaneria,
            binding.cbCristaleria,
            binding.cbPiscinas,
            binding.cbElectricidad,
            binding.cbJardineria,
            binding.cbConstruccion,
            binding.cbCarpinteria,
            binding.cbClimatizacion,
            binding.cbAscensores,
            binding.cbCerrajeria,
            binding.cbLimpieza,
            binding.cbPintura
        )

        // Configurar el Listener para cada CheckBox
        for (checkBox in checkBoxes) {
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                // Obtener el texto asociado al CheckBox
                val serviceName = checkBox.text.toString()

                // Actualizar selectedServices dependiendo del estado del CheckBox
                if (isChecked) {
                    selectedServices.add(serviceName)
                } else {
                    selectedServices.remove(serviceName)
                }

            }
        }
    }
    private fun buscarUsuarios() {
        // Construir la consulta Firebase
        val query = FirebaseDatabase.getInstance().getReference("Profesionales")

        // Limpiar el layout de resultados de la búsqueda
        // Esto es opcional y depende de cómo quieras mostrar los resultados

        // Crear una lista para almacenar los resultados
        val resultados: MutableList<String> = mutableListOf()

        // Manejar cada servicio seleccionado
        for (serviceName in selectedServices) {
            // Agregar un filtro para cada servicio seleccionado
            query.orderByChild("selectedServices")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        // Manejar los resultados de la búsqueda
                        for (userSnapshot in snapshot.children) {
                            val servicesSnapshot = userSnapshot.child("selectedServices")
                            val userServices = mutableListOf<String>()
                            for (serviceSnapshot in servicesSnapshot.children) {
                                userServices.add(serviceSnapshot.value.toString())
                            }
                            // Verificar si el profesional ofrece el servicio seleccionado por el usuario
                            if (userServices.contains(serviceName)) {
                                // Obtener los datos del usuario
                                val userName = userSnapshot.child("userName").value.toString()
                                val userSurname = userSnapshot.child("userSurname").value.toString()
                                // Agregar el resultado a la lista si no está duplicado
                                val resultado =
                                    "Nombre: $userName, Apellido: $userSurname, Servicio: $serviceName"
                                if (!resultados.contains(resultado)) {
                                    resultados.add(resultado)
                                }
                            }
                        }
                        for (resultado in resultados) {
                            Log.d("SearchServiceActivity", resultado)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Manejar el error
                        Log.e("SearchServiceActivity", "Error al buscar usuarios: ${error.message}")
                    }
                })
        }
    }}
