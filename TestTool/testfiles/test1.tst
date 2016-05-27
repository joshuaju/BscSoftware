[AUTHOR] 	Max Muster
[TESTNAME] 	Mehrere Geräte
[DESC] 		Es wird geprüft ob das Verändern von Werten 
			am Gerät erfolgreich ist.
[LIB]		"D:/DeviceKeywordLibrary.jar" dev1
[LIB]		"D:/DeviceKeywordLibrary.jar" dev2

[TEST]	
	dev1.Setze Wert auf 	"14"
	{val1} = 				dev1.Lese Wert
	dev2.Setze Wert auf 	{val1}
	{val2} = 				dev2.Lese Wert
	Ist gleich 				{val1}, {val2}
	
