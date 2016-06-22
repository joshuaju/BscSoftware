[AUTHOR] Joshua Jungen
[TESTNAME] Überprüfe langsamen Regler

[LIB] 		"TCUIVKeywordLibrary.jar" tcu
[VAR] 		"config.var"

[SETUP]
	{status_reg} = Lese Status von Bit		"18"
	Verwende langsamen Regler	"false"	
	
[TEST]
	{poti} = 					Lese Poti Wert
	Setze Versorgungsspannung auf	"[{poti}-1.5]"
	Verwende langsamen Regler	"true"
	Warte für					{delay_extra_long}
	{rotor} = 					Lese Versorgungsspannung am Rotor
	{nom} 	=					Lese Nominalspannung
	Ist ungefähr gleich			{rotor}, {nom}, "0.3"
	
	Verwende langsamen Regler	"false"
	
	{poti} = 					Lese Poti Wert
	Setze Versorgungsspannung auf	"[{poti}+1.5]"
	Verwende langsamen Regler	"true"
	Warte für					{delay_extra_long}
	{rotor} = 					Lese Versorgungsspannung am Rotor
	{nom} 	=					Lese Nominalspannung
	Ist ungefähr gleich			{rotor}, {nom}, "0.3"

[TEARDOWN]
	Verwende langsamen Regler	{status_reg}