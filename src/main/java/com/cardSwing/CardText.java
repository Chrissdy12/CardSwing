package com.cardSwing;

import java.awt.*;
import java.io.Serializable;
import javax.swing.*;

/**
 * Texto de corpo do Card — suporta múltiplas linhas via HTML.
 *
 * <p><b>PALETTE:</b> Arraste para dentro de um CardPanel.</p>
 *
 * <p>Para múltiplas linhas, use HTML: {@code <html>Linha 1<br>Linha 2</html>}</p>
 */
public class CardText extends JLabel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Color textColor = new Color(71, 85, 105);
    private int fontSize = 12;

    public CardText() {
        this("Texto do card");
    }

    public CardText(String text) {
        super(text);
        setFont(new Font("Segoe UI", Font.PLAIN, fontSize));
        setForeground(textColor);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        if (font != null && font.getSize() != this.fontSize) {
            this.fontSize = font.getSize();
        }
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
        if (this.textColor != fg && fg != null) {
            this.textColor = fg;
        }
    }

    public Color getTextColor() { return textColor; }
    public void setTextColor(Color textColor) {
        Color old = this.textColor;
        this.textColor = textColor;
        setForeground(textColor);
        firePropertyChange("textColor", old, textColor);
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
