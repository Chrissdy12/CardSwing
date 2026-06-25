<div align="center">
  <br />
  <h1>✨ CardSwing UI</h1>
  <p>
    <strong>A biblioteca definitiva de componentes modernos e Cards Interativos para Java Swing.</strong>
  </p>
  <p>Construa aplicações de desktop que parecem sistemas web modernos, com facilidade de arrastar e soltar diretamente no NetBeans IDE.</p>
</div>

---

## 📖 Sumário
- [📌 O que é o CardSwing?](#-o-que-é-o-cardswing)
- [🚀 Como Instalar (Dependência Maven)](#-como-instalar-dependência-maven)
- [🎨 Adicionando à Palette do NetBeans](#-adicionando-à-palette-do-netbeans)
- [🧩 Componentes Disponíveis e Como Usar](#-componentes-disponíveis-e-como-usar)
  - [📦 Containers Principais](#-containers-principais)
    - [CardPanel](#cardpanel)
    - [CardTabs](#cardtabs)
    - [CardListPanel](#cardlistpanel)
    - [CardSkeleton](#cardskeleton)
  - [🎛️ Componentes de Interação](#️-componentes-de-interação)
    - [CardSwitch](#cardswitch)
    - [CardButton](#cardbutton)
    - [CardCheck](#cardcheck)
    - [CardTextField](#cardtextfield)
    - [CardPasswordField](#cardpasswordfield)
    - [CardComboBox](#cardcombobox)
    - [CardDatePicker](#carddatepicker)
    - [CardDateTimePicker](#carddatetimepicker)
  - [🔔 Alertas e Diálogos](#-alertas-e-diálogos-popups-modernos)
    - [CardToast](#cardtoast)
    - [CardDialog](#carddialog)
  - [🖼️ Dados e Visuais](#️-dados-e-visuais)
    - [CardTable](#cardtable)
    - [CardGrafic](#cardgrafic)
    - [CardAvatar](#cardavatar)
    - [CardProgress](#cardprogress)
    - [CardStatusBadge](#cardstatusbadge)
    - [CardTag](#cardtag)
    - [CardImage](#cardimage)
    - [CardSeparator](#cardseparator)
  - [✍️ Tipografia](#️-tipografia)
- [💻 Exemplo de Uso Avançado](#-exemplo-de-uso-avançado-cardlistmodel)

---

## 📌 O que é o CardSwing?
O **CardSwing** é uma biblioteca visual de alto nível para Java Swing. Ele resolve o problema histórico do Java Desktop parecer "antigo", trazendo conceitos modernos de design web, como UI Baseada em Cards, Layouts fluidos, Efeito Ripple (Ondinha de Clique), Skeleton Loading (Shimmer) e Botões Toggle (estilo iOS).

Tudo isso 100% integrado à Palette do **Apache NetBeans (Matisse)**: basta arrastar, configurar pelas Propriedades e rodar!

## 🚀 Como instalar (Dependência Maven)

Para utilizar o CardSwing no seu projeto, instale este projeto na sua máquina (`mvn clean install`) e depois adicione a dependência no `pom.xml` do seu projeto principal:

```xml
<dependency>
    <groupId>com</groupId>
    <artifactId>CardSwing</artifactId>
    <version>1.0</version>
</dependency>
```

### 🎨 Adicionando à Palette do NetBeans
Para que os componentes apareçam na sua janela de design e possam ser arrastados:
1. No NetBeans, vá em **Tools > Palette > Swing/AWT Components**.
2. Clique em **New Category...** e crie uma categoria chamada `CardSwing`.
3. Clique em **Add from Project...**, localize o projeto onde você baixou a biblioteca CardSwing.
4. Selecione **APENAS as classes de componentes visuais** (veja a lista abaixo) para evitar lixo visual.
5. Selecione a categoria `CardSwing` e clique em Finish!

---

## 🧩 Componentes Disponíveis e Como Usar

Todos os componentes possuem a categoria exclusiva **"CardSwing Configs"** na janela de Propriedades do NetBeans, centralizando tudo que você precisa alterar (Cores, Fontes, Tamanhos).

### 📦 Containers Principais
<a id="cardpanel"></a>
- **`CardPanel`:** O coração da biblioteca. Um painel com cantos perfeitamente arredondados, sombra 3D suave, barra de cabeçalho colorida (Theme Color) e suporte a Título com Ícone.
  - **Exemplo de Uso via Código:**
    ```java
    CardPanel painel = new CardPanel();
    painel.setThemeColor(new Color(59, 130, 246));
    painel.setCollapsible(true); // Permite sanfonar o card
    ```

<a id="cardtabs"></a>
- **`CardTabs`:** Um `JTabbedPane` completamente modernizado. Possui abas limpas com espaçamento respirável, sem bordas pesadas e com um traço inferior animado e colorido na aba ativa (Material Design / Web style).
  - **Exemplo de Uso:** Basta arrastar pela Palette e adicionar painéis normalmente. O visual premium é automático.

<a id="cardlistpanel"></a>
- **`CardListPanel`:** Uma lista inteligente que organiza seus Cards automaticamente (Fluida ou em Grid).
  - **Exemplo de Uso:**
    ```java
    CardListPanel listPanel = new CardListPanel();
    listPanel.setShowSearch(true); // Exibe uma barra de busca no topo
    listPanel.addCard(new CardPanel()); 
    listPanel.setOnSearch(texto -> filtrarBusca(texto)); // Callback
    ```

<a id="cardskeleton"></a>
- **`CardSkeleton`:** Componente mágico que simula o carregamento. Mostra o formato de um card vazio com um efeito "Shimmer".
  - **Exemplo de Uso:** Basta arrastar para a tela ou usar no código `painel.add(new CardSkeleton());` enquanto busca dados no banco.

### 🎛️ Componentes de Interação
<a id="cardswitch"></a>
- **`CardSwitch`:** Substitui os checkboxes feios por um Toggle moderno (estilo botão do iPhone), com transição suave.
  - **Exemplo de Uso:**
    ```java
    CardSwitch toggle = new CardSwitch();
    toggle.setChecked(true);
    toggle.addPropertyChangeListener("checked", evt -> salvar(toggle.isChecked()));
    ```

<a id="cardbutton"></a>
- **`CardButton` & `CardButtonRow`:** Botões com cantos arredondados e mudança de cor inteligente. O Row garante espaçamento perfeito lado a lado.
  - **Exemplo de Uso:**
    ```java
    CardButtonRow botoes = new CardButtonRow()
        .addButton("Salvar", new Color(16, 185, 129), () -> salvar())
        .addButton("Cancelar", Color.GRAY, () -> cancelar());
    ```

<a id="cardcheck"></a>
- **`CardCheck`:** Checkbox minimalista com ícone desenhado à mão.
  - **Exemplo de Uso:** `CardCheck check = new CardCheck("Aceito os termos");`

<a id="cardtextfield"></a>
- **`CardTextField`:** Campo de texto moderno com cantos arredondados, padding confortável e anel de foco.
  - **Exemplo de Uso:**
    ```java
    CardTextField txtNome = new CardTextField();
    txtNome.setPlaceholder("Digite seu nome...");
    ```

<a id="cardpasswordfield"></a>
- **`CardPasswordField`:** Idêntico ao TextField, mas preparado para senhas (`***`). Ele já vem com um botão interativo integrado no canto (olho desenhado vetorialmente) que permite mostrar ou ocultar a senha ao ser clicado, incluindo suporte a `placeholder`.
  - **Exemplo de Uso:** `CardPasswordField txtSenha = new CardPasswordField(); txtSenha.setPlaceholder("Senha secreta");`

<a id="cardcombobox"></a>
- **`CardComboBox`:** Seletor (Dropdown) de visual limpo e seta de expansão (chevron) discreta.
  - **Exemplo de Uso:**
    ```java
    CardComboBox<String> combo = new CardComboBox<>();
    combo.addItem("Opção 1");
    ```

<a id="carddatepicker"></a>
- **`CardDatePicker`:** Substituto moderno para o antigo `JDateChooser`. É um campo de texto com ícone de calendário. Ao clicar, exibe um PopUp com um calendário interativo e elegante para escolha de dias e meses. Retorna nativamente a API moderna de Datas do Java (`LocalDate`).
  - **Exemplo de Uso:**
    ```java
    CardDatePicker picker = new CardDatePicker();
    picker.setLocalDate(LocalDate.now());
    LocalDate dataEscolhida = picker.getLocalDate();
    ```

<a id="carddatetimepicker"></a>
- **`CardDateTimePicker`:** Evolução do DatePicker, mas focado na seleção completa de **Data e Hora**. O calendário exibe os seletores de Horas e Minutos acoplados, perfeito para agendamentos. Retorna nativamente o `LocalDateTime`.
  - **Exemplo de Uso:**
    ```java
    CardDateTimePicker dateTimePicker = new CardDateTimePicker();
    dateTimePicker.setLocalDateTime(LocalDateTime.now());
    LocalDateTime dataHoraEscolhida = dateTimePicker.getLocalDateTime();
    ```

### 🔔 Alertas e Diálogos (Popups Modernos)
<a id="cardtoast"></a>
- **`CardToast`:** Esqueça o chato `JOptionPane` para mensagens simples. O Toast exibe uma notificação flutuante e animada no canto inferior direito da tela. Ele desliza, tem sombra 3D, ícones desenhados manualmente (sem depender de fontes) e desaparece sozinho após 3 segundos sem travar o aplicativo.
  - **Exemplo de Uso:**
    ```java
    CardToast.showSuccess(this, "Salvo com sucesso!");
    CardToast.showError(this, "Falha na conexão!");
    CardToast.showWarning(this, "Estoque baixo.");
    CardToast.showInfo(this, "Sistema atualizado.");
    ```

<a id="carddialog"></a>
- **`CardDialog`:** Substitui os alertas modais intrusivos do Java. Cria um efeito de véu escuro (Backdrop) que cobre toda a janela e traz um Card branco centralizado com bordas altamente arredondadas, ícone ilustrativo vetorial e botões modernos de confirmação.
  - **Exemplo de Uso:**
    ```java
    // Confirmação
    boolean continuar = CardDialog.showConfirm(this, "Excluir", "Tem certeza?");
    
    // Entrada de Texto
    String nome = CardDialog.showInput(this, "Nome", "Qual o seu nome?");
    
    // Entrada com Combo
    String[] profissoes = {"Dev", "Design"};
    String profissao = CardDialog.showComboInput(this, "Profissão", "Selecione:", profissoes);
    ```

### 🖼️ Dados e Visuais
<a id="cardtable"></a>
- **`CardTable`:** A tão sonhada tabela com design moderno! Sem linhas de grade (no-grid), espaçamento generoso, hover styles, cor de seleção suave e **Auto-Bind via Reflection**!
  - **Mágica do Auto-Bind:**
    ```java
    // Ao invés de escrever dezenas de linhas configurando DefaultTableModel, basta fazer isso:
    List<Cliente> clientes = obterClientesDoBanco();
    cardTable1.bindList(clientes, Cliente.class);
    // A tabela automaticamente lê as variáveis da classe Cliente e monta as colunas!
    ```

<a id="cardgrafic"></a>
- **`CardGrafic`:** Gráficos leves e instantâneos. Possui **17 opções** incríveis: `BAR`, `BAR_MODERN`, `BAR_GRADIENT`, `BAR_GRADIENT_COLORFUL`, `BAR_LOLLIPOP`, `BAR_LOLLIPOP_COLORFUL`, `HORIZONTAL_BAR`, `HORIZONTAL_BAR_MODERN`, `HORIZONTAL_BAR_MODERN_2`, `HORIZONTAL_BAR_GRADIENT`, `HORIZONTAL_BAR_GRADIENT_COLORFUL`, `HORIZONTAL_BAR_LOLLIPOP`, `HORIZONTAL_BAR_LOLLIPOP_COLORFUL`, `LINE`, `AREA`, `PIE`, e `DONUT`.
  - **Exemplo de Uso Dinâmico (Binding com Expressões Lambda):**
    ```java
    // Vincula diretamente uma lista de objetos ao gráfico, sem converter dados manualmente!
    List<Venda> vendas = List.of(new Venda("Janeiro", 1500), new Venda("Fevereiro", 3200));
    
    // Mostrando nomes no eixo X e valores no eixo Y, definindo a cor de cada fatia dinamicamente
    meuGrafico.setChartType(CardGrafic.ChartType.DONUT);
    meuGrafico.setItems(vendas, Venda::getMes, Venda::getValor, v -> {
        return v.getValor() > 2000 ? Color.GREEN : Color.RED; // Vendeu bem? Fica verde!
    });
    ```

<a id="cardavatar"></a>
- **`CardAvatar`:** Exibe imagens de perfil de forma circular perfeita. Se o usuário não tiver foto, ele extrai as iniciais do nome e desenha um fundo colorido automaticamente.
  - **Exemplo:** `cardAvatar1.setImageOrInitials(null, "João Silva"); // Renderiza um círculo com "JS"`

<a id="cardprogress"></a>
- **`CardProgress`:** Barra de progresso super estilizada com suporte numérico opcional ao centro.
  - **Exemplo (Calculando % sozinho):** `cardProgress1.setValue(750, 1500); // Ele preenche 50% sozinho`

<a id="cardstatusbadge"></a>
- **`CardStatusBadge` & `CardStatusRow`:** Exibem pares de "Chave: Valor" em pequenos quadros coloridos.
  - **Exemplo de Uso:**
    ```java
    CardStatusRow status = new CardStatusRow()
        .addStatus("Pendente", "5", Color.RED)
        .addStatus("Pronto", "10", Color.GREEN);
    ```

<a id="cardtag"></a>
- **`CardTag`:** Uma pílula redonda para categorias.
  - **Exemplo de Uso:** `CardTag tag = new CardTag("Novo", Color.BLUE);`

<a id="cardimage"></a>
- **`CardImage`:** Placeholder arredondado para imagens grandes de produtos.
  - **Exemplo de Uso:** `card.add(new CardImage(minhaImagemFile));`

<a id="cardseparator"></a>
- **`CardSeparator`:** Uma linha sutil para separar seções.
  - **Exemplo de Uso:** `card.add(new CardSeparator());`

### ✍️ Tipografia
- **`CardTitle`**, **`CardSubtitle`**, e **`CardText`**: Rótulos padronizados utilizando as melhores práticas tipográficas de tamanho e espaçamento.
  - **Exemplo:** `painel.add(new CardTitle("Faturamento"));`

---

## 💻 Exemplo de Uso Avançado (CardListModel)

Em vez de criar dezenas de cards manualmente na tela, você pode usar o **`CardListModel`**! Ele cria os cards dinamicamente baseados na sua lista do Banco de Dados.

```java
// 1. Crie a regra de como os dados viram Cards
public class ProdutoModel extends CardListModel<Produto> {
    @Override
    protected void configureCard(Produto p, CardPanel card) {
        card.setThemeColor(new Color(59, 130, 246));
        
        // Adiciona um Avatar e Título
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setOpaque(false);
        header.add(new CardAvatar("PR")); // Avatar com iniciais
        header.add(new CardTitle(p.getNome()));
        
        card.add(header);
        card.add(new CardSeparator());
        
        card.add(new CardStatusRow()
            .addStatus("Estoque", p.getQtd() + " unid.", Color.GRAY)
            .addStatus("Preço", "R$ " + p.getPreco(), new Color(16, 185, 129)));
            
        card.add(new CardButtonRow()
            .addButton("Editar", Color.BLUE, () -> fireButtonClick(p)));
    }
}

// 2. Na sua tela (onde você arrastou o CardListPanel):
ProdutoModel model = new ProdutoModel();
model.bindTo(meuCardListPanel);

// Se quiser loading, limpe a lista e jogue uns Skeletons:
// meuCardListPanel.addCard(new CardSkeleton());

// Quando a busca no banco terminar, injete os dados reais:
model.setItems(meusProdutos);

// Capturar eventos:
model.setOnButtonClick(produto -> abrirTelaEdicao(produto));
meuCardListPanel.setOnSearch(textoDigitado -> refazerBusca(textoDigitado));
```

