package uz.masters.springbootbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.masters.springbootbot.entity.Birthdays;

import java.util.ArrayList;
import java.util.List;

public interface BirthDaysRepository extends JpaRepository<Birthdays, Long> {
    List<Birthdays> findBirthdaysByCongratulatedFalse();

}
