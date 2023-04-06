package com.solver.solver_be.domain.access.entity;

import com.solver.solver_be.domain.user.entity.Admin;
import com.solver.solver_be.domain.user.entity.Guest;
import com.solver.solver_be.domain.visitform.entity.VisitForm;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Access {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "GUEST_ID", nullable = false)
    private Guest guest;

    @ManyToOne
    @JoinColumn(name = "ADMIN_ID", nullable = false)
    private Admin admin;

    @OneToOne
    @JoinColumn(name = "VISIT_FORM", nullable = false)
    private VisitForm visitForm;

    public static Access of(Guest guest, Admin admin, VisitForm visitForm) {
        return Access.builder()
                .guest(guest)
                .admin(admin)
                .visitForm(visitForm)
                .build();
    }
}
