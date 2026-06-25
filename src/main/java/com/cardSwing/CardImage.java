package com.cardSwing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Área de imagem do Card — exibe uma imagem com cantos arredondados.
 *
 * <p><b>PALETTE:</b> Arraste para dentro de um CardPanel.</p>
 *
 * <p>Propriedades editáveis:</p>
 * <ul>
 *   <li><b>imagePath</b> — caminho do arquivo de imagem</li>
 *   <li><b>imageHeight</b> — altura da área de imagem</li>
 *   <li><b>imageRadius</b> — raio dos cantos</li>
 *   <li><b>placeholderColor</b> — cor quando sem imagem</li>
 * </ul>
 */
public class CardImage extends JPanel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String imagePath = "";
    private int imageHeight = 120;
    private int imageRadius = 8;
    private Color placeholderColor = new Color(241, 245, 249);
    private transient BufferedImage loadedImage;

    public CardImage() {
        setOpaque(false);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        updateSize();
    }

    public CardImage(String imagePath) {
        this();
        setImagePath(imagePath);
    }

    private void updateSize() {
        setMaximumSize(new Dimension(Integer.MAX_VALUE, imageHeight));
        setPreferredSize(new Dimension(200, imageHeight));
        revalidate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int w = getWidth();
        int h = getHeight();

        // Clip arredondado
        g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, w, h, imageRadius, imageRadius));

        if (loadedImage != null) {
            // Desenha imagem redimensionada (cover)
            double scaleX = (double) w / loadedImage.getWidth();
            double scaleY = (double) h / loadedImage.getHeight();
            double scale = Math.max(scaleX, scaleY);
            int drawW = (int)(loadedImage.getWidth() * scale);
            int drawH = (int)(loadedImage.getHeight() * scale);
            int drawX = (w - drawW) / 2;
            int drawY = (h - drawH) / 2;
            g2.drawImage(loadedImage, drawX, drawY, drawW, drawH, null);
        } else {
            // Placeholder
            g2.setColor(placeholderColor);
            g2.fillRect(0, 0, w, h);
            // Ícone de imagem
            g2.setColor(new Color(148, 163, 184));
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            FontMetrics fm = g2.getFontMetrics();
            String txt = "📷 Imagem";
            int tx = (w - fm.stringWidth(txt)) / 2;
            int ty = (h + fm.getAscent()) / 2 - 2;
            g2.drawString(txt, tx, ty);
        }

        g2.dispose();
    }

    // === PROPRIEDADES ===

    /** Caminho do arquivo de imagem. */
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) {
        String old = this.imagePath;
        this.imagePath = imagePath != null ? imagePath : "";
        firePropertyChange("imagePath", old, this.imagePath);
        loadImage();
        repaint();
    }

    /** Altura da área de imagem. */
    public int getImageHeight() { return imageHeight; }
    public void setImageHeight(int imageHeight) {
        int old = this.imageHeight;
        this.imageHeight = Math.max(20, imageHeight);
        firePropertyChange("imageHeight", old, this.imageHeight);
        updateSize();
        repaint();
    }

    /** Raio dos cantos arredondados. */
    public int getImageRadius() { return imageRadius; }
    public void setImageRadius(int imageRadius) {
        int old = this.imageRadius;
        this.imageRadius = Math.max(0, imageRadius);
        firePropertyChange("imageRadius", old, this.imageRadius);
        repaint();
    }

    /** Cor de fundo quando sem imagem. */
    public Color getPlaceholderColor() { return placeholderColor; }
    public void setPlaceholderColor(Color placeholderColor) {
        Color old = this.placeholderColor;
        this.placeholderColor = placeholderColor;
        firePropertyChange("placeholderColor", old, placeholderColor);
        repaint();
    }

    private void loadImage() {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                loadedImage = ImageIO.read(new File(imagePath));
            } catch (Exception e) {
                loadedImage = null;
            }
        } else {
            loadedImage = null;
        }
    }
}
