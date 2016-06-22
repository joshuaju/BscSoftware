import java.io.IOException;

import annotations.Keyword;
import annotations.KeywordLibrary;
import interfaces.I_TCUIV_Receive;
import interfaces.I_TCUIV_Send;
import xml.Parser;

@KeywordLibrary(Author = "Joshua Jungen", Description = "Die Bibliothek stellt alle Schnittstellen zur TCU bereit")
public class TCUIVKeywordLibrary implements I_TCUIV_Receive, I_TCUIV_Send {

	private static final String TORQUE_XML = "response/torque.xml";
	private static final String STATUS_XML = "status.xml";
	private static final String SETUP_XML = "response/setup.xml";
	private static final String SETTINGS_XML = "response/settings.xml";
	private static final String SERVICE_XML = "response/service.xml";
	private static final String POSITION_XML = "response/position.xml";
	private static final String NEWDATA_XML = "response/newdata.xml";
	private static final String HOME_XML = "response/home.xml";
	private static final String FREQUENCY_XML = "response/frequency.xml";
	private static final String FILTER_XML = "response/filter.xml";
	private static final String ETHERNET_XML = "response/ethernet.xml";
	private static final String CHART_XML = "response/chart.xml";
	private static final String CAN_XML = "response/can.xml";
	private static final String CALIB_F_XML = "response/calibF.xml";
	private static final String CALIB_A_XML = "response/calibA.xml";
	private static final String ANALOG_XML = "response/analog.xml";
	private static final String ALARM_XML = "response/alarm.xml";
	private static final String VALUES_XML = "response/values.xml";
	private static final String SPEED_XML = "response/speed.xml";
	private static final String POWER_XML = "response/power.xml";
	private static final String LOGON_XML = "response/logon.xml";

	private Connection tcu;

	public TCUIVKeywordLibrary() {
		tcu = new Connection("http", "172.16.86.3");
	}

	private Parser getParserForPath(String path) throws IOException {
		Response response = tcu.sendGET(path);
		String xml = response.getResponse();
		Parser parser = new Parser(xml);
		return parser;
	}

	@Override
	// @AKeyword(documentation = "Returns the current torque limit", name = "Get
	// Torque Limit", returns = "")
	public Integer getTorqueLimit() throws IOException {
		Parser parser = getParserForPath(ALARM_XML);
		Integer result = parser.getNodeValue_AsInteger("tli");
		return result;
	}

	public Boolean getTorqueAlarmStatus() throws IOException {
		char status = getStatusword().charAt(1);
		return status == '1';
	}

	@Keyword(Description = "Prüft, ob das Torque Alarm Bit gesetzt ist", Name = "Ist Torque Alarm aktiv")
	public void isTorqueAlarmActive() throws IOException, AssertionError {
		if (!getTorqueAlarmStatus()) {
			throw new AssertionError("Torque Alarm darf nicht inaktiv sein");
		}
	}

	@Keyword(Description = "Prüft, ob das Torque Alarm Bit nicht gesetzt ist", Name = "Ist Torque Alarm inaktiv")
	public void isTorqueAlarmNotActive() throws IOException, AssertionError {
		if (getTorqueAlarmStatus()) {
			throw new AssertionError("Torque Alarm darf nicht aktiv sein");
		}
	}

	@Override
	// @AKeyword(documentation = "Returns the current speed limit", name = "Get
	// Speed Limit", returns = "")
	public Integer getSpeedLimit() throws IOException {
		Parser parser = getParserForPath(ALARM_XML);
		Integer result = parser.getNodeValue_AsInteger("sli");
		return result;
	}

	@Override
	// @AKeyword(documentation = "Returns the current accelaration limit", name
	// = "Get Accelaration Limit", returns = "")
	public Integer getAccelarationLimit() throws IOException {
		Parser parser = getParserForPath(ALARM_XML);
		Integer result = parser.getNodeValue_AsInteger("ali");
		return result;
	}

	@Override
	// @AKeyword(documentation = "Returns output range of voltage/power output",
	// name = "Get Analog Range A", returns = "Voltage<br>: 0 = 0-5V, 1 = +-5V,
	// 2 = 0-10V, 3 = +-10V\nPower: 0 = 4-20mA")
	public Integer getAnaRangeA() throws IOException {
		Parser parser = getParserForPath(ANALOG_XML);
		Integer result = parser.getNodeValue_AsInteger("rA");
		return result;
	}

	@Override
	// @AKeyword(documentation = "Returns the calibration date for analog
	// channel A", name = "Get Calibration Date for Analog A", returns =
	// "year-month-day")
	public String getLastCalibAnaA() throws IOException {
		Parser parser = getParserForPath(CALIB_A_XML);
		String result = parser.getNodeValue_AsString("cda");
		return result;
	}

	@Override
	// @AKeyword(documentation = "Returns the calibration date for analog
	// channel B", name = "Get Calibration Date for Analog B", returns =
	// "year-month-day")
	public String getLastCalibAnaB() throws IOException {
		Parser parser = getParserForPath(CALIB_A_XML);
		String result = parser.getNodeValue_AsString("cdb");
		return result;
	}

	@Override
	// @AKeyword(documentation = "Returns the calibration date for analog
	// channel C", name = "Get Calibration Date for Analog C", returns =
	// "year-month-day")
	public String getLastCalibAnaC() throws IOException {
		Parser parser = getParserForPath(CALIB_A_XML);
		String result = parser.getNodeValue_AsString("cdc");
		return result;
	}

	@Override
	// @AKeyword(documentation = "Returns the calibration date for frequency
	// channel A", name = "Get Calibration Date for Frequency A", returns =
	// "year-month-day")
	public String getLastCalibFreqA() throws IOException {
		Parser parser = getParserForPath(CALIB_F_XML);
		String result = parser.getNodeValue_AsString("cda");
		return result;
	}

	@Override
	// @AKeyword(documentation = "Returns the calibration date for frequency
	// channel B", name = "Get Calibration Date for Frequency B", returns =
	// "year-month-day")
	public String getLastCalibFreqB() throws IOException {
		Parser parser = getParserForPath(CALIB_F_XML);
		String result = parser.getNodeValue_AsString("cdb");
		return result;
	}

	@Override
	public String[] getCanMessage1() throws IOException {
		Parser parser = getParserForPath(CAN_XML);
		String result = parser.getNodeValue_AsString("cm1");
		return result.split(";");
	}

	@Override
	public String[] getCanMessage2() throws IOException {
		Parser parser = getParserForPath(CAN_XML);
		String result = parser.getNodeValue_AsString("cm2");
		return result.split(";");
	}

	@Override
	public String[] getCanMessage3() throws IOException {
		Parser parser = getParserForPath(CAN_XML);
		String result = parser.getNodeValue_AsString("cm3");
		return result.split(";");
	}

