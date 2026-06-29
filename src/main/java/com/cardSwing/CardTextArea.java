package com.cardSwing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.Serializable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 * Text Area do Card — componente de texto, suporta quebra de linha nativa.
 *
 * <p>
 * <b>PALETTE:</b> Arraste para dentro de um CardPanel.
 * </p>
 */
public class CardTextArea extends JTextArea implements Serializable {

    private static final long serialVersionUID = 1L;

    private int radius = 8;
    private Color borderColor = new Color(209, 213, 219); // Gray 300
    private Color focusColor = new Color(59, 130, 246); // Blue 500
    private Color hoverColor = new Color(156, 163, 175); // Gray 400
    private boolean isHovered = false;
    private String placeholder = "";
    private boolean labelMode = false; // Define se vai se comportar visualmente como um JLabel (sem fundo e sem borda)

    public CardTextArea() {
        this("");
    }

    public CardTextArea(String text) {
        super(text);
        setup();
    }

    private void setup() {
        setFont(new Font("Segoe UI", Font.PLAIN, 12));
        setForeground(new Color(31, 41, 55)); // Gray 800
        setBackground(Color.WHITE);
        setCaretColor(focusColor);
        setOpaque(false);
        setBorder(new EmptyBorder(8, 12, 8, 12));
        setLineWrap(true);
        setWrapStyleWord(true);
        setColumns(10);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                isHovered = true;
                if (!hasFocus())
                    repaint();
                if (!isEditable())
                    forwardEventToParent(e);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                isHovered = false;
                if (!hasFocus())
                    repaint();
                if (!isEditable())
                    forwardEventToParent(e);
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (!isEditable())
                    forwardEventToParent(e);
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (!isEditable())
                    forwardEventToParent(e);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                if (!isEditable())
                    forwardEventToParent(e);
            }
        });

        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                if (!isEditable())
                    forwardEventToParent(e);
            }

            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                if (!isEditable())
                    forwardEventToParent(e);
            }
        });

        // Garante que o Caret (cursor piscante) não suma em fundos
        // transparentes/arredondados
        setCaret(new javax.swing.text.DefaultCaret() {
            @Override
            public void paint(Graphics g) {
                g.setColor(getCaretColor());
                super.paint(g);
            }

            @Override
            protected synchronized void damage(Rectangle r) {
                if (r == null)
                    return;
                x = r.x;
                y = r.y;
                width = r.width;
                height = r.height;
                repaint(); // Repaint total do componente para evitar rastros ou sumiços
            }
        });
        getCaret().setBlinkRate(500);

        addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background - só desenha se não estiver no modo label
        if (!labelMode) {
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        }

        // Set UI to not paint background by enforcing transparency right before super
        // paint
        boolean wasOpaque = isOpaque();
        super.setOpaque(false);
        super.paintComponent(g);
        super.setOpaque(wasOpaque);

        // Placeholder
        if (getText().isEmpty() && placeholder != null && !placeholder.isEmpty() && !hasFocus()) {
            g2.setColor(new Color(156, 163, 175)); // Gray 400
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int y = fm.getAscent() + getInsets().top;
            g2.drawString(placeholder, getInsets().left, y);
        }

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        if (labelMode)
            return; // Se for modo label, não desenha bordas de jeito nenhum

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (hasFocus()) {
            g2.setColor(focusColor);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, radius, radius);
        } else {
            g2.setColor(isHovered ? hoverColor : borderColor);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        }

        g2.dispose();
    }

    // === PROPRIEDADES ===
    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }

    public Color getFocusColor() {
        return focusColor;
    }

    public void setFocusColor(Color focusColor) {
        this.focusColor = focusColor;
        setCaretColor(focusColor);
        repaint();
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }

    public Color getHoverColor() {
        return hoverColor;
    }

    public void setHoverColor(Color hoverColor) {
        this.hoverColor = hoverColor;
        repaint();
    }

    public boolean isLabelMode() {
        return labelMode;
    }

    public void setLabelMode(boolean labelMode) {
        boolean old = this.labelMode;
        this.labelMode = labelMode;
        if (labelMode) {
            setBorder(new EmptyBorder(0, 0, 0, 0)); // Remove os espaços internos
        } else {
            setBorder(new EmptyBorder(8, 12, 8, 12)); // Volta os espaços normais de caixa de texto
        }
        firePropertyChange("labelMode", old, labelMode);
        repaint();
    }

    @Override
    public void setOpaque(boolean isOpaque) {
        super.setOpaque(false); // Sempre falso para permitir o desenho customizado
    }

    @Override
    public void updateUI() {
        // Ignora o LookAndFeel atual (como FlatLaf ou Windows) que injeta fundos cinzas
        // ou bordas quadradas em JTextAreas. Força a UI básica do Java.
        setUI(new javax.swing.plaf.basic.BasicTextAreaUI());
    }

    @Override
    public void addNotify() {
        super.addNotify();
        // O NetBeans GUI Builder automaticamente envelopa JTextAreas em um JScrollPane.
        // O JScrollPane tem bordas quadradas e o JViewport tem fundo cinza, o que
        // destrói o nosso design arredondado e transparente.
        // Este código detecta o JScrollPane pai e o deixa 100% invisível.
        java.awt.Container parent = getParent();
        if (parent instanceof javax.swing.JViewport) {
            javax.swing.JViewport viewport = (javax.swing.JViewport) parent;
            viewport.setOpaque(false);
            viewport.setBackground(new Color(0, 0, 0, 0));

            java.awt.Container grandParent = viewport.getParent();
            if (grandParent instanceof javax.swing.JScrollPane) {
                javax.swing.JScrollPane scrollPane = (javax.swing.JScrollPane) grandParent;
                scrollPane.setOpaque(false);
                scrollPane.setBackground(new Color(0, 0, 0, 0));
                scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());
                scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
                scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            }
        }
    }

    private void forwardEventToParent(java.awt.event.MouseEvent e) {
        java.awt.Container parent = getParent();
        while (parent != null) {
            if (parent instanceof javax.swing.JViewport || parent instanceof javax.swing.JScrollPane) {
                parent = parent.getParent();
            } else {
                java.awt.event.MouseEvent parentEvent = javax.swing.SwingUtilities.convertMouseEvent(this, e, parent);
                parent.dispatchEvent(parentEvent);
                break;
            }
        }
    }
}
