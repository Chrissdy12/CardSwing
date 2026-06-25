package com.cardSwing;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.File;
import javax.swing.*;
import javax.imageio.ImageIO;

/**
 * Avatar circular com imagem ou iniciais e bolinha de status.
 */
public class CardAvatar extends JPanel {

    private String initials = "CH";
    private String imagePath = "";
    private Image avatarImage;
    private Color avatarColor = new Color(59, 130, 246);
    private Color textColor = Color.WHITE;
    private boolean showOnlineDot = true;
    private int avatarSize = 40;

    public CardAvatar() {
        setOpaque(false);
        updateSize();
    }

    private void updateSize() {
        Dimension d = new Dimension(avatarSize + 4, avatarSize + 4);
        setPreferredSize(d);
        setMaximumSize(d);
        setMinimumSize(d);
        revalidate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int d = avatarSize;
        Shape circle = new Ellipse2D.Float(2, 2, d, d);

        if (avatarImage != null) {
            g2.setClip(circle);
            // Desenha a imagem preenchendo o círculo (crop center simplificado)
            int imgW = avatarImage.getWidth(null);
            int imgH = avatarImage.getHeight(null);
            if (imgW > 0 && imgH > 0) {
                float scale = Math.max((float) d / imgW, (float) d / imgH);
                int nw = (int) (imgW * scale);
                int nh = (int) (imgH * scale);
                int nx = 2 + (d - nw) / 2;
                int ny = 2 + (d - nh) / 2;
                g2.drawImage(avatarImage, nx, ny, nw, nh, null);
            }
            g2.setClip(null);
        } else {
            g2.setColor(avatarColor);
            g2.fill(circle);
            if (initials != null && !initials.isEmpty()) {
                g2.setColor(textColor);
                g2.setFont(new Font("Segoe UI", Font.BOLD, d / 2));
                FontMetrics fm = g2.getFontMetrics();
                int tx = 2 + (d - fm.stringWidth(initials)) / 2;
                int ty = 2 + (d - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(initials, tx, ty);
            }
        }

        if (showOnlineDot) {
            int dotSize = Math.max(10, d / 4);
            int dx = 2 + d - dotSize + 2; // offset for border
            int dy = 2 + d - dotSize;
            
            // Borda branca do dot
            g2.setColor(Color.WHITE);
            g2.fillOval(dx - 2, dy - 2, dotSize + 4, dotSize + 4);
            
            // Dot Verde
            g2.setColor(new Color(34, 197, 94)); // Green-500
            g2.fillOval(dx, dy, dotSize, dotSize);
        }

        g2.dispose();
    }

    // Properties
    public String getInitials() { return initials; }
    public void setInitials(String initials) {
        this.initials = initials;
        repaint();
    }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        try {
            if (imagePath != null && !imagePath.isEmpty()) {
                avatarImage = ImageIO.read(new File(imagePath));
            } else {
                avatarImage = null;
            }
        } catch (Exception e) {
            avatarImage = null;
        }
        repaint();
    }

    public Color getAvatarColor() { return avatarColor; }
    public void setAvatarColor(Color avatarColor) {
        this.avatarColor = avatarColor;
        repaint();
    }

    public Color getTextColor() { return textColor; }
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        repaint();
    }

    public boolean isShowOnlineDot() { return showOnlineDot; }
    public void setShowOnlineDot(boolean showOnlineDot) {
        this.showOnlineDot = showOnlineDot;
        repaint();
    }

    public int getAvatarSize() { return avatarSize; }
    public void setAvatarSize(int avatarSize) {
        this.avatarSize = Math.max(24, avatarSize);
        updateSize();
        repaint();
    }
}
