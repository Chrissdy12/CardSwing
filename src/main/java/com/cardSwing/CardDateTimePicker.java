package com.cardSwing;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

/**
 * Componente moderno para seleção de Data e Hora (LocalDateTime).
 * Pode ser arrastado pela Palette do NetBeans.
 */
public class CardDateTimePicker extends CardDatePicker {

    private LocalTime selectedTime;
    private DateTimeFormatter dateTimeFormatter;
    
    private CardComboBox<String> comboHour;
    private CardComboBox<String> comboMinute;

    public CardDateTimePicker() {
        super();
        setPlaceholder("");
        
        try {
            MaskFormatter mf = new MaskFormatter("##/##/#### ##:##");
            mf.setPlaceholderCharacter('_');
            setFormatterFactory(new DefaultFormatterFactory(mf));
            setValue(null);
        } catch (Exception ex) {}

        dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", new Locale("pt", "BR"));
        selectedTime = LocalTime.of(0, 0);
        
        // Os listeners do pai (CardDatePicker) chamarão parseText() automaticamente!
        
        updateTextField();
    }
    
    @Override
    protected void parseText() {
        try {
            String text = getText().replace("_", "").trim();
            if (text.length() == 16) {
                LocalDateTime ldt = LocalDateTime.parse(text, dateTimeFormatter);
                selectedDate = ldt.toLocalDate();
                selectedTime = ldt.toLocalTime();
                setValidState(true);
                updateTimeLabels();
            } else if (text.length() <= 2) {
                selectedDate = null;
                selectedTime = LocalTime.of(0, 0);
                setValidState(false);
                updateTimeLabels();
            }
        } catch (Exception ex) {
            setValidState(false);
        }
        // Quando a edição ocorre livremente, definimos o valor interno no JFormattedTextField 
        // chamando updateTextField (que usa setValue)
        updateTextField();
    }

    private JLabel lblHour;
    private JLabel lblMinute;

    @Override
    protected void buildExtraControls(JPanel mainPanel) {
        if (selectedTime == null) {
            selectedTime = LocalTime.of(0, 0);
        }
        
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        timePanel.setOpaque(false);
        timePanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240))); // Slate 200
        
        JLabel lblTime = new JLabel("Horário:");
        lblTime.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTime.setForeground(new Color(100, 116, 139)); // Slate 500
        
        // Controles de Hora
        CardButton btnPrevHour = createMiniButton("<");
        lblHour = new JLabel(String.format("%02d", selectedTime.getHour()), SwingConstants.CENTER);
        lblHour.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblHour.setPreferredSize(new Dimension(24, 24));
        CardButton btnNextHour = createMiniButton(">");
        
        btnPrevHour.addActionListener(e -> {
            selectedTime = selectedTime.minusHours(1);
            updateTimeLabels();
        });
        btnNextHour.addActionListener(e -> {
            selectedTime = selectedTime.plusHours(1);
            updateTimeLabels();
        });
        
        JLabel lblColon = new JLabel(":");
        lblColon.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblColon.setForeground(new Color(51, 65, 85));
        
        // Controles de Minuto
        CardButton btnPrevMin = createMiniButton("<");
        lblMinute = new JLabel(String.format("%02d", selectedTime.getMinute()), SwingConstants.CENTER);
        lblMinute.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMinute.setPreferredSize(new Dimension(24, 24));
        CardButton btnNextMin = createMiniButton(">");
        
        btnPrevMin.addActionListener(e -> {
            selectedTime = selectedTime.minusMinutes(1);
            updateTimeLabels();
        });
        btnNextMin.addActionListener(e -> {
            selectedTime = selectedTime.plusMinutes(1);
            updateTimeLabels();
        });
        
        timePanel.add(lblTime);
        timePanel.add(Box.createHorizontalStrut(5));
        timePanel.add(btnPrevHour);
        timePanel.add(lblHour);
        timePanel.add(btnNextHour);
        
        timePanel.add(lblColon);
        
        timePanel.add(btnPrevMin);
        timePanel.add(lblMinute);
        timePanel.add(btnNextMin);
        timePanel.add(Box.createHorizontalStrut(5));
        
        // Botão OK
        CardButton btnOk = new CardButton();
        btnOk.setText("OK");
        btnOk.setPreferredSize(new Dimension(40, 24));
        btnOk.setButtonColor(new Color(59, 130, 246));
        btnOk.setTextColor(Color.WHITE);
        btnOk.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnOk.addActionListener(e -> {
            popup.setVisible(false);
            requestFocus();
            if (selectedDate != null && selectedTime != null) {
                setValidState(true);
            }
        });
        timePanel.add(btnOk);
        
        mainPanel.add(timePanel, BorderLayout.SOUTH);
    }
    
    private CardButton createMiniButton(String text) {
        CardButton btn = new CardButton();
        btn.setText(text);
        btn.setPreferredSize(new Dimension(24, 24));
        btn.setButtonColor(new Color(241, 245, 249)); // Slate 100
        btn.setTextColor(new Color(71, 85, 105)); // Slate 600
        btn.setFont(new Font("Segoe UI", Font.BOLD, 10));
        return btn;
    }
    
    private void updateTimeLabels() {
        if (lblHour != null && lblMinute != null) {
            lblHour.setText(String.format("%02d", selectedTime.getHour()));
            lblMinute.setText(String.format("%02d", selectedTime.getMinute()));
        }
        updateTextField();
    }

    @Override
    protected void onDayClicked(LocalDate date) {
        // Apenas atualiza a data e repinta o calendário, NÃO FECHA o popup (para dar tempo de escolher a hora)
        this.selectedDate = date;
        updateTextField();
        updateCalendar();
    }

    @Override
    protected void updateTextField() {
        if (selectedDate != null && selectedTime != null) {
            LocalDateTime ldt = LocalDateTime.of(selectedDate, selectedTime);
            setText(ldt.format(dateTimeFormatter));
        } else {
            setValue(null);
        }
    }

    public LocalDateTime getLocalDateTime() {
        if (selectedDate == null) return null;
        return LocalDateTime.of(selectedDate, selectedTime);
    }

    public void setLocalDateTime(LocalDateTime dateTime) {
        if (dateTime != null) {
            this.selectedDate = dateTime.toLocalDate();
            this.selectedTime = dateTime.toLocalTime();
            setValidState(true);
            updateTimeLabels();
        } else {
            this.selectedDate = null;
            this.selectedTime = LocalTime.of(0, 0);
            setValidState(false);
            updateTimeLabels();
        }
        updateTextField();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Opcional: adicionar um ícone de relógio no canto para diferenciar do DatePicker normal
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(59, 130, 246)); // Azulzinho sutil no cantinho do ícone
        
        int iconX = getWidth() - 20;
        int iconY = (getHeight() - 16) / 2 + 10;
        
        g2.fillOval(iconX, iconY, 6, 6);
        g2.dispose();
    }
}
