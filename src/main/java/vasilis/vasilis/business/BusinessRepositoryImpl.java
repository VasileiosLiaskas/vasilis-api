package vasilis.vasilis.business;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TemporalType;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import vasilis.vasilis.business.DTO.BusinessArgsDTO;
import vasilis.vasilis.business.DTO.BusinessDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Primary
@Repository
public class BusinessRepositoryImpl implements BusinessRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<BusinessDTO> searchBusinessByKeyword(int page, int size, String keyword, Date dateFrom, Date dateTo) {
        StringBuilder hql = new StringBuilder("SELECT new vasilis.vasilis.business.DTO.BusinessDTO(b.id, b.type, b.who, b.area, b.details, b.date, b.comments) ")
                .append("FROM Business b WHERE 1=1 ");

        List<Object> params = new ArrayList<>();
        int paramIndex = 1;

        // Add condition for keyword search
        if (keyword != null && !keyword.trim().isEmpty()) {
            hql.append("AND (LOWER(b.type) LIKE LOWER(?").append(paramIndex).append(") ")
                    .append("OR LOWER(b.who) LIKE LOWER(?").append(paramIndex + 1).append(") ")
                    .append("OR LOWER(b.area) LIKE LOWER(?").append(paramIndex + 2).append(") ")
                    .append("OR LOWER(b.details) LIKE LOWER(?").append(paramIndex + 3).append(") ")
                    .append("OR LOWER(b.comments) LIKE LOWER(?").append(paramIndex + 4).append(")) ");
            String keywordPattern = "%" + keyword.toLowerCase() + "%";
            for (int i = 0; i < 5; i++) {
                params.add(keywordPattern);
            }
            paramIndex += 5;
        }

        // Add condition for date range
        if (dateFrom != null) {
            hql.append("AND b.date >= ?").append(paramIndex).append(" ");
            params.add(dateFrom);
            paramIndex++;
        }
        if (dateTo != null) {
            hql.append("AND b.date <= ?").append(paramIndex).append(" ");
            params.add(dateTo);
        }

        // Add pagination
        hql.append("ORDER BY b.date DESC");

        // Create TypedQuery
        TypedQuery<BusinessDTO> query = entityManager.createQuery(hql.toString(), BusinessDTO.class);

        // Set parameters dynamically
        setQueryParameters(query, params);

        // Set pagination
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        List<BusinessDTO> results = query.getResultList();
        long total = getTotalCount(keyword, dateFrom, dateTo);

        return new PageImpl<>(results, PageRequest.of(page, size), total);
    }

    private long getTotalCount(String keyword, Date dateFrom, Date dateTo) {
        StringBuilder hql = new StringBuilder("SELECT COUNT(b) FROM Business b WHERE 1=1 ");
        List<Object> params = new ArrayList<>();
        int paramIndex = 1;

        // Add condition for keyword search
        if (keyword != null && !keyword.trim().isEmpty()) {
            hql.append("AND (LOWER(b.type) LIKE LOWER(?").append(paramIndex).append(") ")
                    .append("OR LOWER(b.who) LIKE LOWER(?").append(paramIndex + 1).append(") ")
                    .append("OR LOWER(b.area) LIKE LOWER(?").append(paramIndex + 2).append(") ")
                    .append("OR LOWER(b.details) LIKE LOWER(?").append(paramIndex + 3).append(") ")
                    .append("OR LOWER(b.comments) LIKE LOWER(?").append(paramIndex + 4).append(")) ");
            String keywordPattern = "%" + keyword.toLowerCase() + "%";
            for (int i = 0; i < 5; i++) {
                params.add(keywordPattern);
            }
            paramIndex += 5;
        }

        if (dateFrom != null) {
            hql.append("AND b.date >= ?").append(paramIndex).append(" ");
            params.add(dateFrom);
            paramIndex++;
        }
        if (dateTo != null) {
            hql.append("AND b.date <= ?").append(paramIndex).append(" ");
            params.add(dateTo);
        }

        TypedQuery<Long> query = entityManager.createQuery(hql.toString(), Long.class);
        setQueryParameters(query, params);

        return query.getSingleResult();
    }

    private <T> void setQueryParameters(TypedQuery<T> query, List<Object> params) {
        for (int i = 0; i < params.size(); i++) {
            Object param = params.get(i);
            if (param instanceof Date) {
                query.setParameter(i + 1, (Date) param, TemporalType.TIMESTAMP); // Explicitly define TemporalType
            } else {
                query.setParameter(i + 1, param);
            }
        }
    }
}
