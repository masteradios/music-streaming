@echo off
setlocal enabledelayedexpansion

set "input_folder=."
set "output_folder=thumbnails"

if not exist "!output_folder!" (
    mkdir "!output_folder!"
)

for %%f in ("%input_folder%\*.mp3") do (
    set "filename=%%~nf"
    ffmpeg -i "%%f" -an -vcodec copy "!output_folder!\!filename!.jpg"
)

echo Thumbnails extracted and saved to !output_folder!
pause