	@Override
	public String[] getCanMessage4() throws IOException {
		Parser parser = getParserForPath(CAN_XML);
		String result = parser.getNodeValue_AsString("cm4");
		return result.split(";");
	}

	@Override
	public String getCanReceiveIdentifier() throws IOException {
		Parser parser = getParserForPath(CAN_XML);
		String result = parser.getNodeValue_AsString("rID");
		return result;
	}

	@Override
	public Integer getCanBaud() throws IOException {
		Parser parser = getParserForPath(CAN_XML);
		Integer result = parser.getNodeValue_AsInteger("cBA");
		return result;
	}

	@Override
	public Integer getCanDataFormat() throws IOException {
		Parser parser = getParserForPath(CAN_XML);
		Integer result = parser.getNodeValue_AsInteger("cDF");
		return result;
	}

	@Override
	public Integer getCanIdentLength() throws IOException {
		Parser parser = getParserForPath(CAN_XML);
		Integer result = parser.getNodeValue_AsInteger("cIL");
		return result;
	}

	@Override
	public Integer getCanTransmitInterval() throws IOException {
		Parser parser = getParserForPath(CAN_XML);
		Integer result = parser.getNodeValue_AsInteger("cTI");
		return result;
	}

	@Override
	@Keyword(Description = "Liest Rated Torque von Kanal A", Name = "Lese Rated Torque von Kanal A", Return = "Integer")
	public Integer getRatedTorqueA() throws IOException {
		Parser parser = getParserForPath(CHART_XML);
		Integer result = parser.getNodeValue_AsInteger("rta");
		return result;
	}

	@Override
	@Keyword(Description = "Liest Rated Torque von Kanal B", Name = "Lese Rated Torque von Kanal B", Return = "Integer")
	public Integer getRatedTorqueB() throws IOException {
		Parser parser = getParserForPath(CHART_XML);
		Integer result = parser.getNodeValue_AsInteger("rtb");
		return result;
	}

	@Override
	@Keyword(Description = "Liest die Nenndrehzahl der TCU", Name = "Lese Nenndrehzahl der TCU", Return = "Integer")
	public Integer getRatedSpeed() throws IOException {
		Parser parser = getParserForPath(CHART_XML);
		Integer result = parser.getNodeValue_AsInteger("rts");
		return result;
	}

	@Override
	public Boolean getDHCPEnable() throws IOException {
		Parser parser = getParserForPath(ETHERNET_XML);
		Boolean result = parser.getNodeValue_AsBoolean("dhcp");
		return result;
	}

	@Override
	public String getHostname() throws IOException {
		Parser parser = getParserForPath(ETHERNET_XML);
		String result = parser.getNodeValue_AsString("host");
		return result;
	}

	@Override
	public String getMAC() throws IOException {
		Parser parser = getParserForPath(ETHERNET_XML);
		String result = parser.getNodeValue_AsString("mac");
		return result;
	}

	@Override
	public String getIP() throws IOException {
		Parser parser = getParserForPath(ETHERNET_XML);
		String result = parser.getNodeValue_AsString("ip");
		return result;
	}

	@Override
	public String getSubnetMask() throws IOException {
		Parser parser = getParserForPath(ETHERNET_XML);
		String result = parser.getNodeValue_AsString("mask");
		return result;
	}

	@Override
	public String getDefaultGateway() throws IOException {
		Parser parser = getParserForPath(ETHERNET_XML);
		String result = parser.getNodeValue_AsString("gway");
		return result;
	}

	@Override
	public String getPrimaryDNS() throws IOException {
		Parser parser = getParserForPath(ETHERNET_XML);
		String result = parser.getNodeValue_AsString("dns1");
		return result;
	}

	@Override
	public String getSecondaryDNS() throws IOException {
		Parser parser = getParserForPath(ETHERNET_XML);
		String result = parser.getNodeValue_AsString("dns2");
		return result;
	}

	@Override
	public Boolean getRebootNow() throws IOException {
		Parser parser = getParserForPath(ETHERNET_XML);
		Boolean result = parser.getNodeValue_AsBoolean("reb");
		return result;
	}

	@Override
	public Integer getFilterRangeA() throws IOException {
		Parser parser = getParserForPath(FILTER_XML);
		Integer result = parser.getNodeValue_AsInteger("fSA");
		return result;
	}

	@Override
	public Integer getFilterRangeB() throws IOException {
		Parser parser = getParserForPath(FILTER_XML);
		Integer result = parser.getNodeValue_AsInteger("fSB");
		return result;
	}

	@Override
	public Integer getFrequenceRangeA() throws IOException {
		Parser parser = getParserForPath(FREQUENCY_XML);
		Integer result = parser.getNodeValue_AsInteger("rA");
		return result;
	}

	@Override
	public Integer getFrequenceRangeB() throws IOException {
		Parser parser = getParserForPath(FREQUENCY_XML);
		Integer result = parser.getNodeValue_AsInteger("rB");
		return result;
	}

	@Override
	public String getBenchName() throws IOException {
		Parser parser = getParserForPath(HOME_XML);
		String result = parser.getNodeValue_AsString("bna");
		return result;
	}

	@Override
	public String getSerialNumberRotor() throws IOException {
		Parser parser = getParserForPath(HOME_XML);
		String result = parser.getNodeValue_AsString("snr");
		return result;
	}

	@Override
	public String getSerialNumberStator() throws IOException {
		Parser parser = getParserForPath(HOME_XML);
		String result = parser.getNodeValue_AsString("sns");
		return result;
	}

	@Override
	public Boolean getPerformedZero() throws IOException {
		Parser parser = getParserForPath(HOME_XML);
		Boolean result = parser.getNodeValue_AsBoolean("pze");
		return result;
	}

	@Override
	@Keyword(Description = "Liest das Inkrement der TCU aus", Name = "Lese TCU Inkrement")
	public Integer getSpeedIncrement() throws IOException {
		Parser parser = getParserForPath(NEWDATA_XML);
		Integer result = parser.getNodeValue_AsInteger("sI");
		return result;
	}

	@Override
	public Boolean getResc() throws IOException {
		Parser parser = getParserForPath(POSITION_XML);
		Boolean result = parser.getNodeValue_AsBoolean("resc");
		return result;
	}

	@Override
	public String getVersionRotor() throws IOException {
		Parser parser = getParserForPath(SERVICE_XML);
		String result = parser.getNodeValue_AsString("vor");
		return result;
	}

	@Override
	public String getVersionPIC() throws IOException {
		Parser parser = getParserForPath(SERVICE_XML);
		String result = parser.getNodeValue_AsString("vpi");
		return result;
	}

	@Override
	public String getVersionFPGA() throws IOException {
		Parser parser = getParserForPath(SERVICE_XML);
		String result = parser.getNodeValue_AsString("vfp");
		return result;
	}

	@Override
	public String getServicePhone() throws IOException {
		Parser parser = getParserForPath(SERVICE_XML);
		String result = parser.getNodeValue_AsString("sPH");
		return result;
	}

