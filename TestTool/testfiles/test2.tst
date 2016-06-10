[AUTHOR] 	Max Muster
[TESTNAME] 	Mehrere Geräte 2
[DESC] 		Es wird geprüft ob das Verändern von Werten 
			am Gerät erfolgreich ist.
[LIB]		"DeviceKeywordLibrary.jar" dev1
[REPEAT] 5
[SETUP]
	Ist gleich 				"Hallo", "Hallo"
[TEST]	
	Warte für "1000"
	dev1.Setze Wert auf 	"14"
	Warte für "1000"
	{val1} = 				dev1.Lese Wert	
	Warte für "1000"
	Ist gleich 				{val1}, "14"	
[TEARDOWN]
	Ist gleich 				"1", "1"
	
