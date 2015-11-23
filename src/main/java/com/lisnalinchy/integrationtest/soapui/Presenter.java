package com.lisnalinchy.integrationtest.soapui;

import com.eviware.soapui.impl.support.AbstractHttpRequest;
import com.eviware.soapui.impl.support.http.HttpRequestTestStep;
import com.eviware.soapui.impl.wsdl.teststeps.HttpTestRequestStep;
import com.eviware.soapui.impl.wsdl.teststeps.RestTestRequestStep;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlMessageAssertion;
import com.eviware.soapui.model.testsuite.TestAssertion;
import com.eviware.soapui.model.testsuite.TestCase;
import com.eviware.soapui.impl.rest.RestRequestInterface.HttpMethod;
import com.eviware.soapui.model.testsuite.TestStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Presenter {

    static final Logger logger = LoggerFactory.getLogger(Presenter.class);

    static void logRequestDetails(TestCase testCase) {
        logger.debug("logRequestDetails() entering for testCase: {}", testCase.getLabel());

        // TODO - should creating list of the subclass HttpRequestTestSteps to avoid casting when using entries
        List<TestStep> httpRequestTestSteps = testCase.getTestStepList()
            .stream()
            .filter(t -> HttpRequestTestStep.class.isAssignableFrom(t.getClass()))
            .collect(Collectors.toList());

        for (TestStep httpRequestTestStep : httpRequestTestSteps) {
            logRequestDetails((HttpRequestTestStep)httpRequestTestStep);
            logAssertionDetails((HttpRequestTestStep) httpRequestTestStep);
        }
    }

    private static <T extends HttpRequestTestStep> void logRequestDetails(T httpRequestTestStep) {
        AbstractHttpRequest<?> httpRequest = httpRequestTestStep.getHttpRequest();
        logGenericRequestDetails(httpRequest);
        logRequestContentIfPresent(httpRequest);
    }

    static void logGenericRequestDetails(AbstractHttpRequest<?> request) {
        logger.debug("RequestDetails are as follows -");
        String originalUri = request.getConfig().getOriginalUri();

        // N.b. in some cases Original Uri appears to be null (when pointing to another rest request)
        if (originalUri != null) {
            originalUri = originalUri.replaceAll("\\r|\\n", " ");
        }
        logger.debug("OriginalUri: {}", originalUri);
        logger.debug("Method: {}", request.getMethod());
    }

    static void logRequestContentIfPresent(AbstractHttpRequest<?> httpRequest) {
        if (httpRequest.getMethod() == HttpMethod.POST) {
            logger.debug("Request Content: \n" + httpRequest.getRequestContent());
        }
    }

    // TODO - printing null atm, need to find correct target object
    static void logAssertionDetails(HttpRequestTestStep step) {
        int assertionsCount = step.getAssertionCount();
        logger.debug("Assertions to be performed: {}", assertionsCount);
        for (int i = 0; i < assertionsCount; i++) {
            TestAssertion assertion = step.getAssertionAt(0);
            logger.debug("Assertion[{}] assertableContent: \n{}", i, assertion.getAssertable().getAssertableContent());
        }
    }

    static void logResponseContent(TestCase testCase) {
        for (RestTestRequestStep step : testCase.getTestStepsOfType(RestTestRequestStep.class)) {
            try {
                AbstractHttpRequest<?> httpRequest = step.getHttpRequest();
                logger.debug("ResponseContent:\n" + httpRequest.getResponse().getContentAsString());
            } catch (NullPointerException npe) {
                logger.debug("logResponseContent() - Unable to find Response Content, looks like a problem occured");
            }
        }
    }

    static void printTestPreamble(TestCase testCase) {
        logger.debug("\n*\n**\n*** Running Test Case: {} ***", testCase.getName());
    }
}