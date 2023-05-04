# mini-autorizador
Este é um projeto que consiste em uma aplicação Spring Boot que simula um autorizador de transações.

## Dependências do ambiente
JDK 17

[Apache Maven 3.6+](https://maven.apache.org/download.cgi)

## Utilização

A Interface do Mini-autorizador é totalmente REST, com as seguintes operações disponíveis.

### Criação de Cartões

Para criar um novo cartão, envie uma requisição POST para o endpoint "/cartões" com um payload JSON contendo o número do cartao e a senha desejada.

```JSON
POST /cartoes
  {
    "numeroCartao":"132456789",
    "senha":"senha"
  }
```

A resposta da requisição será um payload JSON contendo os dados do cartão criado ou uma mensagem de erro informando que o cartão já existe e os dados do cartão.

### Obtenção do Saldo

Para obter o saldo atual de um cartão, envie uma requisição GET para o endpoint `/cartoes/{numeroCartao}`, substituindo {numeroCartao} pelo número do cartão desejado.

A resposta da requisição será um payload JSON contendo o número do cartão e o saldo atual.

### Autorização de Transações

Para autorizar uma transação, envie uma requisição POST para o endpoint `/transacoes` com um payload JSON contendo o número do cartão, o valor da transação e a descrição:

```JSON
POST /transacoes
  {
    "numeroCartao":"132456789",
    "senhaCartao":"senha",
    "valor": 50.00
  }
```

A resposta da requisição será um payload JSON contendo o status da transação, que pode ser "OK" se a transação foi autorizada, ou uma mensagem de erro indicando o motivo pelo qual a transação foi recusada.

## Referências das principais tecnologias utilizadas na construção desta API
[SpringBoot 3+](https://spring.io/projects/spring-boot)

[SpringData](https://spring.io/projects/spring-data)

[Lombok 1.18+](https://projectlombok.org/features/all)

[modelmapper 3+](https://modelmapper.org/)