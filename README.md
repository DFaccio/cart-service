# Serviço de gerenciamento de carrinho de compras - Desafio Tech Challenge - Módulo 5

### Este repositório refere-se ao microsserviço de carrinho de compras. No total, o projeto envolve 4 microsserviços, sendo eles:

1. Usuário e autenticação
2. Produtos
3. **Carrinho** *(este)* 
4. Pagamento

### Funcionalidades

* Criar carrinho
* Adicionar itens ao carrinho
* Alterar carrinho
* Confirmar carrinho
* Cancelar carrinho

### Funcionamento

* Para adicionar produtos ao carrinho, usar os padrões de payload de exemplo contidos no arquivo payloads.md, na raiz do projeto
    * Caso o usuário ainda não tenha um carrinho criado, o serviço irá criar
    * Caso o usuário já possua um carrinho no status CREATED, o produto será adicionado ao carrinho
        * Se o produto já existir no carrinho, a quantidade do produto recebida na requisição será adicionada
    * Ao adicionar um produto ao carrinho, será verificada a disponibilidade de reserva junto ao serviço de Produtos:
        * Havendo disponibilidade, o produto será incluído no carrinho
        * Não havendo disponibilidade, o produto será retornado como STOCKOUT e não será adicionado ao carrinho


* Na atualização do carrinho, requisição e carrinho serão comparados. Se houver alteração na quantidade de algum produto:
    * Para uma quantidade maior, será verificada disponibilidade de reserva junto ao serviço de Produtos:
        * Havendo disponibilidade, a alteração será realizada
        * Não havendo disponibilidade, a quantidade permanecerá a mesma, com o status STOCKOUT
    * Para uma quantidade menor, o carrinho será alterado e a diferença será devolvido ao estoque via requisição ao serviço de Produtos
    * Para quantidade igual a zero, o produto será cancelado no carrinho, e devolvido ao estoque via requisição ao serviço de Produtos


* O carrinho terá os detalhes de cada produto, como o SKU, nome, preço, quantidade, total e status


* Ao final de qualquer operação de carrinho, o mesmo terá seu valor recalculado de acordo com a quantidade e valor de cada produto


* Na confirmação de um carrinho, o mesmo não poderá mais ser alterado e as reservas serão confirmadas junto ao serviço de Produtos


* No cancelamento de um carrinho, o mesmo não poderá mais ser alterado e as reservas serão canceladas junto ao serviço de Produtos


### Tecnologias

* Spring Boot para a estrutura do serviço
* MongoDB para persistência
* Spring Cloud para comunicação com outros microsserviços

### Como executar

Caso pretenda utilizar o projeto completo, favor fazer uso do README.md do [_ecommerce_](https://github.com/DFaccio/ecommerce-system). 
Agora, para executar apenas este, siga os passos abaixo.

1. Crie um arquivo .env no diretório root. Este deve ter as chaves abaixo.
    Obs.: Este serviço precisa que o serviço de produtos [inventory](https://github.com/fysabelah/spring-batch-products/tree/main) também esteja executando

```
PROFILE=dev

# MongoDB
MONGO_INITDB_ROOT_USERNAME=ecommerce_admin
MONGO_INITDB_ROOT_PASSWORD=adm!n

# MongoDB Express
ME_CONFIG_BASICAUTH_USERNAME=admin
ME_CONFIG_BASICAUTH_PASSWORD=!nterf@ce#

SWAGGER_SERVER_ADDRESS=http://localhost:7075

# Inventory
PRODUCT_ADDRESS=lb://inventory
```

Obs.: Os valores foram setados por fins de praticidade, no entanto, com exceção do PROFILE, todos valores podem ser
alterado como bem entender.

2. De acordo com o perfil, dev e prod, colocado em PROFILE, sugiro comentar a propriedade
   _eureka.client.serviceUrl.defaultZone_ para
   não ficar apresentando erro de conexão com Eureka Server. O comentário pode ser feito adicionando _#_ na frente da
   propriedade.
4. Digite o comando abaixo.

        docker compose up.

### Informações úteis

* O PROFILE, para fins de desenvolvimento sugiro uso do perfil dev. Caso já tenha o mongo configurado, edite as
  informações de conexão como preferir
* Quando este serviço estiver rodando com o projeto completo ele precisará de autenticação.

## Documentação

A documentação pode ser acessada em:

* Formato json:
  [Docker](http://localhost:7071/documentation)
  [Local](http://localhost:7075/documentation)
* Swagger-Ui: 
  [Docker](http://localhost:7071/doc/cart-service.html)
  [Local](http://localhost:7075/doc/swagger-ui/index.html#/)

Obs.: Esta informação considera apenas uso deste serviço. Para acesso a documentação considerando a execução de todos
serviços, favor consultar o README.md do [_ecommerce_](https://github.com/DFaccio/ecommerce-system).