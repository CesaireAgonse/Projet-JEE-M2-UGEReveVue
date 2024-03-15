package fr.uge.revevue.service;

import fr.uge.revevue.form.UnitTestClassForm;
import fr.uge.revevue.information.UnitTestResultInformation;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientService {

    public static UnitTestResultInformation microServiceExecute(UnitTestClassForm unitTestClassForm){
        WebClient webClient = WebClient.create();
        return webClient.post()
                .uri("http://localhost:8082/api/v1/execute")
                .bodyValue(unitTestClassForm)
                .retrieve().bodyToMono(UnitTestResultInformation.class)
                .block();
    }
}
