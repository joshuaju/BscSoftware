[AUTHOR] 	Joshua Jungen
[TESTNAME] 	Überprüfung der Alarmschwellen

[LIB] 		"TCUIVKeywordLibrary.jar" tcu
[VAR] 		"config.var"

[REPEAT] 10
[SETUP]			
	Ist Torque Alarm inaktiv
	{status_ch2} = Lese Feature Status von Bit	"4"
	Deaktiviere das Feature Kanal 2
	Warte für {delay_short}

[TEST]		
	{ratedTorqueA} = 	Lese Rated Torque von Kanal A		
	Setze Torque Input Limit auf 	"[0.25*{ratedTorqueA}]"
	
	Schalte das Testsignal des Rotors um
	Warte für {delay_short}
	Ist Torque Alarm aktiv
[TEARDOWN]
	Schalte das Testsignal des Rotors um
	Setze Torque Input Limit auf {ratedTorqueA}		
	Setze Alarmausgänge zurück
	Setze Feature Kanal 2 auf		{status_ch2}
	Warte für {delay_short}
	{rtA} = Lese Kanal A
	Ist ungefähr gleich {rtA}, "0", "10"
