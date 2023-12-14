@echo off
:: Establece el título del script por lotes
title pardon


:: Encuentra el PID del script por lotes
for /f "tokens=2" %%a in ('tasklist /FI "WINDOWTITLE eq ataque" /NH') do (
    set "pid=%%a"
)

:: Si se encontró el PID, entonces intenta cerrar el script por lotes
if defined pid (
    taskkill /F /PID %pid%
) else (
    echo El script por lotes no está en ejecución.
)

:: Agrega el comando pause para mantener la consola abierta
pause