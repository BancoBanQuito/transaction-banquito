package com.banquito.transaction.controller;

import com.banquito.transaction.Utils.Messages;
import com.banquito.transaction.Utils.RSCode;
import com.banquito.transaction.Utils.RSFormat;
import com.banquito.transaction.Utils.Utils;
import com.banquito.transaction.controller.dto.RQSavingsAccountInterest;
import com.banquito.transaction.controller.dto.RSInvestmentInterest;
import com.banquito.transaction.controller.dto.RSSavingsAccountInterest;
import com.banquito.transaction.controller.mapper.InterestMapper;
import com.banquito.transaction.exception.RSRuntimeException;
import com.banquito.transaction.service.InterestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transaction/interest")
public class InterestController {

    private final InterestService interestService;

    public InterestController(InterestService interestService){
        this.interestService = interestService;
    }

    @PostMapping
    public ResponseEntity<RSFormat<RSSavingsAccountInterest>> createSavingsAccountInterest(@RequestBody RQSavingsAccountInterest interest) {
        try{

            if (!Utils.hasAllAttributes(interest)) {
                return ResponseEntity.status(RSCode.BAD_REQUEST.code).build();
            }

            RSSavingsAccountInterest response = interestService.createSavingsAccountInterest(
                    InterestMapper.map(interest),
                    interest.getBaseCalc());

            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.<RSSavingsAccountInterest>builder().message("Success").data(response).build());

        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{codeLocalAccount}/{from}/{to}")
    public ResponseEntity<RSFormat<List<RSSavingsAccountInterest>>> getTransactionBetween(
            @PathVariable("codeLocalAccount") String codeLocalAccount,
            @PathVariable("from") LocalDateTime from,
            @PathVariable("to") LocalDateTime to) {
        try{

            if(Utils.isNullEmpty(codeLocalAccount)||Utils.isNullEmpty(from)||Utils.isNullEmpty(to)){
                return ResponseEntity.status(RSCode.BAD_REQUEST.code).build();
            }

            List<RSSavingsAccountInterest> interests = interestService.getInterestBetweenDates(codeLocalAccount, from, to);

            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.<List<RSSavingsAccountInterest>>builder().message("Success").data(interests).build());

        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("investment/{codeLocalAccount}/{days}/{capital}/{ear}")
    public ResponseEntity<RSFormat<RSInvestmentInterest>> getInvestmentInterest(
            @PathVariable("codeLocalAccount") String codeLocalAccount,
            @PathVariable("days") Integer days,
            @PathVariable("capital") BigDecimal capital,
            @PathVariable("ear") BigDecimal ear) {

        try{

            if(Utils.isNullEmpty(codeLocalAccount)
                    ||Utils.isNullEmpty(days)
                    ||Utils.isNullEmpty(capital)
                    ||Utils.isNullEmpty(ear)){

                return ResponseEntity.status(RSCode.BAD_REQUEST.code).build();
            }

            RSInvestmentInterest response = interestService.getInvestmentInterest(
                    codeLocalAccount,
                    days,
                    capital,
                    ear
            );

            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.<RSInvestmentInterest>builder().message("Success").data(response).build());

        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

}
