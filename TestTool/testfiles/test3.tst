[AUTHOR] 	Max Muster
[TESTNAME] 	Verändern von Werten
[DESC] 		Es wird geprüft ob das Verändern von Werten 
			am Gerät erfolgreich ist.
[LIB]		"DeviceKeywordLibrary.jar" dev1
[VAR]		"test.var"

[TEST]	
	Warte für {delay}
	dev1.Setze Wert auf		"20"	
	Warte für {delay}
	{val} = 				Lese Wert
	Warte für {delay}
	Ist gleich 				{val}, "22"
