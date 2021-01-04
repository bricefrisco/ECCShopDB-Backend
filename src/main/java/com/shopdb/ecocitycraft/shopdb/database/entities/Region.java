package com.shopdb.ecocitycraft.shopdb.database.entities;

import com.shopdb.ecocitycraft.shopdb.database.entities.embedded.Location;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "server"}))
@Getter
@Setter
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

    @Override
    public String toString() {
        return "Region{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", server=" + server +
                ", iBounds=" + iBounds +
                ", oBounds=" + oBounds +
                ", lastUpdated=" + lastUpdated +
                ", active=" + active +
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
