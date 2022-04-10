package org.zibble.dbedwars.database.jooq.sql;

import org.jooq.DSLContext;
import org.zibble.dbedwars.api.future.ActionFuture;

import java.util.function.Supplier;

public interface ISQL {

    DSLContext context();

    class Stage<T> {

        private final Supplier<T> supplier;

        private Stage(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        public static <T> Stage<T> of(Supplier<T> supplier) {
            return new Stage<>(supplier);
        }

        public ActionFuture<T> executeAsync() {
            return ActionFuture.supplyAsync(supplier);
        }

        public T execute() {
            return supplier.get();
        }

    }

}
