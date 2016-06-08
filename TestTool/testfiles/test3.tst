[AUTHOR] 	Max Muster
[TESTNAME] 	Verändern von Werten
[DESC] 		Es wird geprüft ob das Verändern von Werten 
			am Gerät erfolgreich ist.
[LIB]		"DeviceKeywordLibrary.jar" dev1

[TEST]	
	dev1.Setze Wert auf		"20"	
	{val} = 				Lese Wert
	Ist gleich 				{val}, "22"
