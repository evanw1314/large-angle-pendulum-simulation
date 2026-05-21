package main.java.pendulum;

/**
 * A Runge-Kutta 4th order integrator for numerically updating the pendulum state.
 */
public class RK4Integrator {
    
    /**
     * Advances the pendulum state by the specified time step using RK4.
     *
     * @param pendulum the pendulum to update
     * @param timeStep the simulation time increment in seconds
     * @return an array containing the updated angle and angular velocity
     */
    public double[] step(Pendulum pendulum, double timeStep) {

        //To clear things up, k#Angle is the angular velocity, while k#AngularVelocity is the angular acceleration. 
        //These values are required in order to calculate the new angle and new angular velocity of the pendulum after a time step.

        double currAngle = pendulum.getAngle();
        double currAngularVelocity = pendulum.getAngularVelocity();

        double k1Angle = currAngularVelocity;
        double k1AngularVelocity = pendulum.getAngularAcceleration(currAngle);

        double midAngle = currAngle + 0.5 * timeStep * k1Angle;
        double midAngularVelocity = currAngularVelocity + 0.5 * timeStep * k1AngularVelocity;

        double k2Angle = midAngularVelocity;
        double k2AngularVelocity = pendulum.getAngularAcceleration(midAngle);

        double midAngle2 = currAngle + 0.5 * timeStep * k2Angle;
        double midAngularVelocity2 = currAngularVelocity + 0.5 * timeStep * k2AngularVelocity;

        double k3Angle = midAngularVelocity2;
        double k3AngularVelocity = pendulum.getAngularAcceleration(midAngle2);

        double midAngle3 = currAngle + timeStep * k3Angle;
        double midAngularVelocity3 = currAngularVelocity + timeStep * k3AngularVelocity;

        double k4Angle = midAngularVelocity3;
        double k4AngularVelocity = pendulum.getAngularAcceleration(midAngle3);

        double newAngle = currAngle + (timeStep / 6.0) * (k1Angle + 2.0 * k2Angle + 2.0 * k3Angle + k4Angle);
        double newAngularVelocity = currAngularVelocity + (timeStep / 6.0) * (k1AngularVelocity + 2.0 * k2AngularVelocity + 2.0 * k3AngularVelocity + k4AngularVelocity);

        pendulum.updateAngle(newAngle);
        pendulum.updateAngularVelocity(newAngularVelocity);

        return new double[]{newAngle, newAngularVelocity};
    }
}
