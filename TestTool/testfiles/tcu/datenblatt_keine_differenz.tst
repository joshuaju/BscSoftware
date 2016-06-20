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
	{rot_rtA} =			Lese Rated Torque A aus dem Rotor
	{rot_psA} = 		Lese positive Sensitivity an Kanal A aus dem Rotor
	{rot_nsA} = 		Lese negative Sensitivity an Kanal A aus dem Rotor
	{rot_rtB} =			Lese Rated Torque B aus dem Rotor
	{rot_psB} = 		Lese positive Sensitivity an Kanal B aus dem Rotor
	{rot_nsB} = 		Lese negative Sensitivity an Kanal B aus dem Rotor
	{rot_nenn} = 		Lese Nenndrehzahl des Rotors
	{rot_inc} = 		Lese Rotor Inkrement
	
	# TCU Werte setzen
	Setze Rated Torque von Kanal A auf			{rot_rtA}
	Setze positive Sensitivity an Kanal A auf	"9000"
	Setze negative Sensitivity an Kanal A auf	"9000"
	Setze Rated Torque von Kanal B auf			{rot_rtB}
	Setze positive Sensitivity an Kanal B auf	"9000"
	Setze negative Sensitivity an Kanal B auf	"9000"
	Setze Nenndrehzahl der TCU auf				"25000"
	Setze Inkremente der TCU auf				"60"
	
	# Rotor
	Sende Datenblatt an Rotor	"9000", "9000", "9000", "9000", "25000", "60"
	Warte für 	{delay_datasheet}
	
	Deaktiviere die Stromversorgung des Rotors
	Warte für 				{delay_short}

[TEST]
	Aktiviere die Stromversorgung des Rotors
	Warte für 					{delay_extra_long}	
	Ist Statusbit nicht gesetzt	"16"
	
[TEARDOWN]
	Setze Rated Torque von Kanal A auf			{tcu_rtA}
	Setze positive Sensitivity an Kanal A auf	{tcu_psA}
	Setze negative Sensitivity an Kanal A auf	{tcu_nsA}
	Setze Rated Torque von Kanal B auf			{tcu_rtB}
	Setze positive Sensitivity an Kanal B auf	{tcu_psB}
	Setze negative Sensitivity an Kanal B auf	{tcu_nsB}
	Setze Nenndrehzahl der TCU auf				{tcu_nenn}
	Setze Inkremente der TCU auf				{tcu_inc}	
	
	Deaktiviere die Stromversorgung des Rotors
	Warte für 				{delay_short}
	Aktiviere die Stromversorgung des Rotors
	
	Sende Datenblatt an Rotor {rot_psA}, {rot_nsA}, {rot_psB}, {rot_nsB}, {rot_nenn}, {rot_inc}
	Warte für 				{delay_datasheet}
	Ist Statusbit gesetzt	"16"
	Behalte Stator Werte
	
	Deaktiviere das Feature Speed
