package main.java.pendulum;

public class Main {
    
    public static void main(String[] args) {

        double length = 1.0;
        double initialAngle = 45.0;

        Pendulum pendulum = new Pendulum(length, initialAngle);
        RK4Integrator integrator = new RK4Integrator();

        new SimulationFrame(pendulum, integrator);
    }

}
