package main.java.pendulum;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

public class SimulationFrame extends JFrame{
    
    private Pendulum pendulum;
    private PendulumPanel panel;
    private JPanel controlPanel;
    private RK4Integrator integrator;
    private Timer timer;

    private double previousAngularAcceleration;
    private double periodTime;
    private boolean determined;

    private Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    private int screenWidth = (int) size.getWidth();
    private int screenHeight = (int) size.getHeight();

    public SimulationFrame(Pendulum pendulum, RK4Integrator integrator) {
        this.pendulum = pendulum;
        this.integrator = integrator;
        panel = new PendulumPanel(this.pendulum);
        controlPanel = new JPanel();
        
        this.setTitle("Group 9 - Large Angle Pendulum Simulation");
        this.setBounds(screenWidth / 8, screenHeight / 8, screenWidth*3/4, screenHeight*3/4);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);

        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        this.add(controlPanel, BorderLayout.EAST);

        JLabel initialAngleLabel = new JLabel("Initial Angle: " + Math.toDegrees(pendulum.getAngle()) + " degrees");
        JSlider angleSlider = new JSlider(1, 179, (int) Math.toDegrees(pendulum.getAngle()));
        JLabel lengthLabel = new JLabel("Pendulum Length: " + pendulum.getLength()  + " m");
        JSlider lengthSlider = new JSlider(1, 200, (int) (pendulum.getLength() * 100));
        JButton startPauseButton = new JButton("Pause");
        JButton resetButton = new JButton("Reset");

        JLabel angleLabel = new JLabel("Angle: " + Math.toDegrees(pendulum.getAngle()) + " degrees");
        JLabel accelerationLabel = new JLabel("Acceleration: " + pendulum.getAngularAcceleration(pendulum.getAngle())  + " rad/s²");
        JLabel velocityLabel = new JLabel("Angular Velocity: " + pendulum.getAngularVelocity() + " rad/s");
        JLabel periodLabel = new JLabel("Period: " + "Determining...");

        JPanel controlsSection = new JPanel();
        JPanel liveDataSection = new JPanel();
        JPanel buttonsSection = new JPanel();

        controlsSection.setLayout(new BoxLayout(controlsSection, BoxLayout.Y_AXIS));
        liveDataSection.setLayout(new BoxLayout(liveDataSection, BoxLayout.Y_AXIS));
        buttonsSection.setLayout(new BoxLayout(buttonsSection, BoxLayout.Y_AXIS));

