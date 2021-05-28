package uz.mk.springbootphonenumberverificationdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.mk.springbootphonenumberverificationdemo.payload.ApiResponse;
import uz.mk.springbootphonenumberverificationdemo.service.TwilioService;

@RestController
@RequestMapping("/api/twilio")
public class TwilioController {

    @Autowired
    TwilioService twilioService;

    @GetMapping("/send")
    public HttpEntity<?> send(@RequestParam String phoneNumber) {
        ApiResponse apiResponse = twilioService.sendCode(phoneNumber);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/verify")
    public HttpEntity<?> verify(@RequestParam String phoneNumber,
                                @RequestParam Integer code) {
        ApiResponse apiResponse = twilioService.verify(phoneNumber, code);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
