package vasilis.vasilis.business;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Integer>, BusinessRepositoryCustom {

    @Override
    @EntityGraph(attributePaths = "invoices")
    Page<Business> findAll(Pageable pageable);
}
