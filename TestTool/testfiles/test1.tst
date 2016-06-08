[AUTHOR]	Max Muster
[TESTNAME] 	Mehrere Geräte
[DESC] 		Es wird geprüft ob das Verändern von Werten 
			am Gerät erfolgreich ist.
[LIB]		"DeviceKeywordLibrary.jar" dev1


[TEST]	
	{f1} = 		"1"	
	{f2} = 		"-7"
	{v1} = 		"[{f1}-{f2}]"			
	Ist gleich 	{v1}, "8"

	
