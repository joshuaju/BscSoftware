[AUTHOR] 	Max Muster
[TESTNAME] 	Verändern von Werten
[DESC] 		Es wird geprüft ob das Verändern von Werten 
			am Gerät erfolgreich ist.
			
[LIB]		"D:/Bsc/DeviceKeywordLibrary.jar"
[VAR] 		"U:/Developement/Java/BscSoftware/BscSoftware/Testtool/testfiles/test.var"

[TEST]		
	{val1} = 1
	Ist gleich		{delay}, "500"
	Ist gleich 		{global}, "Globale Variabel"

