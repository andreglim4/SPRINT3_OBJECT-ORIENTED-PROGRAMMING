# 🌱 VeroAI (Sistema MOTIVA)

## 📌 Sobre o Projeto

Este projeto tem como objetivo desenvolver um sistema de monitoramento e priorização de roçada de vegetação em rodovias. 

A aplicação modela trechos rodoviários e equipes de manutenção, permitindo acompanhar o crescimento da vegetação e identificar automaticamente situações críticas que exigem intervenção.

Desenvolvido como parte da disciplina **Object-Oriented Programming (OOP)**. Na **Sprint 3**, o sistema foi evoluído para suportar **Persistência de Dados** utilizando um banco de dados **Oracle** e **JDBC puro**, garantindo que todo o histórico de intervenções, equipes, trechos e relatórios gerados pelo motor de regras seja salvo permanentemente.

---

## 🚀 Funcionalidades

### ⚙️ Lógica e Motor de Regras (Sprint 2)
- **Criação de trechos de rodovia** com identificação e quilometragem.
- **Simulação de crescimento inteligente** da vegetação (trechos úmidos crescem mais rápido que secos).
- **Identificação automática** de trechos críticos com base em um limite predefinido.
- **Integração com IoT** para monitoramento automático via sensores (`Interface MonitoravelViaIoT`).
- **Tipos de intervenção:**
  - **Roçada mecanizada** (trator, vegetação zerada).
  - **Pulverização química** (controle parcial da vegetação).
- **Validação de dados** (não permite valores negativos ou ilógicos).

### 💾 Persistência de Dados (Sprint 3 - Novo!)
- **Conexão Singleton** com o banco de dados Oracle FIAP.
- **CRUD Completo (Create, Read, Update, Delete)** utilizando o padrão de projeto **DAO** para:
  - Equipes de Manutenção.
  - Trechos de Rodovia.
  - Intervenções Operacionais.
- **Histórico Persistente:** O motor de regras agora salva o resultado das análises e os relatórios de prioridade gerados diretamente no banco de dados.
- **Prevenção de SQL Injection** garantida pelo uso de `PreparedStatement`.
- **Segurança e Performance:** Uso do bloco `try-with-resources` para fechamento automático de conexões e *ResultSets*.

---

## 👥 Integrantes do Grupo
- Vitor Komura – RM563694  
- Caio Castelão – RM563630  
- Mirella Mascarenhas – RM562092  
- Guilherme Tamai – RM563276  
- André Gouveia – RM564219  
- André Nobrega – RM561754

---

## ▶️ Como executar o Projeto (Guia do Professor)

### 🧰 Pré-requisitos
- **Java 8+** instalado.
- **Rede da FIAP** (ou VPN ativada) para acesso ao servidor Oracle remoto (`oracle.fiap.com.br`).
- Driver JDBC **`ojdbc17.jar`** (deve ser baixado e inserido na pasta `lib/` do projeto, caso não esteja incluído no repositório).

### 🗄️ Passo 1: Preparando o Banco de Dados
Antes de rodar o código Java, as tabelas precisam existir no seu *schema* do Oracle.
1. Abra o **Oracle SQL Developer** e conecte-se ao host `oracle.fiap.com.br` na porta `1521` (SID: `ORCL`).
2. Abra o arquivo `SQL/seu-script-criacao.sql` e execute-o para criar as tabelas.
3. Abra o arquivo `SQL/seu-script-dados.sql` e execute-o para popular as tabelas com os dados de teste iniciais.

### 🔑 Passo 2: Configurando Credenciais no Java
Abra o arquivo `src/br/com/rodovia/db/ConexaoBD.java` e altere as constantes com o seu RM e Senha do banco da FIAP:
```java
private static final String USER = "rmXXXXXX"; // Insira seu usuário aqui
private static final String PASSWORD = "sua_senha"; // Insira sua senha aqui
```

