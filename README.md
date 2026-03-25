# Pagamentos

Aplicacao full stack simples para simular um fluxo de carteira digital, inspirada em um cenário de teste tecnico.

O projeto permite:
- cadastrar carteiras do tipo `USER` e `MERCHANT`
- listar carteiras com saldo
- realizar transferencias entre carteiras
- bloquear transferencias feitas por `MERCHANT`
- validar saldo disponivel
- consultar autorizacao externa antes de concluir a transferencia
- enviar notificacao externa apos a transacao
- testar tudo por uma interface web servida pelo proprio backend

## Demo funcional

Depois de subir a aplicacao, a interface fica disponivel em:

```text
http://localhost:8080/
```

APIs disponiveis:
- `GET /wallets`
- `POST /wallets`
- `GET /transactions`
- `POST /transactions`

## Tecnologias utilizadas

### Backend
- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Hibernate
- OpenFeign
- MySQL
- Maven

### Frontend
- HTML
- CSS
- JavaScript puro

### Testes
- JUnit 5

## Regras de negocio

- Apenas carteiras do tipo `USER` podem enviar dinheiro
- `MERCHANT` pode apenas receber transferencias
- O pagador precisa ter saldo suficiente
- O pagador e o recebedor precisam ser carteiras diferentes
- A transacao consulta um servico externo de autorizacao antes de concluir
- A notificacao externa e tentada apos a transferencia
- Falha de notificacao nao desfaz a transacao ja autorizada

## Estrutura do projeto

```text
pagamentos
├── docker
│   └── docker-compose.yml
├── src
│   ├── main
│   │   ├── java/com/leonardojpy/pagamentos
│   │   │   ├── client
│   │   │   ├── config
│   │   │   ├── controller
│   │   │   ├── entity
│   │   │   ├── exception
│   │   │   ├── repository
│   │   │   └── service
│   │   └── resources
│   │       ├── application.properties
│   │       └── static
│   └── test
│       └── java/com/leonardojpy/pagamentos
├── pom.xml
└── README.md
```

## Como executar o projeto

### 1. Pre-requisitos

Antes de iniciar, tenha instalado:
- Java 21 ou superior
- Maven ou use o wrapper `mvnw`
- Docker Desktop, se quiser subir o MySQL via container

### 2. Subir o banco de dados

O jeito mais simples e recomendado para rodar localmente e:

```powershell
docker compose up -d
```

Isso sobe um MySQL com:
- banco: `picpaydb`
- usuario: `admin`
- senha: `123`

### 3. Configurar o Java no PowerShell

Se necessario, configure a sessao com:

```powershell
$env:JAVA_HOME="C:\Program Files\Java\jdk-23"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
```

Importante:
- `JAVA_HOME` deve apontar para a raiz do JDK
- nao deve apontar para a pasta `bin`

### 4. Rodar a aplicacao

Na raiz do projeto:

```powershell
.\mvnw.cmd spring-boot:run
```

Se estiver usando o Maven instalado na maquina:

```powershell
mvn spring-boot:run
```

### 5. Acessar a interface

Abra no navegador:

```text
http://localhost:8080/
```

## Como testar

### Pela interface web

Fluxo sugerido:

1. Criar uma carteira `USER` com saldo inicial
2. Criar uma segunda carteira `USER` ou `MERCHANT`
3. Realizar uma transferencia
4. Conferir a atualizacao de saldo e o historico de transacoes

### Validacoes interessantes para testar

- tentar transferir usando uma carteira `MERCHANT` como pagador
- tentar transferir um valor maior do que o saldo
- tentar cadastrar duas carteiras com o mesmo email
- tentar cadastrar duas carteiras com o mesmo CPF/CNPJ

### Pela API

Exemplo de criacao de carteira:

```json
{
  "fullName": "Maria da Silva",
  "cpfCnpj": "12345678900",
  "email": "maria@email.com",
  "password": "123456",
  "walletType": "USER",
  "balance": 100.00
}
```

Exemplo de transferencia:

```json
{
  "payer": 1,
  "payee": 2,
  "value": 10.00
}
```

## Rodando os testes

```powershell
.\mvnw.cmd test
```

Os testes atuais cobrem:
- criacao de carteiras
- regras principais de transferencia
- autorizacao externa
- comportamento em caso de falha de notificacao

## Integracoes externas

A aplicacao utiliza servicos externos configurados em:

`src/main/resources/application.properties`

Propriedades relevantes:
- `clients.authorization.url`
- `clients.notification.url`

## Melhorias futuras

- autenticacao de usuarios
- uso de DTOs de erro mais completos no frontend
- filtros e ordenacao na interface
- paginacao para carteiras e transacoes
- perfil de ambiente para banco em memoria em modo local
- conteinerizacao completa da aplicacao

## Autor

Projeto desenvolvido como estudo e teste tecnico por Leonardo, com suporte de implementacao e refinamento durante a construcao.
