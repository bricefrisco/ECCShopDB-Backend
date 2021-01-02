package com.shopdb.ecocitycraft.shopdb.database.entities;

import com.shopdb.ecocitycraft.shopdb.database.entities.embedded.Location;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "server"}))
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Server server;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="x", column=@Column(name="i_x")),
            @AttributeOverride(name="y", column=@Column(name="i_y")),
            @AttributeOverride(name="z", column=@Column(name="i_z"))
    })
    private Location iBounds;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="x", column=@Column(name="o_x")),
            @AttributeOverride(name="y", column=@Column(name="o_y")),
            @AttributeOverride(name="z", column=@Column(name="o_z"))
    })
    private Location oBounds;

    @OneToMany(mappedBy = "town")
    private List<ChestShopSign> chestShopSigns;

    @ManyToMany
    private List<Player> mayors;

    private Boolean active = false;

    private Timestamp lastUpdated;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Server getServer() {
        return server;
    }

    public Location getiBounds() {
        return iBounds;
    }

    public Location getoBounds() {
        return oBounds;
    }

    public List<ChestShopSign> getChestShopSigns() {
        return chestShopSigns;
    }

    public List<Player> getMayors() {
        return mayors;
    }

    public Boolean isActive() {
        return active;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setiBounds(Location iBounds) {
        this.iBounds = iBounds;
    }

    public void setoBounds(Location oBounds) {
        this.oBounds = oBounds;
    }

    public void setChestShopSigns(List<ChestShopSign> chestShopSigns) {
        this.chestShopSigns = chestShopSigns;
    }

    public void setMayors(List<Player> mayors) {
        this.mayors = mayors;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "Region{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", server=" + server +
                ", iBounds=" + iBounds +
                ", oBounds=" + oBounds +
                ", lastUpdated=" + lastUpdated +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Region that = (Region) o;
        return id.equals(that.id);
    }
}
