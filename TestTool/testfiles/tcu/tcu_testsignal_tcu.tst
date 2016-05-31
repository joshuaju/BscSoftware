[AUTHOR] 	Joshua Jungen
[TESTNAME] 	Überprüfung des Rotor Testsignals
[DESC] 		
[LIB]		"D:/Bsc/TCUIVKeywordLibrary.jar" tcu


[SETUP]	
	Aktiviere das Feature Speed
	Aktiviere das Testsignal des Controllers
[TEST]				
	{ratedTorqueA} =	Lese Rated Torque von Kanal A
	{ratedSpeed} = Lese Rated Speed

	{chA} = 	Lese Kanal A
#	{chB} = 	Lese Kanal B
	{speed} = 	Lese Rated Speed
	
	Ist ungefähr gleich {chA}, {ratedTorqueA}, "5"
#	Ist ungefähr gleich {chB}, {ratedTorque}, "5"
	Ist ungefähr gleich {speed}, {ratedSpeed}, "5"
[TEARDOWN]
	Deaktiviere das Testsignal des Controllers
	Deaktiviere das Feature Speed