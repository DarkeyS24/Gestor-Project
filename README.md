# Gestor de Clientes

API REST para gerenciamento de clientes pessoas físicas e jurídicas, com validação de CPF/CNPJ, cache e tratamento de erros centralizado.

## Tecnologias

- **Java 25**
- **Spring Boot 4.0.5**
  - Spring Web MVC
  - Spring Data JPA
  - Spring Cache
  - Spring Validation
  - Spring DevTools
- **Banco de dados:** H2 (in-memory)
- **Build:** Maven

## Estrutura do projeto

```
src/
└── main/
    └── java/br/com/drky/gestor/
        ├── controller/       # Endpoints REST
        ├── service/          # Regras de negócio e validação de CPF/CNPJ
        ├── repository/       # Acesso ao banco via JPA
        ├── model/            # Entidade Cliente e enum TipoCliente
        ├── dto/              # DTOs de request e response
        ├── exception/        # Exceções de domínio
        └── validation/       # Handler global de erros
```

## Endpoints

Base URL: `http://localhost:8080`

| Método | Rota                    | Descrição                  |
|--------|-------------------------|----------------------------|
| GET    | `/clientes`             | Lista todos os clientes     |
| GET    | `/clientes/findById/{id}` | Busca cliente por ID      |
| POST   | `/clientes`             | Cadastra novo cliente       |
| PUT    | `/clientes/{id}`        | Atualiza telefone ou e-mail |
| DELETE | `/clientes/{id}`        | Remove cliente por ID       |

## Exemplos de uso

### Cadastrar cliente (pessoa física)

```http
POST /clientes
Content-Type: application/json

{
  "nome": "João Silva",
  "tipo": "FISICO",
  "cpfCnpj": "529.982.247-25",
  "telefone": "34999999999",
  "email": "joao@email.com"
}
```

### Cadastrar cliente (pessoa jurídica)

```http
POST /clientes
Content-Type: application/json

{
  "nome": "Empresa LTDA",
  "tipo": "JURIDICO",
  "cpfCnpj": "11.222.333/0001-81",
  "telefone": "34999999999",
  "email": "contato@empresa.com"
}
```

### Atualizar cliente

```http
PUT /clientes/1
Content-Type: application/json

{
  "telefone": "34988888888",
  "email": "novo@email.com"
}
```

## Regras de negócio

- O campo `tipo` aceita apenas `FISICO` ou `JURIDICO` (case-insensitive).
- O CPF/CNPJ é validado matematicamente antes do cadastro.
- Não é permitido cadastrar dois clientes com o mesmo CPF ou CNPJ.
- O campo `email` é opcional no cadastro.
- Na atualização, apenas `telefone` e `email` podem ser alterados.

## Respostas de erro

| Situação                        | Status HTTP | Mensagem                        |
|---------------------------------|-------------|---------------------------------|
| Campo obrigatório ausente        | `400`       | Lista de campos inválidos       |
| Tipo de cliente inválido         | `400`       | `"Tipo de cliente invalido"`    |
| CPF inválido                    | `400`       | `"CPF inválido"`                |
| CNPJ inválido                   | `400`       | `"CNPJ inválido"`               |
| CPF/CNPJ já cadastrado          | `400`       | `"CPF já cadastrado"` / `"CNPJ já cadastrado"` |
| Cliente não encontrado          | `404`       | `"Cliente não encontrado"`      |

## Cache

Os endpoints de leitura utilizam Spring Cache com a chave `todosOsClientes`. O cache é invalidado automaticamente nas operações de escrita (POST, PUT, DELETE).

## Como executar

### Pré-requisitos

- Java 25+
- Maven 3.x

### Rodando a aplicação

```bash
# Clone o repositório
git clone https://github.com/DarkeyS24/Gestor-Project.git
cd gestor

# Execute com Maven Wrapper
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

### Rodando os testes

```bash
./mvnw test
```

Os testes cobrem `ClienteController` e `ClienteService`.