	@Override
	public String getServiceFax() throws IOException {
		Parser parser = getParserForPath(SERVICE_XML);
		String result = parser.getNodeValue_AsString("sFA");
		return result;
	}

	@Override
	public String getEmail() throws IOException {
		Parser parser = getParserForPath(SERVICE_XML);
		String result = parser.getNodeValue_AsString("sEM");
		return result;
	}

	@Override
	public String getServiceWebsite() throws IOException {
		Parser parser = getParserForPath(SERVICE_XML);
		String result = parser.getNodeValue_AsString("sWE");
		return result;
	}

	@Override
	public Boolean getCpw() throws IOException {
		Parser parser = getParserForPath(SETTINGS_XML);
		Boolean result = parser.getNodeValue_AsBoolean("cpw");
		return result;
	}

	@Override
	@Keyword(Description = "Liest die nominale Versorgungsspannung des Senders", Name = "Lese Nominalspannung", Return = "Double zwischen 4 und 10,5 mit maximal 2 Nachkommastellen")
	public Double getNominalVoltage() throws IOException {
		Parser parser = getParserForPath(SETUP_XML);
		Double result = parser.getNodeValue_AsDouble("spM");		
		return result;
	}

	@Override
	public String getNewHostName() throws IOException {
		Parser parser = getParserForPath(SETUP_XML);
		String result = parser.getNodeValue_AsString("host");
		return result;
	}

	@Override
	@Keyword(Description = "Liest das im Rotor gespeicherte Nennmoment an Kanal A", Name = "Lese Rated Torque A aus dem Rotor")
	public Integer getRotorTorqueA() throws IOException {
		Parser parser = getParserForPath(SETUP_XML);
		Integer result = parser.getNodeValue_AsInteger("rAT");
		return result;
	}

	@Override
	@Keyword(Description = "Liest die positive Steigung an Kanal A aus dem Rotor", Name = "Lese positive Sensitivity an Kanal A aus dem Rotor")
	public Double getRotorSensivityPositiveA() throws IOException {
		Parser parser = getParserForPath(SETUP_XML);
		Double result = parser.getNodeValue_AsDouble("rAP");
		return result;
	}

	@Override
	@Keyword(Description = "Liest die negative Steigung an Kanal A aus dem Rotor", Name = "Lese negative Sensitivity an Kanal A aus dem Rotor")
	public Double getRotorSensivityNegativeA() throws IOException {
		Parser parser = getParserForPath(SETUP_XML);
		Double result = parser.getNodeValue_AsDouble("rAN");
		return result;
	}

	@Override
	@Keyword(Description = "Liest das im Rotor gespeicherte Nennmoment an Kanal B", Name = "Lese Rated Torque B aus dem Rotor")
	public Integer getRotorTorqueB() throws IOException {
		Parser parser = getParserForPath(SETUP_XML);
		Integer result = parser.getNodeValue_AsInteger("rBT");
		return result;
	}

	@Override
	@Keyword(Description = "Liest die positive Steigung an Kanal B aus dem Rotor", Name = "Lese positive Sensitivity an Kanal B aus dem Rotor")
	public Double getRotorSensivityPositiveB() throws IOException {
		Parser parser = getParserForPath(SETUP_XML);
		Double result = parser.getNodeValue_AsDouble("rBP");
		return result;
	}

	@Override
	@Keyword(Description = "Liest die negative Steigung an Kanal B aus dem Rotor", Name = "Lese negative Sensitivity an Kanal B aus dem Rotor")
	public Double getRotorSensivityNegativeB() throws IOException {
		Parser parser = getParserForPath(SETUP_XML);
		Double result = parser.getNodeValue_AsDouble("rBN");
		return result;
	}

	@Override
	public Integer getRotorCompensationA() throws IOException {
		Parser parser = getParserForPath(SETUP_XML);
		Integer result = parser.getNodeValue_AsInteger("rCA");
		return result;
	}

	@Override
	public Integer getRotorCompensationB() throws IOException {
		Parser parser = getParserForPath(SETUP_XML);
		Integer result = parser.getNodeValue_AsInteger("rCB");
		return result;
	}

	@Override
	@Keyword(Description = "Liest das Inkrement des Rotors aus", Name = "Lese Rotor Inkrement")
	public Integer getRotorSpeedIncrements() throws IOException {
		Parser parser = getParserForPath(SETUP_XML);
		Integer result = parser.getNodeValue_AsInteger("rSI");
		return result;
	}

	@Override
	@Keyword(Description = "Liest die Nenndrehzahl des Rotors", Name = "Lese Nenndrehzahl des Rotors")
	public Integer getRotorRatedSpeed() throws IOException {
		Parser parser = getParserForPath(SETUP_XML);
		Integer result = parser.getNodeValue_AsInteger("rRS");
		return result;
	}

	@Override
	@Keyword(Description = "Liest den aktuellen Wert von Kanal A aus", Name = "Lese Kanal A", Parameter = "", Return = "Wert als Kommazahl")
	public Double getChannelA() throws IOException {
		Parser parser = getParserForPath(STATUS_XML);
		Double result = parser.getNodeValue_AsDouble("chA");
		return result;
	}

	@Override
	@Keyword(Description = "Liest den aktuellen Wert von Kanal B aus", Name = "Lese Kanal B", Parameter = "", Return = "Wert als Kommazahl")
	public Double getChannelB() throws IOException {
		Parser parser = getParserForPath(STATUS_XML);
		Double result = parser.getNodeValue_AsDouble("chB");
		return result;
	}

	@Override
	public Double getChannelS() throws IOException {
		Parser parser = getParserForPath(STATUS_XML);
		Double result = parser.getNodeValue_AsDouble("chS");
		return result;
	}

	@Override
	public Double getSpR() throws IOException {
		Parser parser = getParserForPath(STATUS_XML);
		Double result = parser.getNodeValue_AsDouble("spR");
		return result;
	}

	@Override
	public Double getSpC() throws IOException {
		Parser parser = getParserForPath(STATUS_XML);
		Double result = parser.getNodeValue_AsDouble("spC");
		return result;
	}

	@Override
	public String getStatusword() throws IOException {
		Parser parser = getParserForPath(STATUS_XML);
		String result = parser.getNodeValue_AsString("sta");
		return result;
	}

