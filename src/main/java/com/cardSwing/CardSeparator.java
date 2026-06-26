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

    public enum SeparatorStyle { SOLID, DASHED, DOTTED, GRADIENT, SHADOW }

    private Color lineColor = new Color(229, 231, 235);
    private int thickness = 1;
    private int verticalPadding = 8;
    private SeparatorStyle style = SeparatorStyle.SOLID;
    private Color gradientEndColor = new Color(255, 255, 255, 0);

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
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = getWidth();
        int y = getHeight() / 2;
        
        if (style == SeparatorStyle.SOLID) {
            g2.setColor(lineColor);
            g2.fillRect(0, y - thickness / 2, w, thickness);
        } else if (style == SeparatorStyle.DASHED) {
            g2.setColor(lineColor);
            g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{Math.max(4, thickness * 4), Math.max(2, thickness * 2)}, 0));
            g2.drawLine(0, y, w, y);
        } else if (style == SeparatorStyle.DOTTED) {
            g2.setColor(lineColor);
            g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{0, Math.max(2, thickness * 2)}, 0));
            g2.drawLine(0, y, w, y);
        } else if (style == SeparatorStyle.GRADIENT) {
            GradientPaint gp = new GradientPaint(0, y, lineColor, w, y, gradientEndColor);
            g2.setPaint(gp);
            g2.fillRect(0, y - thickness / 2, w, thickness);
        } else if (style == SeparatorStyle.SHADOW) {
            GradientPaint gp1 = new GradientPaint(0, y - thickness, new Color(0,0,0,15), 0, y, new Color(0,0,0,0));
            g2.setPaint(gp1);
            g2.fillRect(0, y - thickness, w, thickness);
            GradientPaint gp2 = new GradientPaint(0, y, new Color(255,255,255,200), 0, y + thickness, new Color(255,255,255,0));
            g2.setPaint(gp2);
            g2.fillRect(0, y, w, thickness);
        }
        
        g2.dispose();
    }

    public SeparatorStyle getStyle() { return style; }
    public void setStyle(SeparatorStyle style) {
        SeparatorStyle old = this.style;
        this.style = style;
        firePropertyChange("style", old, this.style);
        repaint();
    }

    public Color getGradientEndColor() { return gradientEndColor; }
    public void setGradientEndColor(Color gradientEndColor) {
        Color old = this.gradientEndColor;
        this.gradientEndColor = gradientEndColor;
        firePropertyChange("gradientEndColor", old, this.gradientEndColor);
        repaint();
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
