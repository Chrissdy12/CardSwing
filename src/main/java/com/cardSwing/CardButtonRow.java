package com.cardSwing;

import java.awt.*;
import java.io.Serializable;
import javax.swing.*;

/**
 * Linha horizontal de botões — alinhados à direita.
 *
 * <p><b>PALETTE:</b> Arraste para dentro de um CardPanel, depois arraste
 * CardButtons para dentro deste CardButtonRow.</p>
 *
 * <p>Programaticamente:</p>
 * <pre>
 *   CardButtonRow row = new CardButtonRow();
 *   row.addButton(new CardButton("Salvar", Color.BLUE));
 *   row.addButton(new CardButton("Cancelar", Color.GRAY));
 * </pre>
 */
public class CardButtonRow extends JPanel implements Serializable {

    private static final long serialVersionUID = 1L;

    private int gap = 8;

    public CardButtonRow() {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.RIGHT, gap, 4));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        setPreferredSize(new Dimension(100, 44));
    }

    /** Adiciona um botão à linha. */
    public CardButtonRow addButton(CardButton button) {
        add(button);
        revalidate();
        return this;
    }

    /** Adiciona um botão com texto e ação rápida. */
    public CardButtonRow addButton(String text, Color cor, Runnable action) {
        CardButton btn = new CardButton(text, cor);
        if (action != null) btn.addActionListener(e -> action.run());
        return addButton(btn);
    }

    /** Espaço entre botões. */
    public int getGap() { return gap; }
    public void setGap(int gap) {
        int old = this.gap;
        this.gap = Math.max(0, gap);
        setLayout(new FlowLayout(FlowLayout.RIGHT, this.gap, 4));
        firePropertyChange("gap", old, this.gap);
        revalidate();
    }
}
