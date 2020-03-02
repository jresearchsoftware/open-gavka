package org.jresearch.gavka.dao;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Types;
import java.util.Objects;
import java.util.function.Function;

import org.jooq.Allow;
import org.jooq.Binding;
import org.jooq.BindingGetResultSetContext;
import org.jooq.BindingGetSQLInputContext;
import org.jooq.BindingGetStatementContext;
import org.jooq.BindingRegisterContext;
import org.jooq.BindingSQLContext;
import org.jooq.BindingSetSQLOutputContext;
import org.jooq.BindingSetStatementContext;
import org.jooq.Converter;
import org.jooq.JSONB;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

@Allow(SQLDialect.POSTGRES)
@SuppressWarnings({ "serial", "nls" })
public abstract class AbstractJsonBinder<U> implements Binding<JSONB, U> {

	private final Converter<JSONB, U> converter;

	public AbstractJsonBinder(final Class<U> userClass, final Function<JSONB, U> from, final Function<U, JSONB> to) {
		converter = Converter.of(JSONB.class, userClass, from, to);
	}

	@Override
	public Converter<JSONB, U> converter() {
		return converter;
	}

	@Override
	public void sql(final BindingSQLContext<U> ctx) throws SQLException {
		ctx.render().visit(DSL.val(ctx.convert(this.converter()).value())).sql("::jsonb");
	}

	@Override
	public void register(final BindingRegisterContext<U> ctx) throws SQLException {
		ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR);
	}

	@Override
	public void set(final BindingSetStatementContext<U> ctx) throws SQLException {
		ctx.statement().setString(ctx.index(), Objects.toString(ctx.convert(this.converter()).value(), null));
	}

	@Override
	public void set(final BindingSetSQLOutputContext<U> ctx) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void get(final BindingGetResultSetContext<U> ctx) throws SQLException {
		ctx.convert(this.converter()).value(JSONB.valueOf(ctx.resultSet().getString(ctx.index())));
	}

	@Override
	public void get(final BindingGetStatementContext<U> ctx) throws SQLException {
		ctx.convert(this.converter()).value(JSONB.valueOf(ctx.statement().getString(ctx.index())));
	}

	@Override
	public void get(final BindingGetSQLInputContext<U> ctx) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}
}
