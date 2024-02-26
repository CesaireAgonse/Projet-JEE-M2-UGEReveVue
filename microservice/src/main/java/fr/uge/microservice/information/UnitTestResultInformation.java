package fr.uge.microservice.information;

import java.util.List;

public record UnitTestResultInformation(
        long testsTotalCount,
        long testsSucceededCount,
        long testsFailedCount,
        long testsTotalTime,
        String failures
) {}
