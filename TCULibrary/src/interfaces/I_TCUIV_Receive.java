package interfaces;

import java.io.IOException;

public interface I_TCUIV_Receive {

	// alarm.xml	
	Integer getTorqueLimit() throws IOException;
		
	Integer getSpeedLimit() throws IOException;

	Integer getAccelarationLimit() throws IOException;

	// analog.xml
	/**
	 * @return Spannung: 0=0-5V throws IOException; 1=±5V throws IOException; 2=0-10V throws IOException; 3=±10V <br>
	 *         Strom: 0=4-20mA
	 */
	Integer getAnaRangeA() throws IOException;

	// calibA.xml
	String getLastCalibAnaA() throws IOException;

	String getLastCalibAnaB() throws IOException;

	String getLastCalibAnaC() throws IOException;

	// calibF.xml
	String getLastCalibFreqA() throws IOException;

	String getLastCalibFreqB() throws IOException;

	// can.xml
	/**
	 * m11/m12:<br>
	 * 0=None<br>
	 * 1=Torque Input 1<br>
	 * 2=Torque input 2 oder Beschleunigung (Wenn Feature aktiviert)<br>
	 * 3=Drehzahl (Wenn Feature aktiviert)<br>
	 * 4=Temperatur<br>
	 * 5=Statuswort<br>
	 * 6=Watchdog (Integer der jede Sekunde inkremetiert wird)
	 * 
	 * @return m1E throws IOException;m1I throws IOException;m11 throws IOException;m12
	 */
	String[] getCanMessage1() throws IOException;

	/**
	 * m21/m22:<br>
	 * 0=None<br>
	 * 1=Torque Input 1<br>
	 * 2=Torque input 2 oder Beschleunigung (Wenn Feature aktiviert)<br>
	 * 3=Drehzahl (Wenn Feature aktiviert)<br>
	 * 4=Temperatur<br>
	 * 5=Statuswort<br>
	 * 6=Watchdog (Integer der jede Sekunde inkremetiert wird)
	 * 
	 * @return m1E throws IOException;m1I throws IOException;m11 throws IOException;m12
	 */
	String[] getCanMessage2() throws IOException;

	/**
	 * m31/m32:<br>
	 * 0=None<br>
	 * 1=Torque Input 1<br>
	 * 2=Torque input 2 oder Beschleunigung (Wenn Feature aktiviert)<br>
	 * 3=Drehzahl (Wenn Feature aktiviert)<br>
	 * 4=Temperatur<br>
	 * 5=Statuswort<br>
	 * 6=Watchdog (Integer der jede Sekunde inkremetiert wird)
	 * 
	 * @return m1E throws IOException;m1I throws IOException;m11 throws IOException;m12
	 */
	String[] getCanMessage3() throws IOException;

	/**
	 * m41/m42:<br>
	 * 0=None<br>
	 * 1=Torque Input 1<br>
	 * 2=Torque input 2 oder Beschleunigung (Wenn Feature aktiviert)<br>
	 * 3=Drehzahl (Wenn Feature aktiviert)<br>
	 * 4=Temperatur<br>
	 * 5=Statuswort<br>
	 * 6=Watchdog (Integer der jede Sekunde inkremetiert wird)
	 * 
	 * @return m1E throws IOException;m1I throws IOException;m11 throws IOException;m12
	 */
	String[] getCanMessage4() throws IOException;

	String getCanReceiveIdentifier() throws IOException;
	
	/**
	 * 
	 * @return 0=250 kbit<br>
	 *         1=500 kbit<br>
	 *         2=1 Mbit
	 */
	Integer getCanBaud() throws IOException;

	/**
	 * 
	 * @return 0=Motorola<br>
	 *         1=Intel
	 */
	Integer getCanDataFormat() throws IOException;

	/**
	 * 
	 * @return 0=11 Bit<br>
	 *         1=29 Bit
	 */
	Integer getCanIdentLength() throws IOException;

	/**
	 * 
	 * @return Time in milliseconds
	 */
	Integer getCanTransmitInterval() throws IOException;

	// chart.xml
	Integer getRatedTorqueA() throws IOException;

	Integer getRatedTorqueB() throws IOException;

	Integer getRatedSpeed() throws IOException;

	// current.xml
	
	/* Boolean getResc(); */
	
	// ethernet.xml
	Boolean getDHCPEnable() throws IOException;

	String getHostname() throws IOException;

	String getMAC() throws IOException;

	String getIP() throws IOException;

	String getSubnetMask() throws IOException;

