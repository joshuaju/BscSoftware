[AUTHOR] 	Joshua Jungen
[TESTNAME] 	Nullabgleich Channel A und B
[DESC] 		Prüfe ob der Nullabgleich an Kanal A und B 
			funktioniert.
[LIB]		"D:/TCUIVKeywordLibrary.jar" tcu


[SETUP]
	{chA1} = 					Lese Kanal A
	{chB1} = 					Lese Kanal B
	Ist gleich 				{chA1}, "0.0"
	Ist gleich 				{chB1}, "0.0"
[TEST]			
	Führe Nullabgleich durch
	
	{chA2} = 					Lese Kanal A
	{chB2} = 					Lese Kanal B
	
	Ist gleich 					{chA2}, "0.0"
	Ist ungleich 					{chB2}, "0.0"