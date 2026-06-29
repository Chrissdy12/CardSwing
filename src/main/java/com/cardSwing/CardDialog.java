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

    public enum DialogType {
        CONFIRM(new Color(59, 130, 246), "?"),
        INPUT(new Color(59, 130, 246), "✎"),
        SUCCESS(new Color(16, 185, 129), "✓"),
        ERROR(new Color(239, 68, 68), "✕"),
        INFO(new Color(59, 130, 246), "i"),
        WARNING(new Color(245, 158, 11), "!");

        Color color;
        String iconText;

        DialogType(Color c, String i) {
            this.color = c;
            this.iconText = i;
        }
    }

    private boolean confirmed = false;
    private String inputValue = null;
    private DialogType type;

    private static Window getWindow(Component parentComponent) {
        if (parentComponent == null)
            return null;
        if (parentComponent instanceof Window)
            return (Window) parentComponent;
        return SwingUtilities.getWindowAncestor(parentComponent);
    }

    // Construtor privado (usado internamente pelos métodos estáticos)
    private CardDialog(Component parentComponent, String title, String message, DialogType type, Object[] options) {
        super(getWindow(parentComponent));
        this.type = type;
        setModal(true);
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

                // Ícone ilustrativo sutil
                Color fgIconColor = CardDialog.this.type.color;
                Color bgIconColor;

                if (CardDialog.this.type == DialogType.SUCCESS) {
                    bgIconColor = new Color(240, 253, 244); // Green 50
                } else if (CardDialog.this.type == DialogType.ERROR) {
                    bgIconColor = new Color(254, 242, 242); // Red 50
                } else if (CardDialog.this.type == DialogType.WARNING) {
                    bgIconColor = new Color(255, 251, 235); // Amber 50
                } else {
                    bgIconColor = new Color(239, 246, 255); // Blue 50
                }

                g2.setColor(bgIconColor);
                g2.fillOval(30, 25, 40, 40);

                g2.setColor(fgIconColor);
                
                if (CardDialog.this.type == DialogType.SUCCESS) {
                    g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(42, 46, 47, 51);
                    g2.drawLine(47, 51, 58, 38);
                } else if (CardDialog.this.type == DialogType.ERROR) {
                    g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(42, 37, 58, 53);
                    g2.drawLine(58, 37, 42, 53);
                } else if (CardDialog.this.type == DialogType.INPUT) {
                    g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    // Lápis customizado
                    g2.drawLine(52, 38, 43, 47);
                    g2.drawLine(55, 41, 46, 50);
                    g2.drawLine(52, 38, 55, 41);
                    g2.drawLine(43, 47, 41, 52);
                    g2.drawLine(46, 50, 41, 52);
                } else {
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
                    FontMetrics fm = g2.getFontMetrics();
                    String text = CardDialog.this.type.iconText;
                    int textX = 30 + (40 - fm.stringWidth(text)) / 2;
                    int textY = 25 + ((40 - fm.getHeight()) / 2) + fm.getAscent();
                    g2.drawString(text, textX, textY);
                }

                // Separador sutil acima dos botões
                g2.setColor(new Color(241, 245, 249)); // Slate 100
                g2.fillRect(0, getHeight() - 75, getWidth() - 8, 2);

                g2.dispose();
            }
        };
        cardPanel.setOpaque(false);
        cardPanel.setLayout(null);

        int cardWidth = 360; 

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
        if (type == DialogType.INPUT) {
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

        CardButton btnConfirm = new CardButton();
        btnConfirm.setText("Confirmar");
        btnConfirm.setButtonColor(CardDialog.this.type.color); // Usa a cor principal
        btnConfirm.setTextColor(Color.WHITE);
        btnConfirm.setBounds(240, footerY, 95, 36);

        final CardTextField finalInputField = inputField;
        final CardComboBox<Object> finalComboField = comboField;
        btnConfirm.addActionListener(e -> {
            confirmed = true;
            if (CardDialog.this.type == DialogType.INPUT) {
                if (finalComboField != null) {
                    Object item = finalComboField.getSelectedItem();
                    inputValue = (item != null) ? item.toString() : "";
                } else if (finalInputField != null) {
                    inputValue = finalInputField.getText();
                }
            }
            dispose();
        });

        if (type == DialogType.CONFIRM || type == DialogType.INPUT) {
            CardButton btnCancel = new CardButton();
            btnCancel.setText("Cancelar");
            btnCancel.setButtonColor(new Color(241, 245, 249)); // Slate 100
            btnCancel.setTextColor(new Color(71, 85, 105)); // Slate 600
            btnCancel.setBounds(135, footerY, 95, 36);
            btnCancel.addActionListener(e -> {
                confirmed = false;
                dispose();
            });
            cardPanel.add(btnCancel);
        } else {
            // Apenas botão OK para SUCCESS, ERROR, WARNING, INFO
            btnConfirm.setText("OK");
        }

        cardPanel.add(btnConfirm);

        // Tamanho do Card garantido para evitar que o GridBagLayout esmague ele a 0x0
        int dialogHeight = footerY + 65;
        Dimension size = new Dimension(cardWidth, dialogHeight);
        cardPanel.setPreferredSize(size);
        cardPanel.setMinimumSize(size);
        cardPanel.setMaximumSize(size);

        wrapper.add(cardPanel, new GridBagConstraints());
        setContentPane(wrapper);

        // Permite fechar a janela ao pressionar ESCAPE
        getRootPane().registerKeyboardAction(e -> {
            confirmed = false;
            dispose();
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        // Define o tamanho igual ao do parent (overlay total apenas na área de conteúdo)
        if (parentComponent != null) {
            try {
                Component target = parentComponent;
                if (parentComponent instanceof RootPaneContainer) {
                    target = ((RootPaneContainer) parentComponent).getContentPane();
                }
                Point p = target.getLocationOnScreen();
                Dimension s = target.getSize();
                setBounds(p.x, p.y, s.width, s.height);
            } catch (Exception ex) {
                setBounds(parentComponent.getBounds());
            }
        } else {
            Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            setBounds(bounds);
        }

        if (type == DialogType.INPUT && inputField != null) {
            inputField.requestFocus();
        }
    }

    // ========================================================================
    // MÉTODOS ESTÁTICOS DE FÁCIL USO (COMO O JOPTIONPANE)
    // ========================================================================

    /**
     * Mostra um diálogo de Confirmação (Sim/Não).
     */
    public static boolean showConfirm(Component parent, String title, String message) {
        CardDialog dialog = new CardDialog(parent, title, message, DialogType.CONFIRM, null);
        dialog.setVisible(true); // Trava a tela aqui até fechar
        return dialog.confirmed;
    }

    /**
     * Mostra um diálogo para o usuário digitar um valor (Input).
     */
    public static String showInput(Component parent, String title, String message) {
        CardDialog dialog = new CardDialog(parent, title, message, DialogType.INPUT, null);
        dialog.setVisible(true); // Trava a tela aqui até fechar

        if (dialog.confirmed) {
            return dialog.inputValue;
        }
        return null;
    }

    /**
     * Mostra um diálogo de Input em formato de ComboBox (Lista de opções).
     */
    public static String showComboInput(Component parent, String title, String message, Object[] options) {
        CardDialog dialog = new CardDialog(parent, title, message, DialogType.INPUT, options);
        dialog.setVisible(true); // Trava a tela aqui até fechar

        if (dialog.confirmed) {
            return dialog.inputValue;
        }
        return null;
    }

    /**
     * Mostra um diálogo de Sucesso (apenas botão OK).
     */
    public static void showSuccess(Component parent, String title, String message) {
        CardDialog dialog = new CardDialog(parent, title, message, DialogType.SUCCESS, null);
        dialog.setVisible(true);
    }

    /**
     * Mostra um diálogo de Erro (apenas botão OK).
     */
    public static void showError(Component parent, String title, String message) {
        CardDialog dialog = new CardDialog(parent, title, message, DialogType.ERROR, null);
        dialog.setVisible(true);
    }

    /**
     * Mostra um diálogo de Alerta/Aviso (apenas botão OK).
     */
    public static void showAlert(Component parent, String title, String message) {
        CardDialog dialog = new CardDialog(parent, title, message, DialogType.WARNING, null);
        dialog.setVisible(true);
    }

    /**
     * Mostra um diálogo de Informação (apenas botão OK).
     */
    public static void showInfo(Component parent, String title, String message) {
        CardDialog dialog = new CardDialog(parent, title, message, DialogType.INFO, null);
        dialog.setVisible(true);
    }
}
