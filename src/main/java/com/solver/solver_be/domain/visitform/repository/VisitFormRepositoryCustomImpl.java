package com.solver.solver_be.domain.visitform.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.solver.solver_be.domain.visitform.dto.VisitFormSearchRequestDto;
import com.solver.solver_be.domain.visitform.entity.VisitForm;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static com.solver.solver_be.domain.visitform.entity.QVisitForm.visitForm;


@Repository
public class VisitFormRepositoryCustomImpl{

    private final JPAQueryFactory queryFactory;

    public VisitFormRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 1. Search VisitForm By Keyword
    public List<VisitForm> findAllByContainKeyword(int page, VisitFormSearchRequestDto visitFormSearchRequestDto) {

        BooleanBuilder builder = new BooleanBuilder();
        
        if(visitFormSearchRequestDto.getGuestName() != null){
            builder.and(visitForm.guest.name.containsIgnoreCase(visitFormSearchRequestDto.getGuestName()));
        }
        if(visitFormSearchRequestDto.getLocation() != null){
            builder.and(visitForm.location.containsIgnoreCase(visitFormSearchRequestDto.getLocation()));
        }
        if(visitFormSearchRequestDto.getAdminName() != null){
            builder.and(visitForm.admin.name.containsIgnoreCase(visitFormSearchRequestDto.getAdminName()));
        }
        if(visitFormSearchRequestDto.getStartDate() != null){
            builder.and(visitForm.startDate.goe(LocalDate.parse(visitFormSearchRequestDto.getStartDate())));
        }
        if(visitFormSearchRequestDto.getEndDate() != null){
            builder.and(visitForm.endDate.loe(LocalDate.parse(visitFormSearchRequestDto.getEndDate())));
        }
        if(visitFormSearchRequestDto.getPurpose() != null){
            builder.and(visitForm.purpose.containsIgnoreCase(visitFormSearchRequestDto.getPurpose()));
        }
        if(visitFormSearchRequestDto.getStatus() != null){
            builder.and(visitForm.status.containsIgnoreCase(visitFormSearchRequestDto.getStatus()));
        }

        return queryFactory.selectFrom(visitForm)
                .where(builder)
                .offset((page-1)*10)
                .limit(10)
                .orderBy(
                        visitForm.guest.name.asc(),
                        visitForm.location.asc(),
                        visitForm.admin.name.asc(),
                        visitForm.startDate.asc(),
                        visitForm.endDate.asc(),
                        visitForm.purpose.asc(),
                        visitForm.status.asc()
                )
                .fetch();
    }
    public Long count(VisitFormSearchRequestDto visitFormSearchRequestDto) {

        BooleanBuilder builder = new BooleanBuilder();

        if(visitFormSearchRequestDto.getGuestName() != null){
            builder.and(visitForm.guest.name.containsIgnoreCase(visitFormSearchRequestDto.getGuestName()));
        }
        if(visitFormSearchRequestDto.getLocation() != null){
            builder.and(visitForm.location.containsIgnoreCase(visitFormSearchRequestDto.getLocation()));
        }
        if(visitFormSearchRequestDto.getAdminName() != null){
            builder.and(visitForm.admin.name.containsIgnoreCase(visitFormSearchRequestDto.getAdminName()));
        }
        if(visitFormSearchRequestDto.getStartDate() != null){
            builder.and(visitForm.startDate.goe(LocalDate.parse(visitFormSearchRequestDto.getStartDate())));
        }
        if(visitFormSearchRequestDto.getEndDate() != null){
            builder.and(visitForm.endDate.loe(LocalDate.parse(visitFormSearchRequestDto.getEndDate())));
        }
        if(visitFormSearchRequestDto.getPurpose() != null){
            builder.and(visitForm.purpose.containsIgnoreCase(visitFormSearchRequestDto.getPurpose()));
        }
        if(visitFormSearchRequestDto.getStatus() != null){
            builder.and(visitForm.status.containsIgnoreCase(visitFormSearchRequestDto.getStatus()));
        }

        return queryFactory.select(visitForm.count()).from(visitForm)
                .where(builder)
                .fetchOne();
    }
}