	String getDefaultGateway() throws IOException;

	String getPrimaryDNS() throws IOException;

	String getSecondaryDNS() throws IOException;

	Boolean getRebootNow() throws IOException;

	// filter.xml
	/**
	 * 
	 * @return 0=Off<br>
	 *         1=1 Hz<br>
	 *         2=10 Hz <br>
	 *         3=50 Hz <br>
	 *         4=100 Hz <br>
	 *         5=150 Hz <br>
	 *         6=250 Hz <br>
	 *         7=500 Hz <br>
	 *         8=1000 Hz <br>
	 *         9=2000 Hz <br>
	 *         10=4000 Hz
	 */
	Integer getFilterRangeA() throws IOException;

	/**
	 * 
	 * @return 0=Off<br>
	 *         1=1 Hz<br>
	 *         2=10 Hz <br>
	 *         3=50 Hz <br>
	 *         4=100 Hz <br>
	 *         5=150 Hz <br>
	 *         6=250 Hz <br>
	 *         7=500 Hz <br>
	 *         8=1000 Hz <br>
	 *         9=2000 Hz <br>
	 *         10=4000 Hz
	 */
	Integer getFilterRangeB() throws IOException;

	// frequency.xml
	/**
	 * 
	 * @return 0=10 ± 5 kHz<br>
	 *         1=60 ± 20 kHz<br>
	 *         2=60 ± 30 kHz<br>
	 *         3=240 ± 120 kHz
	 */
	Integer getFrequenceRangeA() throws IOException;

	/**
	 * 
	 * @return 0=10 ± 5 kHz<br>
	 *         1=60 ± 20 kHz<br>
	 *         2=60 ± 30 kHz<br>
	 *         3=240 ± 120 kHz
	 */
	Integer getFrequenceRangeB() throws IOException;

	// home.xml
	String getBenchName() throws IOException;

	String getSerialNumberRotor() throws IOException;

	String getSerialNumberStator() throws IOException;

	Boolean getPerformedZero() throws IOException;

	// logon.xml

	// newdate.xml
	/*
	 * Integer getRotorTorqueA() throws IOException; Double getRotorSensivityPositiveA() throws IOException; Double
	 * getRotorSensivityNegativeA() throws IOException; Integer getRotorTorqueB() throws IOException; Double
	 * getRotorSensivityPositiveB() throws IOException; Double getRotorSensivityNegativeB() throws IOException;
	 * Integer getRotorRatedSpeed() throws IOException; Integer getRotorSpeedIncrements() throws IOException; Integer
	 * getRatedTorqueA() throws IOException; Integer getRatedTorqueB() throws IOException; Double
	 * getSensitivityPositiveA() throws IOException; Double getSensitivityNegativeA() throws IOException; Double
	 * getSensitivityPositiveB() throws IOException; Double getSensitivityNegativeB() throws IOException; Integer
	 * getRatedSpeed() throws IOException;
	 */
	Integer getSpeedIncrement() throws IOException;

	// parameter.xml

	// position.xml
	Boolean getResc() throws IOException;

	// power.xml

	// servive.xml
	String getVersionRotor() throws IOException;

	String getVersionPIC() throws IOException;

	String getVersionFPGA() throws IOException;

	String getServicePhone() throws IOException;

	String getServiceFax() throws IOException;

	String getEmail() throws IOException;

	String getServiceWebsite() throws IOException;

	// settings.xml
	Boolean getCpw() throws IOException;
	/* String getBenchName() throws IOException; */

	// setup.xml
	Double getSpM() throws IOException;

	/*
	 * String getSerialNumberStator() throws IOException; Boolean getRebootNow() throws IOException; Boolean get
	 * DatasheetSubmit() throws IOException;
	 */
	String getNewHostName() throws IOException;

	Integer getRotorTorqueA() throws IOException;

	Double getRotorSensivityPositiveA() throws IOException;

	Double getRotorSensivityNegativeA() throws IOException;

	Integer getRotorTorqueB() throws IOException;

	Double getRotorSensivityPositiveB() throws IOException;

	Double getRotorSensivityNegativeB() throws IOException;

	Integer getRotorCompensationA() throws IOException;

	Integer getRotorCompensationB() throws IOException;

	Integer getRotorSpeedIncrements() throws IOException;

	Integer getRotorRatedSpeed() throws IOException;
	/*
	 * String getServicePhone() throws IOException; String getServiceFax() throws IOException; String getEmail() throws IOException;
	 * String getServiceWebsite() throws IOException;
	 */

	// status.xml
	Double getChannelA() throws IOException;

