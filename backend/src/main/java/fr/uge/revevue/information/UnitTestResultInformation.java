package fr.uge.revevue.information;

import fr.uge.revevue.entity.TestResults;

public record UnitTestResultInformation(
        long testsTotalCount,
        long testsSucceededCount,
        long testsFailedCount,
        long testsTotalTime,
        String failures
) {
    public static UnitTestResultInformation from(TestResults testResults){
        if (testResults == null){
            return null;
        }
        return new UnitTestResultInformation(
                testResults.getTestsTotalCount(),
                testResults.getTestsSucceededCount(),
                testResults.getTestsFailedCount(),
                testResults.getTestsTotalTime(),
                testResults.getFailures()
        );
    }
}
