package me.machi;

import com.mongodb.MongoClientSettings;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

public class FruitCodec implements CollectibleCodec<Fruit> {

    private final Codec<Document> documentCodec;

    public FruitCodec() {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document.class);
    }

    @Override
    public void encode(BsonWriter writer, Fruit fruit, EncoderContext encoderContext) {
        Document doc = new Document();
        doc.put("name", fruit.getName());
        doc.put("description", fruit.getDescription());
        documentCodec.encode(writer, doc, encoderContext);
    }

    @Override
    public Class<Fruit> getEncoderClass() {
        return Fruit.class;
    }

    @Override
    public Fruit generateIdIfAbsentFromDocument(Fruit document) {
        if (!documentHasId(document)) {
            document.set_id(new ObjectId().toString());
        }
        return document;
    }

    @Override
    public boolean documentHasId(Fruit document) {
        return document.get_id() != null;
    }

    @Override
    public BsonValue getDocumentId(Fruit document) {
        return new BsonObjectId(new ObjectId(document.get_id()));
    }

    @Override
    public Fruit decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader, decoderContext);
        Fruit fruit = new Fruit();
        if (document.containsKey("_id")) {
            fruit.set_id(document.getObjectId("_id").toString());
        }
        fruit.setName(document.getString("name"));
        fruit.setDescription(document.getString("description"));
        return fruit;
    }
}