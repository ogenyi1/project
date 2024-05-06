package ng.optisoft.rosabon.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ng.optisoft.rosabon.util.CrUtil;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base abstract class for entities which will hold definitions for created, last modified, created by,
 * last modified by attributes.
 */

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(pattern="dd-MM-yyyy hh:mm a")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonFormat(pattern="dd-MM-yyyy hh:mm a")
    @Column(name = "updated_at", nullable = false)
    private  LocalDateTime updatedAt;

    public LocalDateTime getUpdatedAt() {
        return CrUtil.getZonedLocalDateTime(updatedAt);
    }

    public LocalDateTime getCreatedAt() {
        return CrUtil.getZonedLocalDateTime(createdAt);
    }
}


