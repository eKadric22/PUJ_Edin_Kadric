package DataBase;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDbConnection {
    private static final String URI = "mongodb://localhost:27017"; // Replace with your MongoDB URI
    private static final String DATABASE_NAME = "staffProjectDB";

    private MongoClient mongoClient;
    private MongoDatabase database;

    public MongoDbConnection() {
        mongoClient = MongoClients.create(URI);
        database = mongoClient.getDatabase(DATABASE_NAME);
        System.out.println("Connected to MongoDB!");
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Connection to MongoDB closed!");
        }
    }
}
