package com.leew.stock.service;

import com.leew.stock.domain.Stock;
import com.leew.stock.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    @Autowired
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void decrease(Long id, Long quantity) {
        // Stock 조회
        // 재고를 감소시킨 뒤에, 갱신된 값을 저장하려고 한다.

        // Stock 조회
        Stock stock = stockRepository.findById(id).orElseThrow();
        // 재고 감소
        stock.decrease(quantity);

        // 갱신된 값을 저장
        stockRepository.save(stock);
    }
}
