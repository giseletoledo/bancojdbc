# Projeto Final Parte 2 - Banco Digital(API)

*Não é necessário o envio da solução deste exercício
**Não é necessário a criação do fluxograma
***Prazo de 2 a 3 semanas

## Como rodar o projeto

**Clone o repositório**
git clone https://github.com/seu-usuario/banco-digital-jdbc.git

## Importe como projeto Maven no Eclipse/IntelliJ
## Ou use:
./mvnw spring-boot:run

O banco H2 é reiniciado a cada execução. Nenhum dado é persistido.

📄 Tabelas SQL (H2)
sql
-- clientes, enderecos, contas, transacoes, cartoes, cartao_transacoes
As tabelas são criadas automaticamente ao iniciar o sistema.




A partir da descrição do projeto anterior, crie uma API para Banco Digital com os
seguintes endpoints:
### Endpoints da API Banco Digital
#### Cliente
- **POST /clientes** - Criar um novo cliente
- **GET /clientes/{id}** - Obter detalhes de um cliente
- **PUT /clientes/{id}** - Atualizar informações de um cliente
- **DELETE /clientes/{id}** - Remover um cliente
- **GET /clientes** - Listar todos os clientes
  
#### Conta
- **POST /contas** - Criar uma nova conta
- **GET /contas/{id}** - Obter detalhes de uma conta
- **POST /contas/{id}/transferencia** - Realizar uma transferência entre contas
- **GET /contas/{id}/saldo** - Consultar saldo da conta
- **POST /contas/{id}/pix** - Realizar um pagamento via Pix
- **POST /contas/{id}/deposito** - Realizar um depósito na conta
- **POST /contas/{id}/saque** - Realizar um saque
- **PUT /contas/{id}/manutencao** - Aplicar taxa de manutenção mensal (para conta
corrente)
- **PUT /contas/{id}/rendimentos** - Aplicar rendimentos (para conta poupança)


#### Cartão
- **POST /cartoes** - Emitir um novo cartão
- **GET /cartoes/{id}** - Obter detalhes de um cartão
- **POST /cartoes/{id}/pagamento** - Realizar um pagamento com o cartão
- **PUT /cartoes/{id}/limite** - Alterar limite do cartão
- **PUT /cartoes/{id}/status** - Ativar ou desativar um cartão
- **PUT /cartoes/{id}/senha** - Alterar senha do cartão
- **GET /cartoes/{id}/fatura** - Consultar fatura do cartão de crédito
- **POST /cartoes/{id}/fatura/pagamento** - Realizar pagamento da fatura do cartão
de crédito
- **PUT /cartoes/{id}/limite-diario** - Alterar limite diário do cartão de débito
