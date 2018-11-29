@echo off

echo GGIT init "\\C:\\Users\\the88\\Desktop\\CMPS411\\Repos\\ConflictTest2Ancestor\\ConflictTest2Ancestor"
pause
start /wait .\GGIT.bat init "\\C:\\Users\\the88\\Desktop\\CMPS411\\Repos\\ConflictTest2Ancestor\\ConflictTest2Ancestor"
echo GGIT clone "\\C:\\Users\\the88\\Desktop\\CMPS411\\GGIT\\repositories\\local"
pause
start /wait .\GGIT.bat clone "\\C:\\Users\\the88\\Desktop\\CMPS411\\GGIT\\repositories\\local"
echo GGIT checkout branch1 "First commit!" "C:\Users\the88\Desktop\CMPS411\Repos\ConflictTest2Graph1\ConflictTest2Graph1"
pause
start /wait .\GGIT.bat checkout branch1 "First commit!" "C:\Users\the88\Desktop\CMPS411\Repos\ConflictTest2Graph1\ConflictTest2Graph1"
echo GGIT checkout master
pause
start /wait .\GGIT.bat checkout master
echo GGIT checkout branch2 "First commit!" "C:\Users\the88\Desktop\CMPS411\Repos\ConflictTest2Graph2\ConflictTest2Graph2"
pause
start /wait .\GGIT.bat checkout branch2 "First commit!" "C:\Users\the88\Desktop\CMPS411\Repos\ConflictTest2Graph2\ConflictTest2Graph2"