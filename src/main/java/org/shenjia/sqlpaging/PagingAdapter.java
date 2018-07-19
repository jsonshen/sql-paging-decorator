package org.shenjia.sqlpaging;

import java.util.Iterator;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.function.Function;

import org.mybatis.dynamic.sql.render.RenderingStrategy;
import org.mybatis.dynamic.sql.select.SelectModel;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;

public class PagingAdapter<R> {

	private SelectModel selectModel;
	private Function<SelectStatementProvider, R> mapperMethod;
	private int limit;
	private int offset;

	private PagingAdapter(SelectModel selectModel, Function<SelectStatementProvider, R> mapperMethod, int limit,
			int offset) {
		this.selectModel = Objects.requireNonNull(selectModel);
		this.mapperMethod = Objects.requireNonNull(mapperMethod);
		this.limit = limit;
		this.offset = offset;
	}

	public static <R> PagingAdapter<R> of(SelectModel selectModel, Function<SelectStatementProvider, R> mapperMethod,
			int limit, int offset) {
		return new PagingAdapter<>(selectModel, mapperMethod, limit, offset);
	}

	public R execute() {
		SelectStatementProvider ssp = selectModel.render(RenderingStrategy.MYBATIS3);
		return mapperMethod.apply(pagingDecorator().decorate(ssp, limit, offset));
	}

	public PagingDecorator pagingDecorator() {
		// Create a PagingDecorator by System property.
		String interfaceName = PagingDecorator.class.getName();
		String implClassName = System.getProperty(interfaceName);
		if (null != implClassName) {
			try {
				return (PagingDecorator) Class.forName(implClassName).getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				throw new PagingException(
						e.getMessage() + ", System.getProperty(\"" + interfaceName + "\"); return is " + implClassName);
			}
		}
		// Create a PagingDecorator by SPI.
		Iterator<PagingDecorator> iterator = ServiceLoader.load(PagingDecorator.class).iterator();
		while (iterator.hasNext()) {
			return iterator.next();
		}
		// Not found PagingDecorator
		throw new PagingException("Please define a paging decorator, Method 1 has a higher priority. "
				+ "Method 1: Call System.setProperty(\"" + interfaceName + "\", pagingDecoratorClassName); method."
				+ "Method 2: Set the META-INF/services/" + interfaceName + " file.");
	}

}
