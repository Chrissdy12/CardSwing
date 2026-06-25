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
 * Campo de senha estilizado para Card — com ícone de olho para mostrar/ocultar senha.
 */
public class CardPasswordField extends JPasswordField implements Serializable {

    private static final long serialVersionUID = 1L;

    private int radius = 8;
    private Color borderColor = new Color(209, 213, 219); // Gray 300
    private Color focusColor = new Color(59, 130, 246); // Blue 500
    private Color hoverColor = new Color(156, 163, 175); // Gray 400
    
    private boolean isHovered = false;
    private boolean showPassword = false;
    private char defaultEchoChar;
    
    private Rectangle eyeBounds = new Rectangle(0, 0, 0, 0);

    private String placeholder = "";

    public CardPasswordField() {
        this("");
    }

    public CardPasswordField(String text) {
        super(text);
        defaultEchoChar = getEchoChar();
        if (defaultEchoChar == (char) 0) {
            defaultEchoChar = '•'; // Garante um fallback
            setEchoChar(defaultEchoChar);
        }
        setup();
    }

    private void setup() {
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(new Color(31, 41, 55)); // Gray 800
        setBackground(Color.WHITE);
        setCaretColor(focusColor);
        setOpaque(false);
        setBorder(new EmptyBorder(8, 12, 8, 36)); // Espaço extra para o ícone
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                if (!hasFocus()) repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                setCursor(new Cursor(Cursor.TEXT_CURSOR));
                if (!hasFocus()) repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (eyeBounds.contains(e.getPoint())) {
                    showPassword = !showPassword;
                    setEchoChar(showPassword ? (char) 0 : defaultEchoChar);
                    repaint();
                }
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (eyeBounds.contains(e.getPoint())) {
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else {
                    setCursor(new Cursor(Cursor.TEXT_CURSOR));
                }
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
        
        // Placeholder
        if (new String(getPassword()).isEmpty() && placeholder != null && !placeholder.isEmpty() && !hasFocus()) {
            g2.setColor(new Color(156, 163, 175)); // Gray 400
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(placeholder, getInsets().left, y);
        }
        
        // Desenha o olhinho
        drawEye(g2);
        
        g2.dispose();
    }
    
    private void drawEye(Graphics2D g2) {
        int iconSize = 18;
        int x = getWidth() - iconSize - 12;
        int y = (getHeight() - iconSize) / 2;
        
        eyeBounds.setBounds(x - 5, y - 5, iconSize + 10, iconSize + 10);
        
        g2.setColor(isHovered ? hoverColor : borderColor);
        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        // Olho usando curva quadrática para um formato amendoado perfeito
        java.awt.geom.QuadCurve2D topCurve = new java.awt.geom.QuadCurve2D.Float(x, y + iconSize/2f, x + iconSize/2f, y + 2, x + iconSize, y + iconSize/2f);
        java.awt.geom.QuadCurve2D bottomCurve = new java.awt.geom.QuadCurve2D.Float(x, y + iconSize/2f, x + iconSize/2f, y + iconSize - 2, x + iconSize, y + iconSize/2f);
        
        g2.draw(topCurve);
        g2.draw(bottomCurve);
        
        // Íris do olho
        int irisSize = 6;
        int ix = x + (iconSize - irisSize) / 2;
        int iy = y + (iconSize - irisSize) / 2;
        
        if (showPassword) {
            g2.fillOval(ix, iy, irisSize, irisSize);
        } else {
            g2.drawOval(ix, iy, irisSize, irisSize);
            // Traço diagonal cortando (senha oculta)
            g2.drawLine(x + 1, y + iconSize - 1, x + iconSize - 1, y + 1);
        }
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
    
    public String getPlaceholder() { return placeholder; }
    public void setPlaceholder(String placeholder) { 
        this.placeholder = placeholder; 
        repaint(); 
    }
}
