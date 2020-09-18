#!/usr/bin/env bash

set -e

WRITE_PATH="cyfhir/src/main/java/com/Optum/CyFHIR/models/Validator.java"

declare -a fhirVersionList=("dstu3" "r4" "r5")

declare -a DSTU3List=("Account" "ActivityDefinition" "AdverseEvent" "AllergyIntolerance" "Appointment" "AppointmentResponse" "AuditEvent" "Basic" "Binary" "BodySite" "Bundle" "CapabilityStatement" "CarePlan" "CareTeam" "ChargeItem" "Claim" "ClaimResponse" "ClinicalImpression" "CodeSystem" "Communication" "CommunicationRequest" "CompartmentDefinition" "Composition" "ConceptMap" "Condition" "Consent" "Contract" "Coverage" "DataElement" "DetectedIssue" "Device" "DeviceComponent" "DeviceMetric" "DeviceRequest" "DeviceUseStatement" "DiagnosticReport" "DocumentManifest" "DocumentReference" "EligibilityRequest" "EligibilityResponse" "Encounter" "Endpoint" "EnrollmentRequest" "EnrollmentResponse" "EpisodeOfCare" "ExpansionProfile" "ExplanationOfBenefit" "FamilyMemberHistory" "Flag" "Goal" "GraphDefinition" "Group" "GuidanceResponse" "HealthcareService" "ImagingManifest" "ImagingStudy" "Immunization" "ImmunizationRecommendation" "ImplementationGuide" "Library" "Linkage" "ListResource" "Location" "Measure" "MeasureReport" "Media" "Medication" "MedicationAdministration" "MedicationDispense" "MedicationRequest" "MedicationStatement" "MessageDefinition" "MessageHeader" "NamingSystem" "NutritionOrder" "Observation" "OperationDefinition" "OperationOutcome" "Organization" "Parameters" "Patient" "PaymentNotice" "PaymentReconciliation" "Person" "PlanDefinition" "Practitioner" "PractitionerRole" "Procedure" "ProcedureRequest" "ProcessRequest" "ProcessResponse" "Provenance" "Questionnaire" "QuestionnaireResponse" "ReferralRequest" "RelatedPerson" "RequestGroup" "ResearchStudy" "ResearchSubject" "RiskAssessment" "Schedule" "SearchParameter" "Sequence" "ServiceDefinition" "Slot" "Specimen" "StructureDefinition" "StructureMap" "Subscription" "Substance" "SupplyDelivery" "SupplyRequest" "Task" "TestReport" "TestScript" "ValueSet" "VisionPrescription")

