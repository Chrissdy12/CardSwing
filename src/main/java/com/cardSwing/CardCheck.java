package com.cardSwing;

import java.awt.*;
import java.io.Serializable;
import javax.swing.*;

/**
 * Checkbox estilizado para Card — renderiza com visual moderno.
 *
 * <p><b>PALETTE:</b> Arraste para dentro de um CardPanel.</p>
 *
 * <p>Propriedades editáveis:</p>
 * <ul>
 *   <li><b>text</b> — texto ao lado do check</li>
 *   <li><b>selected</b> — marcado ou não</li>
 *   <li><b>checkColor</b> — cor do check quando marcado</li>
 * </ul>
 */
public class CardCheck extends JCheckBox implements Serializable {

    private static final long serialVersionUID = 1L;

    private Color checkColor = new Color(59, 130, 246);

    public CardCheck() {
        this("Opção");
    }

    public CardCheck(String text) {
        super(text);
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setForeground(new Color(51, 65, 85));
        setOpaque(false);
        setFocusPainted(false);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
    }

    public CardCheck(String text, boolean selected) {
        this(text);
        setSelected(selected);
    }

    /** Cor do check quando marcado. */
    public Color getCheckColor() { return checkColor; }
    public void setCheckColor(Color checkColor) {
        Color old = this.checkColor;
        this.checkColor = checkColor;
        firePropertyChange("checkColor", old, checkColor);
        repaint();
    }

    @Override
    public Icon getIcon() {
        return new CheckIcon(false);
    }

    @Override
    public Icon getSelectedIcon() {
        return new CheckIcon(true);
    }

    /**
     * Ícone customizado do checkbox — renderiza um quadrado arredondado
     * com check mark quando selecionado.
     */
    private class CheckIcon implements Icon {
        private final boolean checked;

        CheckIcon(boolean checked) { this.checked = checked; }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = 14;
            int cx = x + 1; // 1 pixel de margem
            int cy = y + 1;

            if (checked) {
                // Fundo colorido
                g2.setColor(checkColor);
                g2.fillRoundRect(cx, cy, size, size, 4, 4);
                // Check mark branco
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(cx + 3, cy + 7, cx + 5, cy + 10);
                g2.drawLine(cx + 5, cy + 10, cx + 10, cy + 4);
            } else {
                // Borda cinza
                g2.setColor(new Color(203, 213, 225));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(cx, cy, size, size, 4, 4);
            }
            g2.dispose();
        }

        @Override public int getIconWidth() { return 16; }
        @Override public int getIconHeight() { return 16; }
    }
}