	Double getChannelB() throws IOException;

	Double getChannelS() throws IOException;

	Double getSpR() throws IOException;

	Double getSpC() throws IOException;

	/**
	 * sta
	 * 
	 * @return0 0 - Receiver error <br>
	 *          1 - Torque error <br>
	 *          2 - Speed error <br>
	 *          3 - Current error <br>
	 *          4 - MD2/MD1 (Outputswitch aktiv/inaktiv) <br>
	 *          5 - Zero (Nullabgleich wurde durchgeführt) <br>
	 *          6 - Test mode rotor (Testsignal Rotor aktiv) <br>
	 *          7 - Test mode pic (Testsignal Controller aktiv) <br>
	 *          8 - Config mode (Empfang zwischen FPGA-PIC unterbrochen,
	 *          Sendemodus-FPGA) <br>
	 *          9 - Calibration mode (Keine Datenverrechnung, Fixe
	 *          CAN-Configuration) <br>
	 *          10 - Auto power supply (Suchlauf Speisespannung - aktiv) <br>
	 *          11 - CAN transmit (CAN-Modul sendet) <br>
	 *          12 - Acceleration error (Alarmschwelle wurde überschritten) <br>
	 *          13 - Startup (Wird nicht genutzt) <br>
	 *          14 - DHCP-Client aktiv <br>
	 *          15 - SPS_POWER (Speisespannung ist eingeschaltet) <br>
	 *          16 - Neues elektronisches Datenblatt verfügbar <br>
	 *          17 - Positionnierungsfehler (SpSp-Suchlauf Fehlgeschlagen) <br>
	 *          18 - Langsamer Speisespannungsregler aktiv <br>
	 *          19 - Erste Warnung für Überstrom <br>
	 *          20 - Letzte Warnung für Überstrom
	 * 
	 */
	String getStatusword() throws IOException;

	/**
	 * fea
	 * 
	 * @return 0 - Feature analog (Analog-Platine verbaut)<br>
	 *         1 - Feature frequency (Frequenz-Platine verbaut) <br>
	 *         2 - Feature Speed (Drehzahlmodul verbaut) <br>
	 *         3 - Feature can (CAN-Modul aktiviert) <br>
	 *         4 - Channel B (2. Kanal aktiviert) <br>
	 *         5 - Current (Analoger Stromausgang statt dem
	 *         Analogen-Spannungsausgang) <br>
	 *         6 - Acceleration (Beschleunigung statt 2. Drehmomentkanal) <br>
	 *         7 - Stationary (Flange ist stationär)
	 */
	String getFeatures() throws IOException;

	/*
	 * Integer getSpeedLimit() throws IOException; Integer getTorqueLimit() throws IOException; Integer
	 * getAccelarationLimit() throws IOException;
	 */
	Integer getTemperatureCompensationA() throws IOException;

	Integer getTemperatureCompensationB() throws IOException;

	/** sQu */
	Integer getSignalQuality() throws IOException;

	/** sSu */
	Integer getRotorSupplyVoltage() throws IOException;

	Double getPoti() throws IOException;

	/** tem */
	Integer getRotorTemperature() throws IOException;

	/** scf */
	Integer getScaleFactor() throws IOException;

	/* Double getSpM() throws IOException; */
	Integer getRwA() throws IOException;

	Integer getRwB() throws IOException;

	Integer getCoS() throws IOException;

	/**
	 * cer
	 * 
	 * @return 1 - CAN_RX_BUS_PASSIVE, CAN_TX_BUS_PASSIVE_STATE<br>
	 *         2 - CAN_TX_BUS_OFF_STATE <br>
	 *         3 - CAN_ERROR_OK
	 */
	Integer getCanError() throws IOException;

	// torque.xml
	/**
	 * Nur relevant wenn 2. Messkanal aktiviert wurde
	 * 
	 * @return 1 - Nicht aktiv In1 -> Out1 <br>
	 *         2 - Aktiv In1 -> Out2
	 */
	Integer getMd() throws IOException;

	/*
	 * Integer getRatedTorqueA() throws IOException; Integer getRatedTorqueB() throws IOException;
	 */
	Double getSensitivityPositiveA() throws IOException;

	Double getSensitivityNegativeA() throws IOException;

	Double getSensitivityPositiveB() throws IOException;

	Double getSensitivityNegativeB() throws IOException;

	// response.xml
	/*
	 * Integer getRatedTorqueA(); Integer getRatedTorqueB(); Integer
	 * getRatedSpeed();
	 */

}
