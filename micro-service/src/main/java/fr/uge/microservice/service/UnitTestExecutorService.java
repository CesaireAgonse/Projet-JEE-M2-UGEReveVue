package fr.uge.microservice.service;

import fr.uge.microservice.information.UnitTestResultInformation;
import org.springframework.stereotype.Service;

@Service
public class UnitTestExecutorService {

    public UnitTestResultInformation execute(byte[] javaCode, byte[] unitCode){
        System.out.println("TESTS");
        return new UnitTestResultInformation(1, 1);
    }
}
