import com.github.jasync.sql.db.Connection;
import com.github.jasync.sql.db.mysql.MySQLConnection;
import com.github.jasync.sql.db.mysql.MySQLConnectionBuilder;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.google.gson.GsonBuilder;
import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import repositories.impl.TripRepositoryImpl;
import services.impl.ServiceImpl;
import services.impl.TripServiceImpl;
import utils.Helper;

import java.util.concurrent.ExecutionException;

public class App {
    public static final Logger logger = LoggerFactory.getLogger(App.class);

    private static final ServiceImpl buildService(Connection connection, Jedis jedis) {
        TripRepositoryImpl tripRepository = new TripRepositoryImpl(connection);
        TripServiceImpl tripService = new TripServiceImpl(tripRepository, jedis);
        return new ServiceImpl(tripService);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int listenPort = Helper.tryParseInt(System.getenv("CAB_LISTEN_PORT"), 7000);

        logger.info("environment variable LISTEN_PORT=" + listenPort);

        String hostDB = Helper.tryParseString(System.getenv("CAB_HOST_DB"), "localhost");
        int portDB = Helper.tryParseInt(System.getenv("CAB_LISTEN_PORT_DB"), 3306);
        String databaseDB = Helper.tryParseString(System.getenv("CAB_DATABASE_DB"), "ny_cab_data");
        String usernameDB = Helper.tryParseString(System.getenv("CAB_USERNAME_DB"), "root");
        String passwordDB = Helper.tryParseString(System.getenv("CAB_PASSWORD_DB"), "password");

        logger.info("environment variable CAB_HOST_DB=" + hostDB);
        logger.info("environment variable CAB_LISTEN_PORT_DB=" + portDB);
        logger.info("environment variable CAB_DATABASE_DB=" + databaseDB);
        logger.info("environment variable CAB_USERNAME_DB=" + usernameDB);
        logger.info("environment variable CAB_PASSWORD_DB=" + passwordDB);

        String hostRedis = Helper.tryParseString(System.getenv("CAB_HOST_REDIS"), "localhost");
        int portRedis = Helper.tryParseInt(System.getenv("CAB_LISTEN_PORT_REDIS"), 6379);

        logger.info("environment variable CAB_HOST_REDIS=" + hostRedis);
        logger.info("environment variable CAB_LISTEN_PORT_REDIS=" + portRedis);

        ConnectionPool<MySQLConnection> connection = MySQLConnectionBuilder.createConnectionPool(
                String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s", hostDB, portDB, databaseDB, usernameDB, passwordDB));
        connection.connect().get();
        logger.info("Connected to the database");

        Jedis jedis = new Jedis(hostRedis, portRedis);
        logger.info("Connected to Redis");

        var gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        JavalinJson.setFromJsonMapper(gson::fromJson);
        JavalinJson.setToJsonMapper(gson::toJson);

        Javalin app = Javalin.create().start(listenPort);
        logger.info("Application started");

        // Build service
        ServiceImpl service = buildService(connection, jedis);

        Routes routes = new Routes();
        routes.setRoutes(app, service);
    }
};