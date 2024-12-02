package com.leew.stock.service;

import com.leew.stock.domain.Stock;
import com.leew.stock.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PessimisticLockStockService {

    // 이 Service에서도 Stock에 대한 CRUD가 가능하도록 Repository를 필드로 추가
    // 및 생성자 생성
    @Autowired
    private final StockRepository stockRepository;

    public PessimisticLockStockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public void decrease(Long id, Long quantity) {
        // 먼저 Lock을 가져온 후에
        Stock stock = stockRepository.findByIdWithPessimisticLock(id);
        // 값을 수정하고,
        stock.decrease(quantity);
        // 수정된 값을 저장한다.
        stockRepository.save(stock);
    }
}
