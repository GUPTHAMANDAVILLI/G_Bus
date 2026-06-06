@echo off
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_211
set PATH=%JAVA_HOME%\bin;%PATH%

echo.
echo [1/2] Compiling G_Bus...
javac -encoding UTF-8 Bus.java User.java Booking.java Utils.java Main.java

if %errorlevel% neq 0 (
    echo Compilation FAILED. Fix errors above.
    pause
    exit /b 1
)

echo [2/2] Starting G_Bus...
echo.
java -Dfile.encoding=UTF-8 Main

pause
