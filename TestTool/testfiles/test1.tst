[AUTHOR]	Max Muster
[TESTNAME] 	Mehrere Geräte
[DESC] 		Es wird geprüft ob das Verändern von Werten 
			am Gerät erfolgreich ist.
[LIB]		"D:/Bsc/DeviceKeywordLibrary.jar" dev1

[SETUP]
	Ist ungefähr gleich	"2", "0", "2.0"
[TEST]	
	dev1.Setze Wert auf 	"14"
	{val1} = 				dev1.Lese Wert	
	Ist gleich 				{val1}, "14"	
[TEARDOWN]
	Ist gleich 				"1", "1"
	
