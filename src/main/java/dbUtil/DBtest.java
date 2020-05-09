package dbUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;


public class DBtest {
	static Connection con = null;

	// Local DB test
//	public void localMysqlTest() {
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
////			con = DriverManager.getConnection(
////					"jdbc:mysql://localhost:3306/javabip?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&useSSL=false",
////					"gia", "1234");
//			con = DriverManager.getConnection(
//					"jdbc:mysql://localhost:3306/javabip?user=gia&password=1234&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC");
//			System.out.println(" MySQL Connection is successful !!!!!");
//			con.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public void localPostgresqlTest() {
		try {
			Class.forName("org.postgresql.Driver");
//			con = DriverManager.getConnection(
//					"jdbc:postgresql://localhost:5432/javabip?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&useSSL=false",
//					"postgres", "123");
//			System.out.println(" PostgreSQL Connection is successful !!!!!");
			con = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/IoT?user=postgres&password=123&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC");
			System.out.println(" PostgreSQL Connection is successful !!!!!");
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	// Heroku
	public static String buildJdbcUrl(String URL) throws URISyntaxException {
		URI dbUri = new URI(URL);
		String username = dbUri.getUserInfo().split(":")[0];
//		System.out.println("Username: " + username);
		String password = dbUri.getUserInfo().split(":")[1];
//		System.out.println("Password: " + password);
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
//		System.out.println("DBUrl: " + dbUrl);
		String JdbcUrl = dbUrl + "?" + "user=" + username + "&" + "password=" + password + "&sslmode=require"
				+ "&ssl=true" + "&sslfactory=org.postgresql.ssl.NonValidatingFactory";
//		System.out.println("JdbcUrl: " + JdbcUrl);
		return JdbcUrl;
	}

	public static void PostgresqlHerokuTest(String JDBCURL) {
		try {
			Class.forName("org.postgresql.Driver");
//			con = DriverManager.getConnection(
//					"jdbc:postgresql://ec2-54-247-85-251.eu-west-1.compute.amazonaws.com:5432/d3cduui8428u5l?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",
//					"xkxtjvpzvnhpnm", "a368f47b501eb93092a92f4e6d9ec1b4fc47f35abeccd98bdce90de459c2685b");
			con = DriverManager.getConnection(JDBCURL);
			System.out.println(" PostgreSQL Connection on Heroku is successful !!!!!");
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws URISyntaxException {
		// TODO Auto-generated method stub
		String uri = "postgres://raeurnikaltquu:946d69e235696580d7b8f6db05cf4a4bc6403a13846620bf715491c30467858c@ec2-54-75-231-215.eu-west-1.compute.amazonaws.com:5432/damf1hqq0uf37l";
		String JdbcUrl = buildJdbcUrl(uri);
		PostgresqlHerokuTest(JdbcUrl);
		
//		DBtest test = new DBtest();
//		test.localPostgresqlTest();
	}

}
