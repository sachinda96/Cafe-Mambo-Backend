package lk.cafemambo.repository;

import lk.cafemambo.entity.EventBookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventBookingRepository extends JpaRepository<EventBookingEntity,String> {
    List<EventBookingEntity> findAllByStatus(String active);

    List<EventBookingEntity> findAllByStatusAndEventStatus(String active, String eventStatusPending);
}
