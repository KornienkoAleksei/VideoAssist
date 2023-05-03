package com.example.videoassist.functions

//get Person movement if true static else movement
fun getPersonMovement (
    personOrientation: PersonOrientation
): Boolean {
    return  when (personOrientation) { PersonOrientation.StaticRight -> true
        PersonOrientation.StaticLeft -> true
        PersonOrientation.StaticForward -> true
        PersonOrientation.StaticBackward -> true
        PersonOrientation.MoveForward -> false
        PersonOrientation.MoveBackward -> false
        PersonOrientation.MoveLeft -> false
        PersonOrientation.MoveRight -> false}
}

// change CameraMovementVertical by press button between direction/static
fun inverseCameraVertical(
    cameraMovementVertical: CameraMovementVertical,
    button: CameraMovementVertical
): CameraMovementVertical {
    return if (cameraMovementVertical != button)
        button
    else CameraMovementVertical.Static
}

// change CameraMovementHorizontal by press button between direction/static
fun inverseCameraHorizontal(
    cameraMovementHorizontal:CameraMovementHorizontal,
    button: CameraMovementHorizontal
): CameraMovementHorizontal{
    return if (cameraMovementHorizontal != button)
        button
    else CameraMovementHorizontal.Static
}

// change person orientation
//button always static
fun personOrientationButton (
    personOrientation: PersonOrientation,
    staticPersonOrientation: Boolean,
    button: PersonOrientation
): PersonOrientation {
    return when (button) {
        PersonOrientation.StaticForward -> {
            inversePersonOrientation(
                personOrientation = personOrientation,
                staticPersonOrientation = staticPersonOrientation,
                buttonStatic = PersonOrientation.StaticForward,
                buttonDynamic = PersonOrientation.MoveForward
            )
        }
        PersonOrientation.StaticBackward -> {
            inversePersonOrientation(
                personOrientation = personOrientation,
                staticPersonOrientation = staticPersonOrientation,
                buttonStatic = PersonOrientation.StaticBackward,
                buttonDynamic = PersonOrientation.MoveBackward
            )
        }
        PersonOrientation.StaticLeft -> {
            inversePersonOrientation(
                personOrientation = personOrientation,
                staticPersonOrientation = staticPersonOrientation,
                buttonStatic = PersonOrientation.StaticLeft,
                buttonDynamic = PersonOrientation.MoveLeft
            )
        }
        PersonOrientation.StaticRight -> {
            inversePersonOrientation(
                personOrientation = personOrientation,
                staticPersonOrientation = staticPersonOrientation,
                buttonStatic = PersonOrientation.StaticRight,
                buttonDynamic = PersonOrientation.MoveRight
            )
        }
        else -> {inversePersonDynamic(
            personOrientation = personOrientation,
        )}
    }
}

fun inversePersonOrientation(
    personOrientation: PersonOrientation,
    staticPersonOrientation: Boolean,
    buttonStatic: PersonOrientation,
    buttonDynamic: PersonOrientation

): PersonOrientation {
    return when (personOrientation) {
        buttonStatic -> buttonDynamic
        buttonDynamic -> buttonStatic
        else -> if (staticPersonOrientation) buttonStatic else buttonDynamic
    }
}

fun inversePersonDynamic(
    personOrientation: PersonOrientation
): PersonOrientation {
    return when (personOrientation) {
        PersonOrientation.MoveBackward -> PersonOrientation.StaticBackward
        PersonOrientation.MoveForward -> PersonOrientation.StaticForward
        PersonOrientation.MoveLeft -> PersonOrientation.StaticLeft
        PersonOrientation.MoveRight -> PersonOrientation.StaticRight
        PersonOrientation.StaticBackward -> PersonOrientation.MoveBackward
        PersonOrientation.StaticForward -> PersonOrientation.MoveForward
        PersonOrientation.StaticLeft -> PersonOrientation.MoveLeft
        PersonOrientation.StaticRight -> PersonOrientation.MoveRight}
}