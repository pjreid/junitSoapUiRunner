package com.lisnalinchy.integrationtest.soapui;

import java.io.File;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * Created by paulReid on 16/11/2015.
 */
public class SetupHelper {

    private static Logger logger = LoggerFactory.getLogger(SoapUiTestCaseRunner.class);

    public static void init() {
        SetupHelper.validateSystemProperties();
        SetupHelper.testFileExists(deriveProjectAddress());
        testSoapUiLibIsInClasspath();
    }

    private static void testSoapUiLibIsInClasspath() {
        try {
            //Class.forName("net.sf.ezmorph.Morpher");
            Class.forName("org.dom4j.Attribute");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    private static void testFileExists(String absoluteFilePath) throws RuntimeException {
        logger.debug("Testing file exists at path: {}", absoluteFilePath);
        File file = new File(absoluteFilePath);
        if(!file.exists() && file.isDirectory()) {
            throw new RuntimeException("SoapUiTestCaseRunner.testFileExists(): file not found at path " + absoluteFilePath);
            }
    }

    private static void validateSystemProperties() {
        try {
            validateSystemProperty("soapUiProjectRoot");
            validateSystemProperty("soapUiProjectName");
            validateSystemProperty("targetServerEndpoint");
        }
        catch(IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    private static void validateSystemProperty(String propertyName) throws IllegalArgumentException {
        String value = System.getProperty(propertyName);
        logger.debug("validateSystemProperty() found value: {} for property: {}", value, propertyName);
        if(value == null || value.equals("") || value.equals("null")) {
            throw new IllegalArgumentException(propertyName + " Is missing or an invalid value has been used.");
        }
    }

    protected static String deriveProjectAddress() {
        return System.getProperty("soapUiProjectRoot")
            + "/" + System.getProperty("soapUiProjectName");
    }
}
