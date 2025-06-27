@echo off
setlocal enabledelayedexpansion

set "input_folder=."
set "output_folder=metadata_json_v1"

if not exist "!output_folder!" (
    mkdir "!output_folder!"
)

echo Starting metadata extraction...
echo.

for %%f in ("%input_folder%\*.mp3") do (
    set "filename=%%~nf"
    echo Processing: !filename!

    rem Extract metadata with ffprobe
    ffprobe -v quiet -print_format json -show_format "%%f" > "!output_folder!\!filename!_raw.json"

    rem Process with PowerShell to create final JSON
    powershell -NoProfile -Command "& {$json = Get-Content '!output_folder!\!filename!_raw.json' | ConvertFrom-Json; $artist = if($json.format.tags.artist) { $json.format.tags.artist } else { 'Unknown Artist' }; $album = if($json.format.tags.album) { $json.format.tags.album } else { 'Unknown Album' }; $date = if($json.format.tags.date) { $json.format.tags.date } else { 'Unknown Date' }; $out = @{ musicName = '!filename!'; artistName = $artist; createdDate = $date; album=$album; musicUrl = 'sg/!filename!.mp3'; musicThumbnailUrl = 'sg/thumbnails/!filename!.jpg' }; $out | ConvertTo-Json -Depth 3 | Out-File '!output_folder!\!filename!.json' -Encoding UTF8; Remove-Item '!output_folder!\!filename!_raw.json'}"

    echo   ✅ Created: !filename!.json
)

echo.
echo ✅ All metadata extracted to !output_folder!
pause

