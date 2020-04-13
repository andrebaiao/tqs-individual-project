# tqs-individual-project
Develop a multi-layer web application, in Spring Boot, supplied with automated tests.

# Third-Party APIs Documentation
[Breezometer](https://docs.breezometer.com/api-documentation/air-quality-api/v2/#examples)

[Air Visual](https://api-docs.airvisual.com/?version=latest)

# Endpoints
|     |              Rest-Endpoints             |                                        Output                                       | Description                                                                                                                                                                                                                                                        |
|-----|:---------------------------------------:|:-----------------------------------------------------------------------------------:|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| GET | /api/getCity?city_name={nome da região} |                                         City                                        | Retorna um objecto City em json com os valores da região: índice da qualidade do ar, categoria e poluente dominante.                                                                                                                                               |
| GET |              /api/getCities             |                                        City[]                                       | Retorna um array em json com todos os objectos City que estejam guardados na cache.                                                                                                                                                                                |
| GET |              /api/statistics            | { Number of requests: misses+hits, Number of hits: hits, Number of misses: misses } |  Retorna as várias estatísticas da cache. Miss: Quando um utilizador procura por uma região que não esteja na cache. Hit: Quando um utilizador procura por uma região que esteja na cache. As duas variáveis juntas tem como resultado o número de pedidos feitos. |
