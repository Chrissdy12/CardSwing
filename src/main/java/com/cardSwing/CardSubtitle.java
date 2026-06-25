package com.cardSwing;

import java.awt.*;
import java.io.Serializable;
import javax.swing.*;

/**
 * Subtítulo do Card — texto menor e cinza.
 *
 * <p><b>PALETTE:</b> Arraste para dentro de um CardPanel.</p>
 */
public class CardSubtitle extends JLabel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Color subtitleColor = new Color(100, 116, 139);
    private int fontSize = 12;

    public CardSubtitle() {
        this("Subtítulo do card");
    }

    public CardSubtitle(String text) {
        super(text);
        setFont(new Font("Segoe UI", Font.PLAIN, fontSize));
        setForeground(subtitleColor);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
    }

    public Color getSubtitleColor() { return subtitleColor; }
    public void setSubtitleColor(Color subtitleColor) {
        Color old = this.subtitleColor;
        this.subtitleColor = subtitleColor;
        setForeground(subtitleColor);
        firePropertyChange("subtitleColor", old, subtitleColor);
    }

    public int getFontSize() { return fontSize; }
    public void setFontSize(int fontSize) {
        int old = this.fontSize;
        this.fontSize = Math.max(8, fontSize);
        setFont(new Font("Segoe UI", Font.PLAIN, this.fontSize));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, this.fontSize + 12));
        firePropertyChange("fontSize", old, this.fontSize);
    }
}
