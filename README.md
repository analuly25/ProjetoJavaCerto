# Trabalho - Linguagem e Técnicas de Programação II

Ana Luiza Gomes Santana - 22409471

Bárbara Parente de Carvalho Soares - 22402134


-----------------------------------------------------------------------------------------------------------------------------------

# Sistema de Agência de Viagens 

## 📄 Descrição do Sistema

Este sistema foi desenvolvido com o objetivo de gerenciar o cadastro de **clientes**, **pacotes de viagens** e **serviços adicionais** para uma agência de turismo.  

A aplicação permite:
- O cadastro de **clientes nacionais** (com CPF) e **clientes estrangeiros** (com passaporte), garantindo a validação dos documentos conforme a nacionalidade.
- O registro de **pacotes de viagem** com destino, descrição, duração, preço e tipo (ex: nacional, internacional).
- A associação entre clientes e pacotes, permitindo que um **cliente contrate mais de um pacote** de viagem, por meio de **pedidos** registrados no sistema.
- A inclusão de **serviços adicionais** a cada pedido, como translado, passeios turísticos, aluguel de veículos, entre outros, com controle individual de valor e descrição.

### 🔍 Funcionalidades implementadas:
- Cadastro de clientes (nacionais e estrangeiros)
- Cadastro de pacotes de viagem
- Registro de pedidos (cliente + pacote)
- Inclusão de serviços adicionais em pedidos
- Relatórios:
  - Pacotes contratados por cliente
  - Clientes que contrataram determinado pacote

### 🛠️ Tecnologias utilizadas:
- Linguagem: Java
- Banco de Dados: MySQL
- Conexão: JDBC
- Organização do código: DAO, Model, View (menus via terminal)

## 🛠️ Como rodar o projeto

### 1. Pré-requisitos

- Java JDK 11 ou superior
- MySQL instalado e rodando
- IntelliJ IDEA (ou outra IDE compatível com Maven)
- Git (opcional, mas recomendado)

### 2. Clonar o projeto
```
git clone https://github.com/analuly25/ProjetoJava.git cd ProjetoJava 
```
Onde colocar: No terminal da nova máquina, dentro da pasta onde você quer clonar o repositório.

### 3. Abrir no IntelliJ IDEA
Abra o IntelliJ.

Vá em File > Open e selecione a pasta onde está o projeto.

O IntelliJ reconhecerá automaticamente o pom.xml como projeto Maven.

Caso não reconheça, clique com o botão direito no pom.xml > Add as Maven Project.

### 4. Configurar o banco de dados
Crie o banco de dados no MySQL com o nome usado no código (agenciaviagens).

Execute o script SQL localizado na pasta Banco de dados

Altere as credenciais de conexão no código, se necessário:

```
String url = "jdbc:mysql://localhost:3306/agenciaviagens";
String usuario = "root";
String senha = "sua_senha";
```
###5. Executar
Vá até a classe com o método main() (ex: Main.java), utilize a MainGUI.java para a opção com interface e execute o programa.
