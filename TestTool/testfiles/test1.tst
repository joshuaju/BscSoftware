[AUTHOR]	Max Muster
[TESTNAME] 	Mehrere Geräte
[DESC] 		Es wird geprüft ob das Verändern von Werten 
			am Gerät erfolgreich ist.
[LIB]		"DeviceKeywordLibrary.jar" dev1
[VAR]		"test.var"

[TEST]	
	Warte für {delay}
	{f1} = 		"1"	
	Warte für {delay}
	{f2} = 		"-7"
	Warte für {delay}
	{v1} = 		"[{f1}-{f2}]"			
	Warte für {delay}
	Ist gleich 	{v1}, "8"	

	
