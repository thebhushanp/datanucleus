package the.bhushan.packer.jar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

public class JarCreator {

	public static void main(String[] args) throws Exception {
		create("/tmp/jd/", "/tmp/op.jar");
	}

	public static void create(String sourceDir, String jarOutputLocation)
			throws IOException {
		create(sourceDir, jarOutputLocation, null);
	}

	public static void create(String sourceDir, String jarOutputLocation,
			Manifest manifest) throws IOException {
		List<File> sources = (List<File>) FileUtils.listFiles(new File(
				sourceDir), new RegexFileFilter("^(.*?)"),
				DirectoryFileFilter.DIRECTORY);
		// Create a buffer for reading the files
		byte[] buf = new byte[1024];
		try {
			// Create the ZIP file
			JarOutputStream out = null;
			if (manifest == null) {
				out = new JarOutputStream(new FileOutputStream(
						jarOutputLocation));
			} else {
				out = new JarOutputStream(new FileOutputStream(
						jarOutputLocation), manifest);
			}
			// Compress the files
			for (File source : sources) {
				FileInputStream in = new FileInputStream(
						source.getAbsolutePath());

				// Add ZIP entry to output stream.
				out.putNextEntry(new ZipEntry(source.getAbsolutePath().replace(
						sourceDir, "")));

				// Transfer bytes from the file to the ZIP file
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				// Complete the entry
				out.closeEntry();
				in.close();
			}
			// Complete the ZIP file
			out.close();
		} catch (IOException e) {
			throw e;
		}
	}

}
