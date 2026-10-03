package com.example.boardproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @EnableJpaAuditing: BaseEntity에 있는 @CreatedDate, @LastModifiedDate가
 *                      실제로 동작하도록 활성화하는 설정입니다. 이게 없으면
 *                      두 필드는 그냥 계속 null로 남습니다.
 */
@EnableJpaAuditing
@SpringBootApplication
public class BoardProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoardProjectApplication.class, args);
    }

}