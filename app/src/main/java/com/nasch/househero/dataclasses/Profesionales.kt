package com.nasch.househero.dataclasses

data class Profesionales(
    var userId: String? = null,
    var userName: String? = null,
    var userSurname: String? = null,
    var selectedRole: String? = null,
    var selectedServices: List<String>? = null,
)
