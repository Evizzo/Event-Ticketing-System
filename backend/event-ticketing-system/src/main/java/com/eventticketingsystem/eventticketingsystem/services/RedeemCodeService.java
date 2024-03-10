package com.eventticketingsystem.eventticketingsystem.services;

import com.eventticketingsystem.eventticketingsystem.entities.RedeemCode;
import com.eventticketingsystem.eventticketingsystem.repositories.RedeemCodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RedeemCodeService {
    private final RedeemCodeRepository redeemCodeRepository;
    public List<RedeemCode> getAllRedeemCodes() {
        return redeemCodeRepository.findAll();
    }
    public BigDecimal calculatePriceWithCode(BigDecimal ticketPrice, String codeName){
        Optional<RedeemCode> redeemCodeOptional = redeemCodeRepository.findByName(codeName);

        if (redeemCodeOptional.isEmpty()) {
            throw new RuntimeException("Invalid redeem code !");
        }

        RedeemCode redeemCode = redeemCodeOptional.get();
        Integer discountPercentage = redeemCode.getDiscountPercentage();

        BigDecimal discountAmount = ticketPrice.multiply(BigDecimal.valueOf(discountPercentage).divide(BigDecimal.valueOf(100)));
        BigDecimal discountedPrice = ticketPrice.subtract(discountAmount);

        return discountedPrice.setScale(2, RoundingMode.HALF_UP);
    }
    public Optional<RedeemCode> editRedeemCode(UUID id, RedeemCode updatedRedeemCode) {
        return Optional.ofNullable(redeemCodeRepository.findById(id)
                .map(existingRedeemCode -> {
                    Optional.ofNullable(updatedRedeemCode.getName()).ifPresent(existingRedeemCode::setName);
                    Optional.ofNullable(updatedRedeemCode.getDiscountPercentage()).ifPresent(existingRedeemCode::setDiscountPercentage);
                    Optional.ofNullable(updatedRedeemCode.getOwnerEmail()).ifPresent(existingRedeemCode::setOwnerEmail);

                    return redeemCodeRepository.save(existingRedeemCode);
                })
                .orElseThrow(() -> new RuntimeException("Redeem code not found with ID: " + id)));
    }
    public String deleteRedeemCodeById(UUID id) {
        Optional<RedeemCode> redeemCodeOptional = redeemCodeRepository.findById(id);
        if (redeemCodeOptional.isPresent()) {
            redeemCodeRepository.deleteById(id);
            return "Redeem code with ID " + id + " deleted successfully.";
        }
        return "Redeem code with ID " + id + " not found.";
    }
    public RedeemCode addRedeemCode(RedeemCode redeemCode) {
        return redeemCodeRepository.save(redeemCode);
    }
}
