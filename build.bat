
call git pull

mvn -Declipse.workspace=C:\users\alexandre\dev\eclipse\ws-kidux eclipse:eclipse

REM mvn -Dmaven.test.failure.ignore=true clean install
mvn clean install

