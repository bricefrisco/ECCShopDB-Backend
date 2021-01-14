package com.shopdb.ecocitycraft.shopdb.database.repositories;

import com.shopdb.ecocitycraft.shopdb.database.entities.ChestShopSign;
import com.shopdb.ecocitycraft.shopdb.database.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ChestShopSignRepository extends JpaRepository<ChestShopSign, String>, JpaSpecificationExecutor<ChestShopSign> {

    @Query(value = "SELECT DISTINCT material FROM chest_shop_sign WHERE is_hidden = false ORDER BY material ASC", nativeQuery = true)
    List<String> findDistinctMaterials();

    @Query(value = "SELECT DISTINCT material FROM chest_shop_sign WHERE is_hidden = false AND is_buy_sign = true ORDER BY material ASC", nativeQuery = true)
    List<String> findDistinctBuyMaterials();

    @Query(value = "SELECT DISTINCT material FROM chest_shop_sign WHERE is_hidden = false AND is_sell_sign = true ORDER BY material ASC", nativeQuery = true)
    List<String> findDistinctSellMaterials();

    @Query(value = "SELECT DISTINCT material FROM chest_shop_sign WHERE is_hidden = false AND server = :server ORDER BY material ASC", nativeQuery = true)
    List<String> findDistinctMaterialsByServer(@Param("server") String server);

    @Query(value="SELECT DISTINCT material FROM chest_shop_sign WHERE is_hidden = false AND server = :server AND is_buy_sign = true ORDER BY material ASC", nativeQuery = true)
    List<String> findDistinctBuyMaterialsByServer(@Param("server") String server);

    @Query(value="SELECT DISTINCT material FROM chest_shop_sign WHERE is_hidden = false AND server = :server AND is_sell_sign = true ORDER BY material ASC", nativeQuery = true)
    List<String> findDistinctSellMaterialsByServer(@Param("server") String server);

    @Query(value="SELECT * FROM chest_shop_sign WHERE " +
            "server = :server AND " +
            ":lx <= x AND :ux >= x AND " +
            ":ly <= y AND :uy >= y AND " +
            ":lz <= z AND :uz >= z", nativeQuery = true)
    List<ChestShopSign> getChestShopSignByLocation(@Param("server") String server, @Param("lx") int lx, @Param("ux") int ux, @Param("ly") int ly, @Param("uy") int uy, @Param("lz") int lz, @Param("uz") int uz);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM chest_shop_sign WHERE id IN :ids", nativeQuery = true)
    void deleteByIdIn(@Param("ids") List<String> ids);
}
