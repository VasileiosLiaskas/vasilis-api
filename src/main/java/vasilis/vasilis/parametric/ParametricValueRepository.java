package vasilis.vasilis.parametric;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParametricValueRepository extends JpaRepository<ParametricValue, Long> {

    List<ParametricValue> findAllByOrderByTypeAscDescriptionAsc();

    List<ParametricValue> findByTypeOrderByDescriptionAsc(String type);

    void deleteByType(String type);
}

