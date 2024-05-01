package com.nasch.househero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
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
    private lateinit var rvServices: RecyclerView
    private lateinit var database: FirebaseDatabase
    private lateinit var binding: ActivityProfessionalBinding
    private lateinit var profesionalesRef: DatabaseReference // Cambio aquí
    private var firstSelectionPosition: Int? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfessionalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("userName")
        val userSurname = intent.getStringExtra("userSurname")
        val selectedRole = intent.getStringExtra("selectedRole")

        database = FirebaseDatabase.getInstance() // Aquí inicializamos la instancia de FirebaseDatabase
        profesionalesRef = database.getReference("Profesionales") // Inicializa profesionalesRef después de database

        initcomponents()
        initUI()
        binding.btSave.setOnClickListener{
            saveDataToFirebase(userName, userSurname, selectedRole)
        }

    }
    private fun initcomponents() {
        rvServices = binding.rvServices


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
        if (firstSelectionPosition == null) {
            // Es la primera selección, asignar a mainService
            services[position].isSelected = true
            firstSelectionPosition = position
        } else {
            if (firstSelectionPosition == position) {
                // Desmarcar la primera selección
                services[firstSelectionPosition!!].isSelected = false
                firstSelectionPosition = null
            } else {
                // Agregar a otherServices
                services[position].isSelected = !services[position].isSelected
            }
        }
        servicesAdapter.notifyItemChanged(position)
    }

    private fun initUI(){

        servicesAdapter = ServicesAdapter(services) {position -> onCategoriesSelected(position)}
        rvServices.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvServices.adapter = servicesAdapter
        //el orden es súper importante
    }

    private fun saveDataToFirebase(username: String?, userSurname: String?, selectedRole: String?) {
        val userId = getUserId() ?: return
        val mainService = if (firstSelectionPosition != null) services[firstSelectionPosition!!].name else null
        val otherServices = services.filterIndexed { index, service -> index != firstSelectionPosition && service.isSelected }.map { it.name }

        val profesional = Profesionales(userId, username, userSurname, selectedRole, mainService,
            otherServices.toString()
        )
        profesionalesRef.child(userId).setValue(profesional)
    }
    private fun getUserId(): String? {

        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid
    }
}
