package ng.optisoft.rosabon.model;

import lombok.Data;
import ng.optisoft.rosabon.dto.request.MessageInDto;
import ng.optisoft.rosabon.treasury.model.TrPlan;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "message")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Message extends GenericModuleBaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id", nullable = false)
//    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private MessageStatus messageStatus;

    private String body;

    private MessageInDto.RecipientType recipientType;

    private Integer paymentDueDate;
    private Integer timeofLastActivity;
    private Long product;
    @ManyToMany
    private Set<Useraccount> recipient;
    @ManyToMany
    private Set<IndividualUser> individualUsersRecipient;
    @ManyToMany
    private Set<Company> companyUsersRecipient;

    @ManyToMany
    private Set<Useraccount> planUsersRecipient;
    @ManyToMany
    private Set<Useraccount> usersWithNoRunningPlan;

    @ManyToMany
    private Set<Useraccount> usersWithLastLoginDate;

    @ManyToMany
    private Set<Useraccount> paymentDueUser;
    @ManyToMany
    private Set<Useraccount> investmentUsersRecipient;
    @ManyToMany
    private Set<TrPlan> plans;


    @ManyToOne
    @JoinColumn(name = "useraccount_id")
    private Useraccount adminSender;


    private LocalDate sendDate;


//    @CreatedDate
//    private LocalDateTime dateCreated;


    public enum MessageStatus {
        DRAFT, PENDING, SENT
    }


}