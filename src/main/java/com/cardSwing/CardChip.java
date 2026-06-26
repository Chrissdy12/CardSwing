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

        // Define a fonte baseada no componente
        Font f = getFont();
        if (f == null || f.getSize() <= 1) {
            f = new Font("Segoe UI", Font.BOLD, 12);
            setFont(f);
        }
        g2.setFont(f);

        // Texto
        g2.setColor(fg);
        FontMetrics fm = g2.getFontMetrics();
        int txtY = (h + fm.getAscent() - fm.getDescent()) / 2;
        int txtX = fm.getHeight(); // Padding esquerdo baseado no tamanho da fonte
        g2.drawString(text, txtX, txtY);

        // Ícone de fechar (X) redondo na ponta direita
        int cx = w - (h / 2);
        int cy = h / 2;
        int iconSize = Math.max(8, (int)(fm.getHeight() * 0.6));

        if (hoveredClose) {
            g2.setColor(new Color(255, 0, 0, 40)); // Feedback visual de erro/fechar
            int hoverSize = Math.max(20, fm.getHeight() + 4);
            g2.fillOval(cx - (hoverSize/2), cy - (hoverSize/2), hoverSize, hoverSize);
        }

        g2.setColor(fg);
        g2.setStroke(new BasicStroke(Math.max(1.5f, f.getSize() / 8f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int o = iconSize / 2;
        g2.drawLine(cx - o, cy - o, cx + o, cy + o);
        g2.drawLine(cx - o, cy + o, cx + o, cy - o);

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        Font f = getFont();
        if (f == null || f.getSize() <= 1) f = new Font("Segoe UI", Font.BOLD, 12);
        FontMetrics fm = getFontMetrics(f);
        int pad = fm.getHeight(); // Padding baseado na fonte
        int w = pad + fm.stringWidth(text) + pad + pad; // Esquerda + Texto + Dir + Botão
        int h = fm.getHeight() + 12; // Altura adaptativa
        return new Dimension(w, h);
    }
    
    public String getText() { return text; }
    public void setText(String text) { this.text = text; revalidate(); repaint(); }
    
    public Color getChipColor() { return chipColor; }
    public void setChipColor(Color c) { this.chipColor = c; repaint(); }
    
    public Color getChipTextColor() { return chipTextColor; }
    public void setChipTextColor(Color c) { this.chipTextColor = c; repaint(); }
}
