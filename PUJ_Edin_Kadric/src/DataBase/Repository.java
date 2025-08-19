package DataBase;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.mongodb.client.model.Filters.eq;

public class Repository<T> {
    private final MongoCollection<Document> collection;
    private final Function<Document, T> fromDocument;
    private final Function<T, Document> toDocument;

    public Repository(MongoDatabase database, String collectionName, Function<Document, T> fromDocument, Function<T, Document> toDocument) {
        this.collection = database.getCollection(collectionName);
        this.fromDocument = fromDocument;
        this.toDocument = toDocument;
    }

    public void insert(T entity) {
        Document document = toDocument.apply(entity);
        if (!document.containsKey("_id")) {
            document.append("_id", new ObjectId().toHexString());
        }
        collection.insertOne(document);
        System.out.println("Inserted successfully: " + document);
    }

    public T findById(String id) {
        Document document = collection.find(eq("_id", id)).first();
        return document != null ? fromDocument.apply(document) : null;
    }

    // Read all
    public List<T> findAll() {
        List<T> results = new ArrayList<>();
        for (Document document : collection.find()) {
            results.add(fromDocument.apply(document));
        }
        return results;
    }

    public void update(String id, T updatedEntity) {
        Document updatedDocument = toDocument.apply(updatedEntity);
        collection.updateOne(eq("_id", id), new Document("$set", updatedDocument));
        System.out.println("Updated successfully: " + id);
    }

    public void delete(String id) {
        collection.deleteOne(eq("_id", id));
        System.out.println("Deleted successfully: " + id);
    }
}
