# CardSwing Framework 🚀

**CardSwing** é um framework de interface gráfica premium (UI/UX) construído totalmente do zero para **Java Swing**. Ele traz a estética moderna do Material Design e de aplicações Web contemporâneas para o ambiente desktop clássico, suportando temas globais, paleta do NetBeans, cantos arredondados, efeitos hover, sombras dinâmicas e transições suaves.

---

## 📑 Sumário de Componentes

**Gerenciamento e Estrutura**
* [1. CardThemeManager](#1-cardthememanager)

**Gráficos**
* [2. CardGrafic](#2-cardgrafic)

**Tabelas e Listagens**
* [3. CardTable](#3-cardtable)
* [4. CardListPanel](#4-cardlistpanel)

**Threads, Notificações e PopUps**
* [5. CardLoading](#5-cardloading)
* [6. CardToast](#6-cardtoast)
* [7. CardDialog](#7-carddialog)

**Containers e Tipografia**
* [8. CardPanel](#8-cardpanel)
* [9. CardTitle](#9-cardtitle)
* [10. CardSubtitle](#10-cardsubtitle)
* [11. CardText](#11-cardtext)
* [12. CardSeparator](#12-cardseparator)

**Campos de Entrada (Inputs)**
* [13. CardTextField](#13-cardtextfield)
* [14. CardPasswordField](#14-cardpasswordfield)
* [15. CardComboBox](#15-cardcombobox)
* [16. CardButton](#16-cardbutton)
* [17. CardButtonRow](#17-cardbuttonrow)

**Datas e Calendários**
* [18. CardCalendar](#18-cardcalendar)
* [19. CardDatePicker](#19-carddatepicker)
* [20. CardDateTimePicker](#20-carddatetimepicker)

**Tags, Filtros e Checkboxes**
* [21. CardSwitch](#21-cardswitch)
* [22. CardCheck](#22-cardcheck)
* [23. CardChip](#23-cardchip)
* [24. CardBadge](#24-cardbadge)
* [25. CardTag](#25-cardtag)
* [26. CardStatusBadge](#26-cardstatusbadge)
* [27. CardStatusRow](#27-cardstatusrow)

**Mídia, Abas e Efeitos**
* [28. CardAvatar](#28-cardavatar)
* [29. CardImage](#29-cardimage)
* [30. CardTabs](#30-cardtabs)
* [31. CardProgress](#31-cardprogress)
* [32. CardSkeleton](#32-cardskeleton)

---

## 1. CardThemeManager
O motor principal do framework. Altera todo o LookAndFeel (incluindo JPanels, JButtons nativos, etc).
- `applyTheme(CardTheme)`
- `getCurrentTheme()`

```java
// Escolha entre 5 Temas Premium embutidos:
CardThemeManager.applyTheme(CardThemeManager.CardTheme.LIGHT);
CardThemeManager.applyTheme(CardThemeManager.CardTheme.DARK);
CardThemeManager.applyTheme(CardThemeManager.CardTheme.MIDNIGHT);
CardThemeManager.applyTheme(CardThemeManager.CardTheme.FOREST);
CardThemeManager.applyTheme(CardThemeManager.CardTheme.SUNSET);
```

## 2. CardGrafic
Gráfico vetorial de altíssimo desempenho sem dependências externas.
- `setChartType(ChartType)`: PIE, DONUT, GAUGE, BAR, HORIZONTAL_BAR, LINE, AREA, etc (com variações `_MODERN`, `_GRADIENT`, `_LOLLIPOP`).
- `setChartColor(Color)`, `setShowGrid(boolean)`

**O CardGrafic possui 3 formas incríveis de receber dados:**

### Forma 1: Via String (Ideal para arrastar no NetBeans)
```java
CardGrafic grafico = new CardGrafic();
grafico.setChartType(CardGrafic.ChartType.AREA_SMOOTH_GRADIENT);
grafico.setValues("150.0, 230.0, 180.0"); // Usa String separada por vírgula
grafico.setLabels("Jan, Fev, Mar");
```

### Forma 2: Data Binding com Objetos (Extraindo Valores)
Passe uma lista do banco de dados e ensine o gráfico a extrair o Nome e o Valor usando Lambdas:
```java
List<Venda> vendas = bancoDeDados.getVendasMes();

CardGrafic graficoBarras = new CardGrafic();
graficoBarras.setChartType(CardGrafic.ChartType.BAR_GRADIENT);
// Extrai o nome do vendedor e o total de vendas de cada objeto
graficoBarras.setItems(vendas, Venda::getNomeVendedor, Venda::getTotalVendido);
```

### Forma 3: Data Binding por Agrupamento (Contagem Automática)
Passe uma lista "crua" e ensine o gráfico a agrupar e contar as ocorrências automaticamente (Ideal para Gráficos de Pizza/Status):
```java
List<Cliente> clientes = bancoDeDados.getClientes();

CardGrafic graficoPizza = new CardGrafic();
graficoPizza.setChartType(CardGrafic.ChartType.DONUT_ROUNDED);
// Conta quantos clientes existem em cada "Status" e monta as fatias da Pizza sozinho!
graficoPizza.setItemsObjectCount(clientes, Cliente::getStatus);
```

## 3. CardTable
Tabela sem bordas com linhas espaçadas e efeito *Hover*.
- `setRowHeight(int)`
- `setRowHoverColor(Color)`

```java
CardTable tabela = new CardTable();
tabela.setRowHoverColor(new Color(241, 245, 249)); 
DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Nome"}, 0);
model.addRow(new Object[]{1, "Cliente A"});
tabela.setModel(model);
```

## 4. CardListPanel
Grid ou Lista vertical flexível para abrigar múltiplos Cards. Funciona lado a lado com a classe abstrata `CardListModel` para gerar cards visualmente a partir dos seus objetos (Data Binding).

**Exemplo de como CRIAR seu modelo e UTILIZÁ-LO na tela:**
```java
// 1. COMO CRIAR O MODELO (Você ensina o framework a desenhar 1 Card)
public class ProdutoModel extends CardListModel<Produto> {
    @Override
    protected void configureCard(Produto p, CardPanel card) {
        card.add(new CardTitle(p.getNome()));
        card.add(new CardSubtitle("R$ " + p.getPreco()));
        card.add(new CardSeparator());
        card.add(new CardButtonRow().addButton("Comprar", Color.BLUE, () -> fireButtonClick(p)));
    }
}

// 2. COMO UTILIZAR NA TELA (O framework replica o design pra todos os itens sozinho)
CardListPanel lista = new CardListPanel();
lista.setColumns(2); // Deixa os cards em Grid 2x2

ProdutoModel model = new ProdutoModel(); 
model.bindTo(lista);
model.setItems(meusProdutosDoBanco); // Mágica acontece aqui!
```

## 5. CardLoading
Bloqueia a tela temporariamente com um *Spinner* gerenciando a Thread Principal para evitar que sua interface congele. Possui 3 formas de uso:

```java
// Forma 1: Sem mensagem, apenas bloqueio rápido
CardLoading.execute(this, () -> { carregarRelatorio(); });

// Forma 2: Com mensagem de espera
CardLoading.execute(this, "Processando...", () -> { processarArquivo(); });

// Forma 3: Com mensagem e Callback (Executa algo na UI após terminar)
CardLoading.execute(this, "Salvando Banco...", 
    () -> { Thread.sleep(3000); /* DB Insert pesado em Background */ },
    () -> { CardToast.showSuccess(this, "Pronto!"); /* Roda na Thread da UI! */ }
);
```

## 6. CardToast
Mensagem pop-up temporária (Snackbar) flutuante no canto da tela. Ideal para feedback rápido.

```java
// Variantes de cores e ícones automáticos
CardToast.showSuccess(this, "Dados salvos com êxito!");
CardToast.showError(this, "Falha de conexão com o Banco.");
CardToast.showWarning(this, "Atenção: Licença expirando.");
CardToast.showInfo(this, "Nova atualização disponível.");
```

## 7. CardDialog
Substitui o antigo `JOptionPane` por Modais modernas, animadas e fluidas.

```java
// 1. Confirmação (Sim/Não)
boolean resposta = CardDialog.showConfirm(this, "Atenção", "Deseja apagar o registro?");

// 2. Entrada de Texto Livre
String nome = CardDialog.showInput(this, "Novo Cliente", "Digite o nome:");

// 3. Entrada com Seleção (Dropdown)
String estado = CardDialog.showComboInput(this, "Região", "Selecione a UF:", new String[]{"SP", "RJ", "MG"});
```

## 8. CardPanel
Painel com sombras 3D, cantos redondos e *Hover*. Base para quase tudo.
- `setRadius(int)`
- `setShadowEnabled(boolean)`
- `setHoverEnabled(boolean)`
- `setTitle(String)`

```java
CardPanel panel = new CardPanel();
panel.setRadius(25);
panel.setShadowEnabled(true);
```

## 9. CardTitle
Rótulo de texto estilo H1 (Negrito forte e grande).
- `setText(String)`

```java
CardTitle titulo = new CardTitle("Relatório Anual");
```

## 10. CardSubtitle
Rótulo secundário (Cinza e tamanho médio).
- `setText(String)`

```java
CardSubtitle sub = new CardSubtitle("Valores brutos");
```

## 11. CardText
Rótulo de parágrafo descritivo normal.
- `setText(String)`

```java
CardText txt = new CardText("Lorem ipsum dolor...");
```

## 12. CardSeparator
Linha divisória horizontal ou vertical estilizada.
- `setStyle(SeparatorStyle)`: SOLID, DASHED, DOTTED, GRADIENT, SHADOW.

```java
CardSeparator sep = new CardSeparator();
sep.setStyle(CardSeparator.SeparatorStyle.DASHED);
```

## 13. CardTextField
Campo de texto (Input) com linha base animada e placeholder de fundo.
- `setPlaceholder(String)`
- `setThemeColor(Color)`

```java
CardTextField email = new CardTextField();
email.setPlaceholder("E-mail corporativo");
```

## 14. CardPasswordField
Campo de senha com orelhinha/ícone de ocultar nativo.
- `setPlaceholder(String)`

```java
CardPasswordField senha = new CardPasswordField();
senha.setPlaceholder("Sua senha secreta");
```

## 15. CardComboBox
Dropdown *Select* moderno sem os serrilhados nativos.
- `addItem(Object)`

```java
CardComboBox estado = new CardComboBox();
estado.addItem("SP");
```

## 16. CardButton
Botão minimalista moderno com interatividade Hover.
- `setButtonColor(Color)`
- `setHoverColor(Color)`
- `setButtonRadius(int)`

```java
CardButton btn = new CardButton("Salvar");
btn.setButtonColor(Color.GREEN);
```

## 17. CardButtonRow
Agrupador alinhado à direita para vários `CardButton`s seguidos.
- `addButton(String, Color, Runnable)`

```java
CardButtonRow row = new CardButtonRow();
row.addButton("Deletar", Color.RED, () -> acaoDelete());
```

## 18. CardCalendar
Calendário fixo e interativo feito puramente em `Graphics2D`.
- `getLocalDate()`
- `setLocalDate(LocalDate)`

```java
CardCalendar calendario = new CardCalendar();
LocalDate hoje = calendario.getLocalDate();
```

## 19. CardDatePicker
Campo de texto que formata a data e abre um pop-up de calendário no clique.
- `getSelectedDate()`

```java
CardDatePicker dataNasc = new CardDatePicker();
```

## 20. CardDateTimePicker
Igual ao `CardDatePicker`, porém suporta horas.
- `getSelectedDateTime()`

```java
CardDateTimePicker dataHora = new CardDateTimePicker();
```

## 21. CardSwitch
Toggle no estilo iOS/Android.
- `isSelected()`
- `setTrackOnColor(Color)`

```java
CardSwitch notificar = new CardSwitch();
notificar.setTrackOnColor(Color.BLUE);
```

## 22. CardCheck
Checkbox moderno preenchido.
- `isSelected()`
- `setCheckColor(Color)`

```java
CardCheck aceitar = new CardCheck("Aceito", false);
```

## 23. CardChip
Tag com um botão de **fechar (X)** que se auto-destrói na UI ao ser clicada.
- `setText(String)`
- `setChipColor(Color)`

```java
CardChip chip = new CardChip("Pago");
chip.addActionListener(e -> { /* filtro removido */ });
```

## 24. CardBadge
Pequena bolinha (contador) para exibir volume de notificações em cima de ícones.
- `setText(String)`
- `setBadgeColor(Color)`

```java
CardBadge badge = new CardBadge();
badge.setText("99");
```

## 25. CardTag
Pílula não clicável com texto e cor de fundo maciça para demonstrar Status.
- `setTagColor(Color)`
- `setTagTextColor(Color)`

```java
CardTag tag = new CardTag("Em Andamento");
```

## 26. CardStatusBadge
Variante moderna da `CardTag` em que apenas uma "luz" brilhante à esquerda representa o status.
- `setStatusColor(Color)`

```java
CardStatusBadge online = new CardStatusBadge("Online", Color.GREEN);
```

## 27. CardStatusRow
Painel que agrupa múltiplos Status dinamicamente de forma linear.
- `addStatus(String label, String value, Color)`

```java
CardStatusRow statusInfo = new CardStatusRow();
statusInfo.addStatus("Conexões", "Aativas", Color.GREEN);
```

## 28. CardAvatar
Container que aplica o *Crop* (Máscara Redonda Perfeita) para Fotos de Perfil. Possui fallback para iniciais.

```java
CardAvatar perfil = new CardAvatar();
perfil.setShowOnlineDot(true); // Exibe bolinha verde de "Online"

// Forma 1: Carregando uma foto do disco
perfil.setImagePath("C:\\foto.jpg");

// Forma 2: Quando o usuário não tem foto, gera Iniciais coloridas automaticamente
perfil.setInitials("JS"); // Gera um círculo com "JS" centralizado
```

## 29. CardImage
Container flexível com cantos arredondados para carregar banners ou anexos preservando aspecto visual.
- `setImagePath(String)`
- `setImageRadius(int)`

```java
CardImage banner = new CardImage();
banner.setImageRadius(20);
```

## 30. CardTabs
Sistema de Abas translúcido, desenhando animação *border-bottom* fluida ao mudar de Tab.
- `addTab(String, JPanel)`

```java
CardTabs tabs = new CardTabs();
tabs.addTab("Geral", panelGeral);
```

## 31. CardProgress
Barra de progresso interativa com diversos designs luxuosos embutidos.

```java
CardProgress proc = new CardProgress();
proc.setProgress(70);

// Mude a aparência instantaneamente:
proc.setModel(CardProgress.ProgressModel.LINEAR); // Barra reta grossa
proc.setModel(CardProgress.ProgressModel.LINEAR_SLIM); // Linha minimalista
proc.setModel(CardProgress.ProgressModel.CIRCULAR); // Velocímetro
```

## 32. CardSkeleton
Máscara piscante (*Shimmer Effect*) cinza para exibir o estado de "Carregando a Interface".
- `playAnimation()`
- `stopAnimation()`

```java
CardSkeleton skeleton = new CardSkeleton();
skeleton.playAnimation();
```
