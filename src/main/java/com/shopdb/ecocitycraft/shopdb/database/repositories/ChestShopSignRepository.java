package com.shopdb.ecocitycraft.shopdb.database.repositories;

import com.shopdb.ecocitycraft.shopdb.database.entities.ChestShopSign;
import com.shopdb.ecocitycraft.shopdb.database.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ChestShopSignRepository extends JpaRepository<ChestShopSign, Long>, JpaSpecificationExecutor<ChestShopSign> {

    @Transactional
    long deleteByTown(Region town);

    @Query(value = "SELECT DISTINCT material FROM shopdb2.chest_shop_sign ORDER BY material ASC", nativeQuery = true)
    List<String> findDistinctMaterials();

    @Query(value = "SELECT DISTINCT material FROM shopdb2.chest_shop_sign WHERE is_buy_sign = true ORDER BY material ASC", nativeQuery = true)
    List<String> findDistinctBuyMaterials();

    @Query(value = "SELECT DISTINCT material FROM shopdb2.chest_shop_sign WHERE is_sell_sign = true ORDER BY material ASC", nativeQuery = true)
    List<String> findDistinctSellMaterials();

    @Query(value = "SELECT DISTINCT material FROM shopdb2.chest_shop_sign WHERE server = :server ORDER BY material ASC", nativeQuery = true)
    List<String> findDistinctMaterialsByServer(@Param("server") String server);

    @Query(value="SELECT DISTINCT material FROM shopdb2.chest_shop_sign WHERE server = :server AND is_buy_sign = true ORDER BY material ASC", nativeQuery = true)
    List<String> findDistinctBuyMaterialsByServer(@Param("server") String server);

    @Query(value="SELECT DISTINCT material FROM shopdb2.chest_shop_sign WHERE server = :server AND is_sell_sign = true ORDER BY material ASC", nativeQuery = true)
    List<String> findDistinctSellMaterialsByServer(@Param("server") String server);
}
