package com.spaceofpeoples.repository.jooq;

import com.spaceofpeoples.entity.jooq.Worker;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jooqWorkerRepository")
public class WorkerRepository {

	private static final Table<?> WORKERS = DSL.table(DSL.name("workers"));
	private static final Field<Long> ID = DSL.field(DSL.name("id"), Long.class);
	private static final Field<String> FIRST_NAME = DSL.field(DSL.name("first_name"), String.class);
	private static final Field<String> LAST_NAME = DSL.field(DSL.name("last_name"), String.class);
	private static final Field<String> EMAIL = DSL.field(DSL.name("email"), String.class);

	private final DSLContext dsl;

	public WorkerRepository(DSLContext dsl) {
		this.dsl = dsl;
	}

	public List<Worker> findAll() {
		return dsl.select(ID, FIRST_NAME, LAST_NAME, EMAIL)
				.from(WORKERS)
				.fetch(this::toWorker);
	}

	public Optional<Worker> findById(long id) {
		return dsl.select(ID, FIRST_NAME, LAST_NAME, EMAIL)
				.from(WORKERS)
				.where(ID.eq(id))
				.fetchOptional(this::toWorker);
	}

	public Worker insert(Worker worker) {
		return dsl.insertInto(WORKERS)
				.set(FIRST_NAME, worker.getFirstName())
				.set(LAST_NAME, worker.getLastName())
				.set(EMAIL, worker.getEmail())
				.returning(ID, FIRST_NAME, LAST_NAME, EMAIL)
				.fetchOne(this::toWorker);
	}

	public boolean update(Worker worker) {
		if (worker.getId() == null) {
			return false;
		}

		return dsl.update(WORKERS)
				.set(FIRST_NAME, worker.getFirstName())
				.set(LAST_NAME, worker.getLastName())
				.set(EMAIL, worker.getEmail())
				.where(ID.eq(worker.getId()))
				.execute() > 0;
	}

	public boolean deleteById(long id) {
		return dsl.deleteFrom(WORKERS)
				.where(ID.eq(id))
				.execute() > 0;
	}

	private Worker toWorker(Record record) {
		return new Worker(
				record.get(ID),
				record.get(FIRST_NAME),
				record.get(LAST_NAME),
				record.get(EMAIL));
	}
}