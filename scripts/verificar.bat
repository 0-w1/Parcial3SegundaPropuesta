@echo off
setlocal
set "ROOT=%~dp0.."
set "BUILD=%ROOT%\build-verificacion"
if exist "%BUILD%" rmdir /s /q "%BUILD%"
mkdir "%BUILD%"
(for /r "%ROOT%\src\main\java" %%F in (*.java) do echo "%%F") > "%BUILD%\fuentes.txt"
javac -encoding UTF-8 -d "%BUILD%" @"%BUILD%\fuentes.txt"
if errorlevel 1 exit /b 1
javac -encoding UTF-8 -cp "%BUILD%" -d "%BUILD%" "%ROOT%\scripts\VerificacionDia6.java"
if errorlevel 1 exit /b 1
java -Djava.awt.headless=true -cp "%BUILD%" gestor.VerificacionDia6
if errorlevel 1 exit /b 1
java -Djava.awt.headless=true -cp "%BUILD%" gestor.Main
if errorlevel 1 exit /b 1
endlocal
