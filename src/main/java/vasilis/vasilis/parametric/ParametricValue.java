package vasilis.vasilis.parametric;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "parametric_values")
@Data
public class ParametricValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "type", length = 100)
    private String type;
}

