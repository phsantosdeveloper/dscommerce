# Desafio Consulta de Vendas

API REST desenvolvida com Java e Spring Boot para consultar vendas, gerar relatórios paginados e calcular o total vendido por vendedor.

## Tecnologias

- Java 25
- Spring Boot 4.1.0
- Spring Data JPA
- H2 Database
- Maven

## Funcionalidades

- Consulta de venda por ID.
- Relatório paginado com filtros por período e nome do vendedor.
- Resumo do total vendido por vendedor.
- Banco em memória com dados iniciais.

## Como executar

Requisitos: JDK 25 e Git.

```bash
git clone https://github.com/phsantosdeveloper/desafio-consulta-vendas.git
cd desafio-consulta-vendas
```

**Windows (PowerShell):**

```powershell
.\mvnw.cmd spring-boot:run
```

**Linux / macOS:**

```bash
./mvnw spring-boot:run
```

API disponível em `http://localhost:8080`.

## Endpoints

| Método | Rota | Descrição |
| --- | --- | --- |
| GET | `/sales/{id}` | Consulta uma venda |
| GET | `/sales/report` | Relatório paginado |
| GET | `/sales/summary` | Total vendido por vendedor |

### Exemplo de consulta

```http
GET /sales/report?minDate=2026-06-01&maxDate=2026-06-30&name=odinson&page=0&size=10&sort=date,desc
```

### Parâmetros opcionais

- `minDate`: data inicial no formato `yyyy-MM-dd`.
- `maxDate`: data final no formato `yyyy-MM-dd`.
- `name`: trecho do nome do vendedor, sem diferenciar maiúsculas de minúsculas.
- `page`: número da página, começando em `0`.
- `size`: quantidade de registros por página.
- `sort`: ordenação, como `date,desc`.

O resumo aceita apenas `minDate` e `maxDate`.

Quando ausente ou em branco, a data final será a data atual do servidor, e a inicial será um ano antes da data final utilizada. O intervalo inclui ambas as datas.

## Banco H2

Console disponível em `http://localhost:8080/h2-console`.

- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Usuário:** `sa`
- **Senha:** deixar em branco.

Os dados iniciais são carregados pelo arquivo `import.sql`. O banco é temporário e não mantém alterações após encerrar a aplicação.

## Autor

[Pedro Henrique](https://github.com/phsantosdeveloper)
