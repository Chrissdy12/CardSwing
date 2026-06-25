package com.cardSwing;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Modelo de dados abstrato para alimentar um {@link CardListPanel}.
 *
 * <p>Você implementa APENAS o método {@link #configureCard(Object, CardPanel)},
 * usando os componentes da biblioteca (CardTitle, CardButton, CardCheck, etc.).</p>
 *
 * <h3>Exemplo completo:</h3>
 * <pre>
 *   // 1. Crie o Model:
 *   public class ProdutoModel extends CardListModel&lt;Produto&gt; {
 *       {@literal @}Override
 *       protected void configureCard(Produto p, CardPanel card) {
 *           card.setThemeColor(Color.BLUE);
 *           card.add(new CardTitle(p.getNome()));
 *           card.add(new CardSubtitle(p.getCategoria()));
 *           card.add(new CardSeparator());
 *           card.add(new CardCheck("Ativo", p.isAtivo()));
 *           card.add(new CardStatusRow()
 *               .addStatus("Estoque", String.valueOf(p.getEstoque()), Color.GREEN));
 *           card.add(new CardSeparator());
 *           card.add(new CardButtonRow()
 *               .addButton("Editar", Color.BLUE, () -&gt; fireButtonClick(p)));
 *       }
 *   }
 *
 *   // 2. Use na tela:
 *   CardListPanel listPanel = new CardListPanel();  // arraste da palette
 *   ProdutoModel model = new ProdutoModel();
 *   model.bindTo(listPanel);
 *   model.setItems(listaDeProdutos);
 *   model.setOnCardClick(p -&gt; abrirDetalhe(p));
 *   model.setOnButtonClick(p -&gt; editarProduto(p));
 * </pre>
 *
 * @param <T> tipo do item de dados
 */
public abstract class CardListModel<T> {

    private List<T> items = new ArrayList<>();
    private CardListPanel boundPanel;
    private Consumer<T> onCardClick;
    private Consumer<T> onButtonClick;

    // =========================================================================
    // MÉTODO PRINCIPAL — Só implemente isto!
    // =========================================================================

    /**
     * Configure o card com componentes visuais baseados nos dados do item.
     * Use os componentes: CardTitle, CardSubtitle, CardText, CardSeparator,
     * CardCheck, CardButton, CardButtonRow, CardStatusBadge, CardStatusRow,
     * CardImage, CardProgress, CardTag.
     *
     * @param item o item de dados
     * @param card o CardPanel vazio pronto para adicionar componentes
     */
    protected abstract void configureCard(T item, CardPanel card);

    // =========================================================================
    // BINDING
    // =========================================================================

    /** Conecta este Model a um CardListPanel. */
    public void bindTo(CardListPanel panel) {
        this.boundPanel = panel;
        refresh();
    }

    /** Retorna o painel conectado. */
    public CardListPanel getBoundPanel() { return boundPanel; }

    // =========================================================================
    // OPERAÇÕES DE DADOS
    // =========================================================================

    /** Substitui toda a lista e atualiza a tela. */
    public void setItems(List<T> items) {
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        refresh();
    }

    /** Adiciona um item e cria o card. */
    public void addItem(T item) {
        if (item != null) {
            this.items.add(item);
            if (boundPanel != null) {
                boundPanel.addCard(buildCard(item));
            }
        }
    }

    /** Remove um item e atualiza. */
    public void removeItem(T item) {
        if (items.remove(item)) refresh();
    }

    /** Remove por índice. */
    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            refresh();
        }
    }

    /** Limpa tudo. */
    public void clear() {
        items.clear();
        if (boundPanel != null) boundPanel.clearCards();
    }

    /** Retorna cópia da lista. */
    public List<T> getItems() { return new ArrayList<>(items); }

    /** Retorna item por índice. */
    public T getItem(int index) {
        return (index >= 0 && index < items.size()) ? items.get(index) : null;
    }

    /** Quantidade de itens. */
    public int getItemCount() { return items.size(); }

    /** Força recriação de todos os cards. */
    public void refresh() {
        if (boundPanel == null) return;
        boundPanel.clearCards();
        for (T item : items) {
            boundPanel.addCard(buildCard(item));
        }
    }

    // =========================================================================
    // CALLBACKS
    // =========================================================================

    /** Ação quando card inteiro é clicado. */
    public void setOnCardClick(Consumer<T> action) { this.onCardClick = action; }

    /** Ação quando botão dentro do card é clicado. */
    public void setOnButtonClick(Consumer<T> action) { this.onButtonClick = action; }

    /** Dispara callback de clique no card (chame dentro de configureCard se necessário). */
    protected void fireCardClick(T item) {
        if (onCardClick != null) onCardClick.accept(item);
    }

    /** Dispara callback de botão (use nos CardButtons dentro de configureCard). */
    protected void fireButtonClick(T item) {
        if (onButtonClick != null) onButtonClick.accept(item);
    }

    // =========================================================================
    // INTERNOS
    // =========================================================================

    private CardPanel buildCard(T item) {
        CardPanel card = new CardPanel();
        configureCard(item, card);

        // Click no card inteiro
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                fireCardClick(item);
            }
        });

        return card;
    }
}
