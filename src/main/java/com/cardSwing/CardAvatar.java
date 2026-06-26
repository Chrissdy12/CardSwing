package com.cardSwing;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.Serializable;
import javax.swing.*;
import javax.imageio.ImageIO;

/**
 * Avatar circular com imagem ou iniciais e bolinha de status.
 */
public class CardAvatar extends JPanel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String initials = "CH";
    private String imagePath = "";
    private Icon icon;
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

        int w = getWidth();
        int h = getHeight();
        int d = Math.max(10, Math.min(w, h) - 4); // Calcula o diâmetro disponível
        int cx = (w - d) / 2; // Centraliza X
        int cy = (h - d) / 2; // Centraliza Y

        Shape circle = new Ellipse2D.Float(cx, cy, d, d);

        if (avatarImage != null) {
            java.awt.image.BufferedImage maskImage = new java.awt.image.BufferedImage(d, d, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D gMask = maskImage.createGraphics();
            gMask.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gMask.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            
            gMask.setColor(Color.BLACK);
            gMask.fillOval(0, 0, d, d);
            
            gMask.setComposite(AlphaComposite.SrcIn);
            
            int imgW = avatarImage.getWidth(null);
            int imgH = avatarImage.getHeight(null);
            if (imgW > 0 && imgH > 0) {
                float scale = Math.max((float) d / imgW, (float) d / imgH);
                int nw = (int) (imgW * scale);
                int nh = (int) (imgH * scale);
                int nx = (d - nw) / 2;
                int ny = (d - nh) / 2;
                gMask.drawImage(avatarImage, nx, ny, nw, nh, null);
            }
            gMask.dispose();
            
            g2.drawImage(maskImage, cx, cy, null);
        } else {
            g2.setColor(avatarColor);
            g2.fill(circle);
            if (initials != null && !initials.isEmpty()) {
                g2.setColor(textColor);
                g2.setFont(new Font("Segoe UI", Font.BOLD, Math.max(8, d / 2)));
                FontMetrics fm = g2.getFontMetrics();
                int tx = cx + (d - fm.stringWidth(initials)) / 2;
                int ty = cy + (d - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(initials, tx, ty);
            }
        }

        if (showOnlineDot) {
            int dotSize = Math.max(10, d / 4);
            int dx = cx + d - dotSize;
            int dy = cy + d - dotSize;
            
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
                if (this.icon != null) {
                    setIcon(this.icon);
                    return;
                }
            }
        } catch (Exception e) {
            avatarImage = null;
            if (this.icon != null) {
                setIcon(this.icon);
                return;
            }
        }
        repaint();
    }

    public Icon getIcon() { return icon; }
    public void setIcon(Icon icon) {
        this.icon = icon;
        if (icon instanceof ImageIcon) {
            this.avatarImage = ((ImageIcon) icon).getImage();
        } else if (icon != null) {
            java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(
                icon.getIconWidth(), icon.getIconHeight(), java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = bi.createGraphics();
            icon.paintIcon(this, g, 0, 0);
            g.dispose();
            this.avatarImage = bi;
        } else {
            this.avatarImage = null;
            if (this.imagePath != null && !this.imagePath.isEmpty()) {
                setImagePath(this.imagePath);
                return;
            }
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
