package com.cardSwing;

import java.awt.*;
import java.io.Serializable;
import javax.swing.*;

/**
 * Botão estilizado para Card — cantos arredondados, cor de fundo customizável.
 *
 * <p><b>PALETTE:</b> Arraste para dentro de um CardPanel ou CardButtonRow.</p>
 *
 * <p>Propriedades editáveis:</p>
 * <ul>
 *   <li><b>text</b> — texto do botão</li>
 *   <li><b>buttonColor</b> — cor de fundo</li>
 *   <li><b>textColor</b> — cor do texto</li>
 *   <li><b>buttonRadius</b> — raio dos cantos</li>
 * </ul>
 */
public class CardButton extends JButton implements Serializable {

    private static final long serialVersionUID = 1L;

    private Color buttonColor = new Color(59, 130, 246);
    private Color textColor = Color.WHITE;
    private int buttonRadius = 8;

    public CardButton() {
        this("Botão");
    }

    public CardButton(String text) {
        super(text);
        setup();
    }

    public CardButton(String text, Color buttonColor) {
        super(text);
        this.buttonColor = buttonColor;
        setup();
    }

    public CardButton(String text, Color buttonColor, Color textColor) {
        super(text);
        this.buttonColor = buttonColor;
        this.textColor = textColor;
        setup();
    }

    private void setup() {
        setFont(new Font("Segoe UI", Font.BOLD, 12));
        setForeground(textColor);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color bg = buttonColor;
        if (getModel().isPressed()) {
            bg = darker(bg, 0.85f);
        } else if (getModel().isRollover()) {
            bg = brighter(bg, 1.15f);
        }

        g2.setColor(bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), buttonRadius, buttonRadius);
        g2.dispose();

        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        return new Dimension(d.width + 28, 34);
    }

    private Color brighter(Color c, float factor) {
        return new Color(
            Math.min(255, (int)(c.getRed() * factor)),
            Math.min(255, (int)(c.getGreen() * factor)),
            Math.min(255, (int)(c.getBlue() * factor)),
            c.getAlpha()
        );
    }

    private Color darker(Color c, float factor) {
        return new Color(
            (int)(c.getRed() * factor),
            (int)(c.getGreen() * factor),
            (int)(c.getBlue() * factor),
            c.getAlpha()
        );
    }

    // === PROPRIEDADES ===

    /** Cor de fundo do botão. */
    public Color getButtonColor() { return buttonColor; }
    public void setButtonColor(Color buttonColor) {
        Color old = this.buttonColor;
        this.buttonColor = buttonColor;
        firePropertyChange("buttonColor", old, buttonColor);
        repaint();
    }

    /** Cor do texto do botão. */
    public Color getTextColor() { return textColor; }
    public void setTextColor(Color textColor) {
        Color old = this.textColor;
        this.textColor = textColor;
        setForeground(textColor);
        firePropertyChange("textColor", old, textColor);
    }

    /** Raio dos cantos arredondados. */
    public int getButtonRadius() { return buttonRadius; }
    public void setButtonRadius(int buttonRadius) {
        int old = this.buttonRadius;
        this.buttonRadius = Math.max(0, buttonRadius);
        firePropertyChange("buttonRadius", old, this.buttonRadius);
        repaint();
    }
}
