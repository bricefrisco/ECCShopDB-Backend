package com.shopdb.ecocitycraft.shopdb.database.repositories;

import com.shopdb.ecocitycraft.shopdb.database.entities.Player;
import com.shopdb.ecocitycraft.shopdb.database.entities.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findOneByNameIgnoreCase(String name);

    Page<Player> findByTowns(Region town, Pageable pageable);

    @Query(value = "SELECT name FROM player ORDER BY name", nativeQuery = true)
    List<String> getPlayerNames();

    @Query(value = "SELECT name FROM player WHERE active = true ORDER BY name", nativeQuery = true)
    List<String> getActivePlayerNames();
}
