package org.zibble.dbedwars.database.jooq.binding;

import org.jetbrains.annotations.NotNull;
import org.jooq.Converter;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.zibble.dbedwars.api.util.Color;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ColorBinding extends BindingBase<Byte, Color> {

    @Override
    public void set(SQLDialect dialect, PreparedStatement statement, int index, Color value) throws SQLException {
        statement.setByte(index, value.getData());
    }

    @Override
    public Color get(SQLDialect dialect, ResultSet resultSet, int index) throws SQLException {
        return Color.from(resultSet.getByte(index)).get();
    }

    @Override
    public Field<?> inline(SQLDialect dialect, Color value) {
        return DSL.inline(value.getData(), SQLDataType.TINYINT);
    }

    @Override
    public @NotNull Converter<Byte, Color> converter() {
        return Converter.of(Byte.class, Color.class, b -> Color.from(b).get(), Color::getData);
    }

}
