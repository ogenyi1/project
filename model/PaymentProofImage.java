package ng.optisoft.rosabon.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import ng.optisoft.rosabon.enums.EntityStatus;
import ng.optisoft.rosabon.treasury.model.TrPlan;

@Data
@Entity
public class PaymentProofImage
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private EntityStatus entityStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @ManyToOne
    private TrPlan plan;
}
