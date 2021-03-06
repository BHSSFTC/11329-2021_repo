package org.firstinspires.ftc.teamcode.math;

import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Rotation;


public class PositionRotation {
    public Position position;
    public Orientation rotation;
    
    public PositionRotation() {
        position = new Position();
        rotation = new Orientation();
    }
    
    public PositionRotation(Position position, Orientation rotation) {
        this.position = position;
        this.rotation = rotation;
    }
}