package com.cardSwing;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Utilitário de Toasts flutuantes não-obstrutivos.
 */
public class CardToast extends JDialog {

    public enum Type {
        SUCCESS(new Color(16, 185, 129), "✓"),
        ERROR(new Color(239, 68, 68), "✕"),
        INFO(new Color(59, 130, 246), "i"),
        WARNING(new Color(245, 158, 11), "!");

        Color color;
        String iconText;

        Type(Color c, String i) {
            this.color = c;
            this.iconText = i;
        }
    }

    private Timer slideTimer;
    private Timer fadeTimer;
    private float opacity = 0.0f;
    private int targetY;
    private int currentY;

    private static Window getWindow(Component parentComponent) {
        if (parentComponent == null)
            return null;
        if (parentComponent instanceof Window)
            return (Window) parentComponent;
        return SwingUtilities.getWindowAncestor(parentComponent);
    }

    private CardToast(Component parentComponent, String message, Type type) {
        super(getWindow(parentComponent));
        setModal(false);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0)); // transparent background
        setAlwaysOnTop(true);
        setFocusableWindowState(false); // não rouba o foco quando aparece

        JPanel content = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Sombra suave (simulação simples)
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 15, 15);

                // Fundo do Card
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 15, 15);

                // Faixa lateral colorida
                g2.setColor(type.color);
                g2.fillRoundRect(0, 0, 10, getHeight() - 4, 15, 15);
                g2.fillRect(5, 0, 5, getHeight() - 4); // conserta os cantos direitos

                g2.dispose();
            }
        };
        content.setOpaque(false);
        content.setLayout(new BorderLayout(15, 0));
        content.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Ícone customizado desenhado à mão (Graphics2D) para não depender de fontes
        JLabel lblIcon = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(type.color);
                g2.fillOval(0, 0, 24, 24); // Bolinha de fundo

                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                if (type == Type.SUCCESS) {
                    g2.drawLine(7, 12, 11, 16);
                    g2.drawLine(11, 16, 17, 8);
                } else if (type == Type.ERROR) {
                    g2.drawLine(8, 8, 16, 16);
                    g2.drawLine(16, 8, 8, 16);
                } else if (type == Type.INFO) {
                    g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(12, 7, 12, 7); // Ponto superior
                    g2.drawLine(12, 11, 12, 17); // Traço
                } else if (type == Type.WARNING) {
                    g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(12, 7, 12, 13); // Traço superior
                    g2.drawLine(12, 17, 12, 17); // Ponto inferior
                }

                g2.dispose();
            }
        };
        lblIcon.setPreferredSize(new Dimension(24, 24));

        JLabel lblMessage = new JLabel(message);
        lblMessage.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMessage.setForeground(new Color(51, 65, 85));

        content.add(lblIcon, BorderLayout.WEST);
        content.add(lblMessage, BorderLayout.CENTER);

        setContentPane(content);
        pack();

        setSize(Math.max(getWidth(), 250), getHeight() + 4);

        if (parentComponent != null) {
            try {
                Component target = parentComponent;
                if (parentComponent instanceof RootPaneContainer) {
                    target = ((RootPaneContainer) parentComponent).getContentPane();
                }
                Point p = target.getLocationOnScreen();
                Dimension s = target.getSize();
                int x = p.x + s.width - getWidth() - 20;
                // Subindo um pouco mais o Toast (margem de 20 para 50)
                targetY = p.y + s.height - getHeight() - 50;
                currentY = targetY + 50;
                setLocation(x, currentY);
            } catch (Exception ex) {
                int x = parentComponent.getX() + parentComponent.getWidth() - getWidth() - 20;
                // Subindo um pouco mais o Toast (margem de 20 para 50)
                targetY = parentComponent.getY() + parentComponent.getHeight() - getHeight() - 50;
                currentY = targetY + 50;
                setLocation(x, currentY);
            }
        } else {
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            // Subindo um pouco mais o Toast (margem de 50 para 70)
            targetY = screen.height - getHeight() - 70;
            currentY = targetY + 50;
            setLocation(screen.width - getWidth() - 20, currentY);
        }

        try {
            setOpacity(0.0f);
        } catch (Exception e) {
        } // Ignora se o SO não suportar

        setVisible(true);
        animateIn();
    }

    private void animateIn() {
        slideTimer = new Timer(15, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean doneY = true;
                boolean doneOp = true;

                if (currentY > targetY) {
                    currentY -= 3;
                    setLocation(getX(), currentY);
                    doneY = false;
                }
                if (opacity < 1.0f) {
                    opacity += 0.05f;
                    if (opacity > 1.0f)
                        opacity = 1.0f;
                    try {
                        setOpacity(opacity);
                    } catch (Exception ex) {
                    }
                    doneOp = false;
                }

                if (doneY && doneOp) {
                    slideTimer.stop();
                    startWait();
                }
            }
        });
        slideTimer.start();
    }

    private void startWait() {
        Timer waitTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animateOut();
            }
        });
        waitTimer.setRepeats(false);
        waitTimer.start();
    }

    private void animateOut() {
        fadeTimer = new Timer(15, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (opacity > 0.0f) {
                    opacity -= 0.05f;
                    if (opacity < 0.0f)
                        opacity = 0.0f;
                    try {
                        setOpacity(opacity);
                    } catch (Exception ex) {
                    }
                } else {
                    fadeTimer.stop();
                    dispose();
                }
            }
        });
        fadeTimer.start();
    }

    // === API ESTÁTICA PÚBLICA ===
    public static void showSuccess(Component parent, String message) {
        new CardToast(parent, message, Type.SUCCESS);
    }

    public static void showError(Component parent, String message) {
        new CardToast(parent, message, Type.ERROR);
    }

    public static void showInfo(Component parent, String message) {
        new CardToast(parent, message, Type.INFO);
    }

    public static void showWarning(Component parent, String message) {
        new CardToast(parent, message, Type.WARNING);
    }
}
