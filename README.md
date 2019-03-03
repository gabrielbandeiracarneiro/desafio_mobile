# Desafio Conductor de Seleção

Projeto desenvolvido por Gabriel Bandeira Carneiro para a vaga de Desenvolvedor Mobile da Conductor.

## Execução do projeto

Para executar o projeto basta instalar os APK's que estão dentro da pasta "apk", para rodar o build precisa alterar o "Build Variants" para regular(BlueCard) ou demo(GreenCard).

## Funcionalidades

Conforme solicitado o projeto foi criado na liguagem nativa (Android), com funções para visualizar extrato por mês, gráfico de compra, aleteração de cor de acordo com a versão da aplicação.

## Como foi feito

A aplicação foi feita no Android Stuido, utilizando o Retrofit para fazer as requisições para o Web Service, Graph View para montagem do gráfico.

## Observações

1. No cartão existe um campo "Gastos", mas não estava especificado de onde seria retirado essa informação, então coloquei a soma do resultado da chamada "/dev/card-usage" sendo todo o valor gasto do cartão.

2. Na chamada "/dev/card-usage" não existe o "ano" de referência, isso deixou o gráfico incompleto.
