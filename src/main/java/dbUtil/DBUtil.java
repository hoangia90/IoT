package dbUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import devices.Producer;
import devices.Sensor;
import main.User;
import observation.Observation;

public class DBUtil {

	static Session sessionObj;
	static SessionFactory sessionFactory;

//  private static SessionFactory buildSessionFactory() {
//  // Creating Configuration Instance & Passing Hibernate Configuration File
//  Configuration configObj = new Configuration();
//  configObj.configure("/hibernate.cfg.xml");
//
//  // Since Hibernate Version 4.x, ServiceRegistry Is Being Used
//  ServiceRegistry serviceRegistryObj = new StandardServiceRegistryBuilder().applySettings(configObj.getProperties()).build(); 
//
//  // Creating Hibernate SessionFactory Instance
//  sessionFactoryObj = configObj.buildSessionFactory(serviceRegistryObj);
//  return sessionFactoryObj;
//}
	
	//Heroku
//	static String DRIVER = "org.postgresql.Driver";
//	//Remote to Heroku
////	static String URL = "jdbc:postgresql://ec2-54-247-85-251.eu-west-1.compute.amazonaws.com:5432/d3cduui8428u5l?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory&user=xkxtjvpzvnhpnm&password=a368f47b501eb93092a92f4e6d9ec1b4fc47f35abeccd98bdce90de459c2685b";
//	//On Heroku
//	static String URL = "jdbc:postgresql://ec2-54-247-85-251.eu-west-1.compute.amazonaws.com:5432/d3cduui8428u5l?sslmode=require&user=xkxtjvpzvnhpnm&password=a368f47b501eb93092a92f4e6d9ec1b4fc47f35abeccd98bdce90de459c2685b";
////	static String USER = "xkxtjvpzvnhpnm";
////	static String PASS = "a368f47b501eb93092a92f4e6d9ec1b4fc47f35abeccd98bdce90de459c2685b";
//	static String DIALECT = "org.hibernate.dialect.PostgreSQLDialect";
//	static String SHOW_SQL = "true";
//	static String CURRENT_SESSION_CONTEXT_CLASS = "thread";
//	static String HBM2DDL_AUTO = "create-drop";
	
	//Local
	static String DRIVER = "org.postgresql.Driver";
	static String URL = "jdbc:postgresql://localhost:5432/IoT?user=postgres&password=123&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
//	static String USER = "postgres";
//	static String PASS = "123";
	static String DIALECT = "org.hibernate.dialect.PostgreSQLDialect";
	static String SHOW_SQL = "true";
	static String CURRENT_SESSION_CONTEXT_CLASS = "thread";
	static String HBM2DDL_AUTO = "create-drop";
	
	
	
	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			try {
				Configuration configuration = new Configuration().configure();
				configuration.addAnnotatedClass(Sensor.class);
				configuration.addAnnotatedClass(Producer.class);
				configuration.addAnnotatedClass(Observation.class);
				configuration.addAnnotatedClass(User.class);
//        		configuration.setProperty("hibernate.connection.username", "gia");
//        		configuration.setProperty("hibernate.connection.password", "1234");
				Properties settings = new Properties();
				settings.put(Environment.DRIVER, DRIVER);
				settings.put(Environment.URL, URL);
//				settings.put(Environment.USER, USER);
//				settings.put(Environment.PASS, PASS);
				settings.put(Environment.DIALECT, DIALECT);
				settings.put(Environment.SHOW_SQL, SHOW_SQL);
				settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, CURRENT_SESSION_CONTEXT_CLASS);
//				settings.put(Environment.HBM2DDL_AUTO, HBM2DDL_AUTO);
				configuration.setProperties(settings);

				StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
						.applySettings(configuration.getProperties());
//			SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());
				sessionFactory = configuration.buildSessionFactory(builder.build());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sessionFactory;
	}
	

	public static void createSensor(Sensor sensor) {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		session.save(sensor);
		session.getTransaction().commit();
		session.close();
		System.out.println("Successfully created " + sensor.toString());
	}
	
	public static void createProducer(Producer producer) {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		session.save(producer);
		session.getTransaction().commit();
		session.close();
		System.out.println("Successfully created " + producer.toString());
	}
	
	public static void createObservation(Observation observation, Integer producer_id, Integer sensor_id) {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		
		List<Observation> observations = new ArrayList<>();
		observations.add(observation);
		
		Producer producer =(Producer)session.get(Producer.class, producer_id);
		Sensor sensor =(Sensor)session.get(Sensor.class, sensor_id);
		
		producer.setObservations(observations);
		sensor.setObservations(observations);
		
		session.merge(producer);
		session.merge(sensor);
		
		session.update(producer);
		session.update(sensor);
		
		session.save(observation);
		session.getTransaction().commit();
		session.close();
		System.out.println("Successfully created " + observation.toString());
	}
	
	public static void deleteSensor(Integer id) {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		Sensor sensor = findBySensorID(id);
		session.delete(sensor);
		session.getTransaction().commit();
		session.close();
		System.out.println("Successfully deleted " + sensor.toString());

	}

	public static Sensor findBySensorID(Integer id) {
		Session session = getSessionFactory().openSession();
		Sensor sensor = (Sensor) session.load(Sensor.class, id);
		session.close();
		return sensor;
	}
	
	public static void deleteProducer(Integer id) {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		Producer producer = findByProducerID(id);
		session.delete(producer);
		session.getTransaction().commit();
		session.close();
		System.out.println("Successfully deleted " + producer.toString());

	}

	public static Producer findByProducerID(Integer id) {
		Session session = getSessionFactory().openSession();
		Producer producer = (Producer) session.load(Producer.class, id);
		session.close();
		return producer;
	}
	
	public static void deleteObservation(Integer id) {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		Observation observation = findByObservationID(id);
		session.delete(observation);
		session.getTransaction().commit();
		session.close();
		System.out.println("Successfully deleted " + observation.toString());

	}

	public static Observation findByObservationID(Integer id) {
		Session session = getSessionFactory().openSession();
		Observation observation = (Observation) session.load(Observation.class, id);
		session.close();
		return observation;
	}
	
	public static List<Sensor> selectAllSensor() {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		List<Sensor> result = session.createSQLQuery("SELECT * FROM sensor").list();
		session.getTransaction().commit();
		session.close();
	    return result;
	}
	
	public static List<Producer> selectAllProducer() {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		List<Producer> result = session.createSQLQuery("SELECT * FROM producer").list();
		session.getTransaction().commit();
		session.close();
	    return result;
	}
	
	public static List<Observation> selectAllObservation() {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		List<Observation> result = session.createSQLQuery("SELECT * FROM observation").list();
		session.getTransaction().commit();
		session.close();
	    return result;
	}
	
	public static ArrayList<User> selectAllUser() {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		List<Object[]> result = session.createSQLQuery("SELECT * FROM iot_user").list();	
		ArrayList<User> users = new ArrayList<>();
		for (Object[] u : result) {
			User usr = new User(Integer.parseInt(u[0].toString()), u[1].toString());
			users.add(usr);
		}
		session.getTransaction().commit();
		session.close();
	    return users;
	}
	
	public static ArrayList<String> selectAllSerial() {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
//		List<String> result = session.createSQLQuery("SELECT serial FROM producer").list();
		List<Object[]> result = session.createSQLQuery("SELECT serial FROM producer").list();	
		ArrayList<String> serials = new ArrayList<>();
		for (Object[] o : result) {
			serials.add(o[0].toString());
		}
		session.getTransaction().commit();
		session.close();
	    return serials;
	}



	
	
	public static void main(String[] args) {
		// backup
//    	CodeSource codeSource = Compute.class.getProtectionDomain().getCodeSource();
//		  File jarFile = null;
//		try {
//			jarFile = new File(codeSource.getLocation().toURI().getPath());
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String jarDir = jarFile.getParentFile().getPath();
//		System.out.println(jarDir);
//		Backupdbtosql();
//  
//		Runtime.getRuntime().exec("mysql -u gia -p 1234 javabip  backup.sql");
//		DBUtil test = new DBUtil();
//		test.selectAllSensors();
//		test.deleteAllProducers();
//		test.deleteAllObservations();
	}
}
