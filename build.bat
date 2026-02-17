@echo off
echo Setting JAVA_HOME to c:\Users\aceja\OneDrive\Escritorio\ms-17.0.18\ms-17.0.18
set "JAVA_HOME=c:\Users\aceja\OneDrive\Escritorio\ms-17.0.18\ms-17.0.18"
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo JAVA_HOME is now: %JAVA_HOME%
echo Running Maven Wrapper...
call .\mvnw.cmd clean compile
if %ERRORLEVEL% EQU 0 (
    echo Build Successful!
) else (
    echo Build Failed!
)
pause