	@Keyword(Description = "Prüft ob ein Statusbit des Statusworts gesetzt ist und wirft einen Fehler falls nicht", Name = "Ist Statusbit gesetzt", Parameter = "(0)Receiver error, (1)Torque error, (2)Speed error, (3)Current error, (4)MD2/MD1 (Outputswitch aktiv/inaktiv), (5)Zero (Nullabgleich wurde durchgeführt), (6)Test mode rotor (Testsignal Rotor aktiv), (7)Test mode pic (Testsignal Controller aktiv), (8)Config mode (Empfang zwischen FPGA-PIC unterbrochen, Sendemodus-FPGA), (9)Calibration mode (Keine Datenverrechnung, Fixe CAN-Configuration), (10)Auto power supply (Suchlauf Speisespannung - aktiv), (11)CAN transmit (CAN-Modul sendet), (12)Acceleration error (Alarmschwelle wurde überschritten), (13)Startup (Wird nicht genutzt), (14)DHCP-Client aktiv, (15)SPS_POWER (Speisespannung ist eingeschaltet), (16)Neues elektronisches Datenblatt verfügbar, (17)Positionnierungsfehler (SpSp-Suchlauf Fehlgeschlagen), (18)Langsamer Speisespannungsregler aktiv, (19)Erste Warnung für Überstrom, (20)Letzte Warnung für Überstrom")
	public void isStatuswordSet(int charAt) throws IOException {
		if (!getStatusbitSet(charAt)) {
			throw new AssertionError("Das Statusbit an Position " + charAt + " ist nicht gesetzt");
		}
	}

	@Keyword(Description = "Prüft ob ein Statusbit des Statusworts nicht gesetzt ist und wirft einen Fehler falls doch", Name = "Ist Statusbit nicht gesetzt", Parameter = "(0)Receiver error, (1)Torque error, (2)Speed error, (3)Current error, (4)MD2/MD1 (Outputswitch aktiv/inaktiv), (5)Zero (Nullabgleich wurde durchgeführt), (6)Test mode rotor (Testsignal Rotor aktiv), (7)Test mode pic (Testsignal Controller aktiv), (8)Config mode (Empfang zwischen FPGA-PIC unterbrochen, Sendemodus-FPGA), (9)Calibration mode (Keine Datenverrechnung, Fixe CAN-Configuration), (10)Auto power supply (Suchlauf Speisespannung - aktiv), (11)CAN transmit (CAN-Modul sendet), (12)Acceleration error (Alarmschwelle wurde überschritten), (13)Startup (Wird nicht genutzt), (14)DHCP-Client aktiv, (15)SPS_POWER (Speisespannung ist eingeschaltet), (16)Neues elektronisches Datenblatt verfügbar, (17)Positionnierungsfehler (SpSp-Suchlauf Fehlgeschlagen), (18)Langsamer Speisespannungsregler aktiv, (19)Erste Warnung für Überstrom, (20)Letzte Warnung für Überstrom")
	public void isStatuswordNotSet(int charAt) throws IOException {		
		if (getStatusbitSet(charAt)) {
			throw new AssertionError("Das Statusbit an Position " + charAt + " ist gesetzt");
		}
	}

	@Keyword(Description = "Ermittelt anhand des Statuswort, ob das angegeben Bit gesetzt ist", Name = "Lese Status von Bit", Return = "True oder False, bei gesetztem bzw. nicht gesetztem Bit", Parameter="(0)Receiver error, (1)Torque error, (2)Speed error, (3)Current error, (4)MD2/MD1 (Outputswitch aktiv/inaktiv), (5)Zero (Nullabgleich wurde durchgeführt), (6)Test mode rotor (Testsignal Rotor aktiv), (7)Test mode pic (Testsignal Controller aktiv), (8)Config mode (Empfang zwischen FPGA-PIC unterbrochen, Sendemodus-FPGA), (9)Calibration mode (Keine Datenverrechnung, Fixe CAN-Configuration), (10)Auto power supply (Suchlauf Speisespannung - aktiv), (11)CAN transmit (CAN-Modul sendet), (12)Acceleration error (Alarmschwelle wurde überschritten), (13)Startup (Wird nicht genutzt), (14)DHCP-Client aktiv, (15)SPS_POWER (Speisespannung ist eingeschaltet), (16)Neues elektronisches Datenblatt verfügbar, (17)Positionnierungsfehler (SpSp-Suchlauf Fehlgeschlagen), (18)Langsamer Speisespannungsregler aktiv, (19)Erste Warnung für Überstrom, (20)Letzte Warnung für Überstrom")
	public boolean getStatusbitSet(int charAt) throws IOException{
		char bit = getStatusword().charAt(charAt);
		return bit == '1';
	}

	@Override
	public String getFeatures() throws IOException {
		Parser parser = getParserForPath(STATUS_XML);
		String result = parser.getNodeValue_AsString("fea");
		return result;
	}
	
	@Keyword(Description = "Ermittelt anhand des Feature-Statuswort, ob das angegeben Bit gesetzt ist", Name = "Lese Feature Status von Bit", Return = "True oder False, bei aktivierten btw. deaktiviertem Feauture", Parameter="0 - Feature analog (Analog-Platine verbaut), 1 - Feature frequency (Frequenz-Platine verbaut), 2 - Feature Speed (Drehzahlmodul verbaut), 3 - Feature can (CAN-Modul aktiviert), 4 - Channel B (2. Kanal aktiviert), 5 - Current (Analoger Stromausgang statt dem Analogen-Spannungsausgang), 6 - Acceleration (Beschleunigung statt 2. Drehmomentkanal), 7 - Stationary (Flange ist stationär)")
	public Boolean getFeatureStatus(Integer value) throws IOException{
		return getFeatures().charAt(value) == '1';
	}

	@Override
	public Integer getTemperatureCompensationA() throws IOException {
		Parser parser = getParserForPath(STATUS_XML);
		Integer result = parser.getNodeValue_AsInteger("tcA");
		return result;
	}

	@Override
	public Integer getTemperatureCompensationB() throws IOException {
		Parser parser = getParserForPath(STATUS_XML);
		Integer result = parser.getNodeValue_AsInteger("tcB");
		return result;
	}

	@Override
	public Integer getSignalQuality() throws IOException {
		Parser parser = getParserForPath(STATUS_XML);
		Integer result = parser.getNodeValue_AsInteger("sQu");
		return result;
	}

	@Override
	@Keyword(Description = "Liest die Versorungsspannung am Rotor", Name = "Lese Versorgungsspannung am Rotor")
	public Double getRotorSupplyVoltage() throws IOException {
		Parser parser = getParserForPath(STATUS_XML);
		double result = parser.getNodeValue_AsInteger("sSu") / 100.0;
		return result;
	}

	@Override
	@Keyword(Description = "Liest die Versorgungsspannung aus", Name = "Lese Poti Wert", Return = "Gibt die eingestellte Versorgungsspannung in Prozent zurück")
	public Double getPoti() throws IOException {
		Parser parser = getParserForPath(STATUS_XML);
		Double result = parser.getNodeValue_AsDouble("pot");
		return result;
	}

	@Override
	public Integer getRotorTemperature() throws IOException {
		Parser parser = getParserForPath(STATUS_XML);
		Integer result = parser.getNodeValue_AsInteger("tem");
		return result;
	}

