/**
 *   StaffInformer - Inform players about online staff
 *   
 *   Copyright (C) 2013-2014 matzefratze123
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
