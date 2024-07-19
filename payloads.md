# Exemplos de payload

## Adicionar um produto ao carrinho

Montar uma lista passando sku (pode ser consultado nas APIs de Produtos) e quantidade

    {
        "reservations": [
            {
                "sku": "CLOTHES-SOME-JEANS-JEANS-BRAND-YELLOW-P",
                "quantity": "5"
            }
        ]
    }

## Alterar carrinho

Montar conforme id de carrinho existente e lista de produtos que já estão no carrinho, podendo alterar a quantidade de cada produto

    {
        "id": "123456",
        "productReservation": [
            "productsDetails":{
                "sku": "CLOTHES-SOME-JEANS-JEANS-BRAND-YELLOW-P",
                "quantity": "0"
            },
            "productsDetails":{
                "sku": "CLOTHES-SOME-JEANS-JEANS-BRAND-BLUE-P",
                "quantity": "10"
            }
        ]
    }