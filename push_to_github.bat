@echo off
title Push to smart-retail-pos
color 0A
echo.
echo ============================================
echo  SmartRetailPOS - Push to GitHub
echo  Branch: smart-retail-pos
echo ============================================
echo.

cd /d "D:\Application\Kotlin\Pemograman-Mobile-Dasar"

:: Remove stale lock if exists
if exist ".git\index.lock" (
    echo [INFO] Removing stale index.lock...
    del /f /q ".git\index.lock"
)

echo [1/4] Staging all UI redesign files...
git add app/src/main/java/com/industri/smartretailpos/PosActivity.kt
git add app/src/main/res/layout/activity_main.xml
git add app/src/main/res/layout/activity_pos.xml
git add app/src/main/res/values/colors.xml
git add app/src/main/res/values/strings.xml
git add app/src/main/res/values/themes.xml
git add app/src/main/res/values-night/themes.xml
git add app/src/main/res/drawable/bg_btn_outline.xml
git add app/src/main/res/drawable/bg_btn_primary.xml
git add app/src/main/res/drawable/bg_card_shadow.xml
git add app/src/main/res/drawable/bg_input_field.xml
git add app/src/main/res/drawable/bg_result_accent.xml
git add app/src/main/res/drawable/bg_simulation_item.xml
git add app/src/main/res/drawable/bg_status_badge_error.xml
git add app/src/main/res/drawable/bg_status_badge_paid.xml

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] git add failed. Is Android Studio still open?
    pause
    exit /b 1
)

echo [2/4] Committing...
git commit -m "feat: redesign UI Minimalist Clean White - Apple Stripe style"

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] git commit failed.
    pause
    exit /b 1
)

echo [3/4] Creating and pushing to branch smart-retail-pos...
git push origin SmartRetailPOS:smart-retail-pos

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [INFO] Remote branch may not exist yet, trying with --set-upstream...
    git push -u origin HEAD:smart-retail-pos
)

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Push failed. Check internet connection and GitHub credentials.
    pause
    exit /b 1
)

echo.
echo [4/4] Done!
echo ============================================
echo  SUCCESS: Pushed to smart-retail-pos
echo  Repo: https://github.com/reanzyy/belajar-kotlin
echo  Branch: smart-retail-pos
echo ============================================
echo.
pause
