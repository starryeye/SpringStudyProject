package dev.practice.multipledatasources;

import dev.practice.multipledatasources.repository.memo.MemoEntity;
import dev.practice.multipledatasources.repository.memo.MemoRepository;
import dev.practice.multipledatasources.repository.todo.TodoEntity;
import dev.practice.multipledatasources.repository.todo.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
@SpringBootApplication
public class MultipledatasourcesApplication {

	// 참고
	//https://www.baeldung.com/spring-boot-configure-multiple-datasources

	private final MemoRepository memoRepository;
	private final TodoRepository todoRepository;

	public static void main(String[] args) {
		SpringApplication.run(MultipledatasourcesApplication.class, args);
	}

	/**
	 * todo, 글로벌 트랜잭션, JtaTransactionManager..
	 */

//	@Bean
//	public ApplicationRunner runner() {
//		return args -> {
//
//			MemoEntity memo = MemoEntity.create("memo title", "memo content");
//			memoRepository.save(memo);
//
//			TodoEntity todo = TodoEntity.create("todo title", Boolean.TRUE);
//			todoRepository.save(todo);
//
//			Integer end = 0;
//		};
//	}

}
