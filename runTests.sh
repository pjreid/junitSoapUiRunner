#!/usr/bin/env bash

# source properties
. ./config/Test.properties

echo "Properties as follows -"
echo "soapUiProjectRoot: ${soapUiProjectRoot}"
echo "soapUiProjectName: ${soapUiProjectName}"
echo "targetServerEndpoint: ${targetServerEndpoint}"

JAVA_ARGS="-Dorg.schmant.task.junit4.target=junit_report.xml \
            -DsoapUiProjectRoot=${soapUiProjectRoot} \
            -DsoapUiProjectName=${soapUiProjectName} \
            -DtargetServerEndpoint=${targetServerEndpoint}"

java -cp junitSoapUiRunner-all-1.0.jar ${JAVA_ARGS} -Dlogback.configurationFile=logback.xml barrypitman.junitXmlFormatter.Runner com.lisnalinchy.integrationtest.soapui.SoapUiTestCaseRunner
