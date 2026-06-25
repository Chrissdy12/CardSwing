package com.cardSwing;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Classe utilitária para desenhar ícones para a Palette do NetBeans
 * sem precisar carregar arquivos de imagem externos.
 */
public class CardMuryIcons {
    
    private CardMuryIcons() {} // Impede que apareça na Palette do NetBeans

    public static Image getIconFor(String name) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (name.equals("CardPanel")) {
            g2.setColor(new Color(59, 130, 246)); // Fundo Azul
            g2.fillRoundRect(2, 2, 12, 12, 4, 4);
            g2.setColor(new Color(255, 255, 255, 100)); // Header
            g2.fillRect(2, 2, 12, 4);
        } else if (name.equals("CardButton")) {
            g2.setColor(new Color(59, 130, 246));
            g2.fillRoundRect(1, 4, 14, 8, 4, 4);
        } else if (name.equals("CardButtonRow")) {
            g2.setColor(new Color(59, 130, 246));
            g2.fillRoundRect(1, 5, 6, 6, 2, 2);
            g2.fillRoundRect(9, 5, 6, 6, 2, 2);
        } else if (name.equals("CardCheck")) {
            g2.setColor(new Color(59, 130, 246));
            g2.fillRoundRect(2, 2, 12, 12, 3, 3);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawLine(4, 7, 7, 10);
            g2.drawLine(7, 10, 11, 4);
        } else if (name.equals("CardSwitch")) {
            g2.setColor(new Color(16, 185, 129));
            g2.fillRoundRect(1, 4, 14, 8, 8, 8);
            g2.setColor(Color.WHITE);
            g2.fillOval(8, 5, 6, 6);
        } else if (name.equals("CardAvatar")) {
            g2.setColor(new Color(59, 130, 246));
            g2.fillOval(2, 2, 12, 12);
            g2.setColor(Color.WHITE);
            g2.fillOval(5, 5, 6, 6);
            g2.setColor(new Color(34, 197, 94));
            g2.fillOval(9, 9, 5, 5);
        } else if (name.equals("CardListPanel")) {
            g2.setColor(new Color(203, 213, 225));
            g2.fillRoundRect(1, 1, 14, 14, 2, 2);
            g2.setColor(new Color(59, 130, 246));
            g2.fillRoundRect(3, 3, 10, 4, 2, 2);
            g2.fillRoundRect(3, 9, 10, 4, 2, 2);
        } else if (name.equals("CardProgress")) {
            g2.setColor(new Color(226, 232, 240));
            g2.fillRoundRect(1, 6, 14, 4, 4, 4);
            g2.setColor(new Color(16, 185, 129));
            g2.fillRoundRect(1, 6, 9, 4, 4, 4);
        } else if (name.equals("CardSkeleton")) {
            g2.setColor(new Color(226, 232, 240));
            g2.fillRoundRect(2, 2, 12, 12, 3, 3);
            g2.setColor(Color.WHITE);
            g2.fillRect(4, 4, 8, 2);
            g2.fillRect(4, 8, 6, 2);
        } else if (name.equals("CardTitle") || name.equals("CardText") || name.equals("CardSubtitle")) {
            g2.setColor(new Color(15, 23, 42));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            g2.drawString(name.equals("CardTitle") ? "T" : "t", 4, 12);
        } else if (name.equals("CardSeparator")) {
            g2.setColor(new Color(203, 213, 225));
            g2.fillRect(1, 7, 14, 2);
        } else if (name.equals("CardTag")) {
            g2.setColor(new Color(139, 92, 246));
            g2.fillRoundRect(2, 4, 12, 8, 4, 4);
        } else if (name.equals("CardImage")) {
            g2.setColor(new Color(59, 130, 246));
            g2.drawRoundRect(2, 2, 12, 12, 2, 2);
            g2.drawOval(4, 4, 3, 3);
            g2.drawLine(2, 14, 7, 8);
            g2.drawLine(6, 9, 14, 14);
        } else if (name.equals("CardStatusRow") || name.equals("CardStatusBadge")) {
            g2.setColor(new Color(245, 158, 11));
            g2.fillOval(2, 6, 4, 4);
            g2.setColor(new Color(51, 65, 85));
            g2.fillRect(8, 7, 6, 2);
        } else {
            // Default genérico
            g2.setColor(Color.GRAY);
            g2.fillRoundRect(2, 2, 12, 12, 4, 4);
        }

        g2.dispose();
        return img;
    }
}
