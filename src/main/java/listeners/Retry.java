package listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * TestNG retry analyzer that re-runs a failed test up to a fixed number
 * of times before marking it as permanently failed.
 */
public class Retry implements IRetryAnalyzer {
    private int count = 0;
    private static int maxTry = 3;

    /**
     * Determines whether a failed test should be retried.
     *
     * @param iTestResult the result of the test that just ran
     * @return {@code true} if the test should be retried, {@code false} otherwise
     */
    @Override
    public boolean retry(ITestResult iTestResult) {
        if (!iTestResult.isSuccess()) { // Check if test not succeed
            if (count < maxTry) { // Check if maxtry count is reached
                count++; // Increase the maxTry count by 1
                iTestResult.setStatus(ITestResult.FAILURE); // Mark test as failed
                return true; // Tells TestNG to re-run the test
            } else {
                iTestResult.setStatus(ITestResult.FAILURE); // If maxCount reached,test marked as failed
            }
        } else {
            iTestResult.setStatus(ITestResult.SUCCESS); // If test passes, TestNG marks it as passed
        }
        return false;
    }
}
