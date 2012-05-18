
call git pull

mvn -Dmaven.test.failure.ignore=true clean install

mvn -Declipse.workspace=C:\users\alexandre\dev\eclipse\ws-kidux eclipse:eclipse

