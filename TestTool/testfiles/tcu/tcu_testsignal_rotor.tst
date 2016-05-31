[AUTHOR] 	Joshua Jungen
[TESTNAME] 	Überprüfung des Rotor Testsignals
[DESC] 		
[LIB]		"D:/Bsc/TCUIVKeywordLibrary.jar" tcu

[REPEAT] 5
[SETUP]	
	{chA} = Lese Kanal A
	{chB} = Lese Kanal B
	Ist ungefähr gleich {chA}, "0", "5"
	Ist ungefähr gleich {chB}, "0", "5"	
[TEST]			
	Schalte das Testsignal des Rotors um
	{ratedTorqueA} =	Lese Rated Torque von Kanal A
	{chA} = Lese Kanal A
#	{chB} = Lese Kanal B
	Ist ungefähr gleich {chA}, "[0.5*{ratedTorqueA}]", "0.25"
#	Ist ungefähr gleich {chB}, "[0.5*{ratedTorque}]", "5"	
[TEARDOWN]
	Schalte das Testsignal des Rotors um