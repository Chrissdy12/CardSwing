package com.cardSwing;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import javax.swing.*;

/**
 * Utilitário moderno para exibir um Loading flutuante (overlay) que bloqueia interações 
 * sem congelar as animações da interface.
 * 
 * <p>Para tarefas pesadas, use sempre os métodos <code>execute(...)</code>.</p>
 */
public class CardLoading extends JDialog {

    private static CardLoading currentLoader;
    private String message;
    private float angle = 0;
    private Timer timer;
    private ComponentAdapter moveListener;
    private Window parentWindow;

    private CardLoading(Window parent, String message) {
        super(parent);
        this.parentWindow = parent;
        this.message = message;
        
        setModal(false); // Importante: Modal=false permite que a animação continue caso a EDT não seja brutalmente travada
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 100)); // Fundo escuro semi-transparente
        setAlwaysOnTop(true);
        
        if (parent != null) {
            setBounds(parent.getBounds());
            // Se o usuário mover/redimensionar a tela, o loading acompanha!
            moveListener = new ComponentAdapter() {
                @Override
                public void componentMoved(ComponentEvent e) {
                    if (isVisible()) setBounds(parent.getBounds());
                }
                @Override
                public void componentResized(ComponentEvent e) {
                    if (isVisible()) setBounds(parent.getBounds());
                }
            };
            parent.addComponentListener(moveListener);
        } else {
            setSize(400, 300);
            setLocationRelativeTo(null);
        }

        // Consome eventos de mouse para evitar cliques na janela de trás
        addMouseListener(new MouseAdapter() {});
        addMouseMotionListener(new MouseAdapter() {});
        addKeyListener(new KeyAdapter() {});
        setFocusTraversalKeysEnabled(false);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Dimensões do quadrado branco
                int boxW = 200;
                int boxH = 160;
                int bx = (getWidth() - boxW) / 2;
                int by = (getHeight() - boxH) / 2;

                // Desenha sombra (simulação suave)
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(bx + 2, by + 4, boxW, boxH, 20, 20);

                // Desenha Fundo
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(bx, by, boxW, boxH, 20, 20);

                // Dimensões do Spinner
                int spinSize = 50;
                int spinX = bx + (boxW - spinSize) / 2;
                int spinY = by + 30;

                g2.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                // Trilha de Fundo do Spinner
                g2.setColor(new Color(226, 232, 240));
                g2.drawArc(spinX, spinY, spinSize, spinSize, 0, 360);

                // Arco Animado (Girando)
                g2.setColor(new Color(59, 130, 246)); // Azul vibrante
                g2.draw(new Arc2D.Double(spinX, spinY, spinSize, spinSize, angle, 120, Arc2D.OPEN));

                // Texto Embaixo do Spinner
                g2.setColor(new Color(71, 85, 105)); // Texto dark
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                
                String text = message != null ? message : "Carregando...";
                int txtX = bx + (boxW - fm.stringWidth(text)) / 2;
                int txtY = spinY + spinSize + 35 + fm.getAscent();
                g2.drawString(text, txtX, txtY);

                g2.dispose();
            }
        };
        panel.setOpaque(false);
        setContentPane(panel);

        // Timer de 60fps para animação extremamente fluida
        timer = new Timer(16, e -> {
            angle -= 8;
            if (angle <= -360) angle = 0;
            panel.repaint();
        });
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            timer.start();
        } else {
            timer.stop();
        }
    }

    @Override
    public void dispose() {
        if (parentWindow != null && moveListener != null) {
            parentWindow.removeComponentListener(moveListener);
        }
        super.dispose();
    }

    private static Window getWindow(Component c) {
        if (c == null) return null;
        if (c instanceof Window) return (Window) c;
        return SwingUtilities.getWindowAncestor(c);
    }

    // =========================================================
    // EXIBIÇÃO MANUAL (Para usar manualmente é necessário ter cuidado com a EDT)
    // =========================================================

    /**
     * Mostra o loading manualmente.
     */
    public static void show(Component parent, String message) {
        SwingUtilities.invokeLater(() -> {
            stop(); // Esconde o anterior para evitar duplicação
            Window w = getWindow(parent);
            currentLoader = new CardLoading(w, message);
            currentLoader.setVisible(true);
        });
    }

    public static void show(Component parent) {
        show(parent, "Carregando...");
    }

    public static void stop() {
        SwingUtilities.invokeLater(() -> {
            if (currentLoader != null) {
                currentLoader.setVisible(false);
                currentLoader.dispose();
                currentLoader = null;
            }
        });
    }

    // =========================================================
    // MÉTODOS DE EXECUÇÃO AUTOMÁTICA EM THREAD SEPARADA (OS MELHORES)
    // =========================================================

    /**
     * Tipo 1: Executa apenas a tarefa pesada em background (com texto padrão).
     * @param parent Componente pai para prender o overlay.
     * @param backgroundTask O processamento pesado a ser feito.
     */
    public static void execute(Component parent, Runnable backgroundTask) {
        execute(parent, "Carregando...", backgroundTask, null);
    }

    /**
     * Tipo 2: Executa com texto personalizado e a tarefa pesada.
     * @param parent Componente pai para prender o overlay.
     * @param message Texto customizado (ex: "Buscando dados...").
     * @param backgroundTask O processamento pesado a ser feito.
     */
    public static void execute(Component parent, String message, Runnable backgroundTask) {
        execute(parent, message, backgroundTask, null);
    }

    /**
     * Tipo 3: Executa com texto, tarefa pesada E uma rotina ao finalizar (callback).
     * @param parent Componente pai.
     * @param message Mensagem (ex: "Salvando...").
     * @param backgroundTask Tarefa pesada (banco de dados, download).
     * @param onFinish Tarefa leve que roda na tela quando tudo acaba (ex: mostrar os dados na tabela).
     */
    public static void execute(Component parent, String message, Runnable backgroundTask, Runnable onFinish) {
        // Exibe de imediato na thread de UI
        show(parent, message);
        
        // Dispara o trabalho pesado em outra thread para NUNCA travar a animação
        new Thread(() -> {
            try {
                // Aguarda um pequeno instante (150ms) pra dar tempo da tela de loading renderizar na UI thread sem engasgos iniciais
                Thread.sleep(150); 
                
                if (backgroundTask != null) {
                    backgroundTask.run();
                }
            } catch (Exception ignored) {
            } finally {
                // Esconde a tela
                stop();
                // Roda o callback na Thread principal (já que ele provavelmente vai alterar Labels ou Tabelas)
                if (onFinish != null) {
                    SwingUtilities.invokeLater(onFinish);
                }
            }
        }).start();
    }
}
