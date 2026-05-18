package main.java.pendulum;

public class Pendulum {
    
    private double length;
    private double angle;
    private double angularVelocity;
    
    public static final double GRAVITY = 9.81;

    public Pendulum(double length, double angle) {
        this.length = length;
        this.angle = Math.toRadians(angle);
        this.angularVelocity = 0.0;
    }

    public double getLength() {
        return length;
    }

    public double getAngle() {
        return angle;
    }

    public void updateLength(double length) {
        this.length = length;
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
        return -GRAVITY / length * Math.sin(angle);
    }

    public void reset() {
        this.angle = Math.toRadians(45);
        this.angularVelocity = 0.0;
        this.length = 1.0;
    }

}