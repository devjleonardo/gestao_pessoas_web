# Gestão de Pessoas Web

Este projeto foi desenvolvido utilizando o framework Spring Boot com o propósito de realizar a gestão de pessoas. Por meio de uma API, que posteriormente foi integrada com JSF e o PrimeFaces, proporciona uma experiência de usuário dinâmica e interativa. A estrutura do projeto está organizada em diferentes pacotes, representando módulos específicos.

# Gestão de Pessoas Web

Esta aplicação web foi desenvolvida utilizando o framework Spring Boot, com o propósito de realizar a gestão de pessoas. Seguindo uma arquitetura modular, busca facilitar a manutenção e escalabilidade. A estrutura do projeto está organizada em diferentes pacotes, representando módulos específicos.

## Pré-requisitos

Certifique-se de ter o Docker e o Docker Compose instalados na sua máquina.

- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/)

## Instruções de Uso

### 1. Clone o repositório:

```bash
git clone https://github.com/devjleonardo/gestao_pessoas_web.git
cd gestao_pessoas_web
```

### 2. Iniciar os Serviços com Docker Compose e Construir as Imagens

```bash
docker-compose up -d --build
```

### 3. Listar Containers em Execução

```bash
docker ps
```

Este comando exibe uma lista dos containers em execução, com informações como ID, nomes, portas expostas, etc.

### 4. Verificar Logs do Container

```bash
docker logs nome-do-container
```
Substitua `"nome-do-container"` pelo nome real do seu container, que você pode verificar usando docker ps.

### Acesso

Sua aplicação estará disponível em http://localhost:8080.

O banco de dados PostgreSQL estará disponível em `localhost:5432`.

Acesse o [dashboard principal](http://localhost:8080/gestao-pessoas-web/index.xhtml) do sistema.

Acessa a [Documentação da API](http://localhost:8080/swagger-ui.html).

### Notas Adicionais

- Certifique-se de ajustar os nomes das imagens e containers de acordo com sua preferência e configuração.

- Este README assume que você está no mesmo diretório que o arquivo docker-compose.yml.

- Verifique e ajuste as configurações no arquivo docker-compose.yml conforme necessário para o seu projeto.


## Visão Geral da Arquitetura

### Dependências

- **Spring Boot (v2.7.1):** Framework principal para desenvolvimento Java baseado em padrões de convenção.

- **Spring Boot DevTools:** Ferramentas de desenvolvimento adicionais para Spring Boot, auxiliando na reinicialização automática do aplicativo durante o desenvolvimento.
- **Spring Boot Starter Data JPA:** Starter para usar o Spring Data JPA com Hibernate.

- **PostgreSQL (v42.2.5):** Driver JDBC para PostgreSQL, utilizado em conjunto com o Spring Data JPA.

- **Spring Boot Starter Test:** Starter para testes no Spring Boot.

- **JUnit (v4.13.2):** Framework de testes para Java.

- **Mockito Core:** Framework de mocking para testes.

- **Spring Data Commons:** Componentes comuns para projetos Spring Data.

- **ODFDOM (v0.12.0):** Biblioteca para manipulação de documentos ODF (Open Document Format).

- **JavaFaker (v1.0.2):** Biblioteca para geração de dados fictícios em Java.

- **JavaServer Faces (JSF) API (v2.2.20) e JSF Implementation (v2.2.20):** API e implementação do JavaServer Faces.

- **PrimeFaces (v10.0.0):** Biblioteca de componentes para JSF.

- **Gson (v2.8.9):** Biblioteca para serialização e desserialização de objetos JSON em Java.

- **Tomcat Embed Jasper:** Implementação do Jasper (JSP engine) para Tomcat Embed.

- **Springdoc OpenAPI UI (v1.6.9):** Biblioteca para geração automática de documentação OpenAPI com interface de usuário interativa.

- **Flyway Core (v7.3.1):** Biblioteca para migração de banco de dados.

### Decisões de Design

- **Spring Boot:** Escolhido devido à sua simplicidade e convenções, facilitando o desenvolvimento de aplicativos Java.

- **Spring Data JPA:** Para simplificar a camada de acesso a dados e eliminar a necessidade de escrever consultas SQL manualmente.

- **PostgreSQL:** Escolhido como o banco de dados devido ao suporte ao Spring Boot e às suas características robustas.

- **JavaServer Faces e PrimeFaces:** Utilizados para criar uma interface do usuário rica e interativa.

- **Flyway:** Adotado para gerenciar as migrações do banco de dados de forma controlada.

- **Springdoc:** Introduzido para gerar documentação OpenAPI automaticamente, mantendo a API documentada.

- **Outras Dependências:** Cada dependência foi escolhida com base em sua capacidade de agregar valor ao projeto, contribuindo para a eficiência do desenvolvimento, qualidade do código e experiência do usuário.
