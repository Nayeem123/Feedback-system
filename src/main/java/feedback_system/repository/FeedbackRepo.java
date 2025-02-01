package feedback_system.repository;

import feedback_system.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepo extends JpaRepository<Feedback,Long> {
    List<Feedback> findAllByUsername(String username);

    List<Feedback> findAllByCategoryName(String categoryName);
}
