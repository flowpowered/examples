@echo off
SET BINDIR=%~dp0
CD /D "%BINDIR%"
java -jar target\render-0.1.0-SNAPSHOT.jar
pause