declare -a R4List=("Account" "ActivityDefinition" "AdverseEvent" "AllergyIntolerance" "Appointment" "AppointmentResponse" "AuditEvent" "Basic" "Binary" "BiologicallyDerivedProduct" "BodyStructure" "Bundle" "CapabilityStatement" "CarePlan" "CareTeam" "CatalogEntry" "ChargeItem" "ChargeItemDefinition" "Claim" "ClaimResponse" "ClinicalImpression" "CodeSystem" "Communication" "CommunicationRequest" "CompartmentDefinition" "Composition" "ConceptMap" "Condition" "Consent" "Contract" "Coverage" "CoverageEligibilityRequest" "CoverageEligibilityResponse" "DetectedIssue" "Device" "DeviceDefinition" "DeviceMetric" "DeviceRequest" "DeviceUseStatement" "DiagnosticReport" "DocumentManifest" "DocumentReference" "EffectEvidenceSynthesis" "Encounter" "Endpoint" "EnrollmentRequest" "EnrollmentResponse" "EpisodeOfCare" "EventDefinition" "Evidence" "EvidenceVariable" "ExampleScenario" "ExplanationOfBenefit" "FamilyMemberHistory" "Flag" "Goal" "GraphDefinition" "Group" "GuidanceResponse" "HealthcareService" "ImagingStudy" "Immunization" "ImmunizationEvaluation" "ImmunizationRecommendation" "ImplementationGuide" "InsurancePlan" "Invoice" "Library" "Linkage" "ListResource" "Location" "Measure" "MeasureReport" "Media" "Medication" "MedicationAdministration" "MedicationDispense" "MedicationKnowledge" "MedicationRequest" "MedicationStatement" "MedicinalProduct" "MedicinalProductAuthorization" "MedicinalProductContraindication" "MedicinalProductIndication" "MedicinalProductIngredient" "MedicinalProductInteraction" "MedicinalProductManufactured" "MedicinalProductPackaged" "MedicinalProductPharmaceutical" "MedicinalProductUndesirableEffect" "MessageDefinition" "MessageHeader" "MolecularSequence" "NamingSystem" "NutritionOrder" "Observation" "ObservationDefinition" "OperationDefinition" "OperationOutcome" "Organization" "OrganizationAffiliation" "Parameters" "Patient" "PaymentNotice" "PaymentReconciliation" "Person" "PlanDefinition" "Practitioner" "PractitionerRole" "Procedure" "Provenance" "Questionnaire" "QuestionnaireResponse" "RelatedPerson" "RequestGroup" "ResearchDefinition" "ResearchElementDefinition" "ResearchStudy" "ResearchSubject" "RiskAssessment" "RiskEvidenceSynthesis" "Schedule" "SearchParameter" "ServiceRequest" "Slot" "Specimen" "SpecimenDefinition" "StructureDefinition" "StructureMap" "Subscription" "Substance" "SubstanceNucleicAcid" "SubstancePolymer" "SubstanceProtein" "SubstanceReferenceInformation" "SubstanceSourceMaterial" "SubstanceSpecification" "SupplyDelivery" "SupplyRequest" "Task" "TerminologyCapabilities" "TestReport" "TestScript" "ValueSet" "VerificationResult" "VisionPrescription")

declare -a R5List=("Account" "ActivityDefinition" "AdministrableProductDefinition" "AdverseEvent" "AllergyIntolerance" "Appointment" "AppointmentResponse" "AuditEvent" "Basic" "Binary" "BiologicallyDerivedProduct" "BodyStructure" "Bundle" "CapabilityStatement" "CapabilityStatement2" "CarePlan" "CareTeam" "CatalogEntry" "ChargeItem" "ChargeItemDefinition" "Citation" "Claim" "ClaimResponse" "ClinicalImpression" "ClinicalUseIssue" "CodeSystem" "Communication" "CommunicationRequest" "CompartmentDefinition" "Composition" "ConceptMap" "Condition" "ConditionDefinition" "Consent" "Contract" "Coverage" "CoverageEligibilityRequest" "CoverageEligibilityResponse" "DetectedIssue" "Device" "DeviceDefinition" "DeviceMetric" "DeviceRequest" "DeviceUseStatement" "DiagnosticReport" "DocumentManifest" "DocumentReference" "Encounter" "Endpoint" "EnrollmentRequest" "EnrollmentResponse" "EpisodeOfCare" "EventDefinition" "Evidence" "EvidenceFocus" "EvidenceVariable" "ExampleScenario" "ExplanationOfBenefit" "FamilyMemberHistory" "Flag" "Goal" "GraphDefinition" "Group" "GuidanceResponse" "HealthcareService" "ImagingStudy" "Immunization" "ImmunizationEvaluation" "ImmunizationRecommendation" "ImplementationGuide" "Ingredient" "InsurancePlan" "Invoice" "Library" "Linkage" "ListResource" "Location" "ManufacturedItemDefinition" "Measure" "MeasureReport" "Medication" "MedicationAdministration" "MedicationDispense" "MedicationKnowledge" "MedicationRequest" "MedicationUsage" "MedicinalProductDefinition" "MessageDefinition" "MessageHeader" "MolecularSequence" "NamingSystem" "NutritionIntake" "NutritionOrder" "NutritionProduct" "Observation" "ObservationDefinition" "OperationDefinition" "OperationOutcome" "Organization" "OrganizationAffiliation" "PackagedProductDefinition" "Parameters" "Patient" "PaymentNotice" "PaymentReconciliation" "Permission" "Person" "PlanDefinition" "Practitioner" "PractitionerRole" "Procedure" "Provenance" "Questionnaire" "QuestionnaireResponse" "RegulatedAuthorization" "RelatedPerson" "RequestGroup" "ResearchStudy" "ResearchSubject" "RiskAssessment" "Schedule" "SearchParameter" "ServiceRequest" "Slot" "Specimen" "SpecimenDefinition" "StructureDefinition" "StructureMap" "Subscription" "SubscriptionStatus" "SubscriptionTopic" "Substance" "SubstanceDefinition" "SubstanceNucleicAcid" "SubstancePolymer" "SubstanceProtein" "SubstanceReferenceInformation" "SubstanceSourceMaterial" "SupplyDelivery" "SupplyRequest" "Task" "TerminologyCapabilities" "TestReport" "TestScript" "ValueSet" "VerificationResult" "VisionPrescription")


