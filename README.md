# Leads API

API RESTful para gerenciamento de leads desenvolvido em Java Spring Boot.

## 📝 Descrição
Esta aplicação foi criada com a finalidade de:
- Cadastrar leads de clientes.
- Enviar automaticamente por e-mail aos advogados, filtrando por região geográfica ou área de segmento (civil, criminal, trabalhista, etc.).

## 🚀 Tecnologias
- Java 17
- Spring Boot
- Spring Data JPA
- Spring Mail
- Maven
- Docker & Docker Compose
- PostgreSQL (no container via Docker)

## 📦 Pré-requisitos
- Java 17 ou superior instalado
- Maven 3.6+ instalado
- Docker e Docker Compose instalados

## 🔧 Instalação
1. Clone este repositório:
   ```bash
   git clone https://github.com/Lucaseb94/leads-api.git
   cd leads-api
   ```

2. Configure as variáveis de ambiente para conexão com o banco e e-mail:

   | Variável                    | Descrição                                | Exemplo                         |
   |-----------------------------|------------------------------------------|---------------------------------|
   | `DB_HOST`                   | Host do banco de dados                   | `localhost`                     |
   | `DB_PORT`                   | Porta do PostgreSQL                      | `5432`                          |
   | `DB_NAME`                   | Nome do banco                            | `leads_db`                      |
   | `DB_USERNAME`               | Usuário do banco                         | `postgres`                      |
   | `DB_PASSWORD`               | Senha do banco                           | `postgres`                      |
   | `MAIL_HOST`                 | SMTP host                                | `smtp.gmail.com`                |
   | `MAIL_PORT`                 | Porta SMTP                               | `587`                           |
   | `MAIL_USERNAME`             | Usuário de e-mail                        | `seu.email@gmail.com`           |
   | `MAIL_PASSWORD`             | Senha de e-mail (ou App Password)        | `******`                        |
   | `MAIL_FROM`                 | Endereço de envio                        | `no-reply@empresa.com`          |

3. Inicie com Docker Compose:
   ```bash
   docker-compose up --build
   ```
   Isso levantará a API e um container com PostgreSQL, inicializando o banco com o script `init.sql`.

## 🎯 Endpoints Principais

### Leads
- **POST** `/api/leads`
  - Cadastra um novo lead.
  - Body JSON:
    ```json
    {
      "nome": "Fulano de Tal",
      "email": "fulano@email.com",
      "telefone": "11999999999",
      "regiao": "SP",
      "segmento": "civil"
    }
    ```
- **GET** `/api/leads`
  - Retorna todos os leads.
  - Parâmetros de query (opcionais):
    - `regiao` - filtra por região
    - `segmento` - filtra por segmento

### Envio de E-mail
- **POST** `/api/leads/send`
  - Dispara o envio de e-mails aos advogados baseados nos filtros informados.
  - Body JSON (exemplo):
    ```json
    {
      "regiao": "RJ",
      "segmento": "criminal"
    }
    ```

## ⚙️ Configurações Adicionais
- **Agendamento**: o envio automático pode ser agendado via Quartz ou cron (configurar em `application.properties`).
- **Scripts de inicialização**: o arquivo `init.sql` insere advogados e posições geográficas iniciais.

## 📝 Testes
> Em breve serão adicionados testes unitários e integração.

## 🤝 Contribuindo
1. Fork o projeto
2. Crie uma branch `feature/nova-funcionalidade`
3. Commit suas alterações
4. Push para sua fork
5. Abra um Pull Request

## 📝 Licença
Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.
