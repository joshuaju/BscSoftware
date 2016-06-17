[AUTHOR] Joshua Jungen
[TESTNAME] Sende / Empfange elektronisches Datenblatt (Keine differenz)

[LIB] 		"TCUIVKeywordLibrary.jar" tcu
[VAR] 		"config.var"

[SETUP]	
	Aktiviere das Feature Kanal 2
	Aktiviere das Feature Speed
	
	# TCU Werte speichern
	{tcu_rtA} = 		Lese Rated Torque von Kanal A
	{tcu_psA} = 		Lese positive Sensitivity an Kanal A
	{tcu_nsA} = 		Lese negative Sensitivity an Kanal A
	{tcu_rtB} = 		Lese Rated Torque von Kanal B
	{tcu_psB} = 		Lese positive Sensitivity an Kanal B
	{tcu_nsB} = 		Lese negative Sensitivity an Kanal B
	{tcu_nenn} = 		Lese Nenndrehzahl der TCU
	{tcu_inc} = 		Lese TCU Inkrement

	# Rotor Werte speichern
	{rot_psA} = 		Lese positive Sensitivity an Kanal A aus dem Rotor
	{rot_nsA} = 		Lese negative Sensitivity an Kanal A aus dem Rotor
	{rot_psB} = 		Lese positive Sensitivity an Kanal B aus dem Rotor
	{rot_nsB} = 		Lese negative Sensitivity an Kanal B aus dem Rotor
	{rot_nenn} = 		Setze Nenndrehzahl des Rotors auf
	{rot_inc} = 		Lese Rotor Inkrement
	
	# TCU Werte setzen
	Setze Rated Torque von Kanal A auf			"500"
	Setze positive Sensitivity an Kanal A auf	"9000"
	Setze negative Sensitivity an Kanal A auf	"9000"
	Setze Rated Torque von Kanal B auf			"500"
	Setze positive Sensitivity an Kanal B auf	"9000"
	Setze negative Sensitivity an Kanal B auf	"9000"
	Setze Nenndrehzahl der TCU auf				"25000"
	Setze Inkremente der TCU auf				"60"
	
	# Rotor
	Sende Datenblatt an Rotor	"450", "450", "450", "450", "14000", "680"
	Warte für 	{delay_long}
	
	Deaktiviere die Stromversorgung des Rotors
[TEST]
	Aktiviere die Stromversorgung des Rotors
	Warte für 				{delay_long}
	Ist Statusbit gesetzt	"16"
	!!! Statusflag bleibt deaktiviert !!! ?
[TEARDOWN]
	Setze Rated Torque von Kanal A auf			{tcu_rtA}
	Setze positive Sensitivity an Kanal A auf	{tcu_psA}
	Setze negative Sensitivity an Kanal A auf	{tcu_nsA}
	Setze Rated Torque von Kanal B auf			{tcu_rtB}
	Setze positive Sensitivity an Kanal B auf	{tcu_psB}
	Setze negative Sensitivity an Kanal B auf	{tcu_nsB}
	Setze Nenndrehzahl der TCU auf				{tcu_nenn}
	Setze Inkremente der TCU auf				{tcu_inc}	
	
	Sende Datenblatt an Rotor {rot_psA}, {rot_nsA}, {rot_psB}, {rot_nsB}, {rot_nenn}, {rot_inc}
	Warte für 				{delay_long}
	Ist Statusbit gesetzt	"16"
	Behalte Stator Werte
	