### 🚀 Passo 3: Compilando e Executando
1. Clone o repositório da Sprint 3:
```bash
git clone https://github.com/andreglim4/SPRINT3_OBJECT-ORIENTED-PROGRAMMING.git
cd SPRINT3_OBJECT-ORIENTED-PROGRAMMING
```

2. Compile os arquivos referenciando o driver JDBC na pasta `lib/`:
- **No Windows (PowerShell):**
```powershell
javac -cp "lib\ojdbc17.jar" -d bin (Get-ChildItem -Recurse -Path src -Filter *.java).FullName
```
- **No Mac/Linux:**
```bash
javac -cp "lib/ojdbc17.jar" -d bin $(find src -name "*.java")
```

3. Execute o programa principal:
- **No Windows:**
```powershell
java -cp "bin;lib\ojdbc17.jar" br.com.rodovia.principal.SistemaPrincipal
```
- **No Mac/Linux:**
```bash
java -cp "bin:lib/ojdbc17.jar" br.com.rodovia.principal.SistemaPrincipal
```

---

## 🧱 Estrutura e Arquitetura do Projeto

O projeto adota uma arquitetura em camadas para isolar responsabilidades:

- **`br.com.rodovia.db`**: Contém a classe `ConexaoBD` (Padrão Singleton) responsável por gerenciar a conexão com o Oracle.
- **`br.com.rodovia.dao`**: Classes de acesso a dados (DAOs) que contém os *queries* SQL (`SELECT`, `INSERT`, `UPDATE`, `DELETE`) e utilizam `Java Records` para transportar os dados das tabelas de forma imutável.
- **`br.com.rodovia.modelo`**: Contém as regras de negócio puras (Classes de Domínio: `TrechoRodovia`, `TrechoIoT`, Entidades Abstratas, etc.).
- **`br.com.rodovia.servico`**: Classes lógicas, como o `GerenciadorPrioridade`, que aplicam o motor de regras nas rodovias e salvam o resultado via DAOs.
- **`br.com.rodovia.principal`**: A classe `SistemaPrincipal`, que orquestra a aplicação e atua como uma suíte de testes de integração ponta a ponta.

---

## 🧪 Cenários Testados e Validações

- **Conexão de Banco:** Tratamento robusto de `SQLException` e `ClassNotFoundException` caso o banco caia ou o Driver falte.
- **CRUD em Cascata:** Inserção e leitura correta de objetos complexos e salvamento automático do histórico de relatórios da gerência.
- **Orientação a Objetos:**
  - Impossibilidade de instanciar classe abstrata (`IntervencaoOperacional`).
  - Captura de dados simulada via *Mock* da interface IoT.
- **Validações de Regra de Negócio:** Sistema bloqueia quilometragem negativa, KM final menor que inicial e crescimento negativo.

---

## 🤔 Perguntas Reflexivas

### ❓ Por que não faz sentido para a Motiva que uma equipe execute apenas uma "Intervenção Operacional" genérica sem especificar qual é?
**Resposta:** Porque a abstração genérica não representa uma ação concreta. Na prática, a equipe precisa executar um tipo específico de intervenção (roçada mecanizada, pulverização, etc.), e não apenas uma operação genérica. Isso garante clareza, rastreabilidade e aplicação correta dos recursos.

### ❓ Qual a diferença arquitetural entre fazer um Trecho herdar de uma classe abstrata vs. implementar uma Interface?
**Resposta:** 
- **Classe abstrata**: fornece uma base comum com atributos e métodos compartilhados, além de comportamentos parcialmente implementados. 
- **Interface**: define apenas contratos de comportamento, sem herança de atributos ou implementação. 

Na arquitetura, a classe abstrata é usada para especialização hierárquica, enquanto a interface permite flexibilidade e desacoplamento, possibilitando que diferentes classes (como sensores variados) adotem o mesmo contrato sem relação de herança.
