package com.mk.BackendQuiz.dto.Reporting;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class InventoryStatus {
    private int weekNumber;
    private Long productsInInventory;

    public InventoryStatus(int weekNumber, Long productsInInventory) {
        this.weekNumber = weekNumber;
        this.productsInInventory = productsInInventory;
    }
}
