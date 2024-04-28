package com.nasch.househero.SealedClasses

sealed class Services(var isSelected:Boolean = true) {
    object Fontaneria : Services()
    object Jardineria : Services()
    object Piscinas : Services()
    object Construccion : Services()
    object Pintura : Services()

    object Cristaleria : Services()

    object Climatizacion : Services()

    object Cerrajeria : Services()
    object PequeObras : Services()
    object Limpieza : Services()
    object Electricidad : Services()

    object Carpinteria : Services()

    object Ascensores : Services()

}