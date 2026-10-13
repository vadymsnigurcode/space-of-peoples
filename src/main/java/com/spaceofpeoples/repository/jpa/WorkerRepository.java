package com.spaceofpeoples.repository.jpa;

import com.spaceofpeoples.entity.jpa.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
}