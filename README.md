# Webclient vs RestTemplate

Este projeto visa comparar duas formas de disponibilizar um serviço.

Na primeira situação, é utilizado `RestTemplate` para recuperar os dados por uma requisição get.

No segundo caso, é utilizado `WebFlux` da stack reativa de Spring para recuperação dos dados.

Para construção, criamos um método que demora 10 segundos antes de retornar os dados.
Segue abaixo:

```Java
    @GetMapping("/slow-service")
    private List<Pessoa> getAllTweets() throws Exception{
        Thread.sleep(10000L); // delay
        return Arrays.asList(
                new Pessoa("Fulano"),
                new Pessoa("Beltrano"),
                new Pessoa("Ciclano")
        );
    }
```

Esse método servirá como base para uma requisição que demora alguns segundos.

Os outros dois métodos (um utilizando `restTemplate` e outro `WebClient`) irão consumir dados desse método.

Ao executar a aplicação, montar duas requisições e ver o comportamento no log.  
Segue as requisições que podem ser montadas em um client como o Postman.

Requisição correspondente ao `restTemplate`:
```
http://localhost:8080/pessoas-blocking
```

Requisição correspondente ao `WebClient`:
```
http://localhost:8080/pessoas-non-blocking
```