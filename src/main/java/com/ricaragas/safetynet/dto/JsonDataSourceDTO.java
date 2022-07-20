package com.ricaragas.safetynet.dto;

import com.ricaragas.safetynet.model.Firestation;
import com.ricaragas.safetynet.model.MedicalRecord;
import com.ricaragas.safetynet.model.Person;
import lombok.Data;

// Matching structure of the data source in:
// https://s3-eu-west-1.amazonaws.com/course.oc-static.com/projects/DA+Java+EN/P5+/data.json
public class JsonDataSourceDTO {

    public Person[] persons;
    public Firestation[] firestations;
    public MedicalRecord[] medicalrecords;

}
