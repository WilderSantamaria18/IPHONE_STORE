@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%
cd /d "D:\4 CICLO\DESARROLLO DE APLICACIONES 2\PRESENTACION\Desarrollo_Aplicacion_2\Continua2"
C:\tools\apache-maven-3.9.6\bin\mvn.cmd spring-boot:run -Dmaven.test.skip=true
