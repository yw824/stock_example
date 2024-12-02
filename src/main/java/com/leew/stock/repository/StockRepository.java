package com.leew.stock.repository;

import com.leew.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.persistence.LockModeType; // 강의에서는 javax.persistence.LockModeType이지만, jakarta로 이전됨

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    @Lock(value=LockModeType.PESSIMISTIC_WRITE) // JPA에서는 Lock이라는 Annotation 사용하여 손쉽게 비관적 락울 구현할 수 있다.
    @Query("select s from Stock s where s.id= :id") // Native Query 사용해서 가져온다.
    Stock findByIdWithPessimisticLock(@Param("id") Long id);
    // Param 바인딩 설정 안하면 틀린 결과가 나온다. 강의에서는 없어도 동작한다. 최근에 추가된 기능인 것 같다.
}
