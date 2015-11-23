# Summary 
JunitSoapUiRunner is a command line tool which will parse SoapUi projects, find Test Cases and dynamically generate & run Junit Test Cases for each. 

# Prerequisites

1. SoapUi must be installed on your machine.
2. JUnitXmlFormatter-1.0.jar must be available in mavenLocal()  *(i.e.your .m2 directory)*.

Check JUnitXmlFormatter from https://github.com/barrypitman/JUnitXmlFormatter.git.
Install to your local Maven Repository by running the target *$ mvn clean install*

# Running

junitSoapUiRunner is built as a jar file and run from command line. 

The runTests.sh script is provided for convenience. Before running this you must set the path to the soapUi project containing the tests you wish to execute, by setting this in *config/Test.properties*

# Building

```sh
$ gradle clean fatJar-PsoapUiHome="/Applications/SoapUI-5.2.1.app/Contents/java/app/"
```

# Contributing

All pull requests are welcomed!