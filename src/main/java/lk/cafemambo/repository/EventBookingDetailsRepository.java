package lk.cafemambo.repository;

import lk.cafemambo.entity.EventBookingDetailsEntity;
import lk.cafemambo.entity.EventBookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventBookingDetailsRepository extends JpaRepository<EventBookingDetailsEntity,String> {

    EventBookingDetailsEntity findByEventBookingEntityAndStatus(EventBookingEntity eventBookingEntity, String active);
}
