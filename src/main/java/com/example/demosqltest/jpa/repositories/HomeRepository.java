package com.example.demosqltest.jpa.repositories;

import com.example.demosqltest.jpa.entity.HomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface HomeRepository extends JpaRepository<HomeEntity, Long> {
    @Query("select he.id as id , he.name as name " +
            "from HomeEntity he " +
            "where (he.id = :id and he.name <> :name) or (he.id <> :id and he.name = :name)")
    Optional<HomeEntity> findByDisjointFailed(@Param("id") Long id, @Param("name") String name);
}
