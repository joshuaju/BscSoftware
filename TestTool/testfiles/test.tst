[AUTHOR] 	Max Muster
[TESTNAME] 	Verändern von Werten
[DESC] 		Es wird geprüft ob das Verändern von Werten 
			am Gerät erfolgreich ist.
			
[LIB]		"DeviceKeywordLibrary.jar" lib
[VAR] 		"test.var"

[TEST]			
	Ist gleich {global}, "global"
	
	{dlg} = std.Frage nach Text für "Name"	
	Ist gleich		{dlg}, "Joshua"
	
	std.Warte auf Bestätigung "Ist alles in Ordnung?"
	
	{dlg} = Frage nach ganzer Zahl für "Alter"	
	Ist gleich		{dlg}, "21"
	
	{dlg} = Frage nach Kommazahl für "Wert von 1/2"	
	Ist gleich		{dlg}, "0.5"
	
	

