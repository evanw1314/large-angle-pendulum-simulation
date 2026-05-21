package main.java.pendulum;

/**
 * A physical model of a simple pendulum for large-angle simulation.
 * <p>
 * The model tracks pendulum length, angle, and angular velocity, and computes
 * the angular acceleration due to gravity.
 */
public class Pendulum {
    
    private double length;
    private double angle;
    private double angularVelocity;
    
    public static final double GRAVITY = 9.81;

    /**
     * Constructs a pendulum with a given length and initial angle.
     *
     * @param length the pendulum length in meters
     * @param angle the initial angle in degrees relative to the vertical
     */
    public Pendulum(double length, double angle) {
        this.length = length;
        this.angle = Math.toRadians(angle);
        this.angularVelocity = 0.0;
    }

    /**
     * Returns the pendulum length in meters.
     *
     * @return the pendulum length
     */
    public double getLength() {
        return length;
    }

    /**
     * Returns the current pendulum angle in radians.
     *
     * @return the pendulum angle
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Updates the pendulum length.
     *
     * @param length the new length in meters
     */
    public void updateLength(double length) {
        this.length = length;
    }

    /**
     * Returns the current angular velocity in radians per second.
     *
     * @return the angular velocity
     */
    public double getAngularVelocity() {
        return angularVelocity;
    }

    /**
     * Updates the pendulum angle.
     *
     * @param angle the new angle in radians
     */
    public void updateAngle(double angle) {
        this.angle = angle;
    }

    /**
     * Updates the pendulum angular velocity.
     *
     * @param angularVelocity the new angular velocity in radians per second
     */
    public void updateAngularVelocity(double angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    /**
     * Computes the current angular acceleration from the pendulum equation.
     *
     * @param angle the pendulum angle in radians
     * @return the angular acceleration in radians per second squared
     */
    public double getAngularAcceleration(double angle) {
        return -GRAVITY / length * Math.sin(angle);
    }

    /**
     * Resets the pendulum to the default starting state.
     * <p>
     * The default state uses a 45-degree initial angle, zero angular velocity, and a 1 meter length.
     */
    public void reset() {
        this.angle = Math.toRadians(45);
        this.angularVelocity = 0.0;
        this.length = 1.0;
    }

}