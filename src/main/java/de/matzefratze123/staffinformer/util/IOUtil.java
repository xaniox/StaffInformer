package de.matzefratze123.staffinformer.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.lang.Validate;

public class IOUtil {
	
	public static void copy(InputStream stream, File output) throws IOException {
		Validate.notNull(stream, "InputStream cannot be null");
		Validate.notNull(output, "Output file cannot be null");
		
		final int BUFFER_SIZE = 1024;
		
		OutputStream outStream = null;
		
		try {
			if (!output.exists()) {
				output.createNewFile();
			}
			
			outStream = new FileOutputStream(output);
			
			byte[] buffer = new byte[BUFFER_SIZE];
			int read;
			
			while ((read = stream.read(buffer)) > 0) {
				outStream.write(buffer, 0, read);
			}
			
			outStream.flush();
		} finally {
			try {
				if (stream != null) {
					stream.close();
				}
				if (outStream != null) {
					outStream.close();
				}
			} catch (Exception e) {}
		}
	}
	
}
