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
        
        addComponentListener(new java.awt.event.ComponentAdapter() {
            int lastWidth = -1;
            public void componentResized(java.awt.event.ComponentEvent e) {
                if (lineWrap && getWidth() != lastWidth) {
                    lastWidth = getWidth();
                    revalidate();
                }
            }
        });
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
        Font currentFont = getFont();
        if (currentFont != null) {
            setFont(new Font(currentFont.getFamily(), currentFont.getStyle(), this.fontSize));
        } else {
            setFont(new Font("Segoe UI", Font.BOLD, this.fontSize));
        }
        setMaximumSize(new Dimension(Integer.MAX_VALUE, this.fontSize + 15));
        firePropertyChange("fontSize", old, this.fontSize);
    }

    private boolean lineWrap = false;

    public boolean isLineWrap() { return lineWrap; }
    public void setLineWrap(boolean lineWrap) {
        boolean old = this.lineWrap;
        this.lineWrap = lineWrap;
        firePropertyChange("lineWrap", old, lineWrap);
        revalidate();
        repaint();
    }

    protected void paintComponent(Graphics g) {
        if (!lineWrap || getText() == null || getText().toLowerCase().startsWith("<html>")) {
            super.paintComponent(g);
            return;
        }

        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(getFont());
        g2.setColor(getForeground());

        FontMetrics fm = g2.getFontMetrics();
        int y = fm.getAscent() + getInsets().top;
        int width = getWidth() - getInsets().left - getInsets().right;
        if (width <= 0) width = 10;

        String[] lines = getText().split("\n");
        for (String pLine : lines) {
            String[] words = pLine.split(" ");
            String currentLine = "";
            for (String word : words) {
                if (fm.stringWidth(currentLine + word) <= width) {
                    currentLine += word + " ";
                } else {
                    if (!currentLine.isEmpty()) {
                        g2.drawString(currentLine, getInsets().left, y);
                        y += fm.getHeight();
                    }
                    currentLine = word + " ";
                }
            }
            if (!currentLine.isEmpty()) {
                g2.drawString(currentLine, getInsets().left, y);
                y += fm.getHeight();
            }
        }
        g2.dispose();
    }

    public Dimension getPreferredSize() {
        if (!lineWrap || getText() == null || getText().toLowerCase().startsWith("<html>")) {
            return super.getPreferredSize();
        }
        
        FontMetrics fm = getFontMetrics(getFont());
        int width = getWidth();
        if (width <= 0) return super.getPreferredSize();
        
        int w = width - getInsets().left - getInsets().right;
        if (w <= 0) w = 10;

        int linesCount = 0;
        String[] lines = getText().split("\n");
        for (String pLine : lines) {
            String[] words = pLine.split(" ");
            String currentLine = "";
            for (String word : words) {
                if (fm.stringWidth(currentLine + word) <= w) {
                    currentLine += word + " ";
                } else {
                    if (!currentLine.isEmpty()) {
                        linesCount++;
                    }
                    currentLine = word + " ";
                }
            }
            if (!currentLine.isEmpty()) {
                linesCount++;
            }
        }
        return new Dimension(width, (linesCount * fm.getHeight()) + getInsets().top + getInsets().bottom);
    }
}
