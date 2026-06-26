package com.cardSwing;

import java.awt.Color;
import java.awt.Window;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Gerenciador de Temas Global do CardSwing.
 * <p>Aplica cores dinâmicas a todos os componentes Swing e customizados na tela,
 * transformando instantaneamente o visual do software.</p>
 */
public class CardThemeManager {

    public enum CardTheme {
        LIGHT(
            new Color(248, 250, 252), // Background
            new Color(255, 255, 255), // Surface (Cards)
            new Color(15, 23, 42),    // Text Primary
            new Color(71, 85, 105),   // Text Secondary
            new Color(226, 232, 240), // Border
            new Color(59, 130, 246),  // Primary (Blue)
            false                     // isDark
        ),
        DARK(
            new Color(15, 23, 42),    // Background (Slate 900)
            new Color(30, 41, 59),    // Surface (Slate 800)
            new Color(248, 250, 252), // Text Primary
            new Color(148, 163, 184), // Text Secondary
            new Color(51, 65, 85),    // Border
            new Color(59, 130, 246),  // Primary
            true                      // isDark
        ),
        MIDNIGHT(
            new Color(5, 11, 20),     // Background Deep Blue
            new Color(13, 27, 42),    // Surface
            new Color(224, 231, 255), // Text Primary
            new Color(165, 180, 252), // Text Secondary
            new Color(30, 41, 59),    // Border
            new Color(99, 102, 241),  // Primary (Indigo)
            true                      // isDark
        ),
        FOREST(
            new Color(6, 24, 18),     // Background Dark Green
            new Color(12, 40, 29),    // Surface
            new Color(209, 250, 229), // Text Primary
            new Color(110, 231, 183), // Text Secondary
            new Color(24, 70, 50),    // Border
            new Color(16, 185, 129),  // Primary (Emerald)
            true                      // isDark
        ),
        SUNSET(
            new Color(35, 15, 25),    // Background Dark Red/Purple
            new Color(56, 20, 35),    // Surface
            new Color(255, 237, 213), // Text Primary
            new Color(253, 186, 116), // Text Secondary
            new Color(88, 30, 50),    // Border
            new Color(249, 115, 22),  // Primary (Orange)
            true                      // isDark
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

        // --- INJEÇÃO NO UIMANAGER GLOBAL ---
        // Paneis e Containers
        UIManager.put("Panel.background", theme.background);
        UIManager.put("Viewport.background", theme.background);
        UIManager.put("ScrollPane.background", theme.background);
        UIManager.put("ScrollPane.border", null); // Remove bordas feias nativas
        
        // Textos e Labels
        UIManager.put("Label.foreground", theme.textPrimary);
        UIManager.put("CheckBox.foreground", theme.textPrimary);
        UIManager.put("CheckBox.background", theme.background);
        UIManager.put("RadioButton.foreground", theme.textPrimary);
        UIManager.put("RadioButton.background", theme.background);

        // Inputs Nativos (Para caso o usuário não use o CardTextField em algum momento)
        UIManager.put("TextField.background", theme.surface);
        UIManager.put("TextField.foreground", theme.textPrimary);
        UIManager.put("TextField.caretForeground", theme.textPrimary);

        UIManager.put("PasswordField.background", theme.surface);
        UIManager.put("PasswordField.foreground", theme.textPrimary);
        UIManager.put("PasswordField.caretForeground", theme.textPrimary);

        UIManager.put("TextArea.background", theme.surface);
        UIManager.put("TextArea.foreground", theme.textPrimary);
        UIManager.put("TextArea.caretForeground", theme.textPrimary);

        // Combos Nativos
        UIManager.put("ComboBox.background", theme.surface);
        UIManager.put("ComboBox.foreground", theme.textPrimary);
        UIManager.put("ComboBox.selectionBackground", theme.primary);
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);

        // Botões Nativos
        UIManager.put("Button.background", theme.surface);
        UIManager.put("Button.foreground", theme.textPrimary);

        // Tabelas Nativas (Para combinar 100% com o tema)
        UIManager.put("Table.background", theme.surface);
        UIManager.put("Table.foreground", theme.textSecondary);
        UIManager.put("Table.gridColor", theme.border);
        UIManager.put("Table.selectionBackground", theme.primary);
        UIManager.put("Table.selectionForeground", Color.WHITE);
        UIManager.put("TableHeader.background", theme.background);
        UIManager.put("TableHeader.foreground", theme.textPrimary);

        // Listas
        UIManager.put("List.background", theme.surface);
        UIManager.put("List.foreground", theme.textPrimary);
        UIManager.put("List.selectionBackground", theme.primary);
        UIManager.put("List.selectionForeground", Color.WHITE);

        // --- ATUALIZAÇÃO EM TEMPO REAL ---
        // Percorre todas as janelas (JFrames/JDialogs) abertas e aplica a nova árvore de UI imediatamente
        for (Window window : Window.getWindows()) {
            SwingUtilities.updateComponentTreeUI(window);
            // Repaint agressivo para garantir que cantos e fundos customizados atualizem
            window.repaint(); 
        }
    }
}
