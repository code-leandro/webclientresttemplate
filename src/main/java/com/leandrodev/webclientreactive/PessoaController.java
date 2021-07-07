package com.leandrodev.webclientreactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
public class PessoaController {

    String serverPort = "8080";

    @GetMapping("/slow-service")
    private List<Pessoa> getAllTweets() throws Exception{
        Thread.sleep(10000L); // delay
        return Arrays.asList(
                new Pessoa("Fulano"),
                new Pessoa("Beltrano"),
                new Pessoa("Ciclano")
        );
    }
    
    public String getSlowServiceUri() {
        return "http://localhost:" + serverPort + "/slow-service";
    }

    @GetMapping("/pessoas-blocking")
    public List<Pessoa> getTweetsBlocking() {
        log.info("Starting BLOCKING Controller!");
        final String uri = getSlowServiceUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Pessoa>> response = restTemplate.exchange(
                uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Pessoa>>(){});

        List<Pessoa> result = response.getBody();
        result.forEach(p -> log.info(p.toString()));
        log.info("End BLOCKING Controller!");
        return result;
    }

    @GetMapping(value = "/pessoas-non-blocking",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Pessoa> getTweetsNonBlocking() {
        log.info("Starting NON-BLOCKING Controller!");
        Flux<Pessoa> pessoaFlux = WebClient.create()
                .get()
                .uri(getSlowServiceUri())
                .retrieve()
                .bodyToFlux(Pessoa.class);

        pessoaFlux.subscribe(p -> log.info(p.toString()));
        log.info("End of NON-BLOCKING Controller!");
        return pessoaFlux;
    }
}
