package com.example.videoassist

interface Destinations {
    val route: String
}

object HomeScreen : Destinations{
    override val route = "HomeScreen"
}

object ClipNew : Destinations{
    override val route = "ClipNew"
}

object ClipScreen : Destinations{
    override val route = "ClipScreen"
}

object FootageScreen : Destinations{
    override val route = "FootageScreen"
}

object ClipEquipmentScreen : Destinations{
    override val route = "ClipEquipmentScreen"
}

object EquipmentScreen : Destinations{
    override val route = "EquipmentScreen"
}