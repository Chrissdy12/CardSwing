package com.cardSwing;

import java.awt.*;
import java.io.Serializable;
import javax.swing.*;

/**
 * Tag/Badge colorida — rótulo pequeno com fundo colorido (tipo "Ativo", "Urgente").
 *
 * <p><b>PALETTE:</b> Arraste para dentro de um CardPanel.</p>
 *
 * <p>Propriedades editáveis:</p>
 * <ul>
 *   <li><b>text</b> — texto da tag</li>
 *   <li><b>tagColor</b> — cor de fundo</li>
 *   <li><b>tagTextColor</b> — cor do texto</li>
 * </ul>
 */
public class CardTag extends JPanel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String text = "Tag";
    private Color tagColor = new Color(219, 234, 254);
    private Color tagTextColor = new Color(29, 78, 216);
    private int tagRadius = 12;

    private final JLabel label;

    public CardTag() {
        this("Tag");
    }

    public CardTag(String text) {
        this(text, new Color(219, 234, 254), new Color(29, 78, 216));
    }

    public CardTag(String text, Color tagColor, Color tagTextColor) {
        this.text = text;
        this.tagColor = tagColor;
        this.tagTextColor = tagTextColor;

        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        label = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CardTag.this.tagColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), tagRadius, tagRadius);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(tagTextColor);
        label.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        add(label);
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        if (label != null) {
            label.setFont(font);
        }
    }

    // === PROPRIEDADES ===

    /** Texto da tag. */
    public String getText() { return text; }
    public void setText(String text) {
        String old = this.text;
        this.text = text != null ? text : "";
        if (label != null) label.setText(this.text);
        firePropertyChange("text", old, this.text);
    }

    /** Cor de fundo da tag. */
    public Color getTagColor() { return tagColor; }
    public void setTagColor(Color tagColor) {
        Color old = this.tagColor;
        this.tagColor = tagColor;
        firePropertyChange("tagColor", old, tagColor);
        repaint();
    }

    /** Cor do texto da tag. */
    public Color getTagTextColor() { return tagTextColor; }
    public void setTagTextColor(Color tagTextColor) {
        Color old = this.tagTextColor;
        this.tagTextColor = tagTextColor;
        if (label != null) label.setForeground(tagTextColor);
        firePropertyChange("tagTextColor", old, tagTextColor);
    }

    /** Raio dos cantos da tag. */
    public int getTagRadius() { return tagRadius; }
    public void setTagRadius(int tagRadius) {
        int old = this.tagRadius;
        this.tagRadius = Math.max(0, tagRadius);
        firePropertyChange("tagRadius", old, this.tagRadius);
        repaint();
    }

    public int getFontSize() { 
        return label != null && label.getFont() != null ? label.getFont().getSize() : 11; 
    }
    public void setFontSize(int fontSize) {
        int old = getFontSize();
        int newSize = Math.max(8, fontSize);
        if (label != null && label.getFont() != null) {
            Font f = label.getFont();
            label.setFont(new Font(f.getFamily(), f.getStyle(), newSize));
        }
        firePropertyChange("fontSize", old, newSize);
        revalidate();
        repaint();
    }
}
