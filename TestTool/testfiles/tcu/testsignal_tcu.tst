[AUTHOR] 	Joshua Jungen
[TESTNAME] 	Überprüfung des Rotor Testsignals

[LIB] 		"TCUIVKeywordLibrary.jar" tcu
[VAR] 		"config.var"

[SETUP]	
	Aktiviere das Feature Speed
	Aktiviere das Testsignal des Controllers
[TEST]				
	# Flag im Statuswort prüfen	
	Warte für 	"100"
[TEARDOWN]
	Deaktiviere das Testsignal des Controllers
	Deaktiviere das Feature Speed