package com.lisnalinchy.integrationtest.soapui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.support.PropertiesMap;
import com.eviware.soapui.model.testsuite.TestCase;
import com.eviware.soapui.model.testsuite.TestRunner;
import com.eviware.soapui.model.testsuite.TestSuite;
import com.eviware.soapui.support.SoapUIException;

import static com.lisnalinchy.integrationtest.soapui.SetupHelper.deriveProjectAddress;
import static com.lisnalinchy.integrationtest.soapui.SetupHelper.init;

@RunWith(Parameterized.class)
public class SoapUiTestCaseRunner {

    private static Logger logger = LoggerFactory.getLogger(SoapUiTestCaseRunner.class);
    private TestCase testCase;

    @Parameters(name="{1}")
    public static Collection<Object[]> getTestSuites() throws Exception {
        
        init();
        List<TestSuite> testSuiteList = getTestSuitesFromProject();
        Collection<Object[]> params = new ArrayList<Object[]>();

        if (testSuiteList.isEmpty()) {
            throw new Exception("No Test Suites found in project");
        }
        
        for (TestSuite testSuite : testSuiteList) {
            for (TestCase testCase : testSuite.getTestCaseList()) {
                params.add(new Object[] { testCase, testCase.getLabel() });
            }
        }
        return params;
    }

    private static List<TestSuite> getTestSuitesFromProject() throws XmlException, IOException, SoapUIException {
        WsdlProject project = new WsdlProject(deriveProjectAddress());
        List<TestSuite> testSuiteList = project.getTestSuiteList();
        logger.debug("getTestSuitesFromProject() found {} testSuite", testSuiteList.size());
        return testSuiteList;
    }

    @Test
    public void doTest() {
        Presenter.printTestPreamble(testCase);
        Presenter.logRequestDetails(testCase);
        TestRunner testRunner = testCase.run(new PropertiesMap(), false);
        Presenter.logResponseContent(testCase); // would be nice to see of SoapUi can give us this directly from the testCase?
        Assert.assertEquals(TestRunner.Status.FINISHED, testRunner.getStatus());
    }

    public SoapUiTestCaseRunner(TestCase testCase, String label) {
        this.testCase = testCase;
    }

}
