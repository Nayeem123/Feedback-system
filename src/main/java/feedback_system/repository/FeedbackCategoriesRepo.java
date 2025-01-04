package feedback_system.repository;

import feedback_system.entity.FeedbackCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackCategoriesRepo extends JpaRepository<FeedbackCategory,Long> {
    FeedbackCategory findByCategoryName(String categoryName);
}
