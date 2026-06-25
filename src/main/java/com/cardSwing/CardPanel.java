package com.cardSwing;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.Serializable;
import javax.swing.*;

/**
 * Componente visual de Card — o container principal.
 * Renderiza bordas arredondadas, sombra, barra de tema e efeito hover.
 *
 * <p>
 * <b>PALETTE:</b> Arraste para o form. Depois arraste CardTitle, CardButton,
 * CardCheck, etc. para dentro dele.
 * </p>
 *
 * <p>
 * <b>PROGRAMÁTICO:</b>
 * </p>
 * 
 * <pre>
 * CardPanel card = new CardPanel();
 * card.add(new CardTitle("Meu Título"));
 * card.add(new CardSeparator());
 * card.add(new CardCheck("Ativo"));
 * card.add(new CardButton("Salvar"));
 * </pre>
 */
public class CardPanel extends JPanel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title = "";
    private Color titleColor = Color.WHITE;
    private Color themeColor = new Color(59, 130, 246);
    private Color hoverBorderColor = new Color(99, 102, 241);
    private int cornerRadius = 16;
    private boolean shadowEnabled = true;
    private boolean hoverEnabled = true;
    private boolean hover = false;
    private int cardWidth = 280;

    // Novas Propriedades
    private Icon titleIcon;
    private boolean collapsible = false;
    private boolean collapsed = false;
    private boolean clickable = false;
    private Point clickPoint = null;
    private float rippleRadius = 0f;
    private float rippleAlpha = 0f;
    private Timer rippleTimer;

    public CardPanel() {
        setOpaque(false);
        // Sem layout forçado - o NetBeans vai usar o Free Design (GroupLayout) ou
        // FlowLayout por padrão.
        setBackground(Color.WHITE);

        updatePadding();

        rippleTimer = new Timer(16, e -> {
            rippleRadius += 10f;
            rippleAlpha -= 0.05f;
            if (rippleAlpha <= 0f) {
                rippleAlpha = 0f;
                rippleTimer.stop();
            }
            repaint();
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (hoverEnabled) {
                    hover = true;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (clickable) {
                    clickPoint = e.getPoint();
                    rippleRadius = 10f;
                    rippleAlpha = 0.5f;
                    rippleTimer.start();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                boolean hasTitle = (title != null && !title.trim().isEmpty());
                int headerHeight = hasTitle ? 38 : 5;
                if (collapsible && e.getY() <= headerHeight) {
                    collapsed = !collapsed;
                    for (Component c : getComponents()) {
                        c.setVisible(!collapsed);
                    }
                    revalidate();
                    repaint();
                }
            }
        });
    }

    private void updatePadding() {
        int sh = shadowEnabled ? 5 : 0;
        int top = (title != null && !title.trim().isEmpty()) ? 48 : 22;
        setBorder(BorderFactory.createEmptyBorder(top, 10, 10 + sh, 10 + sh));
        revalidate();
        repaint();
    }

    /**
     * Calcula o tamanho preferido com largura fixa e altura dinâmica
     * baseada no conteúdo (filhos do BoxLayout).
     */
    @Override
    public Dimension getPreferredSize() {
        int sh = shadowEnabled ? 5 : 0;
        if (collapsed) {
            boolean hasTitle = (title != null && !title.trim().isEmpty());
            int headerHeight = hasTitle ? 38 : 5;
            return new Dimension(cardWidth + sh, headerHeight + sh);
        }

        LayoutManager layout = getLayout();
        int h = 50; // altura padrão caso não seja possível calcular
        if (layout != null) {
            Dimension layoutPref = layout.preferredLayoutSize(this);
            if (layoutPref != null) {
                h = layoutPref.height;
            }
        } else {
            Dimension sup = super.getPreferredSize();
            if (sup != null) {
                h = sup.height;
            }
        }

        // A altura (h) já inclui a margem inferior que configuramos no updatePadding, 
        // e essa margem agora já engloba o espaço da sombra (+ sh). 
        // Portanto, não precisamos somar 'sh' novamente aqui na altura.
        return new Dimension(cardWidth + sh, h);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(200, 80);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int sh = shadowEnabled ? 5 : 0;
        int w = getWidth() - sh;
        int h = getHeight() - sh;
        int r = cornerRadius;

        // === SOMBRA ===
        if (shadowEnabled) {
            for (int i = 4; i >= 1; i--) {
                g2.setColor(new Color(0, 0, 0, 8 + (4 - i) * 5));
                g2.fill(new RoundRectangle2D.Float(i, i + 1, w, h, r + 2, r + 2));
            }
        }

        // === FUNDO ===
        RoundRectangle2D bgRect = new RoundRectangle2D.Float(0, 0, w, h, r, r);
        g2.setColor(hover && hoverEnabled ? new Color(249, 250, 251) : Color.WHITE);
        g2.fill(bgRect);

        // === BORDA ===
        // Desenhada antes do Theme Bar, assim a Theme Bar fica por cima e esconde a
        // linha no topo!
        if (hover && hoverEnabled && hoverBorderColor != null) {
            g2.setColor(hoverBorderColor);
            g2.setStroke(new BasicStroke(2f));
            g2.draw(new RoundRectangle2D.Float(1, 1, w - 2, h - 2, r, r));
        } else {
            g2.setColor(new Color(229, 231, 235));
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1, h - 1, r, r));
        }

        // === BARRA DE TEMA / HEADER ===
        if (themeColor != null) {
            Shape oldClip = g2.getClip();
            g2.setClip(bgRect); // Aplica máscara para respeitar os cantos redondos

            boolean hasTitle = (title != null && !title.trim().isEmpty());
            int headerHeight = hasTitle ? 38 : 5;

            g2.setColor(themeColor);
            g2.fillRect(0, 0, w, headerHeight);

            // Desenha o Título se existir
            if (hasTitle) {
                int textX = 10;
                if (titleIcon != null) {
                    int iconY = (headerHeight - titleIcon.getIconHeight()) / 2;
                    titleIcon.paintIcon(this, g2, textX, iconY);
                    textX += titleIcon.getIconWidth() + 8;
                }

                g2.setColor(titleColor);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                int textY = (headerHeight - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(title, textX, textY);
            }

            g2.setClip(oldClip); // Remove máscara
        }

        // === RIPPLE EFFECT ===
        if (clickable && rippleAlpha > 0f && clickPoint != null) {
            Shape oldClip = g2.getClip();
            g2.setClip(bgRect);
            g2.setColor(new Color(0, 0, 0, rippleAlpha));
            float rr = rippleRadius;
            g2.fill(new java.awt.geom.Ellipse2D.Float(clickPoint.x - rr, clickPoint.y - rr, rr * 2, rr * 2));
            g2.setClip(oldClip);
        }

        g2.dispose();
    }

    // === OVERRIDE para garantir filhos opacos false ===
    @Override
    public Component add(Component comp) {
        if (comp instanceof JComponent) {
            ((JComponent) comp).setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        Component result = super.add(comp);
        return result;
    }

    // =========================================================================
    // PROPRIEDADES (editáveis no Property Sheet do NetBeans)
    // =========================================================================

    /** Cor da barra decorativa no topo do card. */
    public Color getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(Color themeColor) {
        Color old = this.themeColor;
        this.themeColor = themeColor;
        firePropertyChange("themeColor", old, themeColor);
        repaint();
    }

    /** Cor da borda ao passar o mouse. */
    public Color getHoverBorderColor() {
        return hoverBorderColor;
    }

    public void setHoverBorderColor(Color hoverBorderColor) {
        Color old = this.hoverBorderColor;
        this.hoverBorderColor = hoverBorderColor;
        firePropertyChange("hoverBorderColor", old, hoverBorderColor);
    }

    /** Raio das bordas arredondadas (pixels). */
    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        int old = this.cornerRadius;
        this.cornerRadius = Math.max(0, cornerRadius);
        firePropertyChange("cornerRadius", old, this.cornerRadius);
        repaint();
    }

    /** Habilita sombra suave. */
    public boolean isShadowEnabled() {
        return shadowEnabled;
    }

    public void setShadowEnabled(boolean shadowEnabled) {
        boolean old = this.shadowEnabled;
        this.shadowEnabled = shadowEnabled;
        firePropertyChange("shadowEnabled", old, shadowEnabled);
        updatePadding();
    }

    /** Habilita efeito hover (borda + fundo). */
    public boolean isHoverEnabled() {
        return hoverEnabled;
    }

    public void setHoverEnabled(boolean hoverEnabled) {
        boolean old = this.hoverEnabled;
        this.hoverEnabled = hoverEnabled;
        firePropertyChange("hoverEnabled", old, hoverEnabled);
    }

    /** Largura do card (pixels). */
    public int getCardWidth() {
        return cardWidth;
    }

    public void setCardWidth(int cardWidth) {
        int old = this.cardWidth;
        this.cardWidth = Math.max(150, cardWidth);
        firePropertyChange("cardWidth", old, this.cardWidth);
        revalidate();
        repaint();
    }

    /** Título do Header (se preenchido, cria um cabeçalho colorido). */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        String old = this.title;
        this.title = title;
        firePropertyChange("title", old, title);
        updatePadding();
    }

    /** Cor do texto do Título do Header. */
    public Color getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(Color titleColor) {
        Color old = this.titleColor;
        this.titleColor = titleColor;
        firePropertyChange("titleColor", old, titleColor);
        repaint();
    }

    public Icon getTitleIcon() {
        return titleIcon;
    }

    public void setTitleIcon(Icon titleIcon) {
        Icon old = this.titleIcon;
        this.titleIcon = titleIcon;
        firePropertyChange("titleIcon", old, titleIcon);
        repaint();
    }

    public boolean isCollapsible() {
        return collapsible;
    }

    public void setCollapsible(boolean collapsible) {
        boolean old = this.collapsible;
        this.collapsible = collapsible;
        firePropertyChange("collapsible", old, collapsible);
        if (!collapsible && collapsed) {
            collapsed = false;
            for (Component c : getComponents())
                c.setVisible(true);
            revalidate();
            repaint();
        }
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        boolean old = this.clickable;
        this.clickable = clickable;
        if (clickable)
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        else
            setCursor(Cursor.getDefaultCursor());
        firePropertyChange("clickable", old, clickable);
    }
}
