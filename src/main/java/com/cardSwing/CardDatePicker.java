package com.cardSwing;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

/**
 * Componente moderno para seleção de datas (LocalDate).
 * Pode ser arrastado pela Palette do NetBeans.
 */
public class CardDatePicker extends CardTextField {

    protected LocalDate selectedDate;
    protected YearMonth displayMonth;
    protected DateTimeFormatter formatter;
    protected JPopupMenu popup;
    protected JPanel calendarPanel;
    protected JLabel lblMonthYear;
    protected JPanel daysGrid;
    protected JPanel mainPopupPanel;

    public CardDatePicker() {
        super();
        setPlaceholder("");
        
        try {
            MaskFormatter mf = new MaskFormatter("##/##/####");
            mf.setPlaceholderCharacter('_');
            setFormatterFactory(new DefaultFormatterFactory(mf));
            setValue(null);
        } catch (Exception ex) {}

        // Formato BR padrão
        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("pt", "BR"));
        selectedDate = null;
        displayMonth = YearMonth.now();

        // Adiciona evento de clique para abrir o calendário
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isEnabled()) {
                    // Clicou no ícone?
                    int iconX = getWidth() - 30;
                    if (e.getX() >= iconX) {
                        showPopup();
                    }
                }
            }
            
            @Override
            public void mouseMoved(MouseEvent e) {
                int iconX = getWidth() - 30;
                if (e.getX() >= iconX) {
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else {
                    setCursor(new Cursor(Cursor.TEXT_CURSOR));
                }
            }
        });
        
        // Ativa o movimento do mouse
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int iconX = getWidth() - 30;
                if (e.getX() >= iconX) {
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else {
                    setCursor(new Cursor(Cursor.TEXT_CURSOR));
                }
            }
        });
        
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                parseText();
            }
        });
        
    // Enter KeyListener removido conforme solicitado, agora ele apenas herda o transferFocus() do CardTextField

        initPopup();
    }
    
    protected void parseText() {
        try {
            String text = getText().replace("_", "").trim();
            if (text.length() == 10) {
                selectedDate = LocalDate.parse(text, formatter);
                setValidState(true);
            } else if (text.length() <= 2) { // Vazio (as barras ficam)
                selectedDate = null;
                setValidState(false);
            }
        } catch (Exception ex) {
            // Data inválida, mantém ou ignora
            setValidState(false);
        }
        updateTextField();
    }

    protected void initPopup() {
        popup = new JPopupMenu();
        popup.setUI(new BasicPopupMenuUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                // Fundo limpo sem bordas default
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Sombra suave
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(2, 2, c.getWidth() - 4, c.getHeight() - 4, 15, 15);
                
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, c.getWidth() - 4, c.getHeight() - 4, 15, 15);
                g2.setColor(new Color(226, 232, 240)); // Slate 200 border
                g2.drawRoundRect(0, 0, c.getWidth() - 4, c.getHeight() - 4, 15, 15);
                g2.dispose();
            }
        });
        popup.setBorder(new EmptyBorder(10, 10, 15, 15));
        popup.setOpaque(false);
        popup.setBackground(new Color(0,0,0,0));

        mainPopupPanel = new JPanel(new BorderLayout());
        mainPopupPanel.setOpaque(false);

        calendarPanel = new JPanel(new BorderLayout(5, 5));
        calendarPanel.setOpaque(false);
        
        // --- CABEÇALHO (MÊS E SETAS) ---
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        
        CardButton btnPrev = createNavButton("<");
        btnPrev.addActionListener(e -> {
            displayMonth = displayMonth.minusMonths(1);
            updateCalendar();
        });
        
        CardButton btnNext = createNavButton(">");
        btnNext.addActionListener(e -> {
            displayMonth = displayMonth.plusMonths(1);
            updateCalendar();
        });

        lblMonthYear = new JLabel("", SwingConstants.CENTER);
        lblMonthYear.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMonthYear.setForeground(new Color(30, 41, 59)); // Slate 800

        header.add(btnPrev, BorderLayout.WEST);
        header.add(lblMonthYear, BorderLayout.CENTER);
        header.add(btnNext, BorderLayout.EAST);
        
        calendarPanel.add(header, BorderLayout.NORTH);

        // --- GRID DE DIAS ---
        daysGrid = new JPanel(new GridLayout(7, 7, 2, 2));
        daysGrid.setOpaque(false);
        calendarPanel.add(daysGrid, BorderLayout.CENTER);

        mainPopupPanel.add(calendarPanel, BorderLayout.CENTER);
        popup.add(mainPopupPanel);
        
        buildExtraControls(mainPopupPanel);
    }
    
    // Método para ser sobrescrito pelo CardDateTimePicker
    protected void buildExtraControls(JPanel mainPanel) {
        // Vazio no DatePicker
    }

    private CardButton createNavButton(String text) {
        CardButton btn = new CardButton();
        btn.setText(text);
        btn.setPreferredSize(new Dimension(30, 30));
        btn.setButtonColor(new Color(241, 245, 249)); // Slate 100
        btn.setTextColor(new Color(71, 85, 105)); // Slate 600
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return btn;
    }

    protected void showPopup() {
        if (selectedDate != null) {
            displayMonth = YearMonth.from(selectedDate);
        } else {
            displayMonth = YearMonth.now();
        }
        updateCalendar();
        popup.show(this, 0, getHeight() + 5);
    }

    protected void updateCalendar() {
        daysGrid.removeAll();

        // Nomes dos meses customizados (Pt-BR)
        String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        lblMonthYear.setText(meses[displayMonth.getMonthValue() - 1] + " " + displayMonth.getYear());

        // Dias da Semana (Domingo a Sábado)
        String[] weekDays = {"D", "S", "T", "Q", "Q", "S", "S"};
        for (String wd : weekDays) {
            JLabel lbl = new JLabel(wd, SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lbl.setForeground(new Color(148, 163, 184)); // Slate 400
            daysGrid.add(lbl);
        }

        LocalDate firstDay = displayMonth.atDay(1);
        int daysInMonth = displayMonth.lengthOfMonth();
        
        // DayOfWeek: 1=Seg, 7=Dom. Queremos Dom=0, Seg=1...
        int offset = firstDay.getDayOfWeek().getValue();
        if (offset == 7) offset = 0;

        // Células vazias antes do dia 1
        for (int i = 0; i < offset; i++) {
            daysGrid.add(new JLabel(""));
        }

        // Dias do mês
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = displayMonth.atDay(day);
            DayLabel lblDay = new DayLabel(date);
            daysGrid.add(lblDay);
        }
        
        // Completar as 42 células para não deformar o grid (7x6 = 42)
        int totalCellsAdded = 7 + offset + daysInMonth;
        for (int i = totalCellsAdded; i < 49; i++) {
            daysGrid.add(new JLabel(""));
        }

        daysGrid.revalidate();
        daysGrid.repaint();
        popup.pack();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Desenha o ícone de calendário no lado direito do campo de texto
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(148, 163, 184)); // Slate 400
        
        int iconX = getWidth() - 30;
        int iconY = (getHeight() - 16) / 2;
        
        // Desenhando um calendário vetorial pequeno (16x16)
        g2.drawRoundRect(iconX, iconY + 2, 14, 12, 3, 3);
        g2.drawLine(iconX, iconY + 6, iconX + 14, iconY + 6);
        g2.drawLine(iconX + 3, iconY, iconX + 3, iconY + 4);
        g2.drawLine(iconX + 11, iconY, iconX + 11, iconY + 4);
        
        // Pontinhos de dias
        g2.fillRect(iconX + 3, iconY + 8, 2, 2);
        g2.fillRect(iconX + 7, iconY + 8, 2, 2);
        g2.fillRect(iconX + 11, iconY + 8, 2, 2);
        
        g2.dispose();
    }

    public LocalDate getLocalDate() {
        return selectedDate;
    }

    public void setLocalDate(LocalDate date) {
        this.selectedDate = date;
        if (date != null) {
            setText(date.format(formatter));
            setValidState(true);
        } else {
            setValue(null);
            setValidState(false);
        }
        updateTextField();
    }
    
    protected void setValidState(boolean valid) {
        if (valid) {
            setFocusColor(new Color(34, 197, 94)); // Green 500
            setBorderColor(new Color(34, 197, 94)); // Green 500
            setHoverColor(new Color(34, 197, 94)); // Green 500
        } else {
            setFocusColor(new Color(59, 130, 246)); // Blue 500
            setBorderColor(new Color(209, 213, 219)); // Gray 300
            setHoverColor(new Color(156, 163, 175)); // Gray 400
        }
    }
    
    protected void updateTextField() {
        if (selectedDate != null) {
            setText(selectedDate.format(formatter));
        } else {
            setValue(null); // Limpa corretamente a máscara
        }
    }
    
    protected void onDayClicked(LocalDate date) {
        setLocalDate(date);
        popup.setVisible(false);
        this.requestFocus();
    }
    
    // --- LABEL CUSTOMIZADA PARA OS DIAS (HOVER E SELEÇÃO) ---
    protected class DayLabel extends JLabel {
        private LocalDate date;
        private boolean hover = false;

        public DayLabel(LocalDate date) {
            super(String.valueOf(date.getDayOfMonth()), SwingConstants.CENTER);
            this.date = date;
            setFont(new Font("Segoe UI", Font.PLAIN, 12));
            setForeground(new Color(51, 65, 85)); // Slate 700
            setPreferredSize(new Dimension(30, 30));
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            if (date.equals(selectedDate)) {
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
            } else if (date.equals(LocalDate.now())) {
                setForeground(new Color(59, 130, 246)); // Azul destaque pro dia de hoje
                setFont(new Font("Segoe UI", Font.BOLD, 12));
            }

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    onDayClicked(date);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (date.equals(selectedDate)) {
                g2.setColor(new Color(59, 130, 246)); // Fundo Azul para Selecionado
                g2.fillOval(2, 2, getWidth() - 4, getHeight() - 4);
            } else if (hover) {
                g2.setColor(new Color(241, 245, 249)); // Slate 100 Hover
                g2.fillOval(2, 2, getWidth() - 4, getHeight() - 4);
            }
            
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
