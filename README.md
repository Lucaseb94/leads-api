# Leads API

API RESTful para gerenciamento inteligente de leads, construída com Java Spring Boot e Docker.

---

## 📋 Sumário

* [🔍 Descrição](#-descrição)
* [✨ Funcionalidades](#-funcionalidades)
* [🛠️ Tecnologias](#️-tecnologias)
* [📦 Pré-requisitos](#-pré-requisitos)
* [🚀 Instalação](#-instalação)
* [⚙️ Configuração](#️-configuração)
* [🎬 Uso](#-uso)

  * [Endpoints](#endpoints)
* [📚 Documentação da API](#-documentação-da-api)
* [🧪 Testes](#-testes)
* [🤝 Contribuindo](#-contribuindo)
* [📄 Licença](#-licença)

---

## 🔍 Descrição

A **Leads API** é uma solução backend desenvolvida com **Java Spring Boot**, voltada para a **transformação digital na captação e qualificação de leads jurídicos**. Com foco em automação, produtividade e inteligência comercial, a API entrega uma experiência completa para registro de usuários, autenticação segura via JWT, comunicação automática por e-mail, dashboard de métricas em tempo real e filtros inteligentes por região e área de especialização.

Ideal para projetos de legaltechs, escritórios de advocacia, departamentos jurídicos e profissionais autônomos, a Leads API centraliza o processo de prospecção e relacionamento com leads em uma interface simples, segura e preparada para ambientes em contêineres com Docker e PostgreSQL.

## ✨ Funcionalidades

* Registro e autenticação de usuários (JWT)
* Recuperação de senha (token via e-mail)
* CRUD de leads
* Filtragem de leads por região e área de interesse
* Envio de e-mails automatizado
* Dashboard com métricas gerais
* Inicialização de base via `init.sql`
* Deploy leve com Docker e Docker Compose

## 🛠️ Tecnologias

* **Java 17**
* **Spring Boot 3** (Web, Data JPA, Security, Mail)
* **Maven**
* **PostgreSQL** (via Docker)
* **JWT** para autenticação
* **Docker & Docker Compose**

## 📦 Pré-requisitos

* Java 17+ instalado
* Maven 3.6+ instalado
* Docker e Docker Compose instalados

## 🚀 Instalação

1. Clone este repositório:

   ```bash
   git clone https://github.com/Lucaseb94/leads-api.git
   cd leads-api
   ```

2. Configure as variáveis de ambiente (veja [⚙️ Configuração](#️-configuração)).

3. Inicie a API e o banco de dados:

   ```bash
   docker compose up --build
   ```

A API estará disponível em `http://localhost:8080` ou, se em produção, pelo endereço definido (por exemplo, `https://leads-api-gigb.onrender.com`).

## ⚙️ Configuração

Defina estas variáveis de ambiente no seu `.env` , na raiz do projeto:

| Variável        | Descrição                     | Exemplo                |
| --------------- | ----------------------------- | ---------------------- |
| `DB_HOST`       | Host do PostgreSQL            | `localhost`            |
| `DB_PORT`       | Porta do PostgreSQL           | `5432`                 |
| `DB_NAME`       | Nome do banco de dados        | `leads_db`             |
| `DB_USERNAME`   | Usuário do banco              | `postgres`             |
| `DB_PASSWORD`   | Senha do banco                | `postgres`             |
| `JWT_SECRET`    | Chave secreta para tokens JWT | `sua_chave_secreta`    |
| `MAIL_HOST`     | SMTP host                     | `smtp.gmail.com`       |
| `MAIL_PORT`     | Porta SMTP                    | `587`                  |
| `MAIL_USERNAME` | Usuário de e-mail             | `seu.email@gmail.com`  |
| `MAIL_PASSWORD` | Senha/apppass do e-mail       | `******`               |
| `MAIL_FROM`     | Endereço de envio             | `no-reply@empresa.com` |

## 🎬 Uso

### Base URL

* **Local**: `http://localhost:8080`

### Endpoints

#### Autenticação & Usuários

* **POST** `/api/auth/register`

  * Registra novo usuário.
  * Exemplo de Request Body:

    ```json
    {
      "nome": "Fulano da Silva",
      "email": "fulano.silva@email.com",
      "senha": "senhaSegura123",
      "regiao": "Sudeste",
      "especializacao": "TRABALHISTA"
    }
    ```

* **POST** `/api/auth/login`

  * Autentica usuário e retorna JWT.
  * Exemplo de Request Body:

    ```json
    {
      "email": "fulano.silva@email.com",
      "senha": "senhaSegura123"
    }
    ```

* **POST** `/api/auth/forgot-password`

  * Solicita reset de senha e envia token por e-mail.
  * Exemplo de Request Body:

    ```json
    {
      "email": "fulano.silva@email.com"
    }
    ```

* **POST** `/api/auth/reset-password`

  * Redefine senha usando token.
  * Exemplo de Request Body:

    ```json
    {
      "token": "exemplo-token-uuid",
      "novaSenha": "novaSenha123"
    }
    ```

#### Leads

> **Requer token JWT no header**: `Authorization: Bearer <token>`

* **POST** `/api/leads`

  * Cadastra um novo lead.
  * Exemplo de Request Body:

    ```json
    {
      "nome": "Fulano da Silva",
      "email": "fulano.silva@email.com",
      "telefone": "+5511999999999",
      "endereco": "Rua das Apis, 100",
      "regiao": "Nordeste",
      "areaInteresse": "CIVIL"
    }
    ```

* **GET** `/api/leads`

  * Lista todos os leads. Parâmetros opcionais: `regiao`, `areaInteresse`.

* **GET** `/api/leads/{areaInteresse}`

  * Lista leads filtrados por área de interesse.

* **PUT** `/api/leads`

  * Atualiza lead existente.
  * Exemplo de Request Body:

    ```json
    {
      "id": 1,
      "nome": "Fulano da Silva",
      "email": "fulano.silva@email.com",
      "regiao": "Norte",
      "endereco": "Rua das Apis, 100"
    }
    ```

#### Envio de E-mail

> **Requer token JWT**

* **POST** `/api/leads/send`

  * Dispara e-mails para advogados conforme filtros.

#### Dashboard

> **Requer token JWT**

* **GET** `/api/dashboard`

  * Retorna métricas gerais: total de leads, por região e por área.

## 🧪 Testes

> Em breve serão adicionados testes unitários e de integração.

## 🤝 Contribuindo

1. Faça um fork
2. Crie uma branch: `feature/nova-funcionalidade`
3. Commit suas alterações: `git commit -m 'Add feature'`
4. Push para sua branch: `git push origin feature/nova-funcionalidade`
5. Abra um Pull Request

## 📄 Licença

Distribuído sob a licença MIT. Veja [LICENSE](LICENSE) para mais detalhes.

---

*Desenvolvido por Lucas Miranda*
