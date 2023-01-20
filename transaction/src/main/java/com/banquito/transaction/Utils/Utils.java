package com.banquito.transaction.Utils;

import com.banquito.transaction.controller.dto.RQTransaction;
import com.banquito.transaction.controller.dto.RSTransaction;

import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.time.LocalDateTime;

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
                || type.equals("TRANSFERENCIA")){
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

    public static Boolean validTransactionStatus(String status){
        Boolean flag = false;

        if(status.equals("EXITOSA")
                || status.equals("PENDIENTE")
                || status.equals("RECHAZADA")){
            flag = true;
        }

        return flag;
    }
}
