package com.cardSwing;

import java.awt.*;
import javax.swing.*;

/**
 * Contador de notificações circular pequeno (Badge).
 * Ideal para colocar no canto superior direito de botões ou listas.
 * <p>Ex: Uma bolinha vermelha escrita "3"</p>
 */
public class CardBadge extends JPanel {

    private String text = "1";
    private Color badgeColor = new Color(239, 68, 68); // Red
    private Color badgeTextColor = Color.WHITE;

    public CardBadge() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        // O badge será um círculo perfeito no centro
        int size = Math.min(w, h); 
        int x = (w - size) / 2;
        int y = (h - size) / 2;

        g2.setColor(badgeColor);
        g2.fillOval(x, y, size, size);

        g2.setColor(badgeTextColor);
        g2.setFont(new Font("Segoe UI", Font.BOLD, (int)(size * 0.55)));
        FontMetrics fm = g2.getFontMetrics();
        int tx = x + (size - fm.stringWidth(text)) / 2;
        int ty = y + (size + fm.getAscent() - fm.getDescent()) / 2;
        g2.drawString(text, tx, ty);

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(24, 24);
    }
    
    // === Propriedades para aparecer no NetBeans ===
    public String getText() { return text; }
    public void setText(String text) { this.text = text; repaint(); }
    
    public Color getBadgeColor() { return badgeColor; }
    public void setBadgeColor(Color c) { this.badgeColor = c; repaint(); }
    
    public Color getBadgeTextColor() { return badgeTextColor; }
    public void setBadgeTextColor(Color c) { this.badgeTextColor = c; repaint(); }
}
