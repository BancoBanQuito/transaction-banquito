package com.banquito.transaction.Utils;

import com.banquito.transaction.controller.dto.RQTransaction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {
    private static final String numberString = "0123456789";
    private static final String alphanumericString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static SecureRandom random = new SecureRandom();

    public static String generateNumberCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(numberString.charAt(random.nextInt(numberString.length())));
        return sb.toString();
    }

    public static String generateAlphanumericCode(int length){
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(alphanumericString.charAt(random.nextInt(numberString.length())));
        return sb.toString();
    }

    public static LocalDateTime currentDate(){
        return LocalDateTime.now();
    }

    public static boolean hasAllAttributes(Object object){
        boolean result = true;
        Field[] fields = object.getClass().getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                if(value == null){
                    result = false;
                }
                else {
                    if(value.toString().isEmpty()){
                        result = false;
                    }
                }
            } catch (IllegalAccessException | NullPointerException e) {
                result = false;
            }
        }
        return result;
    }

    public static Boolean validRsTransaction(RQTransaction rsTransaction){
        Boolean flag = true;

        if(isNullEmpty(rsTransaction.getMovement())){
            flag = false;
        }else if(isNullEmpty(rsTransaction.getType())){
            flag = false;
        }else if(isNullEmpty(rsTransaction.getCodeLocalAccount())){
            flag = false;
        }else if(isNullEmpty(rsTransaction.getCodeInternationalAccount())){
            flag = false;
        }else if(isNullEmpty(rsTransaction.getConcept())){
            flag = false;
        } else if(isNullEmpty(rsTransaction.getValue())){
            flag = false;
        }

        return flag;
    }

    public static boolean isNullEmpty(Object value){
        return (value == null || value.toString().isEmpty());
    }

    public static boolean validTransactionType(String type){
        boolean flag = false;

        if(type.equals("DEPOSITO")
                || type.equals("RETIRO")
                || type.equals("PAGO")
                || type.equals("TRANSFERENCIA")
                || type.equals("INTERES")){
            flag = true;
        }

        return flag;
    }

    public static Boolean validRecipientType(String type){
        Boolean flag = false;

        if(type.isEmpty() || type.equals("ORDENANTE") || type.equals("BENEFICIARIO")){
            flag = true;
        }

        return flag;
    }

    public static Boolean validRecipientBank(String type){
        Boolean flag = false;

        if(type.isEmpty() || type.equals("BANQUITO")|| type.equals("BanQuito")||type.equals("banquito")){
            flag = true;
        }

        return flag;
    }

    public static Boolean validTransactionStatus(String status){
        Boolean flag = false;

        if(status.equals("EXITOSA")
                || status.equals("PENDIENTE")
                || status.equals("RECHAZADA")){
            flag = true;
        }

        return flag;
    }

    public static boolean saveLog(Object object, String codeLocalAccount){
        try {
            String date = currentDate().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String filename = date + codeLocalAccount + ".txt";
            File file = new File("log/"+filename);
            FileWriter fw = new FileWriter(file);
            PrintWriter pw = new PrintWriter(fw);

            String[] messages = object.toString().split(",");

            for(String message: messages){
                pw.println(message);
            }

            pw.close();
            return true;
        }catch (IOException e)  {
            return false;
        }
    }

    //EAR stands for Effective Annual Rate (Tasa efectiva anual)
    //DR  stands for Daily Rate (Tasa diaria)
    //Use double, becuase BigDecimal doesn't support power operation
    public static BigDecimal computeSavingsAccountInterest(BigDecimal balance, BigDecimal EAR){

        Double base = 1d + EAR.doubleValue()/100d;

        Double exponent = 1d/360d;

        BigDecimal dr = balance.multiply(BigDecimal.valueOf(Math.pow(base, exponent) - 1d));
        return dr.setScale(2, RoundingMode.HALF_EVEN);
    }

    public static InvesmentInterest computeInvestmentInterest(Integer days, BigDecimal capital, BigDecimal EAR){

        BigDecimal rawInterest = capital.multiply(BigDecimal.valueOf(days)).multiply(EAR.divide(BigDecimal.valueOf(100))).divide(BigDecimal.valueOf(360));

        BigDecimal roundRawInterest = rawInterest.setScale(2, RoundingMode.HALF_EVEN);

        BigDecimal rawRetention = roundRawInterest.multiply(BigDecimal.valueOf(2).divide(BigDecimal.valueOf(100)));

        BigDecimal roundRetention = rawRetention.setScale(2, RoundingMode.HALF_EVEN);

        BigDecimal netInterest = roundRawInterest.subtract(roundRetention);

        return InvesmentInterest.builder()
                .rawInterest(roundRawInterest)
                .retention(roundRetention)
                .netInterest(netInterest)
                .build();
    }
}
