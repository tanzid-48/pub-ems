@echo off
title PUB EMS v6 — Build & Run
color 0B
echo.
echo  ================================================
echo   PUB EMS v6 — Pundra University, Bogura
echo  ================================================
echo.

set JAVAFX=C:\Program Files\MySQL\openjfx-21.0.10_windows-x64_bin-sdk\javafx-sdk-21.0.10\lib
set MYSQL=lib\mysql-connector-j-9.6.0.jar
set MODS=javafx.controls,javafx.fxml,javafx.graphics,javafx.base
set SRC=src\main\java\com\PUB
set OUT=out

echo  [1/3] Cleaning output folder...
if exist %OUT% rmdir /s /q %OUT%
mkdir %OUT%

echo  [2/3] Compiling Java sources...
javac --module-path "%JAVAFX%" --add-modules %MODS% ^
  -cp "%MYSQL%" ^
  -d %OUT% ^
  %SRC%\model\Employee.java ^
  %SRC%\model\Department.java ^
  %SRC%\model\BusSchedule.java ^
  %SRC%\model\LeaveRequest.java ^
  %SRC%\db\DB.java ^
  %SRC%\dao\DAOFactory.java ^
  %SRC%\dao\EmployeeDAO.java ^
  %SRC%\dao\DepartmentDAO.java ^
  %SRC%\dao\LeaveDAO.java ^
  %SRC%\dao\BusDAO.java ^
  %SRC%\dao\AttendanceDAO.java ^
  %SRC%\SplashScreen.java ^
  %SRC%\LoginWindow.java ^
  %SRC%\DashboardPanel.java ^
  %SRC%\Main.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo  [ERROR] Compilation failed! Check errors above.
    pause
    exit /b 1
)

echo  [3/3] Copying resources...
if not exist %OUT%\com\PUB mkdir %OUT%\com\PUB
copy src\main\resources\com\PUB\style.css %OUT%\com\PUB\style.css >nul

echo.
echo  [OK] Build successful! Launching PUB EMS v6...
echo.

java --module-path "%JAVAFX%" ^
  --add-modules %MODS% ^
  -cp "%OUT%;%MYSQL%" ^
  com.PUB.Main

pause