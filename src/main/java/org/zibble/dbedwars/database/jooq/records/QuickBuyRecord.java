package org.zibble.dbedwars.database.jooq.records;

import org.jooq.impl.UpdatableRecordImpl;
import org.zibble.dbedwars.database.data.QuickBuy;
import org.zibble.dbedwars.database.data.QuickBuyData;
import org.zibble.dbedwars.database.jooq.tables.QuickBuyTable;

import java.util.UUID;

public class QuickBuyRecord extends UpdatableRecordImpl<QuickBuyRecord> {

    public static QuickBuyRecord newRecord(UUID uuid, String name) {
        QuickBuyTable table = QuickBuyTable.QUICK_BUY;
        QuickBuyRecord record = new QuickBuyRecord();
        record.set(table.UUID, uuid);
        record.set(table.NAME, name);
        record.set(table.QUICK_BUY_DATA, new QuickBuyData());
        return record;
    }

    public static QuickBuyRecord fromDataCache(QuickBuy quickBuy) {
        QuickBuyTable table = QuickBuyTable.QUICK_BUY;
        QuickBuyRecord record = new QuickBuyRecord();
        record.set(table.UUID, quickBuy.getUuid());
        record.set(table.NAME, quickBuy.getName());
        record.set(table.QUICK_BUY_DATA, quickBuy.getData());
        return record;
    }

    public QuickBuyRecord() {
        super(QuickBuyTable.QUICK_BUY);
    }

    public QuickBuy toDataCache() {
        QuickBuy data = new QuickBuy();
        data.setUuid(this.get(QuickBuyTable.QUICK_BUY.UUID));
        data.setName(this.get(QuickBuyTable.QUICK_BUY.NAME));
        data.setData(this.get(QuickBuyTable.QUICK_BUY.QUICK_BUY_DATA));
        return data;
    }

}
