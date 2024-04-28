package com.nasch.househero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nasch.househero.SealedClasses.Services
import com.nasch.househero.ServicesRecycler.ServicesAdapter

class ProfileActivity : AppCompatActivity() {
    private lateinit var servicesAdapter: ServicesAdapter
    private lateinit var rvServices: RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initcomponents()
        initUI()

    }
    private fun initcomponents() {
        rvServices = findViewById<RecyclerView>(R.id.rvServices)


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

    private fun onCategoriesSelected(position: Int){
        services[position].isSelected = !services[position].isSelected
        servicesAdapter.notifyItemChanged(position)
    }
    private fun initUI(){

        servicesAdapter = ServicesAdapter(services) {position -> onCategoriesSelected(position)}
        rvServices.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvServices.adapter = servicesAdapter
        //el orden es súper importante
    }
}