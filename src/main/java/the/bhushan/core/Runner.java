package the.bhushan.core;

import java.util.Properties;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import the.bhushan.models.Person;

public class Runner {

	public static void main(String[] args) {
		Properties properties  = new Properties();
		properties.setProperty("javax.jdo.PersistenceManagerFactoryClass",
				"org.datanucleus.api.jdo.JDOPersistenceManagerFactory");
		properties.setProperty("javax.jdo.option.ConnectionURL",
				"excel:file:test7.xls");
		properties.setProperty("datanucleus.schema.autoCreateAll", "true");
		properties.setProperty("datanucleus.schema.validateTables", "false");
		properties.setProperty("datanucleus.schema.validateConstraints",
				"false");
		PersistenceManagerFactory pmf = JDOHelper
				.getPersistenceManagerFactory(properties);
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			Person person = new Person();
			person.setFirstName("Bhushan");
			person.setLastName("Patil");
			pm.makePersistent(person);
			Person person1 = new Person();
			person1.setFirstName("Bhupesh");
			person1.setLastName("Patil");
			pm.makePersistent(person1);
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

}
