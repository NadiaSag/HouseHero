package com.nasch.househero

import ServicesAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nasch.househero.databinding.ActivityResultsBinding

class ResultsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ServicesAdapter // Adaptador personalizado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.rvServices
        adapter = ServicesAdapter()

        // Configurar RecyclerView
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ResultsActivity)
            adapter = this@ResultsActivity.adapter
        }

        // Obtener los resultados de la actividad anterior
        val resultados = intent.getStringArrayListExtra("resultados")
        // Actualizar el adaptador con los resultados
        if (resultados != null) {
            adapter.actualizarResultados(resultados)
        }
    }
}
