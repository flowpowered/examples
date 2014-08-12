@echo off
SET BINDIR=%~dp0
CD /D "%BINDIR%"
java -jar target\networking-test-*.jar
pause
