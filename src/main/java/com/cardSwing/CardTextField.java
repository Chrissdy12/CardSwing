package com.cardSwing;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Campo de texto estilizado para Card — cantos arredondados, borda customizada e efeito de foco.
 *
 * <p><b>PALETTE:</b> Arraste para dentro de um CardPanel.</p>
 */
public class CardTextField extends JTextField implements Serializable {

    private static final long serialVersionUID = 1L;

    private int radius = 8;
    private Color borderColor = new Color(209, 213, 219); // Gray 300
    private Color focusColor = new Color(59, 130, 246); // Blue 500
    private Color hoverColor = new Color(156, 163, 175); // Gray 400
    
    private boolean isHovered = false;

    public CardTextField() {
        this("");
    }

    public CardTextField(String text) {
        super(text);
        setup();
    }

    private void setup() {
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(new Color(31, 41, 55)); // Gray 800
        setBackground(Color.WHITE);
        setCaretColor(focusColor);
        setOpaque(false);
        setBorder(new EmptyBorder(8, 12, 8, 12));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                if (!hasFocus()) repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                if (!hasFocus()) repaint();
            }
        });
        
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        
        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (hasFocus()) {
            g2.setColor(focusColor);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, radius, radius);
        } else {
            g2.setColor(isHovered ? hoverColor : borderColor);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        }
        
        g2.dispose();
    }

    // === PROPRIEDADES ===
    public int getRadius() { return radius; }
    public void setRadius(int radius) { 
        this.radius = radius; 
        repaint(); 
    }
    
    public Color getBorderColor() { return borderColor; }
    public void setBorderColor(Color borderColor) { 
        this.borderColor = borderColor; 
        repaint(); 
    }
    
    public Color getFocusColor() { return focusColor; }
    public void setFocusColor(Color focusColor) { 
        this.focusColor = focusColor; 
        setCaretColor(focusColor);
        repaint(); 
    }
}
