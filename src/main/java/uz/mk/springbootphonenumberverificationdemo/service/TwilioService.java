package uz.mk.springbootphonenumberverificationdemo.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.mk.springbootphonenumberverificationdemo.entity.TwilioVerification;
import uz.mk.springbootphonenumberverificationdemo.payload.ApiResponse;
import uz.mk.springbootphonenumberverificationdemo.repository.TwilioVerificationRepository;
import uz.mk.springbootphonenumberverificationdemo.repository.UserRepository;
import uz.mk.springbootphonenumberverificationdemo.utils.CommonUtils;

import java.util.Optional;

@Service
public class TwilioService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TwilioVerificationRepository twilioVerificationRepository;

    @Value("${twilio.phone}")
    private String twilioPhone;

    @Value("${twilio.sid}")
    private String twilioSid;

    @Value("${twilio.token}")
    private String twilioToken;

    public ApiResponse sendCode(String phoneNumber) {
        try {
            Integer code = CommonUtils.generateCode();
            phoneNumber = phoneNumber.startsWith("+") ? phoneNumber : "+" + phoneNumber;
            phoneNumber = phoneNumber.replace(" ", "");
            Twilio.init(twilioSid, twilioToken);
            Message message = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(twilioPhone),
                    "Hi there! It is your code from Twilio trial : " + code)
                    .create();
            Optional<TwilioVerification> optional = twilioVerificationRepository.findByPhoneNumberAndVerifiedFalse(phoneNumber);
            if (optional.isPresent()) {
                TwilioVerification twilioVerification = optional.get();
                twilioVerification.setCode(code);
                twilioVerification.setVerified(false);
                twilioVerificationRepository.save(twilioVerification);
            } else {
                TwilioVerification twilioVerification = new TwilioVerification();
                twilioVerification.setPhoneNumber(phoneNumber);
                twilioVerification.setCode(code);
                twilioVerification.setVerified(false);
                twilioVerificationRepository.save(twilioVerification);
            }
            return new ApiResponse("Ok", true,code);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Error", false);
        }
    }


    public ApiResponse verify(String phoneNumber, Integer code) {
        phoneNumber=phoneNumber.startsWith("+")? phoneNumber:"+".concat(phoneNumber);
        phoneNumber = phoneNumber.replace(" ", "");

        Optional<TwilioVerification> optional = twilioVerificationRepository
                .findByPhoneNumberAndVerifiedFalse(phoneNumber);

        if (optional.isPresent()) {
            TwilioVerification twilioVerification = optional.get();
            if (twilioVerification.getCode().equals(code)) {
                twilioVerification.setVerified(true);
                twilioVerificationRepository.save(twilioVerification);
                return new ApiResponse("Success", true);
            } else {
                return new ApiResponse("Error code", false);
            }
        } else {
            return new ApiResponse("Error Phone number", false);
        }

    }
}
