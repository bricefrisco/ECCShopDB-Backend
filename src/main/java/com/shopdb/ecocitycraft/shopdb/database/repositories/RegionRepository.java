package com.shopdb.ecocitycraft.shopdb.database.repositories;

import com.shopdb.ecocitycraft.shopdb.database.entities.Player;
import com.shopdb.ecocitycraft.shopdb.database.entities.Region;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    Region findOneByServerAndNameIgnoreCase(Server server, String name);

    Page<Region> findByMayors(Player mayor, Pageable pageable);

    @Query(value = "SELECT DISTINCT name FROM shopdb2.region ORDER BY name", nativeQuery = true)
    List<String> findAllRegionNames();
    @Query(value = "SELECT DISTINCT name FROM shopdb2.region WHERE server = :server ORDER BY name", nativeQuery = true)
    List<String> findAllRegionsNamesByServer(@Param("server") String server);

    @Query(value = "SELECT DISTINCT name FROM shopdb2.region WHERE active = true ORDER BY name", nativeQuery = true)
    List<String> findActiveRegionNames();
    @Query(value = "SELECT DISTINCT name FROM shopdb2.region WHERE active = true AND server = :server ORDER BY name", nativeQuery = true)
    List<String> findActiveRegionNamesByServer(@Param("server") String server);

}
