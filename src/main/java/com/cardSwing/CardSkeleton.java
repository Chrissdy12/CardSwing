package com.cardSwing;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

/**
 * Esqueleto de carregamento animado (Shimmer effect).
 * 
 * <p><b>PALETTE:</b> Arraste para o CardListPanel temporariamente enquanto
 * carrega dados, depois limpe e adicione os dados reais.</p>
 */
public class CardSkeleton extends JPanel {

    private int offset = -300;
    private final Timer timer;
    private int cardWidth = 420;

    public CardSkeleton() {
        setOpaque(false);
        
        timer = new Timer(16, e -> {
            offset += 15;
            if (offset > getWidth() + 300) {
                offset = -300;
            }
            repaint();
        });
        
        // Tenta iniciar a animação só quando visível para economizar CPU
        addPropertyChangeListener("ancestor", e -> {
            if (!java.beans.Beans.isDesignTime()) {
                if (e.getNewValue() != null) timer.start();
                else timer.stop();
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(cardWidth + 5, 260 + 5);
    }
    
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(300, 260);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth() - 5;
        int h = getHeight() - 5;
        int r = 16;

        // === SOMBRA ===
        for (int i = 4; i >= 1; i--) {
            g2.setColor(new Color(0, 0, 0, 8 + (4 - i) * 5));
            g2.fill(new RoundRectangle2D.Float(i, i + 1, w, h, r + 2, r + 2));
        }

        // === FUNDO ===
        RoundRectangle2D bgRect = new RoundRectangle2D.Float(0, 0, w, h, r, r);
        g2.setColor(Color.WHITE);
        g2.fill(bgRect);

        // === BORDA ===
        g2.setColor(new Color(229, 231, 235));
        g2.setStroke(new BasicStroke(1f));
        g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1, h - 1, r, r));

        // Aplica Clip para não desenhar fora dos cantos arredondados
        g2.setClip(bgRect);

        // === SHIMMER PAINT ===
        // Cores com maior contraste para a onda de brilho ficar mais evidente
        Color base = new Color(226, 232, 240); // Slate 200 (fundo do esqueleto um pouco mais escuro)
        Color highlight = new Color(255, 255, 255); // Branco puro (brilho passando)
        
        // Fallback for extreme widths preventing crash
        int endOffset = offset + 250;
        if (offset == endOffset) endOffset++; 
        
        LinearGradientPaint paint = new LinearGradientPaint(
            offset, 0, endOffset, 0,
            new float[]{0f, 0.5f, 1f},
            new Color[]{base, highlight, base}
        );
        g2.setPaint(paint);

        // Header (A faixa vermelha de cima)
        g2.fillRect(0, 0, w, 8);

        // Title skeleton (EAN e Switch)
        g2.fillRoundRect(18, 25, 200, 22, 8, 8); // Title
        g2.fillRoundRect(w - 60, 25, 40, 22, 11, 11); // Switch

        // Subtitle skeleton (Descrição)
        g2.fillRoundRect(18, 65, w - 50, 16, 6, 6);
        
        // NCM block
        g2.fillRoundRect(18, 100, 120, 24, 8, 8);
        
        // Descrição Sugerida (2 linhas)
        g2.fillRoundRect(18, 145, w - 50, 14, 6, 6);
        g2.fillRoundRect(18, 165, w - 80, 14, 6, 6);
        
        // NCM Sugerido
        g2.fillRoundRect(18, 195, 150, 24, 8, 8);
        
        // Footer (Parametrizados / Não Parametrizados)
        g2.fillRoundRect(18, 235, 120, 14, 6, 6);
        g2.fillRoundRect(w - 150, 235, 130, 14, 6, 6);

        g2.dispose();
    }

    public int getCardWidth() { return cardWidth; }
    public void setCardWidth(int cardWidth) {
        this.cardWidth = cardWidth;
        revalidate();
        repaint();
    }
}
