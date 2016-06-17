[AUTHOR] 	Joshua Jungen
[TESTNAME] 	Überprüfung des Rotor Testsignals

[LIB] 		"TCUIVKeywordLibrary.jar" tcu
[VAR] 		"config.var"

[SETUP]			
	Ist Torque Alarm inaktiv
[TEST]		
	{ratedTorqueA} = 	Lese Rated Torque von Kanal A		
	Setze Torque Input Limit auf 	"[0.25*{ratedTorqueA}]"
	
	Schalte das Testsignal des Rotors um
	Ist Torque Alarm aktiv
[TEARDOWN]
	Schalte das Testsignal des Rotors um
	Setze Torque Input Limit auf {ratedTorqueA}		
	Setze Alarmausgänge zurück