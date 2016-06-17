[AUTHOR] Joshua Jungen
[TESTNAME] Überprüfe langsamen Regler

[LIB] 		"TCUIVKeywordLibrary.jar" tcu
[VAR] 		"config.var"

[SETUP]	
	Verwende langsamen Regler	"false"	
	{poti} = 					Lese Poti Wert
	Setze Versorgungsspannung auf	"[{poti}-1.00]"
[TEST]
	Verwende langsamen Regler	"true"
	Warte für					{delay_long}
	{rotor} = 					Lese Versorgungsspannung am Rotor
	{nom} 	=					Lese Nominalspannung
	Ist ungefähr gleich			{rotor}, {nom}, "0.25"
[TEARDOWN]