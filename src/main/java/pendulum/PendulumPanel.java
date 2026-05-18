package main.java.pendulum;

import java.awt.*;
import javax.swing.JPanel;

public class PendulumPanel extends JPanel{
    
    private Pendulum pendulum;
    public static final double SCALE = 200.0;

    public PendulumPanel(Pendulum pendulum) {
        this.pendulum = pendulum;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 5 * 2;
        int bobX = centerX + (int) (SCALE * pendulum.getLength() * Math.sin(pendulum.getAngle()));
        int bobY = centerY + (int) (SCALE * pendulum.getLength() * Math.cos(pendulum.getAngle()));
        
        g.setColor(Color.BLACK);
        g.drawLine(centerX, centerY, bobX, bobY);
        g.setColor(Color.RED);
        g.fillOval(bobX - 10, bobY - 10, 20, 20);
    }

}
