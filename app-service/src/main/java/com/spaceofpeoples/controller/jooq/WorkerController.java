package com.spaceofpeoples.controller.jooq;

import com.spaceofpeoples.entity.jooq.Worker;
import com.spaceofpeoples.repository.jooq.WorkerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController("jooqWorkerController")
@RequestMapping("/api/jooq/workers")
public class WorkerController {

	private final WorkerRepository workerRepository;

	public WorkerController(WorkerRepository workerRepository) {
		this.workerRepository = workerRepository;
	}

	@GetMapping
	public List<Worker> findAll() {
		return workerRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Worker> findById(@PathVariable long id) {
		return workerRepository.findById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Worker> create(@RequestBody Worker worker) {
		Worker created = workerRepository.insert(worker);
		return ResponseEntity.created(URI.create("/api/jooq/workers/" + created.getId())).body(created);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@PathVariable long id, @RequestBody Worker worker) {
		worker.setId(id);
		return workerRepository.update(worker)
				? ResponseEntity.noContent().build()
				: ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable long id) {
		return workerRepository.deleteById(id)
				? ResponseEntity.noContent().build()
				: ResponseEntity.notFound().build();
	}
}