	@Override
	public Integer getScaleFactor() throws IOException {
		Parser parser = getParserForPath(STATUS_XML);
		Integer result = parser.getNodeValue_AsInteger("scf");
		return result;
	}

	@Override
	public Integer getRwA() throws IOException {
		Parser parser = getParserForPath(STATUS_XML);
		Integer result = parser.getNodeValue_AsInteger("rwA");
		return result;
	}

	@Override
	public Integer getRwB() throws IOException {
		Parser parser = getParserForPath(STATUS_XML);
		Integer result = parser.getNodeValue_AsInteger("rwB");
		return result;
	}

	@Override
	public Integer getCoS() throws IOException {
		Parser parser = getParserForPath(STATUS_XML);
		Integer result = parser.getNodeValue_AsInteger("coS");
		return result;
	}

	@Override
	public Integer getCanError() throws IOException {
		Parser parser = getParserForPath(STATUS_XML);
		Integer result = parser.getNodeValue_AsInteger("cer");
		return result;
	}

	@Override
	public Integer getMd() throws IOException {
		Parser parser = getParserForPath(TORQUE_XML);
		Integer result = parser.getNodeValue_AsInteger("md");
		return result;
	}

	@Override
	@Keyword(Description = "Liest die positive Steigung an Kanal A", Name = "Lese positive Sensitivity an Kanal A")
	public Double getSensitivityPositiveA() throws IOException {
		Parser parser = getParserForPath(TORQUE_XML);
		Double result = parser.getNodeValue_AsDouble("sAP");	
		System.err.println("Lese= " + result);
		return result;
	}

	@Override
	@Keyword(Description = "Liest die negative Steigung an Kanal A", Name = "Lese negative Sensitivity an Kanal A")
	public Double getSensitivityNegativeA() throws IOException {
		Parser parser = getParserForPath(TORQUE_XML);
		Double result = parser.getNodeValue_AsDouble("sAN");
		return result;
	}

	@Override
	@Keyword(Description = "Liest die positive Steigung an Kanal B", Name = "Lese positive Sensitivity an Kanal B")
	public Double getSensitivityPositiveB() throws IOException {
		Parser parser = getParserForPath(TORQUE_XML);
		Double result = parser.getNodeValue_AsDouble("sBP");
		return result;
	}

	@Override
	@Keyword(Description = "Liest die negative Steigung an Kanal B", Name = "Lese negative Sensitivity an Kanal B")
	public Double getSensitivityNegativeB() throws IOException {
		Parser parser = getParserForPath(TORQUE_XML);
		Double result = parser.getNodeValue_AsDouble("sBN");
		return result;
	}

	@Override
	@Keyword(Description = "Setzt das Torque Input Limit auf den angegebenen Wert", Name = "Setze Torque Input Limit auf", Parameter = "Integer")
	public void setTorqueLimit(Integer value) throws IOException {
		tcu.sendPOST(ALARM_XML, "tli", value.toString());
	}

	@Override
	public void setSpeedLimit(Integer value) throws IOException {
		tcu.sendPOST(ALARM_XML, "sli", value.toString());
	}

	@Override
	public void setAccelarationLimit(Integer value) throws IOException {
		tcu.sendPOST(ALARM_XML, "ali", value.toString());
	}

	@Override
	public void setAnalogRangeA(Integer value) throws IOException {
		tcu.sendPOST(ANALOG_XML, "rA", value.toString());
	}

	@Override
	public void setLowUnidirectional(Integer value) throws IOException {
		tcu.sendPOST(CALIB_A_XML, "lowU", value.toString());
	}

	@Override
	public void setHighUnidirectional(Integer value) throws IOException {
		tcu.sendPOST(CALIB_A_XML, "highU", value.toString());
	}

	@Override
	public void setLowBidirectional(Integer value) throws IOException {
		tcu.sendPOST(CALIB_A_XML, "lowB", value.toString());
	}

	@Override
	public void setHighBidirectional(Integer value) throws IOException {
		tcu.sendPOST(CALIB_A_XML, "highB", value.toString());
	}

	@Override
	public void setCancelConfigMode() throws IOException {
		tcu.sendPOST(CALIB_A_XML, "canc", "1");
	}

	@Override
	public void setCalibrationRange(Integer value) throws IOException {
		tcu.sendPOST(CALIB_A_XML, "ran", value.toString());
	}

	@Override
	public void setCalibrateSpannungsPlatine(String date, Double v05h, Double v05l, Double v55h, Double v55l,
			Double v01h, Double v01l, Double v11h, Double v11l) throws IOException {
		tcu.sendPOST(CALIB_A_XML, new String[] { "dT", "v05H", "v05L", "v55H", "v55L", "v01H", "v01L", "v11H", "v11L" },
				new String[] { date, v05h.toString(), v05l.toString(), v55h.toString(), v55l.toString(),
						v01h.toString(), v01l.toString(), v11h.toString(), v11l.toString() });
	}

	@Override
	public void setCalibrateStromPlatine(String date, Double v42h, Double v42l) throws IOException {
		tcu.sendPOST(CALIB_A_XML, new String[] { "dT", "v42H", "v42L" },
				new String[] { date, v42h.toString(), v42l.toString() });
	}

	@Override
	public void setLow(Integer value) throws IOException {
		tcu.sendPOST(CALIB_F_XML, "low", value.toString());
	}

	@Override
	public void setHigh(Integer value) throws IOException {
		tcu.sendPOST(CALIB_F_XML, "high", value.toString());
	}

	@Override
	public void setCalibrateFrequency(String date, Integer valH, Integer valL) throws IOException {
		tcu.sendPOST(CALIB_F_XML, new String[] { "dT", "valH", "valL" },
				new String[] { date, valH.toString(), valL.toString() });
	}

	@Override
	public void setEnableCan(Boolean value) throws IOException {
		tcu.sendPOST(CAN_XML, "cans", (value) ? "1" : "0");
	}

	@Override
	public void setReinitializeCAN() throws IOException {
		tcu.sendPOST(CAN_XML, "canr", "1");
	}

	@Override
	public void setCANReceiveID(String value) throws IOException {
		tcu.sendPOST(CAN_XML, "rID", value);
	}

	@Override
	public void setEnableCANMessage1(Boolean value) throws IOException {
		tcu.sendPOST(CAN_XML, "m1E", (value) ? "1" : "0");
	}

	@Override
	public void setEnableCANMessage2(Boolean value) throws IOException {
		tcu.sendPOST(CAN_XML, "m2E", (value) ? "1" : "0");
	}

	@Override
	public void setEnableCANMessage3(Boolean value) throws IOException {
		tcu.sendPOST(CAN_XML, "m3E", (value) ? "1" : "0");
	}

	@Override
	public void setEnableCANMessage4(Boolean value) throws IOException {
		tcu.sendPOST(CAN_XML, "m4E", (value) ? "1" : "0");
	}

