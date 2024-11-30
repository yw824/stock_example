package com.leew.stock.service;

import com.leew.stock.domain.Stock;
import com.leew.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class StockServiceTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private StockRepository stockRepository;

    // 테스트를 위해 모의 재고량이 있어야 하므로 테스트 수행 전에 생성한다.
    @BeforeEach
    public void before() {
        stockRepository.saveAndFlush(new Stock(1L, 100L));
    }

    // 테스트가 끝나면, 테스트에 사용한 모든 재고를 삭제한다.
    @AfterEach
    public void after() {
        stockRepository.deleteAll();
    }

    // 재고 로직에 대한 테스트 케이스
    @Test
    public void 재고감소() {
        // 처음 넣은 재고는 1이기 때문에, id를 1로 저장 + 개수 1개를 감소
        stockService.decrease(1L, 1L);

        // 1개를 제거하면, 제거한 양이 100 - 1 = 99개가 맞는 지 테스트
        // 감소시킨 후 다시 해당 재고의 데이터를 가져온다.
        Stock stock = stockRepository.findById(1L).orElseThrow(); // 없으면 예외 발생
        assertEquals(99, stock.getQuantity());
    }
}
