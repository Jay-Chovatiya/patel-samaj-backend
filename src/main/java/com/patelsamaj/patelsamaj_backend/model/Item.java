package com.patelsamaj.patelsamaj_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "items")
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "item_name", nullable = false, unique = true)
    private String itemName;

    @Column(name = "current_price", nullable = false, precision = 5, scale = 2)
    private BigDecimal currentPrice;

    private String description;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name="is_deleted")
    private Boolean isDeleted;
}
