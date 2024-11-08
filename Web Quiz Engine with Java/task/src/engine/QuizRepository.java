package engine;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuizRepository extends JpaRepository<Quiz, Integer>, PagingAndSortingRepository<Quiz, Integer> {
}
