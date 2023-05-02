package com.example.videoassist.functions

data class Footage (
    var person: Boolean,
    var frame: Frame,
    var cameraMovementVertical: CameraMovementVertical,
    var cameraMovementHorizontal: CameraMovementHorizontal,
    var personOrientation: PersonOrientation,
    var idEquipment: Int, //replace to equipmentId
    var notes: String
)

data class EquipmentClip (
    val idEquipment: Int,
    val nameEquipment: String,
    var counterEquipment: Int,
)

enum class Frame {
    Landscape,
    FullBody,
    Body,
    Face,
    Detail
}

enum class CameraMovementVertical { Static, MoveUp, MoveDown}

enum class CameraMovementHorizontal {
    Static,
    Forward,
    Backward,
    Left,
    Right,
    PanoramicLeft,
    PanoramicRight,
    OrbitLeft,
    OrbitRight
}

enum class PersonOrientation {
    MoveLeft,
    MoveRight,
    MoveForward,
    MoveBackward,
    StaticLeft,
    StaticRight,
    StaticForward,
    StaticBackward
}