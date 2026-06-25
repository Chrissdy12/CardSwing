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

    // Efeitos Modernos
    private Point clickPoint;
    private float rippleRadius = 0f;
    private float rippleAlpha = 0f;
    private Timer rippleTimer;

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

        // Animação de Ripple (Onda)
        rippleTimer = new Timer(16, e -> {
            rippleRadius += 8f;
            rippleAlpha -= 0.04f;
            if (rippleAlpha <= 0f) {
                rippleAlpha = 0f;
                rippleTimer.stop();
            }
            repaint();
        });

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                clickPoint = e.getPoint();
                rippleRadius = 10f;
                rippleAlpha = 0.4f;
                rippleTimer.start();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight() - 2; // Deixa espaço em baixo para a sombra 3D real
        int r = buttonRadius;

        boolean isPressed = getModel().isPressed();
        boolean isHover = getModel().isRollover();

        // 1. Sombra 3D (Drop Shadow)
        // Quando o botão não está pressionado, mostramos a sombra embaixo
        if (!isPressed) {
            g2.setColor(new Color(0, 0, 0, 25));
            g2.fillRoundRect(0, 2, w, h, r, r);
            g2.setColor(new Color(0, 0, 0, 12));
            g2.fillRoundRect(0, 3, w, h, r, r);
        }

        // 2. Cores e Gradiente Sutil (Efeito Premium)
        Color topColor = isPressed ? darker(buttonColor, 0.90f) : (isHover ? brighter(buttonColor, 1.15f) : brighter(buttonColor, 1.08f));
        Color bottomColor = isPressed ? darker(buttonColor, 0.95f) : (isHover ? buttonColor : darker(buttonColor, 0.92f));
        
        GradientPaint gp = isPressed 
                ? new GradientPaint(0, 0, bottomColor, 0, h, topColor)
                : new GradientPaint(0, 0, topColor, 0, h, bottomColor);
        
        int yOffset = isPressed ? 1 : 0; // Desce 1 pixel fisicamente ao clicar!
        
        g2.setPaint(gp);
        g2.fillRoundRect(0, yOffset, w, h, r, r);

        // 3. Borda Externa Crisp (Para dar definição e contraste)
        g2.setColor(darker(buttonColor, 0.7f));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(0, yOffset, w - 1, h - 1, r, r);

        // 4. Ripple Effect
        if (rippleAlpha > 0f && clickPoint != null) {
            Shape oldClip = g2.getClip();
            g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, yOffset, w, h, r, r));
            g2.setColor(new Color(255, 255, 255, (int) Math.max(0, Math.min(255, rippleAlpha * 255))));
            float rr = rippleRadius;
            g2.fill(new java.awt.geom.Ellipse2D.Float(clickPoint.x - rr, clickPoint.y - rr, rr * 2, rr * 2));
            g2.setClip(oldClip);
        }

        g2.dispose();

        // 5. Renderização do Texto (Anti-aliasing e deslocamento do clique)
        Graphics2D gText = (Graphics2D) g;
        gText.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        if (isPressed) {
            gText.translate(0, 1);
        }
        super.paintComponent(gText);
        if (isPressed) {
            gText.translate(0, -1); // Restaura o translate após pintar
        }
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        // Um pouco mais de margem lateral e altura para a sombra
        return new Dimension(d.width + 32, 36);
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