        TitledBorder controlsTitle = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "CONTROLS", TitledBorder.CENTER, TitledBorder.TOP);
        controlsTitle.setTitleFont(new Font("Serif", Font.BOLD, 24));
        controlsSection.setBorder(controlsTitle);

        TitledBorder liveDataTitle = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "LIVE DATA", TitledBorder.CENTER, TitledBorder.TOP);
        liveDataTitle.setTitleFont(new Font("Serif", Font.BOLD, 24));
        liveDataSection.setBorder(liveDataTitle);

        TitledBorder buttonsTitle = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "BUTTONS", TitledBorder.CENTER, TitledBorder.TOP);
        buttonsTitle.setTitleFont(new Font("Serif", Font.BOLD, 24));
        buttonsSection.setBorder(buttonsTitle);

        controlsSection.setAlignmentX(CENTER_ALIGNMENT);
        liveDataSection.setAlignmentX(CENTER_ALIGNMENT);
        buttonsSection.setAlignmentX(CENTER_ALIGNMENT);

        startPauseButton.setAlignmentX(CENTER_ALIGNMENT);
        resetButton.setAlignmentX(CENTER_ALIGNMENT);

        controlPanel.add(Box.createVerticalStrut(screenHeight * 3 / 32));
        controlPanel.add(controlsSection);
        controlsSection.add(initialAngleLabel);
        controlsSection.add(Box.createVerticalStrut(5));
        controlsSection.add(angleSlider);
        controlsSection.add(Box.createVerticalStrut(20));
        controlsSection.add(lengthLabel);
        controlsSection.add(Box.createVerticalStrut(5));
        controlsSection.add(lengthSlider);
        controlPanel.add(Box.createVerticalStrut(screenHeight * 3 / 128));
        controlPanel.add(liveDataSection);
        liveDataSection.add(angleLabel);
        liveDataSection.add(Box.createVerticalStrut(20));
        liveDataSection.add(accelerationLabel);
        liveDataSection.add(Box.createVerticalStrut(20));
        liveDataSection.add(velocityLabel);
        liveDataSection.add(Box.createVerticalStrut(20));
        liveDataSection.add(periodLabel);
        
        initialAngleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        lengthLabel.setFont(new Font("Arial", Font.BOLD, 18));
        angleSlider.setFont(new Font("Arial", Font.BOLD, 18));
        lengthSlider.setFont(new Font("Arial", Font.BOLD, 18));
        startPauseButton.setFont(new Font("Arial", Font.BOLD, 18));
        resetButton.setFont(new Font("Arial", Font.BOLD, 18));
        angleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        accelerationLabel.setFont(new Font("Arial", Font.BOLD, 18));
        velocityLabel.setFont(new Font("Arial", Font.BOLD, 18));
        periodLabel.setFont(new Font("Arial", Font.BOLD, 18));

        startPauseButton.setMaximumSize(new Dimension(300, 40));
        resetButton.setMaximumSize(new Dimension(300, 40));
        
        controlPanel.add(Box.createVerticalStrut(screenHeight * 3 / 128));
        controlPanel.add(buttonsSection);
        buttonsSection.add(startPauseButton);

        buttonsSection.add(Box.createVerticalStrut(20));
        buttonsSection.add(resetButton);

        angleSlider.addChangeListener(e -> {
            double newAngle = Math.toRadians(angleSlider.getValue());
            pendulum.updateAngle(newAngle);
            pendulum.updateAngularVelocity(0);
            initialAngleLabel.setText("Initial Angle: " + angleSlider.getValue() + " degrees");
            previousAngularAcceleration = 0;
            determined = false;
            periodTime = 0;
            panel.repaint();
        });

        lengthSlider.addChangeListener(e -> {
            double newLength = (double)lengthSlider.getValue() / 100.0;
            pendulum.updateLength(newLength);
            pendulum.updateAngle(Math.toRadians(angleSlider.getValue()));
            pendulum.updateAngularVelocity(0);
            previousAngularAcceleration = 0;
            lengthLabel.setText("Pendulum Length: " + String.format("%.3f", newLength) + " m");
            determined = false;
            periodTime = 0;
            panel.repaint();
        });

        startPauseButton.addActionListener(e -> {
            if (timer.isRunning()) {
                timer.stop();
                startPauseButton.setText("Resume");
            } else {
                timer.start();
                startPauseButton.setText("Pause");
            }
        });

        resetButton.addActionListener(e -> {
            timer.start();
            pendulum.reset();
            startPauseButton.setText("Pause");
            angleSlider.setValue((int) Math.toDegrees(pendulum.getAngle()));
            lengthSlider.setValue((int) (pendulum.getLength() * 100));
            initialAngleLabel.setText("Initial Angle: " + String.format("%.3f", Math.toDegrees(pendulum.getAngle())) + " degrees");
            lengthLabel.setText("Pendulum Length: " + String.format("%.3f", pendulum.getLength()) + " m");
            periodTime = 0; // Reset the period timer
            determined = false; // Reset the determined flag
            previousAngularAcceleration = 0; // Reset the previous angular acceleration
        });

        this.setVisible(true);

        timer = new Timer(16, e -> {
            this.integrator.step(this.pendulum, 0.01);
            accelerationLabel.setText("Acceleration: " + String.format("%.3f", pendulum.getAngularAcceleration(pendulum.getAngle()))  + " rad/s²");
            velocityLabel.setText("Angular Velocity: " + String.format("%.3f", pendulum.getAngularVelocity()) + " rad/s");
            angleLabel.setText("Angle: " + String.format("%.3f", Math.toDegrees(pendulum.getAngle())) + " degrees");
            
            periodTime += 16/1000.0;

            if (!determined) {
                periodLabel.setText("Period: Determining...");
                if (previousAngularAcceleration * pendulum.getAngularAcceleration(pendulum.getAngle()) < 0) {
                    periodLabel.setText("Period: " + String.format("%.3f", periodTime * 4) + " s");
                    periodTime = 0; 
                    determined = true;
                }
            }

            previousAngularAcceleration = pendulum.getAngularAcceleration(pendulum.getAngle());

            panel.repaint();
        });
        timer.start();
        
    }

}
