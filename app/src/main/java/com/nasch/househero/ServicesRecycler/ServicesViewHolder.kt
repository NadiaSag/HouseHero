package com.nasch.househero.ServicesRecycler

import android.view.View
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.nasch.househero.R
import com.nasch.househero.SealedClasses.Services

class ServicesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvService: TextView = view.findViewById(R.id.tvService)

    fun render(gameCategory: Services, onItemSelected: (Int)->Unit){ //si hubiera hecho alt e intro lo último se pone solo

        itemView.setOnClickListener { onItemSelected(layoutPosition) }


        when(gameCategory){
            Services.Pintura -> {
                tvService.text = "Pintura"

            }

            Services.Limpieza -> {
                tvService.text = "Limpieza"
            }

            Services.Ascensores-> {
                tvService.text = "Ascensores"
            }

            Services.Carpinteria -> { tvService.text = "Carpinteria"
            }

            Services.Cerrajeria -> {
                tvService.text = "Cerrajeria"
            }
            Services.Climatizacion -> {
                tvService.text = "Climatizacion"
            }
            Services.Construccion -> {
                tvService.text = "Construccion"
            }
            Services.Electricidad -> {
                tvService.text = "Electricidad"
            }
            Services.Fontaneria -> {
                tvService.text = "Fontaneria"

            }
            Services.Jardineria -> {
                tvService.text = "Jardineria"
            }
            Services.PequeObras -> {
                tvService.text = "Pequeñas obras"
            }
            Services.Piscinas -> {
                tvService.text = "Piscinas"
            }
            Services.Cristaleria -> {
                tvService.text = "Cristaleria"
            }

        }
    }
}