echo "package com.Optum.CyFHIR.models;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.validation.FhirValidator;
import org.hl7.fhir.instance.model.api.IAnyResource;

public class Validator {
    FhirContext ctx;
    IParser parser;
    FhirValidator hapiValidator;
    Version fhirVersion;

    public Validator() throws Exception {
        this(\"r4\");
    }

    public Validator(String version) throws Exception {
        version = version.toLowerCase();
        if (version.equals(\"dstu3\")) {
            this.fhirVersion = Version.DSTU3;
            this.ctx = FhirContext.forDstu3();
        } else if (version.equals(\"r4\")) {
            this.fhirVersion = Version.R4;
            this.ctx = FhirContext.forR4();
        } else if (version.equals(\"r5\")) {
            this.fhirVersion = Version.R5;
            this.ctx = FhirContext.forR5();
        } else {
            throw new Exception(\"Unsupported FHIR Version\");
        }
        this.hapiValidator = this.ctx.newValidator();
        this.parser = ctx.newJsonParser();
        this.parser.setParserErrorHandler(new StrictErrorHandler());
    }

    public IAnyResource validate(String json, String resourceType) {
        try {
            switch (this.fhirVersion) {
                case DSTU3:
                    return this.parseDSTU3(json, resourceType);
                case R4:
                    return this.parseR4(json, resourceType);
                case R5:
                    return this.parseR5(json, resourceType);
            }
        } catch (Exception e) {
            throw e;
        }
        return null;
    }" > $WRITE_PATH

for version in "${fhirVersionList[@]}"
do
  (
     if [[ $version == "dstu3" ]]; then
       (
           echo "   public org.hl7.fhir.dstu3.model.Resource parseDSTU3(String fhirResourceJson, String resourceType) {
             switch (resourceType) {" >> $WRITE_PATH

           for line in "${DSTU3List[@]}"
           do
             echo "
                   case \"$line\":
                       org.hl7.fhir.$version.model.$line _${line} = parser.parseResource(org.hl7.fhir.$version.model.$line.class, fhirResourceJson);
                       return _${line};" >> $WRITE_PATH
           done
       )

     elif [[ $version == "r4" ]]; then
       (
       echo "   public org.hl7.fhir.r4.model.Resource parseR4(String fhirResourceJson, String resourceType) {
         switch (resourceType) {" >> $WRITE_PATH
       for line in "${R4List[@]}"
       do
         echo "
               case \"$line\":
                   org.hl7.fhir.$version.model.$line _${line} = parser.parseResource(org.hl7.fhir.$version.model.$line.class, fhirResourceJson);
                   return _${line};" >> $WRITE_PATH
       done
       )


     elif [[ $version == "r5" ]]; then
      (
      echo "   public org.hl7.fhir.r5.model.Resource parseR5(String fhirResourceJson, String resourceType) {
        switch (resourceType) {" >> $WRITE_PATH
     for line in "${R5List[@]}"
     do
       echo "
             case \"$line\":
                 org.hl7.fhir.$version.model.$line _${line} = parser.parseResource(org.hl7.fhir.$version.model.$line.class, fhirResourceJson);
                 return _${line};" >> $WRITE_PATH
     done
      )
     fi

     echo "
             default:
                 throw new IllegalStateException(\"Unexpected value: \" + resourceType);
         }
      }
         " >> $WRITE_PATH

  )
done
echo "  enum Version {
      DSTU3,
      R4,
      R5
   }
}" >> $WRITE_PATH
