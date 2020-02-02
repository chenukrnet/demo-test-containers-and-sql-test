package com.example.demosqltest.jpa.repositories;

import com.example.demosqltest.jpa.entity.HomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;


public interface HomeRepository extends JpaRepository<HomeEntity, Long> {

    //   @Query(value = "select * from home_entity he where  (he.id = :id and he.name <> :name) or (he.id <> :id and he.name = :name)", nativeQuery = true)
    @Query("select he " +
            "from HomeEntity he " +
            "where (he.id = :id and he.name <> :name) or (he.id <> :id and he.name = :name)")
    Optional<HomeEntity> findByDisjointFailed(@Param("id") Long id, @Param("name") String name);

    Collection<HomeEntity> findAllByName(String name);

    //   @Query(value = "select * from home_entity he where  (he.id = :id and he.name <> :name) or (he.id <> :id and he.name = :name)", nativeQuery = true)
    @Query("select he " +
            "from HomeEntity he " +
            "where (he.id = :id and he.name <> :name) or (he.id <> :id and he.name = :name)")
    Collection<HomeEntity> findByDisjoint(@Param("id") Long id, @Param("name") String name);
}