	@Override
	public void setCANIdentifierMessage1(String value) throws IOException {
		tcu.sendPOST(CAN_XML, "m1I", value);
	}

	@Override
	public void setCANIdentifierMessage2(String value) throws IOException {
		tcu.sendPOST(CAN_XML, "m2I", value);
	}

	@Override
	public void setCANIdentifierMessage3(String value) throws IOException {
		tcu.sendPOST(CAN_XML, "m3I", value);
	}

	@Override
	public void setCANIdentifierMessage4(String value) throws IOException {
		tcu.sendPOST(CAN_XML, "m4I", value);
	}

	@Override
	public void setCANContent1Message1(Integer value) throws IOException {
		tcu.sendPOST(CAN_XML, "m11", value.toString());
	}

	@Override
	public void setCANContent1Message2(Integer value) throws IOException {
		tcu.sendPOST(CAN_XML, "m21", value.toString());
	}

	@Override
	public void setCANContent1Message3(Integer value) throws IOException {
		tcu.sendPOST(CAN_XML, "m31", value.toString());
	}

	@Override
	public void setCANContent1Message4(Integer value) throws IOException {
		tcu.sendPOST(CAN_XML, "m41", value.toString());
	}

	@Override
	public void setCANContent2Message1(Integer value) throws IOException {
		tcu.sendPOST(CAN_XML, "m12", value.toString());
	}

	@Override
	public void setCANContent2Message2(Integer value) throws IOException {
		tcu.sendPOST(CAN_XML, "m22", value.toString());
	}

	@Override
	public void setCANContent2Message3(Integer value) throws IOException {
		tcu.sendPOST(CAN_XML, "m32", value.toString());
	}

	@Override
	public void setCANContent2Message4(Integer value) throws IOException {
		tcu.sendPOST(CAN_XML, "m42", value.toString());
	}

	@Override
	public void setCANBaudRate(Integer value) throws IOException {
		tcu.sendPOST(CAN_XML, "cBA", value.toString());
	}

	@Override
	public void setCANTransmitInterval(Integer value) throws IOException {
		tcu.sendPOST(CAN_XML, "cTI", value.toString());
	}

	@Override
	public void setCANDataFormat(Integer value) throws IOException {
		tcu.sendPOST(CAN_XML, "cDF", value.toString());
	}

	@Override
	public void setCANIdentifierLength(Integer value) throws IOException {
		tcu.sendPOST(CAN_XML, "cIL", value.toString());
	}

	@Override
	public void setEnableDHCP(Boolean value) throws IOException {
		tcu.sendPOST(ETHERNET_XML, "dhcp", value ? "1" : "0");
	}

	@Override
	public void setIP(String value) throws IOException {
		tcu.sendPOST(ETHERNET_XML, "ip", value);
	}

	@Override
	public void setSubnetMask(String value) throws IOException {
		tcu.sendPOST(ETHERNET_XML, "mask", value);
	}

	@Override
	public void setGateway(String value) throws IOException {
		tcu.sendPOST(ETHERNET_XML, "gway", value);
	}

	@Override
	public void setPrimaryDNS(String value) throws IOException {
		tcu.sendPOST(ETHERNET_XML, "dns1", value);
	}

	@Override
	public void setSecondaryDNS(String value) throws IOException {
		tcu.sendPOST(ETHERNET_XML, "dns2", value);
	}

	@Override
	public void setFilterRangeA(Integer value) throws IOException {
		tcu.sendPOST(FILTER_XML, "fsA", value.toString());
	}

	@Override
	public void setFilterRangeB(Integer value) throws IOException {
		tcu.sendPOST(FILTER_XML, "fsB", value.toString());
	}

	@Override
	public void setFrequencyRangeA(Integer value) throws IOException {
		tcu.sendPOST(FREQUENCY_XML, "rA", value.toString());
	}

	@Override
	public void setFrequencyRangeB(Integer value) throws IOException {
		tcu.sendPOST(FREQUENCY_XML, "rB", value.toString());
	}

	@Override
	@Keyword(Description = "Führt einen Nullabgleich durch", Name = "Führe Nullabgleich durch")
	public void setZeroCalibration() throws IOException {
		tcu.sendPOST(HOME_XML, "zero", "1");
	}

