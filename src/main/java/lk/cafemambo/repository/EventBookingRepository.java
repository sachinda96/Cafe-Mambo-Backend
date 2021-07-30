package lk.cafemambo.repository;

import lk.cafemambo.entity.EventBookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventBookingRepository extends JpaRepository<EventBookingEntity,String> {
}
