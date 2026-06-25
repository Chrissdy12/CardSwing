package com.cardSwing;

import java.awt.*;

/**
 * Layout manager responsivo que reorganiza componentes em múltiplas linhas.
 * Usado internamente pelo CardListPanel.
 */
public class WrapLayout extends FlowLayout {

    WrapLayout() { super(); }
    WrapLayout(int align) { super(align); }
    WrapLayout(int align, int hgap, int vgap) { super(align, hgap, vgap); }

    @Override
    public Dimension preferredLayoutSize(Container target) {
        return layoutSize(target, true);
    }

    @Override
    public Dimension minimumLayoutSize(Container target) {
        Dimension d = layoutSize(target, false);
        d.width -= (getHgap() + 1);
        return d;
    }

    private int calculateCardWidth(Container target) {
        // Encontra a LARGURA MAXIMA REAL que os cards pedem para nao cortar nenhum conteudo!
        int maxPrefW = 0;
        for (int i = 0; i < target.getComponentCount(); i++) {
            Component m = target.getComponent(i);
            if (m.isVisible()) {
                maxPrefW = Math.max(maxPrefW, m.getPreferredSize().width);
            }
        }
        
        // Retorna o tamanho exato que eles precisam, para não ficarem espremidos!
        return maxPrefW > 0 ? maxPrefW : 320;
    }

    private Dimension layoutSize(Container target, boolean preferred) {
        synchronized (target.getTreeLock()) {
            int w = target.getSize().width;
            if (w == 0) w = Integer.MAX_VALUE;
            int hgap = getHgap(), vgap = getVgap();
            Insets insets = target.getInsets();
            int maxW = w - (insets.left + insets.right + hgap * 2);

            int cardW = calculateCardWidth(target);

            // Avisar aos componentes a nova largura para que eles quebrem as linhas corretamente (Wrap)!
            for (int i = 0; i < target.getComponentCount(); i++) {
                Component m = target.getComponent(i);
                if (m.isVisible()) {
                    m.setSize(cardW, m.getHeight());
                }
            }

            int x = 0, y = insets.top + vgap, rowH = 0, reqW = 0;
            for (int i = 0; i < target.getComponentCount(); i++) {
                Component m = target.getComponent(i);
                if (m.isVisible()) {
                    Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();
                    if (x == 0 || x + cardW <= maxW) {
                        if (x > 0) x += hgap;
                        x += cardW;
                        rowH = Math.max(rowH, d.height);
                    } else {
                        x = cardW;
                        y += vgap + rowH;
                        rowH = d.height;
                    }
                    reqW = Math.max(reqW, x);
                }
            }
            return new Dimension(reqW + insets.left + insets.right + hgap * 2, y + rowH + insets.bottom + vgap);
        }
    }

    @Override
    public void layoutContainer(Container target) {
        synchronized (target.getTreeLock()) {
            Insets insets = target.getInsets();
            int maxW = target.getSize().width - (insets.left + insets.right + getHgap() * 2);
            int nmembers = target.getComponentCount();
            if (nmembers == 0) return;

            int cardW = calculateCardWidth(target);

            // Setar a largura para garantir precisão nas alturas do texto
            for (int i = 0; i < nmembers; i++) {
                Component m = target.getComponent(i);
                if (m.isVisible()) {
                    m.setSize(cardW, m.getHeight());
                }
            }

            int x = 0, y = insets.top + getVgap();
            int rowH = 0;
            int start = 0;

            for (int i = 0; i < nmembers; i++) {
                Component m = target.getComponent(i);
                if (m.isVisible()) {
                    Dimension d = m.getPreferredSize();
                    if (x == 0 || x + cardW <= maxW) {
                        if (x > 0) x += getHgap();
                        x += cardW;
                        rowH = Math.max(rowH, d.height);
                    } else {
                        layoutRow(target, start, i, y, rowH, cardW);
                        x = cardW;
                        y += getVgap() + rowH;
                        rowH = d.height;
                        start = i;
                    }
                }
            }
            layoutRow(target, start, nmembers, y, rowH, cardW);
        }
    }

    private void layoutRow(Container target, int start, int end, int y, int rowH, int cardW) {
        int x = target.getInsets().left + getHgap();
        for (int i = start; i < end; i++) {
            Component m = target.getComponent(i);
            if (m.isVisible()) {
                m.setBounds(x, y, cardW, rowH);
                x += cardW + getHgap();
            }
        }
    }
}
