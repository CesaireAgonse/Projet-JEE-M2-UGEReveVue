package fr.uge.revevue.entity;


import javax.persistence.*;
import java.util.List;

@Embeddable
public class TestResults {
    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private long testsTotalCount;
    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private long testsSucceededCount;
    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private long testsFailedCount;
    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private long testsTotalTime;
    private String failures;

    public TestResults() {}

    public TestResults(long testsTotalCount, long testsSucceededCount, long testsFailedCount, long testsTotalTime, String failures) {
        this.testsTotalCount = testsTotalCount;
        this.testsSucceededCount = testsSucceededCount;
        this.testsFailedCount = testsFailedCount;
        this.testsTotalTime = testsTotalTime;
        this.failures = failures;
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

    public String getFailures() {
        return failures;
    }

    public void setFailures(String failures) {
        this.failures = failures;
    }
}
