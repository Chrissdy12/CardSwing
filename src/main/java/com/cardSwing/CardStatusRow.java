package com.cardSwing;

import java.awt.*;
import java.io.Serializable;
import javax.swing.*;

/**
 * Linha horizontal de badges de status.
 *
 * <p><b>PALETTE:</b> Arraste para dentro de um CardPanel, depois arraste
 * CardStatusBadges para dentro.</p>
 *
 * <p>Programaticamente:</p>
 * <pre>
 *   CardStatusRow row = new CardStatusRow();
 *   row.addStatus("Pendentes", "15", Color.RED);
 *   row.addStatus("OK", "3", Color.GREEN);
 * </pre>
 */
public class CardStatusRow extends JPanel implements Serializable {

    private static final long serialVersionUID = 1L;

    private int gap = 12;

    public CardStatusRow() {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, gap, 2));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        setPreferredSize(new Dimension(100, 30));
    }

    /** Adiciona um badge de status. */
    public CardStatusRow addStatus(CardStatusBadge badge) {
        add(badge);
        revalidate();
        return this;
    }

    /** Adiciona um status rápido com label, valor e cor. */
    public CardStatusRow addStatus(String label, String value, Color color) {
        return addStatus(new CardStatusBadge(label, value, color));
    }

    /** Espaço entre badges. */
    public int getGap() { return gap; }
    public void setGap(int gap) {
        int old = this.gap;
        this.gap = Math.max(0, gap);
        setLayout(new FlowLayout(FlowLayout.LEFT, this.gap, 2));
        firePropertyChange("gap", old, this.gap);
        revalidate();
    }
}
