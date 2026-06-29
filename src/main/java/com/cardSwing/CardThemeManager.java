package com.cardSwing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * Gerenciador de Temas Global do CardSwing.
 * <p>
 * Aplica cores dinâmicas a todos os componentes Swing e customizados na tela,
 * transformando instantaneamente o visual do software.
 * </p>
 */
public class CardThemeManager {

        public enum CardTheme {
                LIGHT(
                                new Color(248, 250, 252), // Background
                                new Color(255, 255, 255), // Surface (Cards)
                                new Color(15, 23, 42), // Text Primary
                                new Color(71, 85, 105), // Text Secondary
                                new Color(226, 232, 240), // Border
                                new Color(59, 130, 246), // Primary (Blue)
                                false// isDark
                ),
                DARK(
                                new Color(15, 23, 42), // Background (Slate 900)
                                new Color(30, 41, 59), // Surface (Slate 800)
                                new Color(248, 250, 252), // Text Primary
                                new Color(148, 163, 184), // Text Secondary
                                new Color(51, 65, 85), // Border
                                new Color(59, 130, 246), // Primary
                                true// isDark
                ),
                MIDNIGHT(
                                new Color(5, 11, 20), // Background Deep Blue
                                new Color(13, 27, 42), // Surface
                                new Color(224, 231, 255), // Text Primary
                                new Color(165, 180, 252), // Text Secondary
                                new Color(30, 41, 59), // Border
                                new Color(99, 102, 241), // Primary (Indigo)
                                true// isDark
                ),
                FOREST(
                                new Color(6, 24, 18), // Background Dark Green
                                new Color(12, 40, 29), // Surface
                                new Color(209, 250, 229), // Text Primary
                                new Color(110, 231, 183), // Text Secondary
                                new Color(24, 70, 50), // Border
                                new Color(16, 185, 129), // Primary (Emerald)
                                true// isDark
                ),
                SUNSET(
                                new Color(35, 15, 25), // Background Dark Red/Purple
                                new Color(56, 20, 35), // Surface
                                new Color(255, 237, 213), // Text Primary
                                new Color(253, 186, 116), // Text Secondary
                                new Color(88, 30, 50), // Border
                                new Color(249, 115, 22), // Primary (Orange)
                                true// isDark
                );

                public final Color background;
                public final Color surface;
                public final Color textPrimary;
                public final Color textSecondary;
                public final Color border;
                public final Color primary;
                public final boolean isDark;

                CardTheme(Color bg, Color sfc, Color txtP, Color txtS, Color bdr, Color prim, boolean isDark) {
                        this.background = bg;
                        this.surface = sfc;
                        this.textPrimary = txtP;
                        this.textSecondary = txtS;
                        this.border = bdr;
                        this.primary = prim;
                        this.isDark = isDark;
                }
        }

        private static CardTheme currentTheme = CardTheme.LIGHT;

        public static CardTheme getCurrentTheme() {
                return currentTheme;
        }

        /**
         * Aplica um tema visual a todos os componentes Swing instanciados e define
         * as propriedades Globais no UIManager para os novos que forem criados.
         */
        public static void applyTheme(CardTheme theme) {
                currentTheme = theme;

                try {
                        // Usa as variáveis nativas do FlatLaf para que ele gere automaticamente
                        // cores de hover, bordas, focos e backgrounds dinamicamente (muito mais
                        // moderno).
                        java.util.Map<String, String> extras = new java.util.HashMap<>();
                        extras.put("@accentColor", String.format("#%06x", theme.primary.getRGB() & 0xFFFFFF));
                        extras.put("@background", String.format("#%06x", theme.surface.getRGB() & 0xFFFFFF));
                        extras.put("@foreground", String.format("#%06x", theme.textPrimary.getRGB() & 0xFFFFFF));
                        extras.put("@textInactiveText",
                                        String.format("#%06x", theme.textSecondary.getRGB() & 0xFFFFFF));

                        // Deixa as tabelas do sistema inteiras automaticamente modernas (sem precisar
                        // do CardTable)
                        extras.put("Table.showHorizontalLines", "true");
                        extras.put("Table.showVerticalLines", "false");
                        extras.put("Table.intercellSpacing", "0,0");
                        extras.put("Table.rowHeight", "35");

                        com.formdev.flatlaf.FlatLaf.setGlobalExtraDefaults(extras);

                        if (theme.isDark) {
                                javax.swing.UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
                        } else {
                                javax.swing.UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
                        }

                        // Configura apenas o Fundo da Janela/Containers para contrastar com os
                        // Componentes (Efeito Card)
                        UIManager.put("Panel.background", theme.background);
                        UIManager.put("RootPane.background", theme.background);
                        UIManager.put("Viewport.background", theme.background);
                        UIManager.put("ScrollPane.background", theme.background);
                        UIManager.put("OptionPane.background", theme.background);

                        com.formdev.flatlaf.FlatLaf.updateUI();
                } catch (Exception ex) {
                        ex.printStackTrace();
                }

                // --- ATUALIZAÇÃO EM TEMPO REAL ---
                for (Window window : Window.getWindows()) {
                        SwingUtilities.updateComponentTreeUI(window);
                        window.repaint();
                }
        }
}
