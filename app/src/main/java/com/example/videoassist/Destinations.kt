package com.example.videoassist

interface Destinations {
    val route: String
}

object HomeScreen : Destinations{
    override val route = "HomeScreen"
}

object NewClip : Destinations{
    override val route = "NewClip"
}

object ClipScreen : Destinations{
    override val route = "ClipScreen"
}

object NewFootage : Destinations{
    override val route = "NewFootage"
}

object FootageEquipmentScreen : Destinations{
    override val route = "FootageEquipmentScreen"
}

object EquipmentScreen : Destinations{
    override val route = "EquipmentScreen"
}