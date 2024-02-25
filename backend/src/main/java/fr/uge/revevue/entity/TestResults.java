package fr.uge.revevue.entity;


import javax.persistence.*;

@Embeddable
public class TestResults {

    private long testsTotalCount;
    private long testsSucceededCount;
    private long testsFailedCount;
    private long testsTotalTime;
    //private Map<String, String> failures;

    public TestResults() {}

    public TestResults(long testsTotalCount, long testsSucceededCount, long testsFailedCount, long testsTotalTime) {
        this.testsTotalCount = testsTotalCount;
        this.testsSucceededCount = testsSucceededCount;
        this.testsFailedCount = testsFailedCount;
        this.testsTotalTime = testsTotalTime;
        //this.failures = failures;
    }

    public long getTestsTotalCount() {
        return testsTotalCount;
    }

    public void setTestsTotalCount(long testsTotalCount) {
        this.testsTotalCount = testsTotalCount;
    }

    public long getTestsSucceededCount() {
        return testsSucceededCount;
    }

    public void setTestsSucceededCount(long testsSucceededCount) {
        this.testsSucceededCount = testsSucceededCount;
    }

    public long getTestsFailedCount() {
        return testsFailedCount;
    }

    public void setTestsFailedCount(long testsFailedCount) {
        this.testsFailedCount = testsFailedCount;
    }

    public long getTestsTotalTime() {
        return testsTotalTime;
    }

    public void setTestsTotalTime(long testsTotalTime) {
        this.testsTotalTime = testsTotalTime;
    }


//    public Map<String, String> getFailures() {
//        return failures;
//    }
//
//    public void setFailures(Map<String, String> failures) {
//        this.failures = failures;
//    }
}
