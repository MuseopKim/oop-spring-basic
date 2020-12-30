package hello.core.singleton;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StatefulServiceTest {

    @Test
    void statefulServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        // 테스트 단순화를 위해 실제 쓰레드를 사용하지는 않는다.
        // ThreadA : A 사용자 10000원 주문
        statefulService1.order("userA", 10000);

        // ThreadB : B 사용자 20000원 주문
        statefulService2.order("userB", 20000);

        // ThreadA : 사용자 주문 금액을 조회
        int price = statefulService1.getPrice();
        System.out.println("price = " + price);

        // 값이 공유 되므로 동등성이 보장되지 않는다.
        assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }

    static class TestConfig {

        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }
}
