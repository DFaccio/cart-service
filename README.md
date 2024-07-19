# Serviço de gerenciamento de carrinho de compras - Desafio Tech Challenge - Módulo 5

### Este repositório refere-se ao microsserviço de carrinho de compras. No total, o projeto envolve 4 microsserviços, sendo eles:

1. Usuário e autenticação
2. Produtos
3. **Carrinho** *(este)* 
4. Pagamento

### Este microsserviço é responsável pelo processamento de carrinho de compras. Isto inclui:

* Criar carrinho
* Adicionar itens ao carrinho
* Alterar carrinho
* Confirmar carrinho
* Cancelar carrinho

### Tecnologias

* Spring Boot para a estrutura do serviço
* MongoDB para persistência
* Spring Cloud para comunicação com outros microsserviços
* Spring Security

### [SWAGGER](http://localhost:7077/swagger-ui/index.html#/)

### Desenvolvedores

- [Aydan Amorim](https://github.com/AydanAmorim)
- [Danilo Faccio](https://github.com/DFaccio)
- [Erick Ribeiro](https://github.com/erickmatheusribeiro)
- [Isabela França](https://github.com/fysabelah)

### Instruções

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