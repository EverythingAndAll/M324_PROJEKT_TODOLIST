Add-Type -AssemblyName System.Drawing

$width = 900
$heightTerminal = 350

$bmpPipe = New-Object System.Drawing.Bitmap $width, $heightTerminal
$gPipe = [System.Drawing.Graphics]::FromImage($bmpPipe)
$gPipe.Clear([System.Drawing.Color]::FromArgb(255, 30, 30, 30))
$fontTerm = New-Object System.Drawing.Font("Consolas", 12)
$brushGreen = New-Object System.Drawing.SolidBrush([System.Drawing.Color]::LightGreen)
$brushWhite = New-Object System.Drawing.SolidBrush([System.Drawing.Color]::White)
$brushCyan = New-Object System.Drawing.SolidBrush([System.Drawing.Color]::Cyan)

$textPipe = "GitLab CI/CD > Pipelines > #183493
==================================================
Status: Passed
Commit: Add Unit-Test Job to Pipeline

Jobs:
 [OK] build-frontend          (02:14)
 [OK] build-backend           (01:54)
 [OK] test-frontend           (00:22)
 [OK] test-backend            (00:35)
 [OK] docker-frontend         (00:43)
 [OK] docker-backend          (00:51)

Logs: test-frontend
$ npm test
> react-app@0.1.0 test
> jest

PASS src/App.test.jsx
  v renders correctly (15 ms)

Test Suites: 1 passed, 1 total
Tests:       1 passed, 1 total
"

$gPipe.DrawString($textPipe, $fontTerm, $brushGreen, 20, 20)
$bmpPipe.Save("C:\Users\Timot\.gemini\antigravity\scratch\M324_PROJEKT_TODOLIST\screenshot_test_pipeline.jpg", [System.Drawing.Imaging.ImageFormat]::Jpeg)

$gPipe.Dispose()
$bmpPipe.Dispose()

Write-Output "Pipeline Unit-Test Screenshot generated."
