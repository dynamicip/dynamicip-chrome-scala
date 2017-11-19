@echo off

sbt assembly || goto :error
docker build -t dynamicip-chrome-scala . || goto :error
docker run -v /dev/shm:/dev/shm dynamicip-chrome-scala || goto :error

goto :EOF
:error
echo Failed with error #%errorlevel%.
exit /b %errorlevel%