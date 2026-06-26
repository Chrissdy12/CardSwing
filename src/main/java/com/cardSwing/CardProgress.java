package com.cardSwing;

import java.awt.*;
import java.io.Serializable;
import javax.swing.*;

/**
 * Painel de progresso visual — barra de progresso estilizada para o card.
 *
 * <p><b>PALETTE:</b> Arraste para dentro de um CardPanel.</p>
 *
 * <p>Propriedades editáveis:</p>
 * <ul>
 *   <li><b>progress</b> — valor de 0 a 100</li>
 *   <li><b>barColor</b> — cor da barra preenchida</li>
 *   <li><b>trackColor</b> — cor do trilho</li>
 *   <li><b>showLabel</b> — exibe o percentual</li>
 *   <li><b>barHeight</b> — altura da barra</li>
 * </ul>
 */
public class CardProgress extends JPanel implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum ProgressModel {
        STANDARD,
        STRIPED,
        GRADIENT,
        DASHED,
        OUTLINED,
        MATERIAL
    }

    private ProgressModel model = ProgressModel.STANDARD;
    private int progress = 50;
    private int currentValue = 50;
    private int totalValue = 100;
    private Color barColor = new Color(59, 130, 246);
    private Color trackColor = new Color(226, 232, 240);
    private boolean showLabel = true;
    private int barHeight = 8;
    private boolean showPercentage = true;

    private final JLabel percentLabel;

    public CardProgress() {
        setOpaque(false);
        setLayout(new BorderLayout(8, 0));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        setPreferredSize(new Dimension(100, 24));

        // Painel da barra
        JPanel barPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                int y = (h - barHeight) / 2;

                int fillW = (int)((progress / 100.0) * w);

                switch (model) {
                    case STRIPED:
                        g2.setColor(trackColor);
                        g2.fillRoundRect(0, y, w, barHeight, barHeight, barHeight);
                        if (fillW > 0) {
                            g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, y, fillW, barHeight, barHeight, barHeight));
                            g2.setColor(barColor);
                            g2.fillRoundRect(0, y, fillW, barHeight, barHeight, barHeight);
                            g2.setColor(new Color(255, 255, 255, 50));
                            g2.setStroke(new BasicStroke(Math.max(1f, barHeight / 2.0f)));
                            for (int i = -barHeight * 2; i < fillW; i += barHeight) {
                                g2.drawLine(i, y + barHeight, i + barHeight * 2, y - barHeight);
                            }
                            g2.setClip(null);
                        }
                        break;
                    case GRADIENT:
                        g2.setColor(trackColor);
                        g2.fillRoundRect(0, y, w, barHeight, barHeight, barHeight);
                        if (fillW > 0) {
                            GradientPaint gp = new GradientPaint(0, y, barColor.brighter().brighter(), fillW, y, barColor);
                            g2.setPaint(gp);
                            g2.fillRoundRect(0, y, fillW, barHeight, barHeight, barHeight);
                        }
                        break;
                    case DASHED:
                        g2.setColor(trackColor);
                        g2.fillRoundRect(0, y, w, barHeight, barHeight, barHeight);
                        if (fillW > 0) {
                            g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, y, fillW, barHeight, barHeight, barHeight));
                            g2.setColor(barColor);
                            int dashWidth = Math.max(4, barHeight / 2);
                            int dashSpace = Math.max(2, dashWidth / 2);
                            for (int i = 0; i < fillW; i += dashWidth + dashSpace) {
                                g2.fillRect(i, y, dashWidth, barHeight);
                            }
                            g2.setClip(null);
                        }
                        break;
                    case OUTLINED:
                        g2.setColor(trackColor);
                        g2.drawRoundRect(0, y, w - 1, barHeight - 1, barHeight, barHeight);
                        if (fillW > 4) {
                            g2.setColor(barColor);
                            g2.fillRoundRect(2, y + 2, fillW - 4, barHeight - 4, barHeight - 4, barHeight - 4);
                        }
                        break;
                    case MATERIAL:
                        g2.setColor(trackColor);
                        g2.fillRect(0, y, w, barHeight);
                        if (fillW > 0) {
                            g2.setColor(barColor);
                            g2.fillRect(0, y, fillW, barHeight);
                        }
                        break;
                    case STANDARD:
                    default:
                        g2.setColor(trackColor);
                        g2.fillRoundRect(0, y, w, barHeight, barHeight, barHeight);
                        if (fillW > 0) {
                            g2.setColor(barColor);
                            g2.fillRoundRect(0, y, fillW, barHeight, barHeight, barHeight);
                        }
                        break;
                }
                g2.dispose();
            }
        };
        barPanel.setOpaque(false);

        percentLabel = new JLabel(progress + "%");
        percentLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        percentLabel.setForeground(new Color(100, 116, 139));
        percentLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        add(barPanel, BorderLayout.CENTER);
        if (showLabel) add(percentLabel, BorderLayout.EAST);
    }

    private void updateLabel() {
        if (showPercentage) {
            percentLabel.setText(progress + "%");
        } else {
            percentLabel.setText(currentValue + "/" + totalValue);
        }
        percentLabel.setVisible(showLabel);
        repaint();
    }

    // === PROPRIEDADES ===

    public ProgressModel getModel() { return model; }
    public void setModel(ProgressModel model) {
        ProgressModel old = this.model;
        this.model = model;
        firePropertyChange("model", old, model);
        repaint();
    }

    public boolean isShowPercentage() { return showPercentage; }
    public void setShowPercentage(boolean showPercentage) {
        boolean old = this.showPercentage;
        this.showPercentage = showPercentage;
        firePropertyChange("showPercentage", old, showPercentage);
        updateLabel();
    }

    /** Define o valor atual e o total de uma vez, calculando a porcentagem automaticamente. */
    public void setValues(int current, int total) {
        this.totalValue = Math.max(1, total);
        this.currentValue = Math.max(0, Math.min(this.totalValue, current));
        this.progress = (int)((this.currentValue * 100.0) / this.totalValue);
        firePropertyChange("currentValue", -1, this.currentValue);
        firePropertyChange("totalValue", -1, this.totalValue);
        firePropertyChange("progress", -1, this.progress);
        updateLabel();
    }

    public int getCurrentValue() { return currentValue; }
    public void setCurrentValue(int currentValue) {
        int old = this.currentValue;
        this.currentValue = currentValue;
        if (totalValue > 0) this.progress = (int)((this.currentValue * 100.0) / this.totalValue);
        firePropertyChange("currentValue", old, this.currentValue);
        updateLabel();
    }

    public int getTotalValue() { return totalValue; }
    public void setTotalValue(int totalValue) {
        int old = this.totalValue;
        this.totalValue = Math.max(1, totalValue);
        this.progress = (int)((this.currentValue * 100.0) / this.totalValue);
        firePropertyChange("totalValue", old, this.totalValue);
        updateLabel();
    }

    /** Progresso de 0 a 100. Se usar setValues(), não precisa chamar esse. */
    public int getProgress() { return progress; }
    public void setProgress(int progress) {
        int old = this.progress;
        this.progress = Math.max(0, Math.min(100, progress));
        this.currentValue = this.progress; // fallback se user setar só progress
        this.totalValue = 100;
        firePropertyChange("progress", old, this.progress);
        updateLabel();
    }

    /** Cor da barra preenchida. */
    public Color getBarColor() { return barColor; }
    public void setBarColor(Color barColor) {
        Color old = this.barColor;
        this.barColor = barColor;
        firePropertyChange("barColor", old, barColor);
        repaint();
    }

    /** Cor do trilho de fundo. */
    public Color getTrackColor() { return trackColor; }
    public void setTrackColor(Color trackColor) {
        Color old = this.trackColor;
        this.trackColor = trackColor;
        firePropertyChange("trackColor", old, trackColor);
        repaint();
    }

    /** Exibe o rótulo de porcentagem. */
    public boolean isShowLabel() { return showLabel; }
    public void setShowLabel(boolean showLabel) {
        boolean old = this.showLabel;
        this.showLabel = showLabel;
        firePropertyChange("showLabel", old, showLabel);
        updateLabel();
    }

    /** Altura da barra em pixels. */
    public int getBarHeight() { return barHeight; }
    public void setBarHeight(int barHeight) {
        int old = this.barHeight;
        this.barHeight = Math.max(2, barHeight);
        firePropertyChange("barHeight", old, this.barHeight);
        repaint();
    }
}
