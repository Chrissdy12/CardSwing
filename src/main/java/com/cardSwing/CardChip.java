package com.cardSwing;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * Chip/Filtro moderno que possui um botão (X) para fechamento.
 * Ao clicar no X, ele se auto-remove do painel pai e dispara eventos.
 */
public class CardChip extends JPanel {

    private String text = "Filtro";
    private Color chipColor = new Color(226, 232, 240); // Fundo padrão neutro
    private Color chipTextColor = new Color(15, 23, 42); // Texto escuro
    private boolean hoveredClose = false;

    private final List<ActionListener> listeners = new ArrayList<>();

    public CardChip() {
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isCloseHovered(e.getPoint())) {
                    fireCloseEvent();
                    // Auto-remoção inteligente
                    Container parent = getParent();
                    if (parent != null) {
                        parent.remove(CardChip.this);
                        parent.revalidate();
                        parent.repaint();
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hoveredClose = false;
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                boolean prev = hoveredClose;
                hoveredClose = isCloseHovered(e.getPoint());
                if (prev != hoveredClose) {
                    repaint();
                }
            }
        });
    }

    private boolean isCloseHovered(Point p) {
        int r = getHeight();
        int closeX = getWidth() - r; // A área final da bolinha à direita
        return p.x >= closeX;
    }

    public void addActionListener(ActionListener l) {
        listeners.add(l);
    }

    private void fireCloseEvent() {
        ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, text);
        for (ActionListener l : listeners) {
            l.actionPerformed(e);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int h = getHeight();
        int w = getWidth();
        int arc = h; // 100% border radius

        // Pintura Base influenciada pelo tema global se existir
        Color bg = chipColor;
        Color fg = chipTextColor;
        if (CardThemeManager.getCurrentTheme() != null) {
            if (CardThemeManager.getCurrentTheme().isDark) {
                bg = new Color(255, 255, 255, 20); // Translúcido no dark mode
                fg = Color.WHITE;
            }
        }

        // Fundo do Chip
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, w, h, arc, arc);

        // Texto
        g2.setColor(fg);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        FontMetrics fm = g2.getFontMetrics();
        int txtY = (h + fm.getAscent() - fm.getDescent()) / 2;
        g2.drawString(text, 12, txtY);

        // Ícone de fechar (X) redondo na ponta direita
        int cx = w - (h / 2);
        int cy = h / 2;
        int iconSize = 8;

        if (hoveredClose) {
            g2.setColor(new Color(255, 0, 0, 40)); // Feedback visual de erro/fechar
            g2.fillOval(cx - 10, cy - 10, 20, 20);
        }

        g2.setColor(fg);
        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int o = iconSize / 2;
        g2.drawLine(cx - o, cy - o, cx + o, cy + o);
        g2.drawLine(cx - o, cy + o, cx + o, cy - o);

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(new Font("Segoe UI", Font.BOLD, 12));
        int w = 12 + fm.stringWidth(text) + 12 + 20; // Esquerda + Texto + Dir + Botão
        return new Dimension(w, 28);
    }
    
    public String getText() { return text; }
    public void setText(String text) { this.text = text; revalidate(); repaint(); }
    
    public Color getChipColor() { return chipColor; }
    public void setChipColor(Color c) { this.chipColor = c; repaint(); }
    
    public Color getChipTextColor() { return chipTextColor; }
    public void setChipTextColor(Color c) { this.chipTextColor = c; repaint(); }
}
