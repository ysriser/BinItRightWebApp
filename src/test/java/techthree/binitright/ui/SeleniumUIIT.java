package techthree.binitright.ui;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        Login.class,
        Dashboard.class,
        CheckInReview.class,
        SustainabilityReport.class,
        IssueManagement.class
})
public class SeleniumUIIT {
}