package com.leew.stock.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private Long quantity;

    public Stock() {}
    public Stock(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public void decrease(Long quantity) {
        if (this.quantity < quantity) {
            throw new RuntimeException("재고는 0개 미만이 될 수 없습니다");
        }
        // 재고가 0개 미만이라면 재고 수만큼 줄인다.
        this.quantity -= quantity;

    }
}
