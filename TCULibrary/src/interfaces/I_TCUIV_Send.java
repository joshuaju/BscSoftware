package interfaces;

import java.io.IOException;

public interface I_TCUIV_Send {

	// alarm.xml
	void setTorqueLimit(Integer value) throws IOException;

	void setSpeedLimit(Integer value) throws IOException;

	void setAccelarationLimit(Integer value) throws IOException;

	// analog.xml
	void setAnalogRangeA(Integer value) throws IOException;

	// calibA.xml
	void setLowUnidirectional(Integer value) throws IOException;

	void setHighUnidirectional(Integer value) throws IOException;

	void setLowBidirectional(Integer value) throws IOException;

	void setHighBidirectional(Integer value) throws IOException;

	void setCancelConfigMode() throws IOException;

	void setCalibrationRange(Integer value) throws IOException;

	void setCalibrateSpannungsPlatine(String date, Double v05H, Double v05L, Double v55H, Double v55L, Double v01H,
			Double v01L, Double v11H, Double v11L) throws IOException;

	void setCalibrateStromPlatine(String date, Double v42H, Double v42L) throws IOException;

	// calibF.xml
	void setLow(Integer value) throws IOException;

	void setHigh(Integer value) throws IOException;

	void setCalibrateFrequency(String date, Integer valH, Integer valL) throws IOException;

	// can.xml
	void setEnableCan(Boolean value) throws IOException;

	void setReinitializeCAN() throws IOException;

	void setCANReceiveID(String value) throws IOException;

	void setEnableCANMessage1(Boolean value) throws IOException;

	void setEnableCANMessage2(Boolean value) throws IOException;

	void setEnableCANMessage3(Boolean value) throws IOException;

	void setEnableCANMessage4(Boolean value) throws IOException;

	void setCANIdentifierMessage1(String value) throws IOException;

	void setCANIdentifierMessage2(String value) throws IOException;

	void setCANIdentifierMessage3(String value) throws IOException;

	void setCANIdentifierMessage4(String value) throws IOException;

	void setCANContent1Message1(Integer value) throws IOException;

	void setCANContent1Message2(Integer value) throws IOException;

	void setCANContent1Message3(Integer value) throws IOException;

	void setCANContent1Message4(Integer value) throws IOException;

	void setCANContent2Message1(Integer value) throws IOException;

	void setCANContent2Message2(Integer value) throws IOException;

	void setCANContent2Message3(Integer value) throws IOException;

	void setCANContent2Message4(Integer value) throws IOException;

	void setCANBaudRate(Integer value) throws IOException;

	void setCANTransmitInterval(Integer value) throws IOException;

	void setCANDataFormat(Integer value) throws IOException;

	void setCANIdentifierLength(Integer value) throws IOException;

	// chart.xml

	// current.xml

	// ethernet.xml
	void setEnableDHCP(Boolean value) throws IOException;

	void setIP(String value) throws IOException;

	void setSubnetMask(String value) throws IOException;

	void setGateway(String value) throws IOException;

	void setPrimaryDNS(String value) throws IOException;

	void setSecondaryDNS(String value) throws IOException;

	// filter.xml
	void setFilterRangeA(Integer value) throws IOException;

	void setFilterRangeB(Integer value) throws IOException;

	// frequency.xml
	void setFrequencyRangeA(Integer value) throws IOException;

	void setFrequencyRangeB(Integer value) throws IOException;

	// home.xml
	void setZeroCalibration() throws IOException;

	void setToggleRotorTestSignal() throws IOException;

	void setToggleControllerTestSignal() throws IOException;

	// logon.xml
	void setTCUPassword() throws IOException;

	// newdate.xml
	void setApplyRotorValues(Boolean value) throws IOException;

	// parameter.xml

	// position.xml

	// power.xml
	void setPotiInPercent(Double value) throws IOException;

	void setSearchPowerSupply() throws IOException;

	void setEnablePowerSuppy(Boolean value) throws IOException;

	// service.xml
	void setVersionRotor(String value) throws IOException;

	void setVersionPIC(String value) throws IOException;

	void setVersionFPGA(String value) throws IOException;

	void setServicePhone(String value) throws IOException;

	void setServiceFax(String value) throws IOException;

	void setServiceEmail(String value) throws IOException;

	void setServiceWebsite(String value) throws IOException;

	// settings.xml
	void setCurrentPassword(String value) throws IOException;

	void setNewPassword(String value) throws IOException;

	void setBenchname(String value) throws IOException;

	// setup.xml
	void setManualCommand(String value) throws IOException;

	void setFPGAConfigMode(Boolean value) throws IOException;

	void setFeatureSecondChannel(Boolean value) throws IOException;

	void setFeatureSpeed(Boolean value) throws IOException;

	void setFeatureCAN(Boolean value) throws IOException;

	void setFeatureFrequency(Boolean value) throws IOException;

	void setFeatureAnalogCurrent(Boolean value) throws IOException;

	void setFeatureAcceleration(Boolean value) throws IOException;

	void setFeatureStationary(Boolean value) throws IOException;

	void setTCUSerialNumber(String value) throws IOException;

	void setDefaultValues() throws IOException;

	void setCalibrationMode(Boolean value) throws IOException;

	void setReboot() throws IOException;

	void setNominalVoltage(Double value) throws IOException;

	void setTestSignalController(Boolean value) throws IOException;

	void setCustomTestSignal(Double value) throws IOException;

	void setSensitivyPositiveRotorA(Long value) throws IOException;

	void setSensitivyNegativeRotorA(Long value) throws IOException;

	void setSensitivyPositiveRotorB(Long value) throws IOException;

	void setSensitivyNegativeRotorB(Long value) throws IOException;

	void setRatedSpeedRotor(Integer value) throws IOException;

	void setSpeedIncrementsRotor(Integer value) throws IOException;

	void setCompensationInput1(Integer value) throws IOException;

	void setCompensationinput2(Integer value) throws IOException;

	void setReloadDatasheet() throws IOException;

	void setCancelDatasheet() throws IOException;

	void setEnableVoltageRegulator(Boolean value) throws IOException;

	// speed.xml
	void setRatedSpeedTCU(Integer value) throws IOException;

	void setGateTimeTCU(Integer value) throws IOException;

	void setSpeedIncrementTCU(Integer value) throws IOException;

	// status.xml

	// torque.xml
	void setMd(Integer value) throws IOException;

	void setRatedTorqueA(Integer value) throws IOException;

	void setRatedTorqueB(Integer value) throws IOException;

	void setSensitivityPositiveA(Long value) throws IOException;

	void setSensitivityNegativeA(Long value) throws IOException;

	void setSensitivityPositiveB(Long value) throws IOException;

	void setSensitivityNegativeB(Long value) throws IOException;

	// values.xml
	void setResetAlarms() throws IOException;

}
