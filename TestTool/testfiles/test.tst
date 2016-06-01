[AUTHOR] 	Max Muster
[TESTNAME] 	Verändern von Werten
[DESC] 		Es wird geprüft ob das Verändern von Werten 
			am Gerät erfolgreich ist.
			
[LIB]		"D:/Bsc/DeviceKeywordLibrary.jar" lib
[VAR] 		"U:/Developement/Java/BscSoftware/BscSoftware/Testtool/testfiles/test.var"

[TEST]				
	{dlg} = Frage nach Text für "Name"	
	Ist gleich		{dlg}, "Joshua"
	
	Warte auf Bestätigung "Ist alles in Ordnung?"
	
	{dlg} = Frage nach ganzer Zahl für "Alter"	
	Ist gleich		{dlg}, "21"
	
	{dlg} = Frage nach Kommazahl für "Wert von 1/2"	
	Ist gleich		{dlg}, "0.5"
	
	

