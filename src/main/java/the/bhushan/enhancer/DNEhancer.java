package the.bhushan.enhancer;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.jdo.JDOEnhancer;
import javax.jdo.JDOHelper;
import javax.jdo.metadata.JDOMetadata;

import org.datanucleus.util.NucleusLogger;

public class DNEhancer {

	public static void main(String[] args) throws Exception {
		DNEhancer enhancer = new DNEhancer();
		String className = "the.bhushan.models.Person";
		String rawClassLocation = "/tmp/jd/the/bhushan/models/Person.class";
		// String enhancedClassLocation = "/tmp/test/Person.class";
		enhancer.enhanceAndOverride(className, rawClassLocation);
	}

	public void enhanceAndOverride(String className, String rawClassLocation)
			throws Exception {
		enhance(className, rawClassLocation, rawClassLocation);
	}

	public void enhance(String className, String rawClassLocation,
			String enhancedClassLocation) throws Exception {
		// Create an in-memory class
		NucleusLogger.PERSISTENCE.info(">> Creating class in-memory");
		Path path = Paths.get(rawClassLocation);
		byte[] classBytes = Files.readAllBytes(path);
		// byte[] classBytes = createClass(className);
		// Add it to a CustomClassLoader
		DynamicEnhanceSchemaToolClassLoader workCL = new DynamicEnhanceSchemaToolClassLoader(
				Thread.currentThread().getContextClassLoader());
		workCL.defineClass(className, classBytes);

		// Create an enhancer (JDO, ASM)
		JDOEnhancer enhancer = JDOHelper.getEnhancer();
		enhancer.setClassLoader(workCL);

		// Create MetaData for the in-memory class and register it with the
		// enhancer
		JDOMetadata jdomd = enhancer.newMetadata();
		enhancer.registerMetadata(jdomd);
		enhancer.addClass(className, classBytes);

		// Enhance the in-memory bytes and obtain the enhanced bytes
		NucleusLogger.PERSISTENCE.info(">> Enhancing class inmemory bytes");
		enhancer.enhance();
		byte[] enhancedBytes = enhancer.getEnhancedBytes(className);

		// Write the enhanced class to disk
		if (!rawClassLocation.equals(enhancedClassLocation)) {
			String enhancedClassLocationDir = enhancedClassLocation.substring(
					0, enhancedClassLocation.lastIndexOf("/"));
			File file = new File(enhancedClassLocationDir);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
		FileOutputStream fos = new FileOutputStream(enhancedClassLocation);
		fos.write(enhancedBytes);
		fos.close();

	}
}
