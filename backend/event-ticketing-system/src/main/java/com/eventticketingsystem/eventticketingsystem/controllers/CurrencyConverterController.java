package com.eventticketingsystem.eventticketingsystem.controllers;

import com.eventticketingsystem.eventticketingsystem.services.CurrencyConverterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;
/**
 * The CurrencyConverterController class handles requests related to currency conversion of event ticket prices.
 */
@AllArgsConstructor
@RestController
@RequestMapping("currency-converter")
public class CurrencyConverterController {
    private final CurrencyConverterService currencyConverterService;

    /**
     * Converts the event ticket price from USD currency to another.
     *
     * @param toCurrency The target currency code to convert to.
     * @param amount     The event ticket price to be converted.
     * @return A ResponseEntity containing the converted event ticket price.
     * @throws RuntimeException If an error occurs during the currency conversion process.
     */
    @GetMapping
    public ResponseEntity<BigDecimal> convertEventPriceCurrency(@RequestParam String toCurrency, @RequestParam BigDecimal amount) {
        return Optional.ofNullable(currencyConverterService.convertEventPriceCurrency(toCurrency, amount))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Error while performing currency conversion"));
    }
}
