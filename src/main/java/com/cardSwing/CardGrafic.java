package com.cardSwing;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 * Componente de Gráficos para o CardSwing.
 * Suporta gráficos de Barras, Barras Horizontais, Linha e Pizza (Pie).
 *
 * <p><b>PALETTE:</b> Arraste para dentro de um CardPanel.</p>
 */
public class CardGrafic extends JPanel implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum ChartType {
        BAR, BAR_MODERN, BAR_GRADIENT, BAR_GRADIENT_COLORFUL, BAR_LOLLIPOP, BAR_LOLLIPOP_COLORFUL, 
        HORIZONTAL_BAR, HORIZONTAL_BAR_MODERN, HORIZONTAL_BAR_MODERN_2, 
        HORIZONTAL_BAR_GRADIENT, HORIZONTAL_BAR_GRADIENT_COLORFUL, HORIZONTAL_BAR_LOLLIPOP, HORIZONTAL_BAR_LOLLIPOP_COLORFUL, 
        LINE, AREA, PIE, DONUT
    }

    public enum TitlePosition {
        TOP_LEFT, TOP_CENTER, TOP_RIGHT, BOTTOM_CENTER
    }

    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_BOLD_12 = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONT_PLAIN_11 = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font FONT_PLAIN_10 = new Font("Segoe UI", Font.PLAIN, 10);

    private ChartType chartType = ChartType.BAR;
    private String values = "15, 30, 45, 10, 25";
    private String labels = "Jan, Fev, Mar, Abr, Mai";
    private String colors = "";
    private Color chartColor = new Color(59, 130, 246);
    private Color gridColor = new Color(226, 232, 240);
    private Color textColor = new Color(100, 116, 139);
    private Color titleColor = new Color(100, 116, 139);
    private boolean showGrid = true;
    private boolean showLabels = true;
    private String title = "Evolução";
    private TitlePosition titlePosition = TitlePosition.TOP_CENTER;

    private final Color[] colorfulPalette = {
        new Color(59, 130, 246), new Color(16, 185, 129), 
        new Color(245, 158, 11), new Color(239, 68, 68), new Color(139, 92, 246)
    };

    private List<Double> dataValues = new ArrayList<>();
    private List<String> dataLabels = new ArrayList<>();
    private List<Color> customColors = new ArrayList<>();
    private List<Color> activeColors = new ArrayList<>();
    private boolean useDataBinding = false;

    // Cache para os valores das propriedades String para evitar parsing no paintComponent
    private List<Double> parsedValuesList;
    private List<String> parsedLabelsList;
    private List<Color> activeColorsList;

    public CardGrafic() {
        setOpaque(false);
        setPreferredSize(new Dimension(300, 200));
        parsedValuesList = parseValues(values);
        parsedLabelsList = parseLabels(labels);
        activeColorsList = parseColors(colors);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Draw title
        g2.setColor(titleColor);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        FontMetrics fmTitle = g2.getFontMetrics();
        
        boolean showTitle = title != null && !title.trim().isEmpty();
        if (showTitle) {
            int titleX = 0;
            int titleY = 0;
            if (titlePosition == TitlePosition.TOP_LEFT) {
                titleX = 20;
                titleY = fmTitle.getAscent() + 5;
            } else if (titlePosition == TitlePosition.TOP_RIGHT) {
                titleX = width - fmTitle.stringWidth(title) - 20;
                titleY = fmTitle.getAscent() + 5;
            } else if (titlePosition == TitlePosition.BOTTOM_CENTER) {
                titleX = (width - fmTitle.stringWidth(title)) / 2;
                titleY = height - 10;
            } else { // TOP_CENTER
                titleX = (width - fmTitle.stringWidth(title)) / 2;
                titleY = fmTitle.getAscent() + 5;
            }
            g2.drawString(title, titleX, titleY);
        }

        List<Double> parsedValues = useDataBinding ? dataValues : parsedValuesList;
        List<String> parsedLabels = useDataBinding ? dataLabels : parsedLabelsList;
        this.activeColors = useDataBinding ? customColors : activeColorsList;

        if (parsedValues.isEmpty()) {
            g2.dispose();
            return;
        }

        int topMargin = (showTitle && titlePosition != TitlePosition.BOTTOM_CENTER) ? fmTitle.getHeight() + 25 : 25;
        if (chartType == ChartType.BAR_MODERN || chartType == ChartType.BAR_GRADIENT || chartType == ChartType.BAR_GRADIENT_COLORFUL || chartType == ChartType.BAR_LOLLIPOP || chartType == ChartType.BAR_LOLLIPOP_COLORFUL) {
            topMargin += 15;
        }
        
        int bottomMargin = (showTitle && titlePosition == TitlePosition.BOTTOM_CENTER) ? fmTitle.getHeight() + 25 : 30;
        boolean isHoriz = (chartType == ChartType.HORIZONTAL_BAR || chartType == ChartType.HORIZONTAL_BAR_MODERN || chartType == ChartType.HORIZONTAL_BAR_MODERN_2 || 
                           chartType == ChartType.HORIZONTAL_BAR_GRADIENT || chartType == ChartType.HORIZONTAL_BAR_GRADIENT_COLORFUL || 
                           chartType == ChartType.HORIZONTAL_BAR_LOLLIPOP || chartType == ChartType.HORIZONTAL_BAR_LOLLIPOP_COLORFUL);
        
        int leftMargin = 30;
        if (isHoriz && showLabels) {
            g2.setFont(FONT_BOLD_12);
            FontMetrics fmLbl = g2.getFontMetrics();
            int maxLblWidth = 0;
            for (String l : parsedLabels) {
                int w = fmLbl.stringWidth(l);
                if (w > maxLblWidth) maxLblWidth = w;
            }
            leftMargin = Math.max(30, maxLblWidth + 15);
        }

        int maxValStrWidth = 0;
        if (showLabels && (chartType == ChartType.HORIZONTAL_BAR_GRADIENT || chartType == ChartType.HORIZONTAL_BAR_GRADIENT_COLORFUL || chartType == ChartType.HORIZONTAL_BAR_LOLLIPOP || chartType == ChartType.HORIZONTAL_BAR_LOLLIPOP_COLORFUL)) {
            g2.setFont(FONT_BOLD_12);
            FontMetrics fmVal = g2.getFontMetrics();
            for (Double v : parsedValues) {
                String vs = (v == Math.floor(v)) ? String.valueOf((int) v.doubleValue()) : String.valueOf(v);
                int w = fmVal.stringWidth(vs);
                if (w > maxValStrWidth) maxValStrWidth = w;
            }
        }
        int rightMargin = 20 + maxValStrWidth + (chartType == ChartType.HORIZONTAL_BAR_LOLLIPOP || chartType == ChartType.HORIZONTAL_BAR_LOLLIPOP_COLORFUL ? 15 : 5);

        int chartWidth = width - leftMargin - rightMargin;
        int chartHeight = height - topMargin - bottomMargin;

        double maxVal = parsedValues.stream().mapToDouble(v -> v).max().orElse(1);
        if (maxVal == 0) maxVal = 1;

        if (chartType == ChartType.PIE || chartType == ChartType.DONUT) {
            drawPieChart(g2, width, height, topMargin, bottomMargin, parsedValues, parsedLabels);
        } else if (chartType == ChartType.HORIZONTAL_BAR) {
            drawHorizontalBarChart(g2, leftMargin, topMargin, chartWidth, chartHeight, maxVal, parsedValues, parsedLabels);
        } else if (chartType == ChartType.HORIZONTAL_BAR_MODERN) {
            drawModernHorizontalBarChart(g2, leftMargin, topMargin, chartWidth, chartHeight, maxVal, parsedValues, parsedLabels);
        } else if (chartType == ChartType.HORIZONTAL_BAR_MODERN_2) {
            drawModern2HorizontalBarChart(g2, leftMargin, topMargin, chartWidth, chartHeight, maxVal, parsedValues, parsedLabels);
        } else if (chartType == ChartType.HORIZONTAL_BAR_GRADIENT || chartType == ChartType.HORIZONTAL_BAR_GRADIENT_COLORFUL) {
            drawGradientHorizontalBarChart(g2, leftMargin, topMargin, chartWidth, chartHeight, maxVal, parsedValues, parsedLabels, chartType == ChartType.HORIZONTAL_BAR_GRADIENT_COLORFUL);
        } else if (chartType == ChartType.HORIZONTAL_BAR_LOLLIPOP || chartType == ChartType.HORIZONTAL_BAR_LOLLIPOP_COLORFUL) {
            drawLollipopHorizontalBarChart(g2, leftMargin, topMargin, chartWidth, chartHeight, maxVal, parsedValues, parsedLabels, chartType == ChartType.HORIZONTAL_BAR_LOLLIPOP_COLORFUL);
        } else if (chartType == ChartType.BAR_MODERN) {
            drawModernBarChart(g2, leftMargin, topMargin, chartWidth, chartHeight, maxVal, parsedValues, parsedLabels);
        } else if (chartType == ChartType.BAR_GRADIENT || chartType == ChartType.BAR_GRADIENT_COLORFUL) {
            drawGradientBarChart(g2, leftMargin, topMargin, chartWidth, chartHeight, maxVal, parsedValues, parsedLabels, chartType == ChartType.BAR_GRADIENT_COLORFUL);
        } else if (chartType == ChartType.BAR_LOLLIPOP || chartType == ChartType.BAR_LOLLIPOP_COLORFUL) {
            drawLollipopBarChart(g2, leftMargin, topMargin, chartWidth, chartHeight, maxVal, parsedValues, parsedLabels, chartType == ChartType.BAR_LOLLIPOP_COLORFUL);
        } else {
            drawCartesianChart(g2, leftMargin, topMargin, chartWidth, chartHeight, maxVal, parsedValues, parsedLabels);
        }

        g2.dispose();
    }

    private void drawHorizontalBarChart(Graphics2D g2, int x, int y, int width, int height, double maxVal, List<Double> vals, List<String> lbls) {
        // Draw grid
        if (showGrid) {
            g2.setColor(gridColor);
            g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0));
            int gridLines = 5;
            for (int i = 0; i <= gridLines; i++) {
                int lineX = x + (i * width / gridLines);
                g2.drawLine(lineX, y, lineX, y + height);
                
                // Draw X axis labels (values)
                String xLabel = String.valueOf((int) ((maxVal / gridLines) * i));
                g2.setFont(FONT_PLAIN_10);
                g2.setColor(textColor);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(xLabel, lineX - (fm.stringWidth(xLabel) / 2), y + height + 15);
            }
        }

        int n = vals.size();
        if (n == 0) return;

        double step = (double) height / n;
        
        for (int i = 0; i < n; i++) {
            double v = vals.get(i);
            int valWidth = (int) ((v / maxVal) * width);
            int py = y + (int) (i * step) + (int) (step / 2);
            int px = x;

            int barHeight = (int) (step * 0.6);
            int by = py - (barHeight / 2);
            
            g2.setColor(getColor(i, new Color[]{chartColor}));
            g2.fillRoundRect(px, by, valWidth, barHeight, 5, 5);

            // Draw Y labels (categories)
            if (showLabels && i < lbls.size()) {
                String lbl = lbls.get(i);
                g2.setFont(FONT_PLAIN_11);
                g2.setColor(textColor);
                FontMetrics fm = g2.getFontMetrics();
                // truncate long labels
                if (fm.stringWidth(lbl) > x - 5) {
                    while (lbl.length() > 0 && fm.stringWidth(lbl + "...") > x - 5) {
                        lbl = lbl.substring(0, lbl.length() - 1);
                    }
                    lbl += "...";
                }
                g2.drawString(lbl, x - fm.stringWidth(lbl) - 5, py + (fm.getAscent() / 2) - 1);
            }
        }
    }

    private void drawModernHorizontalBarChart(Graphics2D g2, int x, int y, int width, int height, double maxVal, List<Double> vals, List<String> lbls) {
        int n = vals.size();
        if (n == 0) return;

        double step = (double) height / n;
        
        Color[] modernColors = {
            chartColor, 
            new Color(16, 185, 129), // Verde esmeralda
            new Color(245, 158, 11), // Laranja amber
            new Color(139, 92, 246), // Roxo violeta
            new Color(239, 68, 68),  // Vermelho
            new Color(14, 165, 233)  // Azul claro
        };

        for (int i = 0; i < n; i++) {
            double v = vals.get(i);
            int valWidth = (int) ((v / maxVal) * width);
            
            int py = y + (int) (i * step) + (int) (step / 2);
            int barHeight = 8;
            int by = py + 2;

            if (showLabels && i < lbls.size()) {
                String lbl = lbls.get(i);
                g2.setFont(FONT_BOLD_12);
                g2.setColor(new Color(51, 65, 85)); 
                g2.drawString(lbl, x, by - 6);
            }

            if (showLabels) {
                String valStr = (v == Math.floor(v)) ? String.valueOf((int) v) : String.valueOf(v);
                g2.setFont(FONT_BOLD_12);
                g2.setColor(getColor(i, modernColors));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(valStr, x + width - fm.stringWidth(valStr), by - 6);
            }

            g2.setColor(gridColor);
            g2.fillRoundRect(x, by, width, barHeight, barHeight, barHeight);

            if (valWidth > 0) {
                g2.setColor(getColor(i, modernColors));
                g2.fillRoundRect(x, by, valWidth, barHeight, barHeight, barHeight);
            }
        }
    }

    private void drawModern2HorizontalBarChart(Graphics2D g2, int x, int y, int width, int height, double maxVal, List<Double> vals, List<String> lbls) {
        int n = vals.size();
        if (n == 0) return;
        double step = (double) height / n;
        Color[] modernColors = { chartColor, new Color(16, 185, 129), new Color(245, 158, 11), new Color(139, 92, 246), new Color(239, 68, 68), new Color(14, 165, 233) };

        for (int i = 0; i < n; i++) {
            double v = vals.get(i);
            int valWidth = (int) ((v / maxVal) * width);
            int py = y + (int) (i * step) + (int) (step / 2);
            int barHeight = 24;
            int by = py - (barHeight / 2);

            // Track com cor baseada na cor principal com alpha
            Color barCol = getColor(i, modernColors);
            g2.setColor(new Color(barCol.getRed(), barCol.getGreen(), barCol.getBlue(), 40));
            g2.fillRoundRect(x, by, width, barHeight, barHeight, barHeight);

            // Barra principal
            if (valWidth > 0) {
                g2.setColor(barCol);
                g2.fillRoundRect(x, by, valWidth, barHeight, barHeight, barHeight);
            }

            g2.setFont(FONT_BOLD_12);
            FontMetrics fm = g2.getFontMetrics();
            
            // Texto da label por cima da barra (ou fora se for muito pequena)
            if (showLabels && i < lbls.size()) {
                String lbl = lbls.get(i);
                if (valWidth > fm.stringWidth(lbl) + 15) {
                    g2.setColor(Color.WHITE);
                    g2.drawString(lbl, x + 10, by + (barHeight/2) + (fm.getAscent()/2) - 1);
                } else {
                    g2.setColor(textColor);
                    g2.drawString(lbl, x + valWidth + 8, by + (barHeight/2) + (fm.getAscent()/2) - 1);
                }
            }

            // Valor alinhado à direita dentro do track
            if (showLabels) {
                String valStr = (v == Math.floor(v)) ? String.valueOf((int) v) : String.valueOf(v);
                g2.setColor(textColor);
                g2.drawString(valStr, x + width - fm.stringWidth(valStr) - 10, by + (barHeight/2) + (fm.getAscent()/2) - 1);
            }
        }
    }

    private void drawModernBarChart(Graphics2D g2, int x, int y, int width, int height, double maxVal, List<Double> vals, List<String> lbls) {
        int n = vals.size();
        if (n == 0) return;
        double step = (double) width / n;
        Color[] modernColors = { chartColor, new Color(16, 185, 129), new Color(245, 158, 11), new Color(139, 92, 246), new Color(239, 68, 68), new Color(14, 165, 233) };

        for (int i = 0; i < n; i++) {
            double v = vals.get(i);
            int valHeight = (int) ((v / maxVal) * height);
            int px = x + (int) (i * step) + (int) (step / 2);
            int py = y + height - valHeight;

            int barWidth = (int) (step * 0.5);
            int bx = px - (barWidth / 2);

            // Track (fundo da barra)
            g2.setColor(gridColor);
            g2.fillRoundRect(bx, y, barWidth, height, barWidth, barWidth);

            // Fill (barra preenchida)
            if (valHeight > 0) {
                g2.setColor(getColor(i, modernColors));
                g2.fillRoundRect(bx, py, barWidth, valHeight, barWidth, barWidth);
            }

            g2.setFont(FONT_BOLD_12);
            FontMetrics fm = g2.getFontMetrics();

            // Valor no topo flutuando
            if (showLabels) {
                String valStr = (v == Math.floor(v)) ? String.valueOf((int) v) : String.valueOf(v);
                g2.setColor(getColor(i, modernColors));
                g2.drawString(valStr, px - (fm.stringWidth(valStr) / 2), py - 8);
            }

            // Label na base
            if (showLabels && i < lbls.size()) {
                String lbl = lbls.get(i);
                g2.setColor(textColor);
                g2.drawString(lbl, px - (fm.stringWidth(lbl) / 2), y + height + 20);
            }
        }
    }

    private void drawGradientBarChart(Graphics2D g2, int x, int y, int width, int height, double maxVal, List<Double> vals, List<String> lbls, boolean colorful) {
        int n = vals.size();
        if (n == 0) return;
        double step = (double) width / n;

        for (int i = 0; i < n; i++) {
            double v = vals.get(i);
            int valHeight = (int) ((v / maxVal) * height);
            int px = x + (int) (i * step) + (int) (step / 2);
            int py = y + height - valHeight;
            int barWidth = (int) (step * 0.6);
            int bx = px - (barWidth / 2);

            if (valHeight > 0) {
                Color base = getColor(i, colorful ? colorfulPalette : new Color[]{chartColor});
                Color topColor = new Color(Math.min(255, base.getRed() + 40), Math.min(255, base.getGreen() + 40), Math.min(255, base.getBlue() + 40));
                Color bottomColor = base.darker();
                
                GradientPaint gp = new GradientPaint(bx, py, topColor, bx, py + valHeight, bottomColor);
                g2.setPaint(gp);
                g2.fillRoundRect(bx, py, barWidth, valHeight, 10, 10);
            }

            g2.setFont(FONT_BOLD_12);
            FontMetrics fm = g2.getFontMetrics();

            if (showLabels) {
                String valStr = (v == Math.floor(v)) ? String.valueOf((int) v) : String.valueOf(v);
                g2.setColor(textColor);
                g2.drawString(valStr, px - (fm.stringWidth(valStr) / 2), py - 8);
            }

            if (showLabels && i < lbls.size()) {
                String lbl = lbls.get(i);
                g2.setColor(textColor);
                g2.drawString(lbl, px - (fm.stringWidth(lbl) / 2), y + height + 20);
            }
        }
    }

    private void drawGradientHorizontalBarChart(Graphics2D g2, int x, int y, int width, int height, double maxVal, List<Double> vals, List<String> lbls, boolean colorful) {
        int n = vals.size();
        if (n == 0) return;
        double step = (double) height / n;

        for (int i = 0; i < n; i++) {
            double v = vals.get(i);
            int valWidth = (int) ((v / maxVal) * width);
            int py = y + (int) (i * step) + (int) (step / 2);
            int barHeight = (int) (step * 0.6);
            int by = py - (barHeight / 2);

            if (valWidth > 0) {
                Color base = getColor(i, colorful ? colorfulPalette : new Color[]{chartColor});
                Color leftColor = new Color(Math.min(255, base.getRed() + 40), Math.min(255, base.getGreen() + 40), Math.min(255, base.getBlue() + 40));
                Color rightColor = base.darker();
                
                GradientPaint gp = new GradientPaint(x, by, leftColor, x + valWidth, by, rightColor);
                g2.setPaint(gp);
                g2.fillRoundRect(x, by, valWidth, barHeight, 10, 10);
            }

            g2.setFont(FONT_BOLD_12);
            FontMetrics fm = g2.getFontMetrics();

            if (showLabels && i < lbls.size()) {
                String lbl = lbls.get(i);
                g2.setColor(textColor);
                g2.drawString(lbl, x - fm.stringWidth(lbl) - 8, py + (fm.getAscent() / 2) - 1);
            }

            if (showLabels) {
                String valStr = (v == Math.floor(v)) ? String.valueOf((int) v) : String.valueOf(v);
                g2.setColor(textColor);
                g2.drawString(valStr, x + valWidth + 8, py + (fm.getAscent() / 2) - 1);
            }
        }
    }

    private void drawLollipopBarChart(Graphics2D g2, int x, int y, int width, int height, double maxVal, List<Double> vals, List<String> lbls, boolean colorful) {
        int n = vals.size();
        if (n == 0) return;
        double step = (double) width / n;

        g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        for (int i = 0; i < n; i++) {
            double v = vals.get(i);
            int valHeight = (int) ((v / maxVal) * height);
            int px = x + (int) (i * step) + (int) (step / 2);
            int py = y + height - valHeight;

            Color base = getColor(i, colorful ? colorfulPalette : new Color[]{chartColor});
            g2.setColor(base);
            
            // Draw stick
            g2.drawLine(px, y + height, px, py);
            
            // Draw candy
            int radius = 12;
            g2.fillOval(px - radius/2, py - radius/2, radius, radius);

            g2.setFont(FONT_BOLD_12);
            FontMetrics fm = g2.getFontMetrics();

            if (showLabels) {
                String valStr = (v == Math.floor(v)) ? String.valueOf((int) v) : String.valueOf(v);
                g2.setColor(textColor);
                g2.drawString(valStr, px - (fm.stringWidth(valStr) / 2), py - radius - 4);
            }

            if (showLabels && i < lbls.size()) {
                String lbl = lbls.get(i);
                g2.setColor(textColor);
                g2.drawString(lbl, px - (fm.stringWidth(lbl) / 2), y + height + 20);
            }
        }
    }

    private void drawLollipopHorizontalBarChart(Graphics2D g2, int x, int y, int width, int height, double maxVal, List<Double> vals, List<String> lbls, boolean colorful) {
        int n = vals.size();
        if (n == 0) return;
        double step = (double) height / n;

        g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        for (int i = 0; i < n; i++) {
            double v = vals.get(i);
            int valWidth = (int) ((v / maxVal) * width);
            int py = y + (int) (i * step) + (int) (step / 2);

            Color base = getColor(i, colorful ? colorfulPalette : new Color[]{chartColor});
            g2.setColor(base);

            // Draw stick
            g2.drawLine(x, py, x + valWidth, py);
            
            // Draw candy
            int radius = 12;
            g2.fillOval(x + valWidth - radius/2, py - radius/2, radius, radius);

            g2.setFont(FONT_BOLD_12);
            FontMetrics fm = g2.getFontMetrics();

            if (showLabels && i < lbls.size()) {
                String lbl = lbls.get(i);
                g2.setColor(textColor);
                g2.drawString(lbl, x - fm.stringWidth(lbl) - 8, py + (fm.getAscent() / 2) - 1);
            }

            if (showLabels) {
                String valStr = (v == Math.floor(v)) ? String.valueOf((int) v) : String.valueOf(v);
                g2.setColor(textColor);
                g2.drawString(valStr, x + valWidth + radius + 4, py + (fm.getAscent() / 2) - 1);
            }
        }
    }

    private void drawCartesianChart(Graphics2D g2, int x, int y, int width, int height, double maxVal, List<Double> vals, List<String> lbls) {
        // Draw grid
        if (showGrid) {
            g2.setColor(gridColor);
            g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0));
            int gridLines = 5;
            for (int i = 0; i <= gridLines; i++) {
                int lineY = y + height - (i * height / gridLines);
                g2.drawLine(x, lineY, x + width, lineY);
                
                // Draw Y axis labels
                String yLabel = String.valueOf((int) ((maxVal / gridLines) * i));
                g2.setFont(FONT_PLAIN_10);
                g2.setColor(textColor);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(yLabel, x - fm.stringWidth(yLabel) - 5, lineY + (fm.getAscent() / 2));
            }
        }

        int n = vals.size();
        if (n == 0) return;

        double step = (double) width / n;
        
        g2.setStroke(new BasicStroke(2f));
        GeneralPath path = new GeneralPath();
        
        int firstPx = -1, lastPx = -1;

        for (int i = 0; i < n; i++) {
            double v = vals.get(i);
            int valHeight = (int) ((v / maxVal) * height);
            int px = x + (int) (i * step) + (int) (step / 2);
            int py = y + height - valHeight;
            
            if (firstPx == -1) firstPx = px;
            lastPx = px;

            if (chartType == ChartType.BAR) {
                int barWidth = (int) (step * 0.6);
                int bx = px - (barWidth / 2);
                g2.setColor(getColor(i, new Color[]{chartColor}));
                g2.fillRoundRect(bx, py, barWidth, valHeight, 5, 5);
            } else if (chartType == ChartType.LINE || chartType == ChartType.AREA) {
                if (i == 0) {
                    path.moveTo(px, py);
                } else {
                    path.lineTo(px, py);
                }
                g2.setColor(getColor(i, new Color[]{chartColor}));
                g2.fillOval(px - 4, py - 4, 8, 8);
            }

            // Draw X labels
            if (showLabels && i < lbls.size()) {
                String lbl = lbls.get(i);
                g2.setFont(FONT_PLAIN_11);
                g2.setColor(textColor);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(lbl, px - (fm.stringWidth(lbl) / 2), y + height + 15);
            }
        }

        if (chartType == ChartType.LINE || chartType == ChartType.AREA) {
            if (chartType == ChartType.AREA && firstPx != -1) {
                GeneralPath fillPath = (GeneralPath) path.clone();
                fillPath.lineTo(lastPx, y + height);
                fillPath.lineTo(firstPx, y + height);
                fillPath.closePath();
                
                Color c = getColor(0, new Color[]{chartColor});
                g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 80));
                g2.fill(fillPath);
            }
            g2.setColor(getColor(0, new Color[]{chartColor}));
            g2.draw(path);
        }
    }

    private void drawPieChart(Graphics2D g2, int width, int height, int topMargin, int bottomMargin, List<Double> vals, List<String> lbls) {
        double total = vals.stream().mapToDouble(v -> v).sum();
        if (total == 0) return;

        int size = Math.min(width - 40, height - topMargin - bottomMargin);
        int cx = (width - size) / 2;
        int cy = topMargin + (height - topMargin - bottomMargin - size) / 2;

        double startAngle = 90;
        
        Color[] colors = {
            chartColor, 
            chartColor.brighter(), 
            chartColor.darker(),
            new Color(chartColor.getRed(), chartColor.getGreen(), chartColor.getBlue(), 180),
            new Color(chartColor.getRed(), chartColor.getGreen(), chartColor.getBlue(), 120)
        };

        for (int i = 0; i < vals.size(); i++) {
            double v = vals.get(i);
            double arcAngle = (v / total) * 360;

            g2.setColor(getColor(i, colors));
            if (chartType == ChartType.DONUT) {
                g2.setStroke(new BasicStroke(size / 4f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
                int offset = size / 8;
                Arc2D arc = new Arc2D.Double(cx + offset, cy + offset, size - (offset * 2), size - (offset * 2), startAngle, -arcAngle, Arc2D.OPEN);
                g2.draw(arc);
            } else {
                Arc2D arc = new Arc2D.Double(cx, cy, size, size, startAngle, -arcAngle, Arc2D.PIE);
                g2.fill(arc);
            }

            if (showLabels && i < lbls.size()) {
                double midAngle = Math.toRadians(startAngle - (arcAngle / 2));
                int lx = (int) (cx + size/2 + Math.cos(midAngle) * (size/2 + 15));
                int ly = (int) (cy + size/2 - Math.sin(midAngle) * (size/2 + 15));
                
                String lbl = lbls.get(i);
                g2.setFont(FONT_PLAIN_11);
                g2.setColor(textColor);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(lbl, lx - (fm.stringWidth(lbl)/2), ly + (fm.getAscent()/2));
            }

            startAngle -= arcAngle;
        }
    }

    private List<Double> parseValues(String text) {
        List<Double> list = new ArrayList<>();
        if (text == null || text.trim().isEmpty()) return list;
        String[] parts = text.split(",");
        for (String p : parts) {
            try {
                list.add(Double.parseDouble(p.trim()));
            } catch (NumberFormatException e) {
                list.add(0.0);
            }
        }
        return list;
    }

    private List<String> parseLabels(String text) {
        List<String> list = new ArrayList<>();
        if (text == null || text.trim().isEmpty()) return list;
        String[] parts = text.split(",");
        for (String p : parts) {
            list.add(p.trim());
        }
        return list;
    }

    private List<Color> parseColors(String text) {
        List<Color> list = new ArrayList<>();
        if (text == null || text.trim().isEmpty()) return list;
        String[] parts = text.split(",");
        for (String p : parts) {
            try {
                p = p.trim();
                if (p.startsWith("#")) {
                    list.add(Color.decode(p));
                } else {
                    list.add(null);
                }
            } catch (Exception e) {
                list.add(null);
            }
        }
        return list;
    }

    // === BINDING DINÂMICO ===

    private Color getColor(int index, Color[] palette) {
        if (activeColors != null && activeColors.size() > index && activeColors.get(index) != null) {
            return activeColors.get(index);
        }
        return palette[index % palette.length];
    }

    /**
     * Permite popular o gráfico via código passando uma lista de objetos sem cores personalizadas.
     */
    public <T> void setItems(List<T> items, java.util.function.Function<T, String> labelExtractor, java.util.function.Function<T, Number> valueExtractor) {
        setItems(items, labelExtractor, valueExtractor, null);
    }

    /**
     * Permite popular o gráfico via código passando uma lista de objetos e funções Lambda para extrair os dados.
     * 
     * @param <T> O tipo do objeto da sua lista
     * @param items Lista de objetos (ex: do banco de dados)
     * @param labelExtractor Função para extrair o texto (ex: Produto::getNome)
     * @param valueExtractor Função para extrair o valor (ex: Produto::getValor)
     * @param colorExtractor Função para extrair a cor (opcional, ex: Produto::getCor).
     */
    public <T> void setItems(List<T> items, java.util.function.Function<T, String> labelExtractor, java.util.function.Function<T, Number> valueExtractor, java.util.function.Function<T, Color> colorExtractor) {
        dataValues.clear();
        dataLabels.clear();
        customColors.clear();
        
        if (items != null) {
            for (T item : items) {
                dataLabels.add(labelExtractor != null ? labelExtractor.apply(item) : "");
                dataValues.add(valueExtractor != null ? valueExtractor.apply(item).doubleValue() : 0.0);
                if (colorExtractor != null) {
                    customColors.add(colorExtractor.apply(item));
                }
            }
        }
        
        useDataBinding = true;
        repaint();
    }
    
    /** Limpa o binding via código e volta a usar as propriedades padrão (values/labels). */
    public void clearItems() {
        useDataBinding = false;
        dataValues.clear();
        dataLabels.clear();
        customColors.clear();
        repaint();
    }

    // === PROPERTIES ===

    public ChartType getChartType() { return chartType; }
    public void setChartType(ChartType chartType) {
        ChartType old = this.chartType;
        this.chartType = chartType;
        firePropertyChange("chartType", old, chartType);
        repaint();
    }

    public String getValues() { return values; }
    public void setValues(String values) {
        String old = this.values;
        this.values = values;
        this.useDataBinding = false;
        this.parsedValuesList = parseValues(values);
        firePropertyChange("values", old, values);
        repaint();
    }

    public String getLabels() { return labels; }
    public void setLabels(String labels) {
        String old = this.labels;
        this.labels = labels;
        this.useDataBinding = false;
        this.parsedLabelsList = parseLabels(labels);
        firePropertyChange("labels", old, labels);
        repaint();
    }

    public String getColors() { return colors; }
    public void setColors(String colors) {
        String old = this.colors;
        this.colors = colors;
        this.useDataBinding = false;
        this.activeColorsList = parseColors(colors);
        firePropertyChange("colors", old, colors);
        repaint();
    }

    public Color getChartColor() { return chartColor; }
    public void setChartColor(Color chartColor) {
        Color old = this.chartColor;
        this.chartColor = chartColor;
        firePropertyChange("chartColor", old, chartColor);
        repaint();
    }

    public Color getGridColor() { return gridColor; }
    public void setGridColor(Color gridColor) {
        Color old = this.gridColor;
        this.gridColor = gridColor;
        firePropertyChange("gridColor", old, gridColor);
        repaint();
    }

    public boolean isShowGrid() { return showGrid; }
    public void setShowGrid(boolean showGrid) {
        boolean old = this.showGrid;
        this.showGrid = showGrid;
        firePropertyChange("showGrid", old, showGrid);
        repaint();
    }

    public boolean isShowLabels() { return showLabels; }
    public void setShowLabels(boolean showLabels) {
        boolean old = this.showLabels;
        this.showLabels = showLabels;
        firePropertyChange("showLabels", old, showLabels);
        repaint();
    }

    public String getTitle() { return title; }
    public void setTitle(String title) {
        String old = this.title;
        this.title = title;
        firePropertyChange("title", old, title);
        repaint();
    }

    public TitlePosition getTitlePosition() { return titlePosition; }
    public void setTitlePosition(TitlePosition titlePosition) {
        TitlePosition old = this.titlePosition;
        this.titlePosition = titlePosition;
        firePropertyChange("titlePosition", old, titlePosition);
        repaint();
    }

    public Color getTitleColor() { return titleColor; }
    public void setTitleColor(Color titleColor) {
        Color old = this.titleColor;
        this.titleColor = titleColor;
        firePropertyChange("titleColor", old, titleColor);
        repaint();
    }
}
