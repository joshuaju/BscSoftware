[AUTHOR] 	Max Muster
[TESTNAME] 	Verändern von Werten
[DESC] 		Es wird geprüft ob das Verändern von Werten 
			am Gerät erfolgreich ist.
[LIB]		"C:/Bibliotheken/DeviceBibliothek.jar" G1

[SETUP]
	Baue Verbindung auf
[TEST]	
	Setze Wert auf		"20"
	{val} = 			Lese Wert
	Ist gleich 			{val}, "20"
[TEARDOWN]
	# 'G1.' könnte hier auch weggelassen werden
	G1.Schließe Verbindung