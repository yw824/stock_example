package com.leew.stock.service;

import com.leew.stock.domain.Stock;
import com.leew.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @Test
    public void 동시에_100개_요청() throws InterruptedException {
        // 동시에 100개의 요청이 들어오기 때문에 Multi-Thread 요청을 사용해야 한다.
        int threadCount = 100;

        // ExecutorService는 비동기로 실행하는 작업을 단순화하여 사용할 수 있게 도와 주는 Java API
        ExecutorService executorService = Executors.newFixedThreadPool(100);

        // 100개의 요청이 모두 끝날 때까지 기다려야 하므로 CountDownMatch를 사용한다.
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; ++i) {
            executorService.submit(() -> {
                try {
                    stockService.decrease(1L, 1L);
                } finally {
                    latch.countDown();
                }

            });
        }
        // CountDownLatch는 다른 Thread에서 수행 중인 작업이 완료될 때까지 대기할 수 있도록
        // 도와 주는 클래스
        latch.await();

        // 모든 요청이 완료된다면, stock을 가져온 다음 수량 비교
        Stock stock = stockRepository.findById(1L).orElseThrow();

        // 예상 시나리오 : 100개로 저장 후 하나의 시나리오마다 1개씩 제거하여, 최종 값은 0이 되기를 기대
        assertEquals(0, stock.getQuantity()); // 무조건 예상치를 앞에, 실제 값을 뒤에 넣자
        // 그러나 Test Fail : Race Condition이 일어났기 때문
    }
}
