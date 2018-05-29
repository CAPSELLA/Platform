package gr.uoa.di.madgik.publicfood.security.repository;

import gr.uoa.di.madgik.publicfood.model.WeeklyQuestionnaireEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeeklyQuestionnaireRepository extends JpaRepository<WeeklyQuestionnaireEntity, Integer> {

    List<WeeklyQuestionnaireEntity> findByUserIdAndDomain(String id, String Domain);

    WeeklyQuestionnaireEntity findByIdAndDomain(int id, String domain);


}
