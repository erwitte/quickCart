package de.hsos.cart.entity;

import java.util.Map;

public record Cart(
        Map<Long, Integer> articleIdAndQuantity
) {
}
