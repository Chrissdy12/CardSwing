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
CardThemeManager.applyTheme(CardThemeManager.CardTheme.DARK);
```

## 2. CardGrafic
Gráfico vetorial de altíssimo desempenho sem dependências.
- `setValues(List<Double>)`, `setLabels(List<String>)`
- `setChartType(ChartType)`: PIE, DONUT, DONUT_ROUNDED, GAUGE, BAR, BAR_MODERN, BAR_GRADIENT, BAR_LOLLIPOP, HORIZONTAL_BAR, LINE, LINE_SMOOTH, AREA, AREA_SMOOTH, AREA_SMOOTH_GRADIENT.
- `setChartColor(Color)`, `setShowGrid(boolean)`

```java
CardGrafic grafico = new CardGrafic();
grafico.setChartType(CardGrafic.ChartType.AREA_SMOOTH_GRADIENT);
grafico.setChartColor(new Color(59, 130, 246)); 
grafico.setValues(Arrays.asList(150.0, 230.0, 180.0));
grafico.setLabels(Arrays.asList("Jan", "Fev", "Mar"));
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
Grid ou Lista vertical flexível para abrigar múltiplos Cards. Funciona perfeitamente com a classe abstrata secundária `CardListModel` para preenchimento dinâmico.
- `setColumns(int)`
- `setGap(int)`

```java
CardListPanel lista = new CardListPanel();
lista.setColumns(2); // Grid 2x2
// Usando o model abstrato embutido para preencher a lista
ProdutoModel model = new ProdutoModel(); 
model.bindTo(lista);
model.setItems(meusDados);
```

## 5. CardLoading
Bloqueia a tela temporariamente com um *Spinner* gerenciando a Thread Principal para evitar que sua interface congele.
- `execute(Component parent, String msg, Runnable inBackground, Runnable onUI)`

```java
CardLoading.execute(this, "Processando...", 
    () -> { Thread.sleep(3000); /* DB Insert */ },
    () -> { CardToast.showSuccess(this, "Pronto!"); }
);
```

## 6. CardToast
Mensagem pop-up temporária (Snackbar) no canto da tela.
- `showSuccess(Component, String)`
- `showError(...)`, `showWarning(...)`, `showInfo(...)`

```java
CardToast.showSuccess(this, "Dados salvos com êxito!");
```

## 7. CardDialog
Substitui o feio `JOptionPane` por Modais fluidas com botão Cancelar/Confirmar.
- `showConfirm(...)`
- `showInput(...)`
- `showComboInput(...)`

```java
boolean resposta = CardDialog.showConfirm(this, "Aviso", "Apagar tudo?");
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
Container que aplica o *Crop* (Máscara Redonda Perfeita) de Fotos de Perfil de Usuários.
- `setImagePath(String)`
- `setInitials(String)`
- `setShowOnlineDot(boolean)`

```java
CardAvatar perfil = new CardAvatar();
perfil.setImagePath("C:\\foto.jpg");
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
Barra de progresso interativa com diversos designs luxuosos.
- `setProgress(int)`
- `setModel(ProgressModel)`

```java
CardProgress proc = new CardProgress();
proc.setModel(CardProgress.ProgressModel.CIRCULAR);
proc.setProgress(70);
```

## 32. CardSkeleton
Máscara piscante (*Shimmer Effect*) cinza para exibir o estado de "Carregando a Interface".
- `playAnimation()`
- `stopAnimation()`

```java
CardSkeleton skeleton = new CardSkeleton();
skeleton.playAnimation();
```
