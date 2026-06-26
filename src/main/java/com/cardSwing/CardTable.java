package com.cardSwing;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Uma Tabela (embutida em um ScrollPane) com estilo web moderno.
 * Destaque para o método mágico bindList() que usa Reflection.
 */
public class CardTable extends JScrollPane implements Serializable {

    private static final long serialVersionUID = 1L;

    private JTable table;
    private Color headerColor = new Color(248, 250, 252); // Slate 50
    private Color headerTextColor = new Color(100, 116, 139); // Slate 500
    private Color rowHoverColor = new Color(241, 245, 249); // Slate 100
    private Color tableGridColor = new Color(226, 232, 240); // Slate 200
    private Color selectionColor = new Color(224, 242, 254); // Light blue
    
    private int hoveredRow = -1;

    public CardTable() {
        table = new JTable();
        setViewportView(table);
        setup();
    }

    private void setup() {
        // Estilo do ScrollPane
        setBorder(BorderFactory.createLineBorder(tableGridColor));
        getViewport().setBackground(Color.WHITE);
        
        // Estilo da Tabela
        table.setFillsViewportHeight(true);
        table.setBackground(Color.WHITE);
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(tableGridColor);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setForeground(new Color(51, 65, 85));
        
        // Default Model para aparecer bonito no editor
        table.setModel(new DefaultTableModel(
            new Object[][] {
                {"João Silva", "joao@email.com", "Ativo"},
                {"Maria Souza", "maria@email.com", "Pendente"},
                {"Carlos Dias", "carlos@email.com", "Inativo"}
            },
            new String[] {"Nome", "Email", "Status"}
        ));
        
        // Efeito Hover
        table.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row != hoveredRow) {
                    hoveredRow = row;
                    table.repaint();
                }
            }
        });
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                hoveredRow = -1;
                table.repaint();
            }
        });

        // Header Customizado
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBackground(headerColor);
                label.setForeground(headerTextColor);
                label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                label.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, tableGridColor),
                    new EmptyBorder(10, 15, 10, 15)
                ));
                return label;
            }
        });
        
        // Células Customizadas
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBorder(new EmptyBorder(0, 15, 0, 15)); // Padding horizontal
                
                if (isSelected) {
                    label.setBackground(selectionColor);
                } else if (row == hoveredRow) {
                    label.setBackground(rowHoverColor);
                } else {
                    label.setBackground(Color.WHITE);
                }
                return label;
            }
        });
    }

    /**
     * MÁGICA: Preenche a tabela automaticamente a partir de uma Lista de Objetos usando Reflection.
     * Sem precisar fazer laços manuais ou criar DefaultTableModel na mão!
     * 
     * @param items A lista de objetos (Ex: List<Produto>)
     * @param clazz A classe dos objetos (Ex: Produto.class)
     */
    public void bindList(List<?> items, Class<?> clazz) {
        if (items == null) return;
        
        Field[] fields = clazz.getDeclaredFields();
        String[] columnNames = new String[fields.length];
        
        // Monta cabeçalho pegando o nome das variáveis
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            String name = fields[i].getName();
            // Transforma "precoCusto" em "PrecoCusto" para ficar bonito no cabeçalho
            columnNames[i] = name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        
        // Monta as linhas extraindo os valores das variáveis
        Object[][] data = new Object[items.size()][fields.length];
        for (int i = 0; i < items.size(); i++) {
            Object obj = items.get(i);
            for (int j = 0; j < fields.length; j++) {
                try {
                    data[i][j] = fields[j].get(obj);
                } catch (Exception e) {
                    data[i][j] = "Erro";
                }
            }
        }
        
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Desativa edição direto na célula
            }
        };
        table.setModel(model);
    }
    
    // Acesso à tabela interna caso precise de configurações avançadas (ex: larguras de colunas)
    public JTable getTable() {
        return table;
    }
    
    // Propriedades visuais expostas
    public Color getHeaderColor() { return headerColor; }
    public void setHeaderColor(Color headerColor) { this.headerColor = headerColor; repaint(); }
    
    public Color getHeaderTextColor() { return headerTextColor; }
    public void setHeaderTextColor(Color headerTextColor) { this.headerTextColor = headerTextColor; repaint(); }
    
    public Color getRowHoverColor() { return rowHoverColor; }
    public void setRowHoverColor(Color rowHoverColor) {
        this.rowHoverColor = rowHoverColor;
        repaint();
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        if (table != null) table.setBackground(bg);
        if (getViewport() != null) getViewport().setBackground(bg);
    }
}
