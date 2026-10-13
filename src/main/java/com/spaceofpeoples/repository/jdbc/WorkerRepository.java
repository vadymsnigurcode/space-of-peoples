package com.spaceofpeoples.repository.jdbc;

import com.spaceofpeoples.entity.jdbc.Worker;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class WorkerRepository {

	private static final String SELECT_COLUMNS = "id, first_name, last_name, email";
	private static final RowMapper<Worker> WORKER_ROW_MAPPER = (resultSet, rowNumber) -> new Worker(
			resultSet.getLong("id"),
			resultSet.getString("first_name"),
			resultSet.getString("last_name"),
			resultSet.getString("email"));

	private final JdbcTemplate jdbcTemplate;

	public WorkerRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Worker> findAll() {
		return jdbcTemplate.query("SELECT " + SELECT_COLUMNS + " FROM workers", WORKER_ROW_MAPPER);
	}

	public Optional<Worker> findById(long id) {
		return jdbcTemplate.query(
				"SELECT " + SELECT_COLUMNS + " FROM workers WHERE id = ?",
				WORKER_ROW_MAPPER,
				id).stream().findFirst();
	}

	public Worker insert(Worker worker) {
		Long id = jdbcTemplate.queryForObject(
				"INSERT INTO workers (first_name, last_name, email) VALUES (?, ?, ?) RETURNING id",
				(resultSet, rowNumber) -> resultSet.getLong("id"),
				worker.getFirstName(),
				worker.getLastName(),
				worker.getEmail());
		worker.setId(id);
		return worker;
	}

	public boolean update(Worker worker) {
		if (worker.getId() == null) {
			return false;
		}

		return jdbcTemplate.update(
				"UPDATE workers SET first_name = ?, last_name = ?, email = ? WHERE id = ?",
				worker.getFirstName(),
				worker.getLastName(),
				worker.getEmail(),
				worker.getId()) > 0;
	}

	public boolean deleteById(long id) {
		return jdbcTemplate.update("DELETE FROM workers WHERE id = ?", id) > 0;
	}
}