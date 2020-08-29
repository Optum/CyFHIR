package com.Optum.CyFHIR.models;


import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.validation.FhirValidator;

public class Validator {

    FhirContext ctx;
    IParser parser;
    FhirValidator hapiValidator;

    public Validator() {
        this.ctx = FhirContext.forR4();
        this.hapiValidator = this.ctx.newValidator();
        this.parser = ctx.newJsonParser();
        this.parser.setParserErrorHandler(new StrictErrorHandler());

    }

    public boolean validateBundle(String json) {

        try {
            org.hl7.fhir.r4.model.Bundle bundle = parser.parseResource(org.hl7.fhir.r4.model.Bundle.class, json);
            String resourceType = bundle.getResourceType().getPath();
            Boolean result = resourceType != null;
            return result;
        } catch (Exception e) {
            return false;
        }

    }


}
