package fr.uge.microservice.information;

import java.util.Map;

public record UnitTestResultInformation(
        long testsTotalCount,
        long testsSucceededCount,
        long testsFailedCount,
        long testsTotalTime,
        Map<String, String> failures
) {}
