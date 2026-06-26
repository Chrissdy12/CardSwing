# CardSwing Framework 🚀

**CardSwing** é um framework de interface gráfica premium (UI/UX) construído totalmente do zero para **Java Swing**. Ele traz a estética moderna do Material Design e de aplicações Web contemporâneas para o ambiente desktop clássico, suportando temas globais, paleta do NetBeans, cantos arredondados, efeitos hover, sombras dinâmicas e transições suaves.

---

## 📑 Sumário

1. [Gerenciamento Global de Tema](#-1-gerenciamento-global-de-tema-cardthememanager)
2. [Gráficos Modernos](#-2-gráficos-modernos-cardgrafic)
3. [Tabelas e Listagens](#-3-tabelas-e-listagens)
4. [Threads, Notificações e PopUps](#-4-threads-notificações-e-popups)
5. [Containers e Tipografia](#-5-containers-e-tipografia)
6. [Campos de Entrada (Inputs)](#-6-campos-de-entrada-inputs)
7. [Datas e Calendários](#-7-datas-e-calendários)
8. [Tags, Filtros e Checkboxes](#-8-tags-filtros-e-checkboxes)
9. [Mídia, Abas e Efeitos](#-9-mídia-abas-e-efeitos)

---

## 🎨 1. Gerenciamento Global de Tema (`CardThemeManager`)
O recurso mais poderoso do CardSwing. Altera **toda a sua aplicação** (incluindo componentes Swing nativos como `JButton`, `JTable`, e `JScrollPane`) para uma nova paleta de cores moderna.

```java
// Tipos disponíveis: LIGHT, DARK, MIDNIGHT, FOREST, SUNSET
CardThemeManager.applyTheme(CardThemeManager.CardTheme.DARK);

// DICA: Chame isso logo no construtor do seu JFrame principal, 
// antes do initComponents() ou logo após ele.
```

---

## 📊 2. Gráficos Modernos (`CardGrafic`)
O `CardGrafic` é altamente flexível e desenha gráficos vetoriais nativos.

### Exemplo A: Gráfico de Barras / Linhas / Área (Cartesiano)
```java
CardGrafic graficoCartesiano = new CardGrafic();
graficoCartesiano.setChartColor(new Color(59, 130, 246)); // Azul
graficoCartesiano.setShowGrid(true);
graficoCartesiano.setShowLabels(true);

// Preenchendo os dados (Ex: Faturamento mensal)
graficoCartesiano.setValues(Arrays.asList(1500.0, 2300.0, 1800.0, 3200.0));
graficoCartesiano.setLabels(Arrays.asList("Jan", "Fev", "Mar", "Abr"));

// Modelos Modernos disponíveis para Cartesiano:
// BAR, BAR_MODERN, BAR_GRADIENT, BAR_LOLLIPOP
// HORIZONTAL_BAR, HORIZONTAL_BAR_MODERN, HORIZONTAL_BAR_GRADIENT, HORIZONTAL_BAR_LOLLIPOP
// LINE, LINE_SMOOTH (Curva Suave), AREA, AREA_SMOOTH, AREA_SMOOTH_GRADIENT (Fundo com Fade out)
graficoCartesiano.setChartType(CardGrafic.ChartType.AREA_SMOOTH_GRADIENT);
```

### Exemplo B: Gráfico de Rosca / Torta / Velocímetro
```java
CardGrafic graficoPizza = new CardGrafic();
graficoPizza.setChartColor(new Color(249, 115, 22)); // Laranja

// Preenchendo os dados proporcionais
graficoPizza.setValues(Arrays.asList(40.0, 30.0, 20.0, 10.0));
graficoPizza.setLabels(Arrays.asList("Aprovados", "Pendentes", "Recusados", "Outros"));

// Modelos disponíveis: PIE, DONUT, DONUT_ROUNDED, GAUGE
graficoPizza.setChartType(CardGrafic.ChartType.DONUT_ROUNDED);
```

---

## 📋 3. Tabelas e Listagens

### `CardTable` (Tabela Moderna Flat)
Remove as bordas nativas do Java, aumenta o espaçamento das linhas e adiciona efeito *Hover*.

```java
CardTable minhaTabela = new CardTable();
minhaTabela.setRowHeight(40);
minhaTabela.setRowHoverColor(new Color(241, 245, 249)); // Efeito Hover

// Como preencher a Tabela:
DefaultTableModel model = new DefaultTableModel(
    new Object[]{"ID", "Nome", "Status"}, 0 // Colunas
);
model.addRow(new Object[]{1, "João Silva", "Ativo"});
model.addRow(new Object[]{2, "Maria Costa", "Pendente"});
minhaTabela.setModel(model);
```

### `CardListPanel` e `CardListModel` (Listas de Cards)
Substituto do `JList`. Cria uma lista de Cards vertical ou horizontal (Grid) scrollável usando uma arquitetura baseada em `Model` e `Binding`.

**1. Crie o seu Modelo de Dados (`CardListModel`)**
```java
public class ProdutoModel extends CardListModel<Produto> {
    @Override
    protected void configureCard(Produto p, CardPanel card) {
        card.setThemeColor(new Color(59, 130, 246)); // Borda azul lateral
        
        card.add(new CardTitle(p.getNome()));
        card.add(new CardSubtitle(p.getCategoria()));
        card.add(new CardSeparator());
        
        // Utilitários de status
        card.add(new CardStatusRow()
            .addStatus("Estoque", String.valueOf(p.getEstoque()), new Color(16, 185, 129))); // Verde
            
        card.add(new CardSeparator());
        
        // Botões dentro do card
        card.add(new CardButtonRow()
            .addButton("Editar", new Color(59, 130, 246), () -> fireButtonClick(p)));
    }
}
```

**2. Use o painel na sua tela:**
```java
CardListPanel listaProdutos = new CardListPanel();
listaProdutos.setColumns(2); // 2 colunas = Grid de produtos!

ProdutoModel model = new ProdutoModel();
model.bindTo(listaProdutos); // Conecta a tela com os dados
model.setItems(listaVindaDoBanco);

// Ouvindo cliques:
model.setOnCardClick(produto -> abrirDetalhe(produto));
model.setOnButtonClick(produto -> editarProduto(produto));
```

---

## ⚡ 4. Threads, Notificações e PopUps

### `CardLoading` (Travamento Seguro de Tela)
Mostra um spinner de carregamento gerenciando as *Threads* para que a animação não trave.
```java
CardLoading.execute(this, "Validando dados...", 
    () -> {
        // TAREFA PESADA [Fundo]: Consultas DB, Threads longas...
        Thread.sleep(3000); 
    },
    () -> {
        // FINALIZAÇÃO [UI Thread]: Executado ao terminar (Ex: recarregar tabela)
        CardToast.showSuccess(this, "Pronto!");
    }
);
```

### `CardToast`
Notificações flutuantes temporárias.
```java
CardToast.showSuccess(this, "Operação concluída!");
CardToast.showError(this, "Erro de conexão!");
CardToast.showWarning(this, "Preencha todos os campos.");
CardToast.showInfo(this, "Atualização em andamento...");
```

### `CardDialog` (Modais Customizados)
Substituto moderno para o `JOptionPane`. Exibe caixas de diálogo super polidas e fluidas.
```java
// 1. Confirmação (Sim/Não)
boolean confirmou = CardDialog.showConfirm(this, "Deletar", "Tem certeza que deseja apagar o registro?");

// 2. Input Simples
String nome = CardDialog.showInput(this, "Nome do Projeto", "Digite o novo nome:");

// 3. Input com ComboBox (Opções)
String[] opcoes = {"Administrador", "Vendedor", "Caixa"};
String nivel = CardDialog.showComboInput(this, "Cargo", "Escolha a permissão:", opcoes);
```

---

## 🧩 5. Containers e Tipografia

### `CardPanel`
Container arredondado com suporte a sombras profundas e interatividade (Hover effect).
```java
CardPanel painel = new CardPanel();
painel.setRadius(20);          // Cantos arredondados
painel.setShadowEnabled(true); // Sombra 3D
painel.setHoverEnabled(true);  // Interatividade ao passar o mouse
painel.setTitle("Dados do Cliente"); // Possui título nativo embutido!
```

### `CardTitle`, `CardSubtitle`, e `CardText`
Labels especializados de texto com pesos de fonte corporativos pré-definidos (H1, H2, Paragraph).
```java
CardTitle titulo = new CardTitle("Painel Principal");
CardSubtitle subtitulo = new CardSubtitle("Gerencie suas estatísticas aqui.");
CardText descricao = new CardText("Os números mostrados referem-se aos últimos 30 dias de processamento.");
```

### `CardSeparator`
Um divisor horizontal/vertical elegante.
```java
CardSeparator linha = new CardSeparator();
// Estilos: SOLID, DASHED, DOTTED, GRADIENT, SHADOW
linha.setStyle(CardSeparator.SeparatorStyle.GRADIENT);
```

---

## 📝 6. Campos de Entrada (Inputs)

### `CardTextField` e `CardPasswordField`
Campos de texto com *Focus Ring* colorido e suporte a placeholder.
```java
CardTextField txtNome = new CardTextField();
txtNome.setPlaceholder("Digite seu nome completo...");
txtNome.setThemeColor(new Color(59, 130, 246)); // Cor da barra de foco (Azul)

CardPasswordField txtSenha = new CardPasswordField();
txtSenha.setPlaceholder("Sua senha secreta...");
```

### `CardComboBox`
Um ComboBox (Select dropdown) moderno.
```java
CardComboBox combo = new CardComboBox();
combo.addItem("Opção 1");
combo.addItem("Opção 2");
```

### `CardButton` e `CardButtonRow`
```java
CardButton btn = new CardButton();
btn.setText("Confirmar");
btn.setButtonRadius(20); // Botão estilo Pílula

// CardButtonRow é útil para alinhar vários botões horizontalmente na base da tela
CardButtonRow grupoBotoes = new CardButtonRow();
grupoBotoes.add(new CardButton("Cancelar"));
grupoBotoes.add(btn);
```

---

## 📅 7. Datas e Calendários

### `CardCalendar`
O calendário interativo visual (Perfeito para Dashboards).
```java
CardCalendar calendario = new CardCalendar();

// Pegar a data clicada pelo usuário:
LocalDate dataEscolhida = calendario.getLocalDate();

// Navegar o calendário programaticamente:
calendario.setLocalDate(LocalDate.now().plusMonths(1));
```

### `CardDatePicker` e `CardDateTimePicker`
Inputs de texto com formatação que abrem o calendário como um PopUp flutuante ao clicar no ícone direito.
```java
CardDatePicker datePicker = new CardDatePicker();
LocalDate nascimento = datePicker.getSelectedDate(); 

CardDateTimePicker dateTimePicker = new CardDateTimePicker();
LocalDateTime agendamento = dateTimePicker.getSelectedDateTime();
```

---

## 🏷️ 8. Tags, Filtros e Checkboxes

### `CardSwitch` e `CardCheck`
O `CardSwitch` é um toggle responsivo (estilo iOS/Android).
```java
CardSwitch toggleAndroid = new CardSwitch();
toggleAndroid.setTrackOnColor(new Color(59, 130, 246)); // Azul ativo
boolean ligado = toggleAndroid.isSelected();

CardCheck checkboxModerno = new CardCheck("Aceitar os Termos", false);
checkboxModerno.setCheckColor(new Color(16, 185, 129)); // Verde
```

### `CardChip` (Filtros Removíveis)
Possui um botão **X** no lado direito, perfeito para campo de Filtros. Ele se auto-remove da tela ao ser clicado.
```java
CardChip chipFiltro = new CardChip("Estoque: Disponível");
chipFiltro.addActionListener(e -> {
    System.out.println("O usuário removeu esse filtro, recarregue a tabela!");
});
```

### `CardBadge` (Notificação)
Pequeno círculo de notificação. Pode ser colado ao lado de botões ou imagens de perfil.
```java
CardBadge badge = new CardBadge();
badge.setText("99+");
badge.setBadgeColor(new Color(239, 68, 68)); // Vermelho alerta
```

### `CardTag`, `CardStatusBadge` e `CardStatusRow`
- `CardTag`: Rótulo de status fixo (Ex: verde para "Concluído").
- `CardStatusBadge`: Uma Tag moderna com efeito de luz sutil de status.
- `CardStatusRow`: Painel auto-organizado para dispor várias Tags lado a lado.
```java
CardTag tag = new CardTag("Aprovado");
tag.setTagColor(new Color(209, 250, 229));

CardStatusRow statusRow = new CardStatusRow();
statusRow.addStatus("Lucro", "+ 15%", new Color(16, 185, 129));
```

---

## 🖼️ 9. Mídia, Abas e Efeitos

### `CardAvatar` e `CardImage`
- `CardAvatar`: Para fotos de perfil com recorte circular impecável (*Crop*).
- `CardImage`: Container genérico para imagens preservando o Aspect-Ratio com bordas arredondadas.
```java
CardAvatar fotoPerfil = new CardAvatar();
fotoPerfil.setImagePath("C:\\fotos\\cliente1.jpg");
fotoPerfil.setInitials("CH"); // Exibido caso a imagem quebre
fotoPerfil.setShowOnlineDot(true); // Exibe bolinha verde de online

CardImage banner = new CardImage();
banner.setImagePath("C:\\fotos\\banner.png");
banner.setImageRadius(20);
```

### `CardTabs`
Abas de navegação web (Fundo transparente com traço animado no item ativo).
```java
CardTabs abas = new CardTabs();
abas.addTab("Dashboard", painelDash);
abas.addTab("Relatórios", painelRelatorios);
```

### `CardProgress`
```java
CardProgress progresso = new CardProgress();
progresso.setProgress(50); // 0 a 100%
// Estilos: SOLID, STRIPED, GRADIENT, CIRCULAR, DASHED
progresso.setModel(CardProgress.ProgressModel.CIRCULAR);
```

### `CardSkeleton` (Shimmer Effect de Carregamento)
O *Skeleton* cria blocos piscantes que simulam uma tela em carregamento (como visto no YouTube ou LinkedIn).
```java
CardSkeleton loadingBlock = new CardSkeleton();
loadingBlock.playAnimation(); // Começa a piscar
// ... Após receber os dados ...
loadingBlock.stopAnimation();
loadingBlock.setVisible(false);
```

---

*Arquitetado inteiramente sob as bases puras do Swing para desempenho máximo e design sublime.*
