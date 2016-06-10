[AUTHOR]	Max Muster
[TESTNAME] 	Mehrere Geräte
[DESC] 		Es wird geprüft ob das Verändern von Werten 
			am Gerät erfolgreich ist.
[LIB]		"DeviceKeywordLibrary.jar" dev1


[TEST]	
	Warte für "1000"
	{f1} = 		"1"	
	Warte für "1000"
	{f2} = 		"-7"
	Warte für "1000"
	{v1} = 		"[{f1}-{f2}]"			
	Warte für "1000"
	Ist gleich 	{v1}, "8"	

	
