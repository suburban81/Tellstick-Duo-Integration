package tell.logger.api;

import com.sun.jna.Library;
import com.sun.jna.ptr.IntByReference;

public interface TellLibrary extends Library {
	static int TELLSTICK_TEMPERATURE = 1;
	static int TELLSTICK_HUMIDITY = 2;

	void tdInit();

	void tdClose();

	int tdTurnOn(int deviceId);

	int tdTurnOff(int deviceId);

	int tdSensor(byte[] protocol, int protocolLength, byte[] model,
			int modelLength, IntByReference id, IntByReference dataTypes);

	int tdSensorValue(byte[] protocol, byte[] model, int id, int dataType,
			byte[] value, int valueLength, IntByReference timestamp);
}
