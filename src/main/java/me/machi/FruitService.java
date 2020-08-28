package me.machi;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import io.quarkus.arc.Unremovable;
import org.bson.BsonDocument;
import org.bson.BsonObjectId;
import org.bson.types.ObjectId;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Unremovable
@Named("fruit-service")
public class FruitService {

    @Inject
    MongoClient mongoClient;

    // TODO filter
    public List<Fruit> list(String filter) {
        List<Fruit> list = new ArrayList<>();
        MongoCursor<Fruit> cursor = getCollection().find().iterator();
        try {
            while (cursor.hasNext()) {
                list.add(cursor.next());
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    public Fruit add(Fruit fruit) {
        getCollection().insertOne(fruit);
        return getCollection().find().first();
    }

    public Fruit findOne(String id) {
        BsonObjectId _id = new BsonObjectId(new ObjectId(id));
        BsonDocument bson = new BsonDocument();
        bson.append("_id", _id);
        return getCollection()
                .find(bson).first();
    }

    public Fruit delete(String id) {
        BsonObjectId _id = new BsonObjectId(new ObjectId(id));
        BsonDocument bson = new BsonDocument();
        bson.append("_id", _id);
        return getCollection()
                .findOneAndDelete(bson);
    }

    public Fruit update(String id, String value) {
        BsonObjectId _id = new BsonObjectId(new ObjectId(id));
        BsonDocument bson = new BsonDocument();
        bson.append("_id", _id);
        FindOneAndUpdateOptions findOneAndUpdateOptions = new FindOneAndUpdateOptions();
        findOneAndUpdateOptions.returnDocument(ReturnDocument.AFTER);

        return getCollection()
                .findOneAndUpdate(bson, BsonDocument.parse(value), findOneAndUpdateOptions);
    }

    // TODO filters
    public long count(String filter) {
        return getCollection().countDocuments();
    }

    private MongoCollection<Fruit> getCollection() {
        return mongoClient.getDatabase("fruits").getCollection("fruit", Fruit.class);
    }
}