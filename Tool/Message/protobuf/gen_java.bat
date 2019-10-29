@echo off
for %%i in (protocol/*.proto) do (
    protoc.exe --java_out=../../../Server/Message/src/main/java/ protocol/%%i
    echo From %%i To %%~ni.java Successfully!  
)
pause