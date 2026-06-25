package com.cardSwing;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

/**
 * Utilitário de Caixas de Diálogo (Confirm e Input) modernas.
 * Substitui o JOptionPane tradicional por algo elegante e arredondado.
 */
public class CardDialog extends JDialog {

    private boolean confirmed = false;
    private String inputValue = null;

    // Construtor privado (usado internamente pelos métodos estáticos)
    private CardDialog(JFrame parent, String title, String message, boolean isInput, Object[] options) {
        super(parent, true);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 80)); // Fundo escuro semi-transparente (Backdrop)

        // Wrapper invisível que cobre toda a tela e centraliza o card
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        // Painel do Card em si (Branco, Arredondado, com Sombra)
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Sombra suave
                g2.setColor(new Color(0, 0, 0, 10));
                g2.fillRoundRect(2, 6, getWidth() - 4, getHeight() - 8, 24, 24);
                g2.setColor(new Color(0, 0, 0, 5));
                g2.fillRoundRect(4, 8, getWidth() - 8, getHeight() - 12, 24, 24);
                
                // Fundo Branco puro
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 8, getHeight() - 10, 24, 24);
                
                // Ícone ilustrativo sutil (Círculo Azul Claro com um 'i' ou '?')
                g2.setColor(new Color(239, 246, 255)); // Blue 50
                g2.fillOval(30, 25, 40, 40);
                g2.setColor(new Color(59, 130, 246)); // Blue 500
                g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                if (isInput) {
                    // Ícone de caneta/edição simplificado
                    g2.drawLine(44, 52, 48, 48);
                    g2.drawLine(48, 48, 54, 38);
                    g2.drawLine(54, 38, 56, 40);
                    g2.drawLine(56, 40, 50, 50);
                    g2.drawLine(50, 50, 44, 52);
                } else {
                    // Ícone de interrogação
                    g2.drawArc(46, 33, 10, 10, 0, 180);
                    g2.drawLine(56, 38, 51, 43);
                    g2.drawLine(51, 43, 51, 46);
                    g2.fillOval(49, 49, 4, 4);
                }

                // Separador sutil acima dos botões
                g2.setColor(new Color(241, 245, 249)); // Slate 100
                g2.fillRect(0, getHeight() - 75, getWidth() - 8, 2);
                
                g2.dispose();
            }
        };
        cardPanel.setOpaque(false);
        cardPanel.setLayout(null);
        
        int cardWidth = 360; // Reduzido de 400 para caber em telas/frames pequenos
        
        // --- TÍTULO ---
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(15, 23, 42)); // Slate 900
        lblTitle.setBounds(85, 25, 250, 40); 
        cardPanel.add(lblTitle);

        // --- MENSAGEM ---
        JTextArea txtMessage = new JTextArea(message);
        txtMessage.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMessage.setForeground(new Color(100, 116, 139)); // Slate 500
        txtMessage.setWrapStyleWord(true);
        txtMessage.setLineWrap(true);
        txtMessage.setOpaque(false);
        txtMessage.setBackground(new Color(0, 0, 0, 0));
        txtMessage.setBorder(BorderFactory.createEmptyBorder());
        txtMessage.setEditable(false);
        txtMessage.setFocusable(false);
        txtMessage.setBounds(30, 80, 300, 45);
        cardPanel.add(txtMessage);

        CardTextField inputField = null;
        CardComboBox<Object> comboField = null;
        int yOffset = 135;

        // --- INPUT FIELD / COMBOBOX ---
        if (isInput) {
            if (options != null && options.length > 0) {
                comboField = new CardComboBox<>(options);
                comboField.setBounds(30, yOffset, 300, 40);
                cardPanel.add(comboField);
            } else {
                inputField = new CardTextField();
                inputField.setPlaceholder("Digite aqui...");
                inputField.setBounds(30, yOffset, 300, 40);
                
                inputField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            confirmed = true;
                            dispose();
                        }
                    }
                });
                cardPanel.add(inputField);
            }
            yOffset += 65;
        }

        // --- BOTÕES ---
        int footerY = yOffset + 15;
        
        CardButton btnCancel = new CardButton();
        btnCancel.setText("Cancelar");
        btnCancel.setButtonColor(new Color(241, 245, 249)); // Slate 100
        btnCancel.setTextColor(new Color(71, 85, 105)); // Slate 600
        btnCancel.setBounds(135, footerY, 95, 36);
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        CardButton btnConfirm = new CardButton();
        btnConfirm.setText("Confirmar");
        btnConfirm.setButtonColor(new Color(59, 130, 246)); // Blue 500
        btnConfirm.setTextColor(Color.WHITE);
        btnConfirm.setBounds(240, footerY, 95, 36);
        
        final CardTextField finalInputField = inputField;
        final CardComboBox<Object> finalComboField = comboField;
        btnConfirm.addActionListener(e -> {
            confirmed = true;
            if (isInput) {
                if (finalComboField != null) {
                    Object item = finalComboField.getSelectedItem();
                    inputValue = (item != null) ? item.toString() : "";
                } else if (finalInputField != null) {
                    inputValue = finalInputField.getText();
                }
            }
            dispose();
        });

        cardPanel.add(btnCancel);
        cardPanel.add(btnConfirm);

        // Tamanho do Card garantido para evitar que o GridBagLayout esmague ele a 0x0
        int dialogHeight = footerY + 65;
        Dimension size = new Dimension(cardWidth, dialogHeight);
        cardPanel.setPreferredSize(size);
        cardPanel.setMinimumSize(size);
        cardPanel.setMaximumSize(size);
        
        wrapper.add(cardPanel, new GridBagConstraints());
        setContentPane(wrapper);
        
        // Permite fechar a janela ao pressionar ESCAPE (Segurança caso a UI quebre no futuro)
        getRootPane().registerKeyboardAction(e -> {
            confirmed = false;
            dispose();
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        // Define o tamanho igual ao do parent (overlay total apenas na área de conteúdo)
        if (parent != null) {
            try {
                Container content = parent.getContentPane();
                Point p = content.getLocationOnScreen();
                Dimension s = content.getSize();
                setBounds(p.x, p.y, s.width, s.height);
            } catch (Exception ex) {
                setBounds(parent.getBounds());
            }
        } else {
            Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            setBounds(bounds);
        }
        
        if (isInput && inputField != null) {
            inputField.requestFocus();
        }
    }

    // ========================================================================
    // MÉTODOS ESTÁTICOS DE FÁCIL USO (COMO O JOPTIONPANE)
    // ========================================================================

    /**
     * Mostra um diálogo de Confirmação (Sim/Não).
     * Exemplo: if (CardDialog.showConfirm(this, "Excluir", "Deseja excluir o item?")) { ... }
     * 
     * @return true se clicou em "Confirmar", false se cancelou ou fechou.
     */
    public static boolean showConfirm(JFrame parent, String title, String message) {
        CardDialog dialog = new CardDialog(parent, title, message, false, null);
        dialog.setVisible(true); // Trava a tela aqui até fechar
        return dialog.confirmed;
    }

    /**
     * Mostra um diálogo para o usuário digitar um valor (Input).
     * Exemplo: String nome = CardDialog.showInput(this, "Novo Cliente", "Qual o nome do cliente?");
     * 
     * @return O texto digitado, ou null se o usuário cancelou.
     */
    public static String showInput(JFrame parent, String title, String message) {
        CardDialog dialog = new CardDialog(parent, title, message, true, null);
        dialog.setVisible(true); // Trava a tela aqui até fechar
        
        if (dialog.confirmed) {
            return dialog.inputValue;
        }
        return null;
    }

    /**
     * Mostra um diálogo de Input em formato de ComboBox (Lista de opções).
     * Exemplo: String cor = CardDialog.showComboInput(this, "Escolha", "Selecione uma cor:", new String[]{"Azul", "Vermelho"});
     * 
     * @return O item selecionado como String, ou null se o usuário cancelou.
     */
    public static String showComboInput(JFrame parent, String title, String message, Object[] options) {
        CardDialog dialog = new CardDialog(parent, title, message, true, options);
        dialog.setVisible(true); // Trava a tela aqui até fechar
        
        if (dialog.confirmed) {
            return dialog.inputValue;
        }
        return null;
    }
}
