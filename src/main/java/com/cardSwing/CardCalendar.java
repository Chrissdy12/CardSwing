package com.cardSwing;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Calendário moderno e interativo.
 * <p><b>PALETTE:</b> Arraste para dentro do seu painel.</p>
 */
public class CardCalendar extends JPanel {

    private LocalDate selectedDate;
    private YearMonth displayMonth;
    private final Locale locale = new Locale("pt", "BR");

    private JLabel lblMonthYear;
    private JPanel daysGrid;
    private JButton btnPrev;
    private JButton btnNext;
    private JPanel dowPanel;
    
    // Cores que serão atualizadas pelo Tema
    private Color primaryColor = new Color(59, 130, 246);
    private Color hoverColor = new Color(241, 245, 249);
    private Color selectedTextColor = Color.WHITE;
    private Color defaultTextColor = new Color(15, 23, 42);
    private Color disabledTextColor = new Color(148, 163, 184);

    public CardCalendar() {
        selectedDate = LocalDate.now();
        displayMonth = YearMonth.now();

        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        lblMonthYear = new JLabel("", SwingConstants.CENTER);
        lblMonthYear.setFont(new Font("Segoe UI", Font.BOLD, 15));
        
        btnPrev = createNavButton("<");
        btnPrev.addActionListener(e -> {
            displayMonth = displayMonth.minusMonths(1);
            updateCalendar();
        });

        btnNext = createNavButton(">");
        btnNext.addActionListener(e -> {
            displayMonth = displayMonth.plusMonths(1);
            updateCalendar();
        });

        headerPanel.add(btnPrev, BorderLayout.WEST);
        headerPanel.add(lblMonthYear, BorderLayout.CENTER);
        headerPanel.add(btnNext, BorderLayout.EAST);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        add(headerPanel, BorderLayout.NORTH);

        // Body
        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setOpaque(false);

        // Days of week
        dowPanel = new JPanel(new GridLayout(1, 7, 5, 5));
        dowPanel.setOpaque(false);
        String[] days = {"D", "S", "T", "Q", "Q", "S", "S"};
        for (String day : days) {
            JLabel lbl = new JLabel(day, SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            dowPanel.add(lbl);
        }
        bodyPanel.add(dowPanel, BorderLayout.NORTH);

        // Grid of dates
        daysGrid = new JPanel(new GridLayout(6, 7, 5, 5));
        daysGrid.setOpaque(false);
        bodyPanel.add(daysGrid, BorderLayout.CENTER);

        add(bodyPanel, BorderLayout.CENTER);

        applyThemeColors();
        updateCalendar();
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    @Override
    public void updateUI() {
        super.updateUI();
        applyThemeColors();
        if (daysGrid != null) {
            updateCalendar(); // Redraw with new colors
        }
    }

    private void applyThemeColors() {
        CardThemeManager.CardTheme theme = CardThemeManager.getCurrentTheme();
        if (theme != null) {
            primaryColor = theme.primary;
            defaultTextColor = theme.textPrimary;
            disabledTextColor = theme.textSecondary;
            hoverColor = theme.isDark ? new Color(255, 255, 255, 20) : new Color(0, 0, 0, 10);
            
            if (lblMonthYear != null) lblMonthYear.setForeground(defaultTextColor);
            if (btnPrev != null) btnPrev.setForeground(defaultTextColor);
            if (btnNext != null) btnNext.setForeground(defaultTextColor);
            
            if (dowPanel != null) {
                for (Component c : dowPanel.getComponents()) {
                    c.setForeground(disabledTextColor);
                }
            }
        }
    }

    private void updateCalendar() {
        String monthName = displayMonth.getMonth().getDisplayName(TextStyle.FULL, locale);
        monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1);
        lblMonthYear.setText(monthName + " " + displayMonth.getYear());

        daysGrid.removeAll();

        LocalDate firstOfMonth = displayMonth.atDay(1);
        int dow = firstOfMonth.getDayOfWeek().getValue(); 
        if (dow == 7) dow = 0; // Domingo = 0

        LocalDate startDate = firstOfMonth.minusDays(dow);

        for (int i = 0; i < 42; i++) {
            LocalDate currentDate = startDate.plusDays(i);
            DayPanel dp = new DayPanel(currentDate, currentDate.getMonth() == displayMonth.getMonth());
            daysGrid.add(dp);
        }

        daysGrid.revalidate();
        daysGrid.repaint();
    }

    // === PROPRIEDADES ===

    /**
     * Retorna o dia selecionado atualmente.
     */
    public LocalDate getLocalDate() {
        return selectedDate;
    }

    /**
     * Define o dia selecionado e muda o mês exibido para o mesmo.
     */
    public void setLocalDate(LocalDate date) {
        LocalDate old = this.selectedDate;
        this.selectedDate = date;
        if (date != null) {
            this.displayMonth = YearMonth.from(date);
        }
        updateCalendar();
        firePropertyChange("selectedDate", old, this.selectedDate);
    }

    // === PAINEL INTERNO DE CADA DIA ===
    private class DayPanel extends JPanel {
        private LocalDate date;
        private boolean isCurrentMonth;
        private boolean hovered = false;

        public DayPanel(LocalDate date, boolean isCurrentMonth) {
            this.date = date;
            this.isCurrentMonth = isCurrentMonth;
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    setLocalDate(date);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            boolean isSelected = date.equals(selectedDate);
            boolean isToday = date.equals(LocalDate.now());

            int s = Math.min(getWidth(), getHeight());
            int x = (getWidth() - s) / 2;
            int y = (getHeight() - s) / 2;

            // Fundo
            if (isSelected) {
                g2.setColor(primaryColor);
                g2.fillOval(x, y, s, s);
            } else if (hovered) {
                g2.setColor(hoverColor);
                g2.fillOval(x, y, s, s);
            } else if (isToday) {
                g2.setColor(new Color(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue(), 30));
                g2.fillOval(x, y, s, s);
            }

            // Texto
            g2.setFont(new Font("Segoe UI", isSelected || isToday ? Font.BOLD : Font.PLAIN, 12));
            String txt = String.valueOf(date.getDayOfMonth());
            FontMetrics fm = g2.getFontMetrics();

            if (isSelected) {
                g2.setColor(selectedTextColor);
            } else if (isCurrentMonth) {
                if (isToday) {
                    g2.setColor(primaryColor);
                } else {
                    g2.setColor(defaultTextColor);
                }
            } else {
                g2.setColor(disabledTextColor);
            }

            g2.drawString(txt, (getWidth() - fm.stringWidth(txt)) / 2, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            g2.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(32, 32);
        }
    }
}
