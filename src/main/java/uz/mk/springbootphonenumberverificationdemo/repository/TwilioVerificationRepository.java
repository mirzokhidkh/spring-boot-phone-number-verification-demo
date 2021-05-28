package uz.mk.springbootphonenumberverificationdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mk.springbootphonenumberverificationdemo.entity.TwilioVerification;
import uz.mk.springbootphonenumberverificationdemo.entity.User;

import java.util.Optional;

public interface TwilioVerificationRepository extends JpaRepository<TwilioVerification, Integer> {
    Optional<TwilioVerification> findByPhoneNumberAndVerifiedFalse(String phoneNumber);

//    Optional<TwilioVerification> findByPhoneNumberAndCodeAndVerifiedFalse(String phoneNumber,Integer code);
}
