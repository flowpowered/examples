@echo off
SET BINDIR=%~dp0
CD /D "%BINDIR%"
"%ProgramFiles%\Java\jre7\bin\java.exe" -jar target\networking-test-1.0SNAPSHOT.jar
pause