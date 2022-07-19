package org.zibble.dbedwars.database.jooq.records;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;
import org.zibble.dbedwars.database.data.QuickBuy;
import org.zibble.dbedwars.database.data.QuickBuyData;
import org.zibble.dbedwars.database.jooq.tables.QuickBuyTable;

import java.util.UUID;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class QuickBuyRecord extends UpdatableRecordImpl<QuickBuyRecord> implements Record3<UUID, String, QuickBuyData> {

    public QuickBuyRecord() {
        super(QuickBuyTable.QUICK_BUY);
    }

    public QuickBuyRecord(UUID uuid, String name, QuickBuyData quickBuyData) {
        super(QuickBuyTable.QUICK_BUY);

        setUuid(uuid);
        setName(name);
        setQuickBuyData(quickBuyData);
    }

    public QuickBuyRecord(QuickBuy quickBuy) {
        super(QuickBuyTable.QUICK_BUY);

        setUuid(quickBuy.getUuid());
        setName(quickBuy.getName());
        setQuickBuyData(quickBuy.getData());
    }

    public QuickBuy toDataCache() {
        QuickBuy data = new QuickBuy();
        data.setUuid(this.getUuid());
        data.setName(this.getName());
        data.setData(this.getQuickBuyData());
        return data;
    }

    public void setUuid(UUID value) {
        set(0, value);
    }

    public UUID getUuid() {
        return (UUID) get(0);
    }

    public void setName(String value) {
        set(1, value);
    }

    public String getName() {
        return (String) get(1);
    }

    public void setQuickBuyData(QuickBuyData value) {
        set(2, value);
    }

    public QuickBuyData getQuickBuyData() {
        return (QuickBuyData) get(2);
    }

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    @Override
    public Row3<UUID, String, QuickBuyData> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<UUID, String, QuickBuyData> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return QuickBuyTable.QUICK_BUY.UUID;
    }

    @Override
    public Field<String> field2() {
        return QuickBuyTable.QUICK_BUY.NAME;
    }

    @Override
    public Field<QuickBuyData> field3() {
        return QuickBuyTable.QUICK_BUY.QUICK_BUY_DATA;
    }

    @Override
    public UUID component1() {
        return getUuid();
    }

    @Override
    public String component2() {
        return getName();
    }

    @Override
    public QuickBuyData component3() {
        return getQuickBuyData();
    }

    @Override
    public UUID value1() {
        return getUuid();
    }

    @Override
    public String value2() {
        return getName();
    }

    @Override
    public QuickBuyData value3() {
        return getQuickBuyData();
    }

    @Override
    public QuickBuyRecord value1(UUID value) {
        setUuid(value);
        return this;
    }

    @Override
    public QuickBuyRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public QuickBuyRecord value3(QuickBuyData value) {
        setQuickBuyData(value);
        return this;
    }

    @Override
    public QuickBuyRecord values(UUID value1, String value2, QuickBuyData value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

}
