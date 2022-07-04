package org.zibble.dbedwars.database.mongo.codec;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.zibble.dbedwars.api.util.NumberUtils;
import org.zibble.dbedwars.database.data.QuickBuyData;

import java.util.Map;

public class QuickBuyDataCodec implements Codec<QuickBuyData> {

    @Override
    public QuickBuyData decode(BsonReader reader, DecoderContext decoderContext) {
        QuickBuyData quickBuyData = new QuickBuyData();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String fieldName = reader.readName();
            quickBuyData.set(NumberUtils.toInt(fieldName), reader.readString());
        }
        return quickBuyData;
    }

    @Override
    public void encode(BsonWriter writer, QuickBuyData value, EncoderContext encoderContext) {
        writer.writeStartDocument();
        for (Map.Entry<Integer, String> entry : value.getSlots().entrySet()) {
            writer.writeName(String.valueOf(entry.getKey()));
            writer.writeString(entry.getValue());
        }
        writer.writeEndDocument();
    }

    @Override
    public Class<QuickBuyData> getEncoderClass() {
        return QuickBuyData.class;
    }

}
