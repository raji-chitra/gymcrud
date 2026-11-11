@echo off
echo ===================================================
echo   Starting Gym Membership Management System...
echo ===================================================

REM Create bin directory if not exists
if not exist "bin" mkdir bin

REM Compile all Java sources
echo Compiling Java source files...
javac -cp ".;lib/*" -d bin src/com/gym/model/*.java src/com/gym/util/*.java src/com/gym/dao/*.java src/com/gym/main/*.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed! Please ensure Java 17+ is installed and configured in PATH.
    pause
    exit /b %ERRORLEVEL%
)

echo Compilation successful!
echo.
REM Run the application
java -cp "bin;lib/*" com.gym.main.Main
pause
