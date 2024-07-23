# Exemplos de payload

## Adicionar um produto ao carrinho

Montar uma lista passando sku (pode ser consultado nas APIs de Produtos) e quantidade

    {
        "reservations": [
            {
                "sku": "CLOTHES-CINTURA-BAIXA-ADIDAS-AMARELO-M",
                "quantity": 5
            },
            {
                "sku": "BOOKS-978-0-596-52068-7",
                "quantity": 5
            }
        ]
    }

## Alterar carrinho

Montar conforme id de carrinho existente e lista de produtos que já estão no carrinho, podendo alterar a quantidade de cada produto. Enviar "quantity": 0 para cancelar a reserva do produto

    {
        "id": "669ef502f23c2759a1fe27a3",
        "productReservation": [
            {
                "productDetails": {
                    "sku": "CLOTHES-CINTURA-BAIXA-ADIDAS-AMARELO-M",
                    "quantity": 5
                }
            },
            {
                "productDetails": {
                    "sku": "BOOKS-978-0-596-52068-7",
                    "quantity": 0
                }
            }
        ]
    }