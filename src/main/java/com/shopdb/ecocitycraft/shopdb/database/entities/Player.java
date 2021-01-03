package com.shopdb.ecocitycraft.shopdb.database.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Size(min = 3, max = 16)
    private String name;

    private Timestamp lastSeen;

    private Timestamp lastUpdated;

    @ManyToMany(mappedBy = "mayors")
    private List<Region> towns;

    @OneToMany(mappedBy = "owner")
    private List<ChestShopSign> chestShopSigns;

    private Boolean active;

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastSeen=" + lastSeen +
                ", lastUpdated=" + lastUpdated +
                ", active=" + active +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player that = (Player) o;
        return id.equals(that.id);
    }
}
