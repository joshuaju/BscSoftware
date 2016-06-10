[AUTHOR] 	Max Muster
[TESTNAME] 	Verändern von Werten
[DESC] 		Es wird geprüft ob das Verändern von Werten 
			am Gerät erfolgreich ist.
[LIB]		"DeviceKeywordLibrary.jar" dev1

[TEST]	
	Warte für "1000"
	dev1.Setze Wert auf		"20"	
	Warte für "1000"
	{val} = 				Lese Wert
	Warte für "1000"
	Ist gleich 				{val}, "22"
