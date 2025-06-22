@echo off
REM Startet Keycloak 26.2.5

cd /d "C:\Program Files\Tools\keycloak-26.2.5"

REM Setzt Umgebungsvariablen, falls notwendig
set KEYCLOAK_ADMIN=admin
set KEYCLOAK_ADMIN_PASSWORD=admin

REM Start im Entwicklungsmodus (ohne HTTPS) – optional
bin\kc.bat start-dev
