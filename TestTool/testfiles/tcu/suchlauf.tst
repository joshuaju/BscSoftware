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
	Ist ungefähr gleich 						{nom}, "9.50", "0.0"
[TEST]
	Aktiviere automatische Speisespannungs Suchlauf
	
	Ist Statusbit gesetzt		"10"	
	Warte für					{delay_long}
	Ist Statusbit nicht gesetzt	"10"
	
	{sp} = 						Lese Versorgungsspannung am Rotor	
	Ist ungefähr gleich		{sp}, {nom}, "0.25"
[TEARDOWN]
