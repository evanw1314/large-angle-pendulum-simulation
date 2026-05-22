package main.java.pendulum;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
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

import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

/**
 * A JFrame container for the pendulum simulation user interface.
 * <p>
 * This frame displays the pendulum animation, live data labels, and control widgets.
 */
public class SimulationFrame extends JFrame{
    
    private Pendulum pendulum;
    private PendulumPanel panel;
    private JPanel controlPanel;
    private RK4Integrator integrator;
    private Timer timer;
    private PrintWriter csvWriter;
    private double initialAngleDeg;

    private double previousAngularAcceleration;
    private double periodTime;
    private boolean determined;

    private Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    private int screenWidth = (int) size.getWidth();
    private int screenHeight = (int) size.getHeight();

    /**
     * Creates a new simulation frame using the given pendulum model and integrator.
     *
     * @param pendulum the pendulum model to simulate
     * @param integrator the numerical integrator for updating the pendulum state
     */
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
        JButton qrButton = new JButton("QRCode");


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
        qrButton.setAlignmentX(CENTER_ALIGNMENT);

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
        qrButton.setFont(new Font("Arial", Font.BOLD, 18));
        angleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        accelerationLabel.setFont(new Font("Arial", Font.BOLD, 18));
        velocityLabel.setFont(new Font("Arial", Font.BOLD, 18));
        periodLabel.setFont(new Font("Arial", Font.BOLD, 18));

        startPauseButton.setMaximumSize(new Dimension(300, 40));
        resetButton.setMaximumSize(new Dimension(300, 40));
        qrButton.setMaximumSize(new Dimension(300, 40));
        
        controlPanel.add(Box.createVerticalStrut(screenHeight * 3 / 128));
        controlPanel.add(buttonsSection);
        buttonsSection.add(startPauseButton);

        buttonsSection.add(Box.createVerticalStrut(20));
        buttonsSection.add(resetButton);
        buttonsSection.add(qrButton);

        qrButton.addActionListener(e -> {
            try {
                // Provide the path to the file you want to convert
                String filePath = "livedata.csv";
                String content = Files.readString(Paths.get(filePath));

                // Enforce the ~3KB size limit for standard QR codes
                if (content.getBytes().length > 2953) {
                    JOptionPane.showMessageDialog(this,
                            "The file is too large to fit in a QR code.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Generate the QR Code matrix
                QRCodeWriter barcodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix = barcodeWriter.encode(content, BarcodeFormat.QR_CODE, 300, 300);

                // Convert the matrix directly into an Image in memory (no need to save to disk)
                BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

                // Display the image in a pop-up dialog
                JOptionPane.showMessageDialog(this,
                        new JLabel(new ImageIcon(qrImage)),
                        "QR Code Data", JOptionPane.PLAIN_MESSAGE);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error generating QR: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        angleSlider.addChangeListener(e -> {
            double newAngle = Math.toRadians(angleSlider.getValue());
            pendulum.updateAngle(newAngle);
            pendulum.updateAngularVelocity(0);
            initialAngleLabel.setText("Initial Angle: " + angleSlider.getValue() + " degrees");
            // Track the selected initial angle in degrees for CSV output
            initialAngleDeg = angleSlider.getValue();
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
            // Update the CSV-tracked initial angle to the reset value
            initialAngleDeg = Math.toDegrees(pendulum.getAngle());
        });

        this.setVisible(true);

        // Initialize CSV writer
        try {
            csvWriter = new PrintWriter(new BufferedWriter(new FileWriter("livedata.csv", false)));
            csvWriter.println("initialAngleDeg,lengthM,angularVelocityRadPerS,angularAccelerationRadPerS2,angleDeg,timestamp");
            csvWriter.flush();
        } catch (Exception ex) {
            System.err.println("Could not open livedata.csv: " + ex.getMessage());
            csvWriter = null;
        }

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

            // Write CSV row: initialAngleDeg, length, angular velocity, angular acceleration, angle, timestamp
            if (csvWriter != null) {
                double lengthM = pendulum.getLength();
                double angularVelocity = pendulum.getAngularVelocity();
                double angularAcceleration = pendulum.getAngularAcceleration(pendulum.getAngle());
                double angleDeg = Math.toDegrees(pendulum.getAngle());
                String timestamp = Instant.now().toString();
                csvWriter.println(String.format("%.6f,%.6f,%.6f,%.6f,%.6f,%s",
                        initialAngleDeg, lengthM, angularVelocity, angularAcceleration, angleDeg, timestamp));
                csvWriter.flush();
            }

            panel.repaint();
        });
        timer.start();
        
        // Ensure CSV is closed when window closes
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (csvWriter != null) {
                    csvWriter.flush();
                    csvWriter.close();
                }
            }
        });
    }

}
