package com.josedev.axity_consolidation_back.persistence.repository;

import com.josedev.axity_consolidation_back.persistence.entity.SucursalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SucursalRepository extends JpaRepository<SucursalEntity, String> {
    // No son necesarios métodos adicionales, ya que JpaRepository proporciona
    // los métodos básicos como findAll, findById, save, delete, etc.
}