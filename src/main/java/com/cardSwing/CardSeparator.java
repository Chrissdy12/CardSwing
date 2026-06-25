package com.cardSwing;

import java.awt.*;
import java.io.Serializable;
import javax.swing.*;

/**
 * Separador visual — linha horizontal fina.
 *
 * <p><b>PALETTE:</b> Arraste para dentro de um CardPanel entre outros componentes.</p>
 */
public class CardSeparator extends JPanel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Color lineColor = new Color(229, 231, 235);
    private int thickness = 1;
    private int verticalPadding = 8;

    public CardSeparator() {
        setOpaque(false);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, thickness + verticalPadding * 2));
        setPreferredSize(new Dimension(100, thickness + verticalPadding * 2));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(lineColor);
        int y = getHeight() / 2;
        g2.fillRect(0, y, getWidth(), thickness);
        g2.dispose();
    }

    /** Cor da linha. */
    public Color getLineColor() { return lineColor; }
    public void setLineColor(Color lineColor) {
        Color old = this.lineColor;
        this.lineColor = lineColor;
        firePropertyChange("lineColor", old, lineColor);
        repaint();
    }

    /** Espessura da linha em pixels. */
    public int getThickness() { return thickness; }
    public void setThickness(int thickness) {
        int old = this.thickness;
        this.thickness = Math.max(1, thickness);
        updateSize();
        firePropertyChange("thickness", old, this.thickness);
        repaint();
    }

    /** Espaçamento vertical acima e abaixo da linha. */
    public int getVerticalPadding() { return verticalPadding; }
    public void setVerticalPadding(int verticalPadding) {
        int old = this.verticalPadding;
        this.verticalPadding = Math.max(0, verticalPadding);
        updateSize();
        firePropertyChange("verticalPadding", old, this.verticalPadding);
    }

    private void updateSize() {
        int h = thickness + verticalPadding * 2;
        setMaximumSize(new Dimension(Integer.MAX_VALUE, h));
        setPreferredSize(new Dimension(100, h));
        revalidate();
    }
}
