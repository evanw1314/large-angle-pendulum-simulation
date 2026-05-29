package main.java.pendulum;

import java.awt.*;
import java.awt.geom.Point2D;
import javax.swing.JPanel;

/**
 * A Swing panel that renders the pendulum using 2D graphics
 * with a realistic metallic sheen.
 */
public class PendulumPanel extends JPanel {

    private Pendulum pendulum;
    public static final double SCALE = 200.0;

    public PendulumPanel(Pendulum pendulum) {
        this.pendulum = pendulum;
    }

    /**
     * Paints the pendulum rod and bob at the current simulation state.
     *
     * @param g the Graphics context used for drawing the pendulum components
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 5 * 2;
        int bobX = centerX + (int) (SCALE * pendulum.getLength() * Math.sin(pendulum.getAngle()));
        int bobY = centerY + (int) (SCALE * pendulum.getLength() * Math.cos(pendulum.getAngle()));

        g2d.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        GradientPaint rodPaint = new GradientPaint(
                centerX, centerY, new Color(180, 180, 180),
                bobX, bobY, new Color(70, 70, 70)
        );
        g2d.setPaint(rodPaint);
        g2d.drawLine(centerX, centerY, bobX, bobY);

        int bobRadius = 16;
        int bobDiameter = bobRadius * 2;

        Point2D center = new Point2D.Float(bobX, bobY);
        Point2D focus = new Point2D.Float(bobX - bobRadius / 2.5f, bobY - bobRadius / 2.5f);

        float[] dist = {0.0f, 0.4f, 1.0f};
        Color[] colors = {Color.WHITE, Color.LIGHT_GRAY, Color.DARK_GRAY};

        RadialGradientPaint metalBobPaint = new RadialGradientPaint(
                center, (float) bobRadius, focus, dist, colors, MultipleGradientPaint.CycleMethod.NO_CYCLE
        );

        g2d.setPaint(metalBobPaint);
        g2d.fillOval(bobX - bobRadius, bobY - bobRadius, bobDiameter, bobDiameter);

        g2d.setColor(new Color(40, 40, 40));
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.drawOval(bobX - bobRadius, bobY - bobRadius, bobDiameter, bobDiameter);

        int pinRadius = 5;

        RadialGradientPaint pinPaint = new RadialGradientPaint(
                new Point2D.Float(centerX, centerY),
                (float) pinRadius,
                new Point2D.Float(centerX - 2, centerY - 2),
                new float[]{0.0f, 1.0f},
                new Color[]{Color.WHITE, Color.DARK_GRAY},
                MultipleGradientPaint.CycleMethod.NO_CYCLE
        );
        g2d.setPaint(pinPaint);
        g2d.fillOval(centerX - pinRadius, centerY - pinRadius, pinRadius * 2, pinRadius * 2);
    }
}