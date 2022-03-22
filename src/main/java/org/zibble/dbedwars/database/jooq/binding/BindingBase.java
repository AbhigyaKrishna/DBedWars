package org.zibble.dbedwars.database.jooq.binding;

import org.jooq.*;
import org.jooq.impl.AbstractBinding;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BindingBase<T, U> extends AbstractBinding<T, U> {

    public abstract void set(SQLDialect dialect, PreparedStatement statement, int index, U value) throws SQLException;

    public abstract U get(SQLDialect dialect, ResultSet resultSet, int index) throws SQLException;

    public abstract Field<?> inline(SQLDialect dialect, U value);

    @Override
    public final void set(BindingSetStatementContext<U> ctx) throws SQLException {
        this.set(ctx.family(), ctx.statement(), ctx.index(), ctx.value());
    }

    @Override
    public final void get(BindingGetResultSetContext<U> ctx) throws SQLException {
        U value = this.get(ctx.dialect(), ctx.resultSet(), ctx.index());
        ctx.value(value);
    }

    @Override
    protected final void sqlInline(BindingSQLContext<U> ctx) {
        ctx.render().visit(inline(ctx.family(), ctx.value()));
    }

}
