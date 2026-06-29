package com.cardSwing;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

/**
 * ComboBox estilizado para Card — cantos arredondados, borda customizada e menu
 * moderno.
 *
 * <p>
 * <b>PALETTE:</b> Arraste para dentro de um CardPanel.
 * </p>
 */
public class CardComboBox<E> extends JComboBox<E> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int radius = 8;
    private Color borderColor = new Color(209, 213, 219); // Gray 300
    private Color focusColor = new Color(59, 130, 246); // Blue 500
    private Color hoverColor = new Color(156, 163, 175); // Gray 400

    private boolean editable = true;
    private boolean isHovered = false;

    public CardComboBox() {
        super();
        setup();
    }

    public CardComboBox(E[] items) {
        super(items);
        setup();
    }

    private void setup() {
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(new Color(31, 41, 55)); // Gray 800
        setBackground(Color.WHITE);
        setOpaque(false);
        setBorder(new EmptyBorder(8, 12, 8, 12));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFocusable(true);

        setUI(new CustomComboBoxUI());
        setRenderer(new CustomComboBoxRenderer());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                if (!hasFocus() && !isPopupVisible())
                    repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                if (!hasFocus() && !isPopupVisible())
                    repaint();
            }
        });

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });
    }

    /**
     * Define os itens do ComboBox.
     * Adiciona um item nulo no início por padrão para permitir a deseleção.
     * 
     * @param items Lista de itens
     */
    public void setItems(java.util.List<E> items) {
        removeAllItems();
        addItem(null); // Primeiro item vazio para descelecionar
        if (items != null) {
            for (E item : items) {
                addItem(item);
            }
        }
    }

    /**
     * Retorna o item selecionado de forma tipada.
     * 
     * @return O item selecionado
     */
    @SuppressWarnings("unchecked")
    public E getSelectItem() {
        return (E) getSelectedItem();
    }

    @Override
    public void setEditable(boolean editable) {
        boolean old = this.editable;
        this.editable = editable;
        firePropertyChange("editable", old, editable);
        repaint();
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setPopupVisible(boolean v) {
        if (v && !editable) {
            return;
        }
        super.setPopupVisible(v);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (hasFocus() || isPopupVisible()) {
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

    // --- Inner classes for custom UI ---

    private class CustomComboBoxUI extends BasicComboBoxUI {
        @Override
        protected JButton createArrowButton() {
            JButton button = new JButton() {
                @Override
                public void paint(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    int size = 10;
                    int x = (getWidth() - size) / 2 + 2;
                    int y = (getHeight() - (size / 2)) / 2;

                    g2.setColor(new Color(107, 114, 128)); // Gray 500
                    if (getModel().isPressed() || comboBox.isPopupVisible()) {
                        g2.setColor(focusColor);
                    } else if (getModel().isRollover()) {
                        g2.setColor(new Color(75, 85, 99)); // Gray 600
                    }

                    // Chevron (Seta pra baixo moderna)
                    g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(x, y, x + size / 2, y + size / 2);
                    g2.drawLine(x + size / 2, y + size / 2, x + size, y);

                    // Separador vertical sutil
                    g2.setColor(borderColor);
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawLine(0, 8, 0, getHeight() - 8);

                    g2.dispose();
                }
            };
            button.setPreferredSize(new Dimension(34, 0));
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return button;
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            // Keep background transparent so paintComponent draws it with rounded corners
        }

        @Override
        protected ComboPopup createPopup() {
            BasicComboPopup popup = new BasicComboPopup(comboBox) {
                @Override
                protected JScrollPane createScroller() {
                    JScrollPane scroller = super.createScroller();
                    scroller.setBorder(BorderFactory.createLineBorder(borderColor, 1));
                    scroller.getVerticalScrollBar().setUI(new CustomScrollBarUI());
                    scroller.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
                    return scroller;
                }

                @Override
                public void show() {
                    setBorder(BorderFactory.createLineBorder(borderColor, 1));
                    super.show();
                }
            };
            popup.setOpaque(true);
            popup.setBackground(Color.WHITE);
            return popup;
        }
    }

    private class CustomComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (c instanceof JLabel) {
                JLabel label = (JLabel) c;
                if (value == null) {
                    label.setText(" "); // Espaço vazio para item nulo
                }
                label.setBorder(new EmptyBorder(8, 12, 8, 12));
                label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                if (isSelected) {
                    label.setBackground(new Color(243, 244, 246)); // Gray 100
                    label.setForeground(focusColor);
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(new Color(31, 41, 55)); // Gray 800
                }
            }
            return c;
        }
    }

    // Simplistic custom scrollbar for the popup
    private class CustomScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            g.setColor(new Color(249, 250, 251)); // Gray 50
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(209, 213, 219)); // Gray 300
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, thumbBounds.width - 4, thumbBounds.height - 4, 4, 4);
            g2.dispose();
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(0, 0));
            btn.setMinimumSize(new Dimension(0, 0));
            btn.setMaximumSize(new Dimension(0, 0));
            return btn;
        }
    }

    // === PROPRIEDADES ===
    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }

    public Color getBorderColor() { return borderColor; }
    public void setBorderColor(Color borderColor) { 
        this.borderColor = borderColor; 
        repaint(); 
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        if (getUI() != null) {
            try {
                Object child = getAccessibleContext().getAccessibleChild(0);
                if (child instanceof javax.swing.plaf.basic.ComboPopup) {
                    javax.swing.JList list = ((javax.swing.plaf.basic.ComboPopup) child).getList();
                    if (list != null) list.setBackground(bg);
                }
            } catch (Exception e) {}
        }
    }

    public Color getFocusColor() {
        return focusColor;
    }

    public void setFocusColor(Color focusColor) {
        this.focusColor = focusColor;
        repaint();
    }
}
