package de.hsos.cart.entity;

import java.util.List;

public record Cart(
        List<Long> articleId
) {
}
