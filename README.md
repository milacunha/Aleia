# Aleia 📚

![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2026.05.01-4285F4?logo=jetpackcompose&logoColor=white)
![Room](https://img.shields.io/badge/Room-2.8.4-3DDC84?logo=android&logoColor=white)
![minSdk](https://img.shields.io/badge/minSdk-24-brightgreen)
![Status](https://img.shields.io/badge/status-conclu%C3%ADdo-success)

> Um app Android que sorteia qual livro da sua estante você deveria ler agora. Porque às vezes o maior problema não é falta de opção, é excesso.

O **Aleia** resolve um problema bem específico: escolher o próximo livro quando você tem uma estante cheia e nenhuma vontade de decidir. Ele sorteia um livro não lido, permite filtrar por gênero, e — o melhor — você não precisa digitar capa nem gênero manualmente. A API do Google Books faz isso por você, e a sincronização em background corrige os que faltam.

---

## 📸 Screenshots

| Tela Principal | Filtro por gênero | Adicionar livro |
|:---:|:---:|:---:|
| <img width="376" height="832" alt="image" src="https://github.com/user-attachments/assets/b4ef80d9-f7dd-49ce-b476-daec3036ea52" /> | <img width="377" height="835" alt="image" src="https://github.com/user-attachments/assets/e784c6fa-a6e3-4eed-8a9c-d561da0ebede" /> | <img width="377" height="837" alt="image" src="https://github.com/user-attachments/assets/a0b4104d-920e-4149-bf00-f995f502678d" /> |

| Loading | Estado vazio | Erro |
|:---:|:---:|:---:|
| <img width="376" height="837" alt="image" src="https://github.com/user-attachments/assets/0ecfa261-e3e0-4e7f-b6c3-6c3e9a8ee103" /> | <img width="377" height="838" alt="image" src="https://github.com/user-attachments/assets/501d835a-f266-4cd9-99c4-f6e901ba0e82" /> | <img width="377" height="831" alt="image" src="https://github.com/user-attachments/assets/ef949daf-06bb-4d1a-a2ba-0ff3165d80c8" /> |

---

## ✨ O que o app faz

- 🎲 **Sorteia um livro não lido** da sua biblioteca, e não repete durante a sessão
- 🏷️ **Filtra por gênero** — quer fantasia? Ele só mostra fantasia
- ➕ **Adiciona livros com um clique** — titulo, capa e gênero vêm da API do Google Books
- 🔄 **Sincronização em background** — ao abrir o app, livros sem capa ou gênero são corrigidos silenciosamente
- ✅ **Aceitar sugestão** marca o livro como lido, então ele sai do sorteio para sempre

---

## 🛠️ Stack

- **[Kotlin](https://kotlinlang.org/)** + **[Jetpack Compose](https://developer.android.com/jetpack/compose)** — UI declarativa
- **[MVVM](https://developer.android.com/topic/architecture)** — separação clara entre UI, lógica e dados
- **[Room](https://developer.android.com/training/data-storage/room)** — persistência local
- **[Retrofit](https://square.github.io/retrofit/)** + **[Moshi](https://github.com/square/moshi)** — consumo da Google Books API
- **[Coil](https://coil-kt.github.io/coil/)** — carregamento de imagens com cache em disco e suporte a GIF
- **[Koin](https://insert-koin.io/)** — injeção de dependência
- **[Coroutines + Flow](https://kotlinlang.org/docs/coroutines-overview.html)** — assincronismo e reatividade

---

## 🧠 Decisões técnicas

- **Sincronização assíncrona com validação seletiva**: ao adicionar um livro, os metadados vêm da API na hora. Se algum livro da biblioteca estiver sem capa ou gênero (por exemplo, adicionado offline), o app corrige silenciosamente em background na próxima abertura — sem travar a UI, com rate limiting entre chamadas.
- **Cache + fallback visual com Coil**: capas têm cache automático em disco e placeholder para livros sem imagem.
- **Estado de UI tipado com `sealed class` + `EmptyAction`**: a tela de estado vazio sabe se deve oferecer "adicionar livro" ou "recomeçar sessão" — o comportamento vem do estado, não de flags soltas na UI.

---

## 🎨 Design System — Neo-brutalismo

A identidade visual do app segue o **neo-brutalismo**: contraste alto, poucas cores fortes, tipografia em caixa alta e sombras sólidas e deslocadas. Nada de gradiente, nada de borda arredondada sutil — os elementos ocupam espaço e chamam atenção.

### Paleta

| Token | Cor | Uso |
|---|---|---|
| `Background` | `#F5EEDD` | Fundo da tela (bege quente) |
| `YellowNeo` | `#EDB240` | Ação principal (aceitar sugestão) |
| `AquaNeo` | `#40EDB2` | Ações secundárias (filtro, adicionar) |
| `PurpleNeo` | `#B240ED` | Botões de estado (empty, error) |
| `Black` | `#000000` | Bordas, sombras e textos principais |
| `White` | `#FFFFFF` | Superfícies e texto sobre cores fortes |
| `DarkGray` | `#797979` | Texto auxiliar e estados secundários |
| `LightGray` | `#DBDBDA` | Divisores e elementos de apoio |

### Tipografia

Fonte **Space Grotesk** (Google Fonts), aplicada via `MaterialTheme.typography`. Os títulos usam **caixa alta** por convenção de estilo — não por transformação da fonte — e a hierarquia é resolvida com tamanhos, pesos e `letterSpacing` distintos.

- **Títulos** (`titleLarge`, `titleMedium`) — 18–22sp, Bold, letter spacing 2–3sp
- **Capa do livro** (`headlineLarge`, `headlineSmall`) — 18–28sp, Bold/SemiBold
- **Corpo** (`bodyMedium`, `bodySmall`) — 12–14sp, SemiBold
- **Botões** (`displayLarge`, `displayMedium`, `displaySmall`) — 16–24sp, Bold

### Componentes próprios

`NeoBrutButton`, `NeoBrutCover` e `NeoBrutTextField` ficam em `ui/component/` e encapsulam bordas grossas, sombras deslocadas e o comportamento visual do estilo — a UI não repete essas decisões em cada tela.

### Ilustrações animadas

GIFs desenhados à mão no **Procreate** para os estados de loading, vazio e erro. Cada um reforça o tom do app sem depender de assets genéricos.

> O objetivo não era copiar uma referência, era criar uma linguagem visual única e coerente que se sustentasse em uma única tela.

---

## 📁 Estrutura

```
app/src/main/java/br/com/camilacunha/aleia/
├── data/
│   ├── local/          # Room: entity, DAO, database, DTOs
│   ├── remote/         # Retrofit: API
│   ├── mapper/         # Entity ↔ Domain
│   └── repository/     # Implementações dos repositórios
├── di/                 # Módulos Koin
├── domain/
│   ├── model/          # Modelos de domínio
│   └── *.kt            # Interfaces dos repositórios
└── ui/
    ├── component/      # Componentes reutilizáveis
    ├── screen/         # Telas e bottom sheets
    ├── state/          # UI States (sealed classes)
    └── theme/          # Cores e tipografia
```

---

## 🧪 Testes

O projeto tem cobertura de testes unitários e instrumentados nos fluxos principais.

**O que está coberto:**

- **Unitários** — repositórios (add, filter, fetch da API) e ViewModel (randomize, filter, accept, empty states)
- **Instrumentados** — fluxo completo de adição de livro, fluxo de filtro por gênero, smoke test de abertura

---

## 🚀 Como rodar

### Pré-requisitos

- **Android Studio** (versão recente, com suporte a AGP 8.11+)
- **JDK 17**
- Um emulador ou dispositivo físico com **Android 7.0 (API 24)** ou superior

### Configuração da API Key (opcional)

O app usa a **Google Books API**. A chave é opcional — sem ela o app funciona normalmente, mas com limite de requisições menor. Se quiser configurar:

1. Crie uma chave de API no [Google Cloud Console](https://console.cloud.google.com/) com a **Books API** habilitada
2. Adicione no `local.properties` na raiz do projeto:

```properties
GOOGLE_BOOKS_API_KEY=sua_chave_aqui
```

> ⚠️ O `local.properties` já está no `.gitignore`. **Nunca** commite essa chave.

### Instalação

```bash
git clone https://github.com/milacunha/Aleia.git
cd Aleia
./gradlew assembleDebug
```

Ou abra direto no Android Studio e clique em ▶️.

### Sobre os dados iniciais

O app vem com um **JSON de exemplo** (`books_mock.json`) contendo 2 livros para você testar rapidamente. Se preferir começar com a sua estante real, substitua o conteúdo pelo formato esperado — o seed roda na primeira inicialização.

---

## 📌 Status

Projeto **concluído** e em uso pessoal. Não está publicado na Play Store e não recebe novas features ativamente — o objetivo era ter algo simples que funcionasse bem e servisse de portfólio.

---

## ✒️ Autora

Projeto desenhado, desenvolvido e documentado por **Camila Cunha**

- [Github](https://github.com/milacunha)
- [LinkedIn](https://www.linkedin.com/in/camila-s-e-cunha/)
