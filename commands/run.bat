@echo off
SET BINDIR=%~dp0
CD /D "%BINDIR%"
java -jar target\cmd-example-*.jar
pause
