@echo off
:: Establece el título del script por lotes
title ataque

setlocal EnableDelayedExpansion

:: Establece el tiempo total de ejecución a 15 segundos
set "TiempoInicio=!time!"

:loop
:: Calcula el tiempo transcurrido
set "TiempoActual=!time!"
set /a "TiempoTranscurrido=((1!TiempoActual:~0,2!-1!TiempoInicio:~0,2!)*3600)+((1!TiempoActual:~3,2!-1!TiempoInicio:~3,2!)*60)+(1!TiempoActual:~6,2!-1!TiempoInicio:~6,2!)"
    
:: Sale del bucle si ha transcurrido más de 15 segundos
if !TiempoTranscurrido! gtr 15 (
    goto :fin
)

:: Ejecuta el bucle
for /L %%i in (1,1,2) do (
    start msedge
    timeout /t 1 /nobreak >nul
)


:: Cierra las instancias de msedge por PID
for /f "tokens=2" %%a in ('tasklist /FI "IMAGENAME eq msedge.exe" /NH') do (
    taskkill /F /PID %%a
)

goto loop

:fin
:: Agrega el comando pause para mantener la consola abierta
pause
