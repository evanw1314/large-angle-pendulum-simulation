package main.java.pendulum;

public class Main {
    
    public static void main(String[] args) {

        double length = 1.0;
        double initialAngle = 45.0;

        Pendulum pendulum = new Pendulum(length, initialAngle);
        RK4Integrator integrator = new RK4Integrator();

        double timeStep = 0.01;
        double totalTime = 10.0;

        new SimulationFrame(pendulum, integrator);

        // for (double t = 0; t <= totalTime; t += timeStep) {
        //     double[] result = integrator.step(pendulum, timeStep);
        //     System.out.println("Time: " + t + "s, Angle: " + Math.toDegrees(result[0]) + " degrees, Angular Velocity: " + Math.toDegrees(result[1]) + " degrees/s");
        // }

    }

}
