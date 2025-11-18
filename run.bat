@echo off
echo       INVENTORY - Sistema de Gestion
echo.

REM Configurar la ruta de JavaFX
set JAVAFX_PATH=C:\Users\57316\OneDrive\Escritorio\openjfx-21.0.9_windows-x64_bin-sdk\javafx-sdk-21.0.9\lib

REM Verificar si la ruta existe
if not exist "%JAVAFX_PATH%" (
    echo ERROR: No se encontro JavaFX en la ruta especificada.
    echo Por favor, edita run.bat y configura JAVAFX_PATH correctamente.
    echo.
    echo Ejemplo: set JAVAFX_PATH=C:\Users\57316\OneDrive\Escritorio\openjfx-21.0.9_windows-x64_bin-sdk\javafx-sdk-21.0.9\lib
    echo.
    pause
    exit /b 1
)

echo [1/2] Compilando el proyecto...
echo.

REM Crear carpeta de salida
if not exist "out" mkdir out

REM Compilar todos los archivos Java
javac --module-path "%JAVAFX_PATH%" ^
      --add-modules javafx.controls,javafx.fxml ^
      -d out ^
      src\Main.java ^
      src\models\*.java ^
      src\structures\*.java ^
      src\controllers\*.java ^
      src\views\*.java ^
      src\utils\*.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: La compilacion fallo.
    echo Verifica que Java 21 este instalado correctamente.
    pause
    exit /b 1
)

echo.
echo [2/2] Ejecutando InvenTory...
echo.

REM Ejecutar la aplicacion
java --module-path "%JAVAFX_PATH%" ^
     --add-modules javafx.controls,javafx.fxml ^
     -cp out ^
     Main

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: La ejecucion fallo.
    pause
    exit /b 1
)

echo.
echo      InvenTory se ha cerrado correctamente
pause
