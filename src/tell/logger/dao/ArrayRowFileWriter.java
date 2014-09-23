package tell.logger.dao;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import org.apache.log4j.Logger;

import tell.logger.model.DataRow;

public class ArrayRowFileWriter {

	private static final Logger log = Logger.getLogger(ArrayRowFileWriter.class);

	public void writeCSVFromArray(String filepath, List<DataRow> rows) {
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath), "utf-8"))) {
			for(DataRow row:rows) {
				writer.write(row.commaSeparated());
			}
		} catch (IOException e) {
			log.error("Failed to write file", e);
			throw new IllegalStateException();
		}
	}

}
