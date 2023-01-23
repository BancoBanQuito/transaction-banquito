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
    public ResponseEntity<RSFormat> createSavingsAccountInterest(@RequestBody RQSavingsAccountInterest interest) {
        try{

            if (!Utils.hasAllAttributes(interest)) {
                return ResponseEntity.status(RSCode.BAD_REQUEST.code)
                        .body(RSFormat.builder().message("Failure").data(Messages.MISSING_PARAMS).build());
            }

            RSSavingsAccountInterest response = interestService.createSavingsAccountInterest(InterestMapper.map(interest));

            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.builder().message("Success").data(response).build());

        } catch(RSRuntimeException e){
            return ResponseEntity.status(e.getCode())
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());

        } catch (Exception e){
            return ResponseEntity.status(500)
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }



    @GetMapping("/{codeLocalAccount}/{from}/{to}")
    public ResponseEntity<RSFormat> getTransactionBetween(
            @PathVariable("codeLocalAccount") String codeLocalAccount,
            @PathVariable("from") LocalDateTime from,
            @PathVariable("to") LocalDateTime to) {
        try{

            if(Utils.isNullEmpty(codeLocalAccount)||Utils.isNullEmpty(from)||Utils.isNullEmpty(to)){
                return ResponseEntity.status(RSCode.BAD_REQUEST.code)
                        .body(RSFormat.builder().message("Failure").data(Messages.MISSING_PARAMS).build());
            }

            List<RSSavingsAccountInterest> interests = interestService.getInterestBetweenDates(codeLocalAccount, from, to);

            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.builder().message("Success").data(interests).build());

        } catch(RSRuntimeException e){
            return ResponseEntity.status(e.getCode())
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());

        } catch (Exception e){
            return ResponseEntity.status(500)
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }

    @GetMapping("investment/{codeLocalAccount}/{days}/{capital}/{ear}")
    public ResponseEntity<RSFormat> getInvestmentInterest(
            @PathVariable("codeLocalAccount") String codeLocalAccount,
            @PathVariable("days") Integer days,
            @PathVariable("capital") BigDecimal capital,
            @PathVariable("ear") BigDecimal ear) {

        try{

            if(Utils.isNullEmpty(codeLocalAccount)
                    ||Utils.isNullEmpty(days)
                    ||Utils.isNullEmpty(capital)
                    ||Utils.isNullEmpty(ear)){

                return ResponseEntity.status(RSCode.BAD_REQUEST.code)
                        .body(RSFormat.builder().message("Failure").data(Messages.MISSING_PARAMS).build());
            }

            RSInvestmentInterest response = interestService.getInvestmentInterest(
                    codeLocalAccount,
                    days,
                    capital,
                    ear
            );

            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.builder().message("Success").data(response).build());

        } catch(RSRuntimeException e){
            return ResponseEntity.status(e.getCode())
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());

        } catch (Exception e){
            return ResponseEntity.status(500)
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }

}
