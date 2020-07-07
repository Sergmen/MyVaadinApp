package com.haulmont.testtask.common;

import com.haulmont.testtask.entities.DoctorEntity;
import com.haulmont.testtask.entities.PatientEntity;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import org.apache.commons.lang3.StringUtils;
;

public class Validators {

   public static final Validator<String> textLenghtValidator = new Validator < String > ( ) {
        @Override
        public ValidationResult apply (String s, ValueContext valueContext ) {
            if(StringUtils.isBlank(s)) {
                return  ValidationResult.error ( "Поле не может быть пустым!");
            }
            else if (s.length()>50) {
                return  ValidationResult.error ( "Поле не может иметь более 50 символов!");
            }
            else {
                return  ValidationResult.ok () ;
            }
        }
    } ;





}
