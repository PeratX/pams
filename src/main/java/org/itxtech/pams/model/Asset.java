package org.itxtech.pams.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "assets")
public class Asset {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String label;

    @Column(nullable = false)
    private int value;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int amount = 0;

    @OneToMany(mappedBy = "asset")
    private Set<AssetLog> logs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Set<AssetLog> getLogs() {
        return logs;
    }

    public void setLogs(Set<AssetLog> logs) {
        this.logs = logs;
    }

    @Override
    public String toString() {
        return "Asset{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", value=" + value +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                '}';
    }
}
