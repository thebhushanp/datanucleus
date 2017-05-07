package the.bhushan.compiler;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

public class CustomCompiler {

	public static void main(String[] args) throws Exception {
		CustomCompiler cc = new CustomCompiler();
		cc.compile("/tmp/jd/");
	}

	public void compile(String sourceDir) throws Exception {
		List<File> sourceFiles = (List<File>) FileUtils.listFiles(new File(
				sourceDir), new RegexFileFilter("^(.*?)"),
				DirectoryFileFilter.DIRECTORY);
		for (File sourceFile : sourceFiles) {
			// Compile source file.
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			int result = compiler.run(null, System.out, null,
					sourceFile.getPath());
			System.out.println("result of compilation process:" + result);
			// Load and instantiate compiled class.
			URLClassLoader classLoader = URLClassLoader
					.newInstance(new URL[] { new File(sourceDir).toURI()
							.toURL() });
			Class<?> cls = Class.forName("the.bhushan.models.Person", true,
					classLoader); // Should
			// print
			// "hello".
			Object instance = cls.newInstance(); // Should print "world".
			System.out.println(instance);
		}
	}
}
