package com.example.demofileuploadtransaction.repository;

import com.example.demofileuploadtransaction.model.MediaResources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaResourcesRepository extends JpaRepository<MediaResources, Long> {
}
