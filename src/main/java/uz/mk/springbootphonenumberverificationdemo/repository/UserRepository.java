package uz.mk.springbootphonenumberverificationdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mk.springbootphonenumberverificationdemo.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByPhoneNumber(String phoneNumber);

}
