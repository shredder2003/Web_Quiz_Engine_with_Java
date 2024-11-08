package engine;

import engine.security.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserQuizCompletedRepository extends JpaRepository<UserQuizCompleted, Integer>, PagingAndSortingRepository<UserQuizCompleted, Integer> {

    Page<UserQuizCompleted> findByEmailIgnoreCase(String email, Pageable pageable);

}
