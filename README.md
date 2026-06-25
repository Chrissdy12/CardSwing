<div align="center">
  <br />
  <h1>✨ CardSwing UI</h1>
  <p>
    <strong>A biblioteca definitiva de componentes modernos e Cards Interativos para Java Swing.</strong>
  </p>
  <p>Construa aplicações de desktop que parecem sistemas web modernos, com facilidade de arrastar e soltar diretamente no NetBeans IDE.</p>
</div>

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

## 🧩 Componentes Disponíveis

Todos os componentes possuem a categoria exclusiva **"CardSwing Configs"** na janela de Propriedades do NetBeans, centralizando tudo que você precisa alterar (Cores, Fontes, Tamanhos).

### 📦 Containers Principais
- **`CardPanel`:** O coração da biblioteca. Um painel com cantos perfeitamente arredondados, sombra 3D suave, barra de cabeçalho colorida (Theme Color) e suporte a Título com Ícone.
  - *Propriedades Especiais:* `collapsible` (Sanfona), `clickable` (Efeito Ripple).
- **`CardListPanel`:** Uma lista inteligente que organiza seus Cards automaticamente (Fluida ou em Grid).
  - *Propriedades Especiais:* `showSearch` (exibe uma barra de busca moderna no topo), e os eventos de paginação (`onScrollEnd`) e filtragem (`onSearch`).
- **`CardSkeleton`:** Componente mágico que simula o carregamento. Mostra o formato de um card vazio com um efeito "Shimmer" (onda de brilho contínua a 60fps) passando por ele.

### 🎛️ Componentes de Interação
- **`CardSwitch`:** Substitui os checkboxes feios por um Toggle moderno (estilo botão do iPhone), com transição suave.
- **`CardButton` & `CardButtonRow`:** Botões com cantos arredondados e mudança de cor inteligente ao passar o mouse ou clicar. O Row garante espaçamento perfeito lado a lado.
- **`CardCheck`:** Checkbox minimalista com ícone desenhado à mão (adeus look and feel quebrado do Windows!).
- **`CardTextField`:** Campo de texto (input) moderno com cantos arredondados, padding confortável, borda reativa ao hover e anel de foco customizável.
- **`CardComboBox`:** Caixa de seleção (combo) totalmente reestilizada para combinar com o CardTextField, com menu flutuante minimalista, scrollbars suavizadas e setinha moderna.

### 🖼️ Dados e Visuais
- **`CardAvatar`:** Renderiza uma foto de perfil em um círculo perfeito ou, na ausência de foto, exibe Iniciais com uma cor de fundo. Pode exibir uma bolinha verde de "Online".
- **`CardProgress`:** Barra de progresso moderníssima. Você pode informar a Porcentagem diretamente ou informar `currentValue` e `totalValue` para que ela calcule sozinha (ex: "15/20").
- **`CardStatusBadge` & `CardStatusRow`:** Exibem pares de "Chave: Valor" com destaque de cores (ex: "Pendentes: 5" em vermelho).
- **`CardTag`:** Uma pílula redonda com texto e cor de fundo, excelente para categorias (ex: "Urgente", "Novo").
- **`CardImage`:** Placeholder arredondado para imagens grandes de produtos.
- **`CardSeparator`:** Uma linha sutil para separar seções dentro do seu card.

### ✍️ Tipografia
- **`CardTitle`**, **`CardSubtitle`**, e **`CardText`**: Rótulos padronizados utilizando as melhores práticas tipográficas (correto espaçamento, tamanho da fonte e contraste de cor).

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

