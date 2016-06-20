[AUTHOR] 	Joshua Jungen
[TESTNAME] 	Überprüfung des Rotor Testsignals

[LIB] 		"TCUIVKeywordLibrary.jar" tcu
[VAR] 		"config.var"

[SETUP]	
	Aktiviere das Feature Speed
	Aktiviere das Testsignal des Controllers
[TEST]				
	Ist Statusbit gesetzt		"7"
[TEARDOWN]
	Deaktiviere das Testsignal des Controllers
	Deaktiviere das Feature Speed