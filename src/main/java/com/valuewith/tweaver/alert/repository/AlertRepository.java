package com.valuewith.tweaver.alert.repository;

import com.valuewith.tweaver.alert.entity.Alert;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long>, AlertDsl {
}
