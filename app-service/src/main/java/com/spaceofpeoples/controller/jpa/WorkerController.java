package com.spaceofpeoples.controller.jpa;

import com.spaceofpeoples.entity.jpa.Worker;
import com.spaceofpeoples.repository.jpa.WorkerRepository;
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

@RestController("jpaWorkerController")
@RequestMapping("/api/jpa/workers")
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
		worker.setId(null);
		Worker created = workerRepository.save(worker);
		return ResponseEntity.created(URI.create("/api/jpa/workers/" + created.getId())).body(created);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@PathVariable long id, @RequestBody Worker worker) {
		if (!workerRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}

		worker.setId(id);
		workerRepository.save(worker);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable long id) {
		if (!workerRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}

		workerRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}