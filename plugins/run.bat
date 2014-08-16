@echo off
SET BINDIR=%~dp0
CD /D "%BINDIR%"
java -jar target\plugins-0.1.0-SNAPSHOT.jar
pause
