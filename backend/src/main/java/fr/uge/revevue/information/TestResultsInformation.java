package fr.uge.revevue.information;

import fr.uge.revevue.entity.TestResults;

import java.util.Objects;

public record TestResultsInformation(long testsTotalCount, long testsSucceededCount, long testsFailedCount, long testsTotalTime) {

    public static TestResultsInformation from(TestResults testResults){
        if (testResults == null){
            return null;
        }
        return new TestResultsInformation(
                testResults.getTestsTotalCount(),
                testResults.getTestsSucceededCount(),
                testResults.getTestsFailedCount(),
                testResults.getTestsTotalTime()
        );
    }
}
