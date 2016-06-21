[AUTHOR]	Max Muster
[TESTNAME] 	Mehrere Geräte
[DESC] 		Es wird geprüft ob das Verändern von Werten 
			am Gerät erfolgreich ist.
[LIB]		"DeviceKeywordLibrary.jar" dev1
[VAR]		"test.var"

[TEST]	
	std.Warte für {delay}
	{f1} = 		"1"	
	std.Warte für {delay}
	{f2} = 		"-7"
	std.Warte für {delay}
	{v1} = 		"[{f1}-{f2}]"			
	std.Warte für {delay}
	Ist gleich 	{v1}, "8"	

	
