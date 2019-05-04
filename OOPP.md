[![coverage report](https://gitlab.ewi.tudelft.nl/cse1105/2018-2019/oopp-group-90/template/badges/master/coverage.svg?job=coverage&style=flat)](https://gitlab.ewi.tudelft.nl/cse1105/2018-2019/oopp-group-90/template/commits/master)
[![pipeline status](https://gitlab.ewi.tudelft.nl/cse1105/2018-2019/oopp-group-90/template/badges/master/pipeline.svg?style=flat)](https://gitlab.ewi.tudelft.nl/cse1105/2018-2019/oopp-group-90/template/commits/master)
# #GoGreen

## Working with Maven
The Maven menu is located on the right side in IntelliJ.  
If you want to use them, run the stages inside the **'oopp-group-90 project (root)'** directory!  
See this image for a reference.

![](readme-assets/maven_menu.png "Maven menu in IntelliJ")

## Run the program
client: `$ java -jar client-[version nr].jar`<br/>
server: `$ java -jar server-[version nr].jar`

## Run the tests
`$ mvn clean test`

## Getting release files
**IntelliJ**  
Doubleclick on 'install' in the Maven menu  
**Terminal**  
Run `$ mvn clean install`

**Build locations**  
client: `./client/target/client-[version].jar`  
server: `./server/target/server-[version].jar`

## Getting coverage report
**IntelliJ**  
Doubleclick on 'install' in the Maven menu  
**Terminal**  
Run `$ mvn clean test jacoco:report-aggregate` 

**Report location**  
`./tests/target/site/jacoco-aggregate/index.html` 

## Getting checkstyle report
**IntelliJ**  
Doubleclick on 'site' in the Maven menu  
**Terminal**  
Run `$ mvn clean site`  

**Report location**  
`./target/site/checkstyle-aggregate.html`
