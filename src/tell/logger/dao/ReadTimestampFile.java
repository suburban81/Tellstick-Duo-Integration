package tell.logger.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

public class ReadTimestampFile {

	private static final Logger log = Logger.getLogger(ReadTimestampFile.class);

	public long readEpoch(String light) {
		String epochStr = "-";
		long epoch;
		try {
			epochStr = new String(Files.readAllBytes(Paths.get("/var/tell/lights/" + light + ".txt")));
			epoch = Long.parseLong(epochStr.trim());
		} catch (IOException e) {
			log.warn("Fail to read file for light " + light, e);
			return 0;
		} catch (NumberFormatException e) {
			log.error("Fail to parse string in file " + light + " val " + epochStr + ".", e);
			return 0;
		}

		return epoch;
	}

}
