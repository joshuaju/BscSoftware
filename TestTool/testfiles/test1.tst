[AUTHOR] 	Max Muster
[TESTNAME] 	Mehrere Geräte
[DESC] 		Es wird geprüft ob das Verändern von Werten 
			am Gerät erfolgreich ist.
[LIB]		"D:/Bsc/DeviceKeywordLibrary.jar" dev1
[LIB]		"D:/Bsc/DeviceKeywordLibrary.jar" dev2

[SETUP]
	Ist gleich 				"2", "2"
[TEST]	
	dev1.Setze Wert auf 	"14"
	{val1} = 				dev1.Lese Wert
	dev2.Setze Wert auf 	{val1}
	{val2} = 				dev2.Lese Wert
	Ist gleich 				{val1}, {val2}	
[TEARDOWN]
	Ist gleich 				"1", "1"
	
