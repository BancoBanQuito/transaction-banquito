package com.banquito.transaction.controller;

import com.banquito.transaction.Utils.Messages;
import com.banquito.transaction.Utils.RSCode;
import com.banquito.transaction.Utils.RSFormat;
import com.banquito.transaction.Utils.Utils;
import com.banquito.transaction.controller.dto.RQInterest;
import com.banquito.transaction.controller.dto.RSInterest;
import com.banquito.transaction.controller.mapper.InterestMapper;
import com.banquito.transaction.exception.RSRuntimeException;
import com.banquito.transaction.service.InterestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<RSFormat> createInterest(@RequestBody RQInterest interest) {
        try{

            if (!Utils.hasAllAttributes(interest)) {
                return ResponseEntity.status(RSCode.BAD_REQUEST.code)
                        .body(RSFormat.builder().message("Failure").data(Messages.MISSING_PARAMS).build());
            }

            interestService.createInterest(InterestMapper.map(interest));

            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.builder().message("Success").data("Interes generado correctamente").build());

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

            List<RSInterest> interests = interestService.getInterestBetweenDates(codeLocalAccount, from, to);

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


}
