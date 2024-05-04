package com.nasch.househero

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.nasch.househero.SealedClasses.Services
import com.nasch.househero.ServicesRecycler.ServicesAdapter
import com.nasch.househero.databinding.ActivityProfessionalBinding
import com.nasch.househero.dataclasses.Profesionales

class ProfessionalActivity : AppCompatActivity() {
    private lateinit var servicesAdapter: ServicesAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var binding: ActivityProfessionalBinding
    private val selectedServices: MutableSet<String> = mutableSetOf()

    private lateinit var profesionalesRef: DatabaseReference // Cambio aquí


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfessionalBinding.inflate(layoutInflater)
        setContentView(binding.root)



        database = FirebaseDatabase.getInstance() // Aquí inicializamos la instancia de FirebaseDatabase
        profesionalesRef = database.getReference("Profesionales") // Inicializa profesionalesRef después de database
        setupCheckBoxListeners()

      //  initcomponents()
        //initUI()
        binding.btSave.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            //saveDataToFirebase(userName, userSurname, selectedRole)
            Toast.makeText(
                baseContext,
                "User information saved.",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
    private val services = mutableListOf(
        Services.Fontaneria,
        Services.Cristaleria,
        Services.Piscinas,
        Services.PequeObras,
        Services.Jardineria,
        Services.Electricidad,
        Services.Construccion,
        Services.Climatizacion,
        Services.Ascensores,
        Services.Cerrajeria,
        Services.Limpieza,
        Services.Carpinteria,
        Services.Pintura)

    private fun onCategoriesSelected(position: Int) {
        // Cambia el estado de la selección del servicio en la posición dada
        services[position].isSelected = !services[position].isSelected

        // Notifica al adaptador sobre el cambio en la selección
        servicesAdapter.notifyItemChanged(position)
    }
    private fun setupCheckBoxListeners() {
        val userName = intent.getStringExtra("userName")
        val userSurname = intent.getStringExtra("userSurname")
        val selectedRole = intent.getStringExtra("selectedRole")
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

                // Guardar los servicios seleccionados en Firebase
                saveDataToFirebase(userName, userSurname,selectedRole, selectedServices)
            }
        }
    }

    private fun saveDataToFirebase(
        username: String?,
        userSurname: String?,
        selectedRole: String?,
        selectedServices: MutableSet<String>
    ) {
        // Obtiene una referencia al nodo del usuario actual
        val userId = getUserId() ?: return
        val userRef = profesionalesRef.child(userId)

        // Crea un objeto Profesionales con los datos del usuario y la lista de servicios seleccionados
        val profesional = Profesionales(
            userId,
            username,
            userSurname,
            selectedRole,
            selectedServices.toList() // Convierte el conjunto de servicios a una lista
        )

        // Guarda el objeto Profesionales en Firebase
        userRef.setValue(profesional)
    }

    private fun getUserId(): String? {

        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid
    }
}