	@Override
	@Keyword(Description = "Schaltet das Testsignal des Rotors um", Name = "Schalte das Testsignal des Rotors um")
	public void setToggleRotorTestSignal() throws IOException {
		try {
			tcu.sendPOST(HOME_XML, "tesr", "1");
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	@Keyword(Description = "Schaltet das Testsignal des Controllers um", Name = "Schalte das Testsignal des Controllers um")
	public void setToggleControllerTestSignal() throws IOException {
		tcu.sendPOST(HOME_XML, "tesc", "1");
	}

	@Override
	public void setTCUPassword() throws IOException {
		tcu.sendPOST(LOGON_XML, "pw", "");
	}

	@Override
	public void setApplyRotorValues(Boolean value) throws IOException {
		tcu.sendPOST(NEWDATA_XML, "apl", value ? "1" : "0");
	}
	
	@Keyword(Description = "Die Rotor-Werte werden übernommen", Name = "Behalte Rotor Werte")
	public void applyRotorValues() throws IOException{
		setApplyRotorValues(true);
	}
	
	@Keyword(Description = "Die Stator-Werte werden übernommen", Name = "Behalte Stator Werte")
	public void keepStatorValues() throws IOException{
		setApplyRotorValues(false);
	}

	@Override
	@Keyword(Description = "Setzt die Versorgungsspannung", Name = "Setze Versorgungsspannung auf", Parameter = "Prozentuale Angabe zwischen 0 und 100. Werte in0.5% Schritten angeben.")
	public void setPotiInPercent(Double value) throws IOException {
		Integer val = (int) (value * 10);
		tcu.sendPOST(POWER_XML, "supV", val.toString());
	}

	@Override
	@Keyword(Description = "Startet den automatische Speisespannungs Suchlauf", Name = "Aktiviere automatische Speisespannungs Suchlauf")
	public void setSearchPowerSupply() throws IOException {
		tcu.sendPOST(POWER_XML, "auto", "1");
	}

	@Override
	public void setEnablePowerSuppy(Boolean value) throws IOException {
		tcu.sendPOST(POWER_XML, "psps", value ? "1" : "0");
	}
	
	@Keyword(Description = "Aktiviert die Stromversorgung des Rotors", Name = "Aktiviere die Stromversorgung des Rotors")
	public void activateRotorSupplyVoltage() throws IOException{
		setEnablePowerSuppy(true);
	}
	
	@Keyword(Description = "Deaktiviert die Stromversorgung des Rotors", Name = "Deaktiviere die Stromversorgung des Rotors")
	public void deactivateRotorSupplyVoltage() throws IOException{
		setEnablePowerSuppy(true);
	}

	@Override
	public void setVersionRotor(String value) throws IOException {
		tcu.sendPOST(SERVICE_XML, "vor", value);
	}

	@Override
	public void setVersionPIC(String value) throws IOException {
		tcu.sendPOST(SERVICE_XML, "vpi", value);
	}

	@Override
	public void setVersionFPGA(String value) throws IOException {
		tcu.sendPOST(SERVICE_XML, "vfp", value);
	}

	@Override
	public void setServicePhone(String value) throws IOException {
		tcu.sendPOST(SERVICE_XML, "sPH", value);
	}

	@Override
	public void setServiceFax(String value) throws IOException {
		tcu.sendPOST(SERVICE_XML, "sFA", value);
	}

	@Override
	public void setServiceEmail(String value) throws IOException {
		tcu.sendPOST(SERVICE_XML, "sEM", value);
	}

	@Override
	public void setServiceWebsite(String value) throws IOException {
		tcu.sendPOST(SERVICE_XML, "sWE", value);
	}

	@Override
	public void setCurrentPassword(String value) throws IOException {
		tcu.sendPOST(SETTINGS_XML, "cpw", value);
	}

	@Override
	public void setNewPassword(String value) throws IOException {
		tcu.sendPOST(SETTINGS_XML, "nw", value);
	}

	@Override
	public void setBenchname(String value) throws IOException {
		tcu.sendPOST(SETTINGS_XML, "cbn", value);
	}

	@Override
	public void setManualCommand(String value) throws IOException {
		tcu.sendPOST(SETUP_XML, "hx", value);
	}

	@Override
	public void setFPGAConfigMode(Boolean value) throws IOException {
		tcu.sendPOST(SETUP_XML, "conf", value ? "1" : "0");
	}

	@Override
	@Keyword(Description = "Setzt das Feature Kanal 2", Name = "Setze Feature Kanal 2 auf")
	public void setFeatureSecondChannel(Boolean value) throws IOException {
		tcu.sendPOST(SETUP_XML, "chnb", value ? "1" : "0");
	}
	
	@Keyword(Description = "Aktiviert das Feature Kanal 2", Name = "Aktiviere das Feature Kanal 2")
	public void activateFeatureSecondChannel() throws IOException {
		setFeatureSecondChannel(Boolean.TRUE);
	}

	@Keyword(Description = "Deaktiviert das Feature Kanal 2", Name = "Deaktiviere das Feature Kanal 2")
	public void deactivateFeatureSecondChannel() throws IOException {
		setFeatureSecondChannel(Boolean.FALSE);
	}
	

	@Override
	public void setFeatureSpeed(Boolean value) throws IOException {
		tcu.sendPOST(SETUP_XML, "feas", value ? "1" : "0");
	}

	@Keyword(Description = "Aktiviert das Feature Speed", Name = "Aktiviere das Feature Speed")
	public void activateFeatureSpeed() throws IOException {
		setFeatureSpeed(Boolean.TRUE);
	}

	@Keyword(Description = "Deaktiviert das Feature Speed", Name = "Deaktiviere das Feature Speed")
	public void deactivateFeatureSpeed() throws IOException {
		setFeatureSpeed(Boolean.FALSE);
	}

	@Override
	public void setFeatureCAN(Boolean value) throws IOException {
		tcu.sendPOST(SETUP_XML, "feac", value ? "1" : "0");
	}

	@Override
	public void setFeatureFrequency(Boolean value) throws IOException {
		tcu.sendPOST(SETUP_XML, "feaf", value ? "1" : "0");
	}

	@Override
	public void setFeatureAnalogCurrent(Boolean value) throws IOException {
		tcu.sendPOST(SETUP_XML, "feau", value ? "1" : "0");
	}

	@Override
	public void setFeatureAcceleration(Boolean value) throws IOException {
		tcu.sendPOST(SETUP_XML, "fear", value ? "1" : "0");
	}

	@Override
	public void setFeatureStationary(Boolean value) throws IOException {
		tcu.sendPOST(SETUP_XML, "feat", value ? "1" : "0");
	}

	@Override
	public void setTCUSerialNumber(String value) throws IOException {
		tcu.sendPOST(SETUP_XML, "sns", value);
	}

	@Override
	public void setDefaultValues() throws IOException {
		tcu.sendPOST(SETUP_XML, "stup", "1");
	}

	@Override
	public void setCalibrationMode(Boolean value) throws IOException {
		tcu.sendPOST(SETUP_XML, "calm", value ? "1" : "0");
	}

	@Override
	public void setReboot() throws IOException {
		tcu.sendPOST(SETUP_XML, "reb", "1");
	}

	@Override
	@Keyword(Description = "Setzt die nominale Versorgungsspannung des Senders", Name = "Setze Nominalspannung auf", Parameter = "Double zwischen 4 und 10,5 mit maximal 2 Nachkommastellen")
	public void setNominalVoltage(Double value) throws IOException {
		int cut = (int) (value * 100);
		tcu.sendPOST(SETUP_XML, "spM", "" + cut);
	}

	@Override
	public void setTestSignalController(Boolean value) throws IOException {
		tcu.sendPOST(SETUP_XML, "tesc", value ? "1" : "0");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Keyword(Description = "Schaltet das Testsignal des Controllers ein", Name = "Aktiviere das Testsignal des Controllers")
	public void activateTestSignalController() throws IOException {
		setTestSignalController(Boolean.TRUE);
	}

	@Keyword(Description = "Schaltet das Testsignal des Controllers aus", Name = "Deaktiviere das Testsignal des Controllers")
	public void deactivateTestSignalController() throws IOException {
		setTestSignalController(Boolean.FALSE);
	}

	@Override
	public void setCustomTestSignal(Double value) throws IOException {
		tcu.sendPOST(SETUP_XML, "outv", value.toString());
	}

	@Override
	@Keyword(Description = "Setzt die positive Steigung an Kanal A des Rotors", Name = "Setze positive Sensitivity an Kanal A des Rotors auf", Parameter="Integer zwischen 1 und 100000")
	public void setSensitivyPositiveRotorA(Double value) throws IOException {
		Long l = (long) (value * 100000);		
		tcu.sendPOST(SETUP_XML, "rAP", l.toString());
	}

	@Override
	@Keyword(Description = "Setzt die negative Steigung an Kanal A des Rotors", Name = "Setze negative Sensitivity an Kanal A des Rotors auf", Parameter="Integer zwischen 1 und 100000")
	public void setSensitivyNegativeRotorA(Double value) throws IOException {
		Long l = (long) (value * 100000);
		tcu.sendPOST(SETUP_XML, "rAN", l.toString());
	}

	@Override
	@Keyword(Description = "Setzt die positive Steigung an Kanal B des Rotors", Name = "Setze positive Sensitivity an Kanal B des Rotors auf", Parameter="Integer zwischen 1 und 100000")
	public void setSensitivyPositiveRotorB(Double value) throws IOException {
		Long l = (long) (value * 100000);
		tcu.sendPOST(SETUP_XML, "rBP", l.toString());
	}

	@Override
	@Keyword(Description = "Setzt die negative Steigung an Kanal B des Rotors", Name = "Setze negative Sensitivity an Kanal B des Rotors auf", Parameter="Integer zwischen 1 und 100000")
	public void setSensitivyNegativeRotorB(Double value) throws IOException {
		Long l = (long) (value * 100000);
		tcu.sendPOST(SETUP_XML, "rBN", l.toString());
	}

	@Override
	@Keyword(Description = "Setzt die Nenndrehzahl des Rotors", Name = "Setze Nenndrehzahl des Rotors auf", Parameter="Integer zwischen 0 und 25000")
	public void setRatedSpeedRotor(Integer value) throws IOException {
		tcu.sendPOST(SETUP_XML, "rRS", value.toString());
	}

	@Override
	@Keyword(Description = "Setzt die Drehzahlinkremente des Rotors", Name = "Setze Inkremente des Rotors auf", Parameter="Integer zwischen 60 und 2896")
	public void setSpeedIncrementsRotor(Integer value) throws IOException {
		tcu.sendPOST(SETUP_XML, "rSI", value.toString());
	}

	@Override
	public void setCompensationInput1(Integer value) throws IOException {
		tcu.sendPOST(SETUP_XML, "rCA", value.toString());
	}

	@Override
	public void setCompensationinput2(Integer value) throws IOException {
		tcu.sendPOST(SETUP_XML, "rCB", value.toString());
	}

	@Override
	public void setReloadDatasheet() throws IOException {
		tcu.sendPOST(SETUP_XML, "rel", "1");
	}

	@Override
	public void setCancelDatasheet() throws IOException {
		tcu.sendPOST(SETUP_XML, "cre", "1");
	}

	@Override
	@Keyword(Description = "Aktiviert/Deaktiviert den langsamen Regler", Name = "Verwende langsamen Regler", Parameter="true oder false")
	public void setEnableVoltageRegulator(Boolean value) throws IOException {
		tcu.sendPOST(SETUP_XML, "volr", value ? "1" : "0");
	}

	@Override
	@Keyword(Description = "Setzt die Nenndrehzahl der TCU", Name = "Setze Nenndrehzahl der TCU auf", Parameter="Integer zwischen 0 und 25000")
	public void setRatedSpeedTCU(Integer value) throws IOException {
		tcu.sendPOST(SPEED_XML, "rS", value.toString());
	}

	@Override
	public void setGateTimeTCU(Integer value) throws IOException {
		tcu.sendPOST(SPEED_XML, "gT", value.toString());
	}

	@Override
	@Keyword(Description = "Setzt die Drehzahlinkremente der TCU", Name = "Setze Inkremente der TCU auf", Parameter="Integer zwischen 60 und 2896")
	public void setSpeedIncrementTCU(Integer value) throws IOException {
		tcu.sendPOST(SPEED_XML, "sI", value.toString());
	}

	@Override
	public void setMd(Integer value) throws IOException {
		tcu.sendPOST(TORQUE_XML, "md", value.toString());
	}

	@Override
	@Keyword(Description = "Setzt Rated Torque von Kanal A", Name = "Setze Rated Torque von Kanal A auf", Parameter = "Integer zwischen 1 und 60000")
	public void setRatedTorqueA(Integer value) throws IOException {
		tcu.sendPOST(TORQUE_XML, "rtA", value.toString());
	}

	@Override
	@Keyword(Description = "Setzt Rated Torque von Kanal B", Name = "Setze Rated Torque von Kanal B auf", Parameter = "Integer zwischen 1 und 60000")
	public void setRatedTorqueB(Integer value) throws IOException {
		tcu.sendPOST(TORQUE_XML, "rtB", value.toString());
	}

	@Override
	@Keyword(Description = "Setzt die positive Steigung an Kanal A", Name = "Setze positive Sensitivity an Kanal A auf", Parameter="Integer zwischen 1 und 100000")
	public void setSensitivityPositiveA(Double value) throws IOException {
		Long l = (long) (value * 100000);
		System.err.println("Schreibe " + value + "->" + l);
		tcu.sendPOST(TORQUE_XML, "sAP", l.toString());
	}

	@Override
	@Keyword(Description = "Setzt die negative Steigung an Kanal A", Name = "Setze negative Sensitivity an Kanal A auf", Parameter="Integer zwischen 1 und 100000")
	public void setSensitivityNegativeA(Double value) throws IOException {
		Long l = (long) (value * 100000);
		tcu.sendPOST(TORQUE_XML, "sAN", l.toString());
	}

	@Override
	@Keyword(Description = "Setzt die positive Steigung an Kanal B", Name = "Setze positive Sensitivity an Kanal B auf", Parameter="Integer zwischen 1 und 100000")
	public void setSensitivityPositiveB(Double value) throws IOException {
		Long l = (long) (value * 100000);
		tcu.sendPOST(TORQUE_XML, "sBP", l.toString());
	}

	@Override
	@Keyword(Description = "Setzt die negative Steigung an Kanal B", Name = "Setze negative Sensitivity an Kanal B auf", Parameter="Integer zwischen 1 und 100000")
	public void setSensitivityNegativeB(Double value) throws IOException {
		Long l = (long) (value * 100000);
		tcu.sendPOST(TORQUE_XML, "sBN", l.toString());
	}

	@Override
	@Keyword(Description = "Setzt alle Alarmausgänge zurück", Name = "Setze Alarmausgänge zurück")
	public void setResetAlarms() throws IOException {
		tcu.sendPOST(VALUES_XML, "resa", "1");
	}
	
	@Keyword(Description = "Sendet das elektronische Datenblatt an den Rotor", Name = "Sende Datenblatt an Rotor", Parameter="Pos. Sens. Kanal A, Neg. Sens. Kanal A, Pos. Sens. Kanal B, Neg. Sens. Kanal B, Nenndrehzahl, Inkremente")												
	public void sendDatasheet(Double rAP, Double rAN, Double rBP, Double rBN, Integer rRS, Integer rSI) throws IOException{
		PostParameter post_rap = new PostParameter("rAP", "" + (long) (rAP * 100000));
		PostParameter post_ran = new PostParameter("rAN", "" + (long) (rAN * 100000));
		PostParameter post_rbp = new PostParameter("rBP", "" + (long) (rBP * 100000));
		PostParameter post_rbn = new PostParameter("rBN", "" + (long) (rBN * 100000));
		PostParameter post_rrs = new PostParameter("rRS", "" + rRS);
		PostParameter post_rsi = new PostParameter("rSI", "" + rSI);
		String parameterString = PostParameter.concat(post_rap, post_ran, post_rbp, post_rbn, post_rrs, post_rsi);
		tcu.sendPOST(SETUP_XML, parameterString);
	}

}
