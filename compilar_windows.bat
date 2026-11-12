@echo off
setlocal
if not exist bin mkdir bin
javac -encoding UTF-8 --release 17 -d bin src\modelo\*.java src\estructuras\*.java src\algoritmos\*.java src\app\*.java src\pruebas\*.java
if errorlevel 1 exit /b 1
echo Compilacion correcta.
echo Iniciar menu: java -cp bin app.Main
echo Ejecutar pruebas: java -cp bin pruebas.PruebasGrafo
endlocal
