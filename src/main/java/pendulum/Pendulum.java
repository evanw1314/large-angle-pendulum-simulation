package main.java.pendulum;

public class Pendulum {
    
    private double length;
    private double angle; //in degrees
    private double angularVelocity;
    
    public static final double GRAVITY = 9.81;

    public Pendulum(double length, double angle) {
        this.length = length;
        this.angle = angle;
        this.angularVelocity = 0.0;
    }

    public double getLength() {
        return length;
    }

    public double getAngle() {
        return angle;
    }

    public double getAngularVelocity() {
        return angularVelocity;
    }

    public void updateAngle(double angle) {
        this.angle = angle;
    }

    public void updateAngularVelocity(double angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public double getAngularAcceleration(double angle) {
        return -GRAVITY / length * Math.sin(Math.toRadians(angle));
    }

}