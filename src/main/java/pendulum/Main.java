package main.java.pendulum;

/**
 * Entry point for the large-angle pendulum simulation.
 * <p>
 * This class initializes the pendulum state and starts the graphical simulation frame.
 */
public class Main {
    
    /**
     * Starts the pendulum simulation.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {

        double length = 1.0;
        double initialAngle = 45.0;

        Pendulum pendulum = new Pendulum(length, initialAngle);
        RK4Integrator integrator = new RK4Integrator();

        new SimulationFrame(pendulum, integrator);
    }

}
