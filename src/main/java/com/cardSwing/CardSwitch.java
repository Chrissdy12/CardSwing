package com.cardSwing;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

/**
 * Botão Toggle estilo iOS.
 * 
 * <p><b>PALETTE:</b> Arraste para dentro de um CardPanel.</p>
 */
public class CardSwitch extends JCheckBox {
    
    private Color trackOnColor = new Color(16, 185, 129); // Verde
    private Color trackOffColor = new Color(203, 213, 225); // Cinza
    private Color knobColor = Color.WHITE;
    
    private int knobX = 2;
    private final Timer animTimer;
    private boolean editable = true;

    public CardSwitch(String text) {
        super(text);
        setOpaque(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setForeground(new Color(51, 65, 85));
        
        animTimer = new Timer(15, e -> {
            int targetX = isSelected() ? 20 : 2;
            if (knobX < targetX) knobX = Math.min(knobX + 3, targetX);
            else if (knobX > targetX) knobX = Math.max(knobX - 3, targetX);
            else ((Timer)e.getSource()).stop();
            repaint();
        });
        
        addActionListener(e -> animTimer.start());
    }

    public CardSwitch(String text, boolean selected) {
        this(text);
        setSelected(selected);
        knobX = selected ? 20 : 2;
    }

    public CardSwitch() {
        this("Opção");
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = 40;
        int h = 22;
        int y = (getHeight() - h) / 2;
        
        // Trilho
        g2.setColor(isSelected() ? trackOnColor : trackOffColor);
        g2.fill(new RoundRectangle2D.Float(0, y, w, h, h, h));
        
        // Bolinha (Knob)
        g2.setColor(knobColor);
        g2.fillOval(knobX, y + 2, 18, 18);
        
        // Texto
        if (getText() != null && !getText().isEmpty()) {
            g2.setColor(getForeground());
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int ty = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(getText(), w + 8, ty);
        }
        
        g2.dispose();
    }
    
    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        FontMetrics fm = getFontMetrics(getFont());
        int textW = (getText() != null) ? fm.stringWidth(getText()) : 0;
        return new Dimension(40 + 8 + textW, Math.max(22, d.height));
    }
    
    public Color getTrackOnColor() { return trackOnColor; }
    public void setTrackOnColor(Color trackOnColor) { 
        this.trackOnColor = trackOnColor; 
        repaint(); 
    }
    
    public Color getTrackOffColor() { return trackOffColor; }
    public void setTrackOffColor(Color trackOffColor) { 
        this.trackOffColor = trackOffColor; 
        repaint(); 
    }

    public boolean isEditable() { return editable; }
    
    public void setEditable(boolean editable) {
        boolean old = this.editable;
        this.editable = editable;
        firePropertyChange("editable", old, editable);
    }
    
    @Override
    public void setSelected(boolean b) {
        super.setSelected(b);
        if (animTimer != null && !animTimer.isRunning()) {
            animTimer.start();
        }
    }

    @Override
    protected void processMouseEvent(java.awt.event.MouseEvent e) {
        if (!editable) {
            int id = e.getID();
            if (id == java.awt.event.MouseEvent.MOUSE_PRESSED || 
                id == java.awt.event.MouseEvent.MOUSE_RELEASED || 
                id == java.awt.event.MouseEvent.MOUSE_CLICKED) {
                e.consume();
                return;
            }
        }
        super.processMouseEvent(e);
    }

    @Override
    protected void processKeyEvent(java.awt.event.KeyEvent e) {
        if (!editable) {
            if (e.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE) {
                e.consume();
                return;
            }
        }
        super.processKeyEvent(e);
    }
}
