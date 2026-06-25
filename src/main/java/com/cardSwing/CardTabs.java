package com.cardSwing;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.io.Serializable;

/**
 * Substituto moderno para o JTabbedPane.
 * Apresenta abas com estilo "Material Design" (traço inferior, cores suaves e sem bordas 3D).
 * Totalmente compatível com a Palette do NetBeans (arrastar e soltar painéis dentro).
 */
public class CardTabs extends JTabbedPane implements Serializable {

    private static final long serialVersionUID = 1L;

    private Color themeColor = new Color(59, 130, 246); // Blue 500
    private Color unselectedTextColor = new Color(100, 116, 139); // Gray 500
    private Color trackColor = new Color(226, 232, 240); // Gray 200

    public CardTabs() {
        setUI(new CardTabsUI());
        setOpaque(false);
        setBackground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    public Color getThemeColor() { return themeColor; }
    public void setThemeColor(Color themeColor) {
        this.themeColor = themeColor;
        repaint();
    }

    public Color getUnselectedTextColor() { return unselectedTextColor; }
    public void setUnselectedTextColor(Color unselectedTextColor) {
        this.unselectedTextColor = unselectedTextColor;
        repaint();
    }
    
    public Color getTrackColor() { return trackColor; }
    public void setTrackColor(Color trackColor) {
        this.trackColor = trackColor;
        repaint();
    }

    private class CardTabsUI extends BasicTabbedPaneUI {
        
        @Override
        protected void installDefaults() {
            super.installDefaults();
            // Espaçamento das abas
            tabAreaInsets.left = 20; 
            tabAreaInsets.right = 20;
            tabInsets = new Insets(10, 25, 10, 25); 
        }

        @Override
        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            g.setColor(getBackground());
            g.fillRect(x, y, w, h);
        }

        @Override
        protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            Graphics2D g2 = (Graphics2D) g;
            
            // Desenha a linha trilho base (track) embaixo de todas as abas
            g2.setColor(trackColor);
            g2.drawLine(x, y + h - 1, x + w, y + h - 1); 
            
            // Desenha o traço grosso indicador de foco/seleção na aba atual
            if (isSelected) {
                g2.setColor(themeColor);
                g2.fillRect(x, y + h - 3, w, 3);
            }
        }

        @Override
        protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
            // Remove completamente o pontilhado nativo horrendo do Java Swing
        }

        @Override
        protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            g2.setFont(font);
            if (isSelected) {
                g2.setColor(themeColor);
            } else {
                g2.setColor(unselectedTextColor);
            }
            
            int textX = textRect.x + (textRect.width - metrics.stringWidth(title)) / 2;
            int textY = textRect.y + metrics.getAscent() + (textRect.height - metrics.getHeight()) / 2;
            g2.drawString(title, textX, textY);
        }

        @Override
        protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
            // Aqui é a borda enorme que o JTabbedPane desenha ao redor de todo o conteúdo da tela.
            // Vamos desabilitar as laterais e o fundo, deixando apenas a linha que conecta nas abas em cima.
            int width = tabPane.getWidth();
            int y = calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
            
            g.setColor(trackColor);
            g.drawLine(0, y - 1, width, y - 1);
        }
    }
}
