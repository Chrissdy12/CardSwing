package com.cardSwing;

import java.awt.*;
import java.io.Serializable;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Painel completo para exibir uma lista de cards com scroll.
 * Contém JScrollPane + WrapLayout internamente.
 *
 * <p><b>PALETTE:</b> Arraste para o form. Use {@code setModel()} para
 * conectar dados, ou adicione CardPanels diretamente.</p>
 *
 * <p>Propriedades editáveis:</p>
 * <ul>
 *   <li><b>horizontalGap</b> — espaço horizontal entre cards</li>
 *   <li><b>verticalGap</b> — espaço vertical entre linhas</li>
 *   <li><b>scrollSpeed</b> — velocidade do scroll</li>
 * </ul>
 */
public class CardListPanel extends JPanel implements Serializable {

    private static final long serialVersionUID = 1L;

    private final JPanel container;
    private final JScrollPane scrollPane;

    private int horizontalGap = 15;
    private int verticalGap = 15;
    private int scrollSpeed = 16;
    private int columns = 0; // 0 = Fluido (WrapLayout), > 0 = Fixo (GridLayout)
    private boolean showSearch = false;
    private transient Consumer<String> onSearch;
    private transient Consumer<Void> onScrollEnd;

    private final JPanel searchPanel;
    private final JTextField searchField;
    private final Timer searchDebounceTimer;

    public CardListPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        container = new JPanel();
        updateLayout();
        container.setBorder(new EmptyBorder(15, 15, 15, 15));
        container.setBackground(new Color(245, 247, 250));

        scrollPane = new JScrollPane(container);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(scrollSpeed);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(new Color(245, 247, 250));

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        
        searchDebounceTimer = new Timer(400, e -> {
            if (onSearch != null) onSearch.accept(searchField.getText());
        });
        searchDebounceTimer.setRepeats(false);
        
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { searchDebounceTimer.restart(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { searchDebounceTimer.restart(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { searchDebounceTimer.restart(); }
        });

        searchPanel = new JPanel(new BorderLayout());
        searchPanel.setOpaque(false);
        searchPanel.setBorder(new EmptyBorder(15, 15, 0, 15));
        searchPanel.add(searchField, BorderLayout.CENTER);

        // Paginação Infinita
        JScrollBar vbar = scrollPane.getVerticalScrollBar();
        vbar.addAdjustmentListener(e -> {
            if (!e.getValueIsAdjusting()) {
                if (vbar.getValue() + vbar.getVisibleAmount() >= vbar.getMaximum() - 20) {
                    if (onScrollEnd != null) onScrollEnd.accept(null);
                }
            }
        });

        add(scrollPane, BorderLayout.CENTER);
    }

    /** Retorna o container interno (para acesso avançado). */
    public JPanel getContainer() { return container; }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        if (container != null) container.setBackground(bg);
        if (scrollPane != null && scrollPane.getViewport() != null) scrollPane.getViewport().setBackground(bg);
    }

    /** Adiciona um componente como card. */
    public void addCard(Component card) {
        container.add(card);
        container.revalidate();
        container.repaint();
    }

    /** Adiciona uma lista de componentes como cards de uma vez. */
    public void addCards(java.util.List<? extends Component> cards) {
        if (cards == null || cards.isEmpty()) return;
        for (Component card : cards) {
            container.add(card);
        }
        container.revalidate();
        container.repaint();
    }

    /** Remove um card. */
    public void removeCard(Component card) {
        container.remove(card);
        container.revalidate();
        container.repaint();
    }

    /** Remove todos os cards. */
    public void clearCards() {
        container.removeAll();
        container.revalidate();
        container.repaint();
    }

    /** Número de cards. */
    public int getCardCount() { return container.getComponentCount(); }

    // === PROPRIEDADES ===

    private void updateLayout() {
        if (container == null) return;
        if (columns > 0) {
            container.setLayout(new GridLayout(0, columns, horizontalGap, verticalGap));
        } else {
            container.setLayout(new WrapLayout(FlowLayout.LEFT, horizontalGap, verticalGap));
        }
        container.revalidate();
    }

    public int getColumns() { return columns; }
    public void setColumns(int columns) {
        int old = this.columns;
        this.columns = Math.max(0, columns);
        firePropertyChange("columns", old, this.columns);
        updateLayout();
    }

    public int getHorizontalGap() { return horizontalGap; }
    public void setHorizontalGap(int horizontalGap) {
        int old = this.horizontalGap;
        this.horizontalGap = Math.max(0, horizontalGap);
        firePropertyChange("horizontalGap", old, this.horizontalGap);
        updateLayout();
    }

    public int getVerticalGap() { return verticalGap; }
    public void setVerticalGap(int verticalGap) {
        int old = this.verticalGap;
        this.verticalGap = Math.max(0, verticalGap);
        firePropertyChange("verticalGap", old, this.verticalGap);
        updateLayout();
    }

    public int getScrollSpeed() { return scrollSpeed; }
    public void setScrollSpeed(int scrollSpeed) {
        int old = this.scrollSpeed;
        this.scrollSpeed = Math.max(1, scrollSpeed);
        scrollPane.getVerticalScrollBar().setUnitIncrement(this.scrollSpeed);
        firePropertyChange("scrollSpeed", old, this.scrollSpeed);
    }

    public boolean isShowSearch() { return showSearch; }
    public void setShowSearch(boolean showSearch) {
        boolean old = this.showSearch;
        this.showSearch = showSearch;
        firePropertyChange("showSearch", old, showSearch);
        if (showSearch) {
            add(searchPanel, BorderLayout.NORTH);
        } else {
            remove(searchPanel);
        }
        revalidate();
        repaint();
    }

    public void setOnSearch(Consumer<String> onSearch) {
        this.onSearch = onSearch;
    }

    public void setOnScrollEnd(Consumer<Void> onScrollEnd) {
        this.onScrollEnd = onScrollEnd;
    }


}
