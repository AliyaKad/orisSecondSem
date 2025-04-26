package ru.itis.inf304.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.itis.inf304.entity.User;
import ru.itis.inf304.repository.UserRepository;
import ru.itis.inf304.Service.CurrencyService;
import ru.itis.inf304.Service.EmailService;

import java.util.Map;

@Component
public class CurrencyScheduler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 8 * * ?")
    public void sendCurrencyRates() {
        Map<String, Double> rates = currencyService.getLatestRates("USD");
        String ratesMessage = formatRatesMessage(rates);

        userRepository.findAll().forEach(user -> {
            if (user.isEmailConfirmed()) {
                emailService.sendEmail(user.getEmail(), "Currency Rates Update", ratesMessage);
            }
        });
    }

    private String formatRatesMessage(Map<String, Double> rates) {
        StringBuilder message = new StringBuilder("Latest currency rates:\n");
        rates.forEach((currency, rate) -> message.append(currency).append(": ").append(rate).append("\n"));
        return message.toString();
    }
}