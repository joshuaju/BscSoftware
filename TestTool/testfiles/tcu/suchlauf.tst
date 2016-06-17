[AUTHOR]	Joshua Jungen
[TESTNAME] 	Überprüfe automatischen Suchlauf

[LIB] 		"TCUIVKeywordLibrary.jar" tcu
[VAR] 		"config.var"

[SETUP]
	Setze Versorgungsspannung auf	"0.0"
	{poti} = 						Lese Poti Wert
	Ist gleich						{poti}, "0.0"
	Setze Nominalspannung auf		"9.50"
	{nom} = 						Lese Nominalspannung
	Ist gleich 						{nom}, "9.50"
[TEST]
	Aktiviere automatische Speisespannungs Suchlauf
	
	Ist Statusbit gesetzt		"10"	
	Warte für					{delay_short}
	Ist Statusbit nicht gesetzt	"10"
	
	{sp} = 						Lese Versorgungsspannung am Rotor	
	Ist ungeführ gleich		{sp}, {nom}, "0.25"
[TEARDOWN]
