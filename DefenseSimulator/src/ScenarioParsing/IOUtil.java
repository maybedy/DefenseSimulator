/**
 * 
 */
package ScenarioParsing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author peter
 * 
 */
public class IOUtil {

	public static BufferedReader getBufferedReaderInJar(String file)
			throws FileNotFoundException {

		InputStream stream = IOUtil.class.getClassLoader().getResourceAsStream(
				file);

		if (stream != null) {
			return new BufferedReader(new InputStreamReader(stream));
		}

		throw new FileNotFoundException("The " + file + " has not founded!");
	}
}
