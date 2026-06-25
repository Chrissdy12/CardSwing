package com.cardSwing;

import java.awt.*;
import java.io.Serializable;
import javax.swing.*;

/**
 * Título do Card — texto grande e negrito.
 *
 * <p><b>PALETTE:</b> Arraste para dentro de um CardPanel.</p>
 *
 * <p>Propriedades editáveis no Property Sheet:</p>
 * <ul>
 *   <li><b>text</b> — o texto do título</li>
 *   <li><b>titleColor</b> — cor do texto</li>
 *   <li><b>fontSize</b> — tamanho da fonte</li>
 * </ul>
 */
public class CardTitle extends JLabel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Color titleColor = new Color(15, 23, 42);
    private int fontSize = 15;

    /** Construtor sem argumentos — NetBeans exige. */
    public CardTitle() {
        this("Título do Card");
    }

    /** Construtor com texto. */
    public CardTitle(String text) {
        super(text);
        setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        setForeground(titleColor);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
    }

    // === PROPRIEDADES ===

    /** Cor do texto do título. */
    public Color getTitleColor() { return titleColor; }
    public void setTitleColor(Color titleColor) {
        Color old = this.titleColor;
        this.titleColor = titleColor;
        setForeground(titleColor);
        firePropertyChange("titleColor", old, titleColor);
    }

    /** Tamanho da fonte. */
    public int getFontSize() { return fontSize; }
    public void setFontSize(int fontSize) {
        int old = this.fontSize;
        this.fontSize = Math.max(8, fontSize);
        setFont(new Font("Segoe UI", Font.BOLD, this.fontSize));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, this.fontSize + 15));
        firePropertyChange("fontSize", old, this.fontSize);
    }
}
