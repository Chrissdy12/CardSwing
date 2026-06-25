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

    private Dimension layoutSize(Container target, boolean preferred) {
        synchronized (target.getTreeLock()) {
            int w = target.getSize().width;
            if (w == 0) w = Integer.MAX_VALUE;
            int hgap = getHgap(), vgap = getVgap();
            Insets insets = target.getInsets();
            int maxW = w - (insets.left + insets.right + hgap * 2);
            int x = 0, y = insets.top + vgap, rowH = 0, reqW = 0;
            for (int i = 0; i < target.getComponentCount(); i++) {
                Component m = target.getComponent(i);
                if (m.isVisible()) {
                    Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();
                    if (x == 0 || x + d.width <= maxW) {
                        if (x > 0) x += hgap;
                        x += d.width;
                        rowH = Math.max(rowH, d.height);
                    } else {
                        x = d.width;
                        y += vgap + rowH;
                        rowH = d.height;
                    }
                    reqW = Math.max(reqW, x);
                }
            }
            return new Dimension(reqW + insets.left + insets.right + hgap * 2, y + rowH + insets.bottom + vgap);
        }
    }
}
