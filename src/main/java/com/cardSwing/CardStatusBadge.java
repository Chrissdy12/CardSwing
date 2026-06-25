package com.cardSwing;

import java.awt.*;
import java.io.Serializable;
import javax.swing.*;

/**
 * Badge de status colorido — exibe "Rótulo: Valor" com cor de destaque.
 *
 * <p><b>PALETTE:</b> Arraste para dentro de um CardPanel ou CardStatusRow.</p>
 *
 * <p>Propriedades editáveis:</p>
 * <ul>
 *   <li><b>label</b> — rótulo descritivo (ex: "Pendentes")</li>
 *   <li><b>value</b> — valor exibido (ex: "15")</li>
 *   <li><b>valueColor</b> — cor do valor</li>
 *   <li><b>showDot</b> — exibe bolinha colorida antes do valor</li>
 * </ul>
 */
public class CardStatusBadge extends JPanel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String label = "Status";
    private String value = "0";
    private Color valueColor = new Color(59, 130, 246);
    private boolean showDot = true;
    private int fontSize = 12;

    private final JLabel labelPart;
    private final JLabel valuePart;
    private final JPanel dotPanel;

    public CardStatusBadge() {
        this("Status", "0", new Color(59, 130, 246));
    }

    public CardStatusBadge(String label, String value, Color valueColor) {
        this.label = label;
        this.value = value;
        this.valueColor = valueColor;

        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        // Bolinha colorida
        dotPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CardStatusBadge.this.valueColor);
                int dotSize = getDotSize();
                int y = (getHeight() - dotSize) / 2;
                g2.fillOval(1, Math.max(0, y), dotSize, dotSize);
                g2.dispose();
            }
        };
        dotPanel.setOpaque(false);
        updateDotSize();

        // Rótulo
        labelPart = new JLabel(label + ":");
        labelPart.setFont(new Font("Segoe UI", Font.PLAIN, fontSize));
        labelPart.setForeground(new Color(100, 116, 139));

        // Valor
        valuePart = new JLabel(value);
        valuePart.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        valuePart.setForeground(valueColor);

        if (showDot) add(dotPanel);
        add(labelPart);
        add(valuePart);
    }

    private void rebuild() {
        removeAll();
        if (showDot) add(dotPanel);
        labelPart.setText(label + ":");
        valuePart.setText(value);
        valuePart.setForeground(valueColor);
        add(labelPart);
        add(valuePart);
        revalidate();
        repaint();
    }

    private int getDotSize() {
        return Math.max(4, (int) (fontSize * 0.66));
    }

    private void updateDotSize() {
        if (dotPanel != null) {
            int dotSize = getDotSize();
            dotPanel.setPreferredSize(new Dimension(dotSize + 4, fontSize + 4));
            dotPanel.revalidate();
            dotPanel.repaint();
        }
    }

    @Override
    public Font getFont() {
        if (labelPart != null) {
            return labelPart.getFont();
        }
        return super.getFont();
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        if (font != null) {
            this.fontSize = font.getSize();
            if (labelPart != null) {
                labelPart.setFont(new Font(font.getFamily(), font.getStyle(), this.fontSize));
            }
            if (valuePart != null) {
                valuePart.setFont(new Font(font.getFamily(), font.getStyle(), this.fontSize));
            }
            updateDotSize();
        }
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
        if (labelPart != null) {
            labelPart.setForeground(fg);
        }
    }

    // === PROPRIEDADES ===

    /** Rótulo descritivo (ex: "Pendentes"). */
    public String getLabel() { return label; }
    public void setLabel(String label) {
        String old = this.label;
        this.label = label != null ? label : "";
        firePropertyChange("label", old, this.label);
        rebuild();
    }

    /** Valor exibido (ex: "15"). */
    public String getValue() { return value; }
    public void setValue(String value) {
        String old = this.value;
        this.value = value != null ? value : "";
        firePropertyChange("value", old, this.value);
        rebuild();
    }

    /** Cor do valor e da bolinha. */
    public Color getValueColor() { return valueColor; }
    public void setValueColor(Color valueColor) {
        Color old = this.valueColor;
        this.valueColor = valueColor;
        firePropertyChange("valueColor", old, valueColor);
        rebuild();
    }

    /** Se true, exibe bolinha colorida antes do valor. */
    public boolean isShowDot() { return showDot; }
    public void setShowDot(boolean showDot) {
        boolean old = this.showDot;
        this.showDot = showDot;
        firePropertyChange("showDot", old, showDot);
        rebuild();
    }

    public int getFontSize() { return fontSize; }
    public void setFontSize(int fontSize) {
        int old = this.fontSize;
        this.fontSize = Math.max(8, fontSize);
        Font currentFont = getFont();
        if (currentFont != null) {
            setFont(new Font(currentFont.getFamily(), currentFont.getStyle(), this.fontSize));
        } else {
            setFont(new Font("Segoe UI", Font.PLAIN, this.fontSize));
        }
        firePropertyChange("fontSize", old, this.fontSize);
    }
}
