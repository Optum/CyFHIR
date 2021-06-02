package com.Optum.CyFHIR.models;

import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.validation.FhirValidator;
import org.hl7.fhir.instance.model.api.IAnyResource;

public class Validator {
    IParser parser;
    FhirContext ctx;
    Version fhirVersion;
    FhirValidator hapiValidator;

    public Validator() throws Exception {
        this("r4");
    }

    public Validator(String version) throws Exception {
        version = version.toLowerCase();
        if (version.equals("dstu3")) {
            this.fhirVersion = Version.DSTU3;
            this.ctx = FhirContext.forDstu3();
        } else if (version.equals("r4")) {
            this.fhirVersion = Version.R4;
            this.ctx = FhirContext.forR4();
        } else if (version.equals("r5")) {
            this.fhirVersion = Version.R5;
            this.ctx = FhirContext.forR5();
        } else {
            throw new Exception("Unsupported FHIR Version");
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
    }

    public org.hl7.fhir.dstu3.model.Resource parseDSTU3(String fhirResourceJson, String resourceType) {
        switch (resourceType) {

        case "Account":
            org.hl7.fhir.dstu3.model.Account _Account = parser.parseResource(org.hl7.fhir.dstu3.model.Account.class,
                    fhirResourceJson);
            return _Account;

        case "ActivityDefinition":
            org.hl7.fhir.dstu3.model.ActivityDefinition _ActivityDefinition = parser
                    .parseResource(org.hl7.fhir.dstu3.model.ActivityDefinition.class, fhirResourceJson);
            return _ActivityDefinition;

        case "AdverseEvent":
            org.hl7.fhir.dstu3.model.AdverseEvent _AdverseEvent = parser
                    .parseResource(org.hl7.fhir.dstu3.model.AdverseEvent.class, fhirResourceJson);
            return _AdverseEvent;

        case "AllergyIntolerance":
            org.hl7.fhir.dstu3.model.AllergyIntolerance _AllergyIntolerance = parser
                    .parseResource(org.hl7.fhir.dstu3.model.AllergyIntolerance.class, fhirResourceJson);
            return _AllergyIntolerance;

        case "Appointment":
            org.hl7.fhir.dstu3.model.Appointment _Appointment = parser
                    .parseResource(org.hl7.fhir.dstu3.model.Appointment.class, fhirResourceJson);
            return _Appointment;

        case "AppointmentResponse":
            org.hl7.fhir.dstu3.model.AppointmentResponse _AppointmentResponse = parser
                    .parseResource(org.hl7.fhir.dstu3.model.AppointmentResponse.class, fhirResourceJson);
            return _AppointmentResponse;

        case "AuditEvent":
            org.hl7.fhir.dstu3.model.AuditEvent _AuditEvent = parser
                    .parseResource(org.hl7.fhir.dstu3.model.AuditEvent.class, fhirResourceJson);
            return _AuditEvent;

        case "Basic":
            org.hl7.fhir.dstu3.model.Basic _Basic = parser.parseResource(org.hl7.fhir.dstu3.model.Basic.class,
                    fhirResourceJson);
            return _Basic;

        case "Binary":
            org.hl7.fhir.dstu3.model.Binary _Binary = parser.parseResource(org.hl7.fhir.dstu3.model.Binary.class,
                    fhirResourceJson);
            return _Binary;

        case "BodySite":
            org.hl7.fhir.dstu3.model.BodySite _BodySite = parser.parseResource(org.hl7.fhir.dstu3.model.BodySite.class,
                    fhirResourceJson);
            return _BodySite;

        case "Bundle":
            org.hl7.fhir.dstu3.model.Bundle _Bundle = parser.parseResource(org.hl7.fhir.dstu3.model.Bundle.class,
                    fhirResourceJson);
            return _Bundle;

        case "CapabilityStatement":
            org.hl7.fhir.dstu3.model.CapabilityStatement _CapabilityStatement = parser
                    .parseResource(org.hl7.fhir.dstu3.model.CapabilityStatement.class, fhirResourceJson);
            return _CapabilityStatement;

        case "CarePlan":
            org.hl7.fhir.dstu3.model.CarePlan _CarePlan = parser.parseResource(org.hl7.fhir.dstu3.model.CarePlan.class,
                    fhirResourceJson);
            return _CarePlan;

        case "CareTeam":
            org.hl7.fhir.dstu3.model.CareTeam _CareTeam = parser.parseResource(org.hl7.fhir.dstu3.model.CareTeam.class,
                    fhirResourceJson);
            return _CareTeam;

        case "ChargeItem":
            org.hl7.fhir.dstu3.model.ChargeItem _ChargeItem = parser
                    .parseResource(org.hl7.fhir.dstu3.model.ChargeItem.class, fhirResourceJson);
            return _ChargeItem;

        case "Claim":
            org.hl7.fhir.dstu3.model.Claim _Claim = parser.parseResource(org.hl7.fhir.dstu3.model.Claim.class,
                    fhirResourceJson);
            return _Claim;

        case "ClaimResponse":
            org.hl7.fhir.dstu3.model.ClaimResponse _ClaimResponse = parser
                    .parseResource(org.hl7.fhir.dstu3.model.ClaimResponse.class, fhirResourceJson);
            return _ClaimResponse;

        case "ClinicalImpression":
            org.hl7.fhir.dstu3.model.ClinicalImpression _ClinicalImpression = parser
                    .parseResource(org.hl7.fhir.dstu3.model.ClinicalImpression.class, fhirResourceJson);
            return _ClinicalImpression;

        case "CodeSystem":
            org.hl7.fhir.dstu3.model.CodeSystem _CodeSystem = parser
                    .parseResource(org.hl7.fhir.dstu3.model.CodeSystem.class, fhirResourceJson);
            return _CodeSystem;

        case "Communication":
            org.hl7.fhir.dstu3.model.Communication _Communication = parser
                    .parseResource(org.hl7.fhir.dstu3.model.Communication.class, fhirResourceJson);
            return _Communication;

        case "CommunicationRequest":
            org.hl7.fhir.dstu3.model.CommunicationRequest _CommunicationRequest = parser
                    .parseResource(org.hl7.fhir.dstu3.model.CommunicationRequest.class, fhirResourceJson);
            return _CommunicationRequest;

        case "CompartmentDefinition":
            org.hl7.fhir.dstu3.model.CompartmentDefinition _CompartmentDefinition = parser
                    .parseResource(org.hl7.fhir.dstu3.model.CompartmentDefinition.class, fhirResourceJson);
            return _CompartmentDefinition;

        case "Composition":
            org.hl7.fhir.dstu3.model.Composition _Composition = parser
                    .parseResource(org.hl7.fhir.dstu3.model.Composition.class, fhirResourceJson);
            return _Composition;

        case "ConceptMap":
            org.hl7.fhir.dstu3.model.ConceptMap _ConceptMap = parser
                    .parseResource(org.hl7.fhir.dstu3.model.ConceptMap.class, fhirResourceJson);
            return _ConceptMap;

        case "Condition":
            org.hl7.fhir.dstu3.model.Condition _Condition = parser
                    .parseResource(org.hl7.fhir.dstu3.model.Condition.class, fhirResourceJson);
            return _Condition;

        case "Consent":
            org.hl7.fhir.dstu3.model.Consent _Consent = parser.parseResource(org.hl7.fhir.dstu3.model.Consent.class,
                    fhirResourceJson);
            return _Consent;

        case "Contract":
            org.hl7.fhir.dstu3.model.Contract _Contract = parser.parseResource(org.hl7.fhir.dstu3.model.Contract.class,
                    fhirResourceJson);
            return _Contract;

        case "Coverage":
            org.hl7.fhir.dstu3.model.Coverage _Coverage = parser.parseResource(org.hl7.fhir.dstu3.model.Coverage.class,
                    fhirResourceJson);
            return _Coverage;

        case "DataElement":
            org.hl7.fhir.dstu3.model.DataElement _DataElement = parser
                    .parseResource(org.hl7.fhir.dstu3.model.DataElement.class, fhirResourceJson);
            return _DataElement;

        case "DetectedIssue":
            org.hl7.fhir.dstu3.model.DetectedIssue _DetectedIssue = parser
                    .parseResource(org.hl7.fhir.dstu3.model.DetectedIssue.class, fhirResourceJson);
            return _DetectedIssue;

        case "Device":
            org.hl7.fhir.dstu3.model.Device _Device = parser.parseResource(org.hl7.fhir.dstu3.model.Device.class,
                    fhirResourceJson);
            return _Device;

        case "DeviceComponent":
            org.hl7.fhir.dstu3.model.DeviceComponent _DeviceComponent = parser
                    .parseResource(org.hl7.fhir.dstu3.model.DeviceComponent.class, fhirResourceJson);
            return _DeviceComponent;

        case "DeviceMetric":
            org.hl7.fhir.dstu3.model.DeviceMetric _DeviceMetric = parser
                    .parseResource(org.hl7.fhir.dstu3.model.DeviceMetric.class, fhirResourceJson);
            return _DeviceMetric;

        case "DeviceRequest":
            org.hl7.fhir.dstu3.model.DeviceRequest _DeviceRequest = parser
                    .parseResource(org.hl7.fhir.dstu3.model.DeviceRequest.class, fhirResourceJson);
            return _DeviceRequest;

        case "DeviceUseStatement":
            org.hl7.fhir.dstu3.model.DeviceUseStatement _DeviceUseStatement = parser
                    .parseResource(org.hl7.fhir.dstu3.model.DeviceUseStatement.class, fhirResourceJson);
            return _DeviceUseStatement;

        case "DiagnosticReport":
            org.hl7.fhir.dstu3.model.DiagnosticReport _DiagnosticReport = parser
                    .parseResource(org.hl7.fhir.dstu3.model.DiagnosticReport.class, fhirResourceJson);
            return _DiagnosticReport;

        case "DocumentManifest":
            org.hl7.fhir.dstu3.model.DocumentManifest _DocumentManifest = parser
                    .parseResource(org.hl7.fhir.dstu3.model.DocumentManifest.class, fhirResourceJson);
            return _DocumentManifest;

        case "DocumentReference":
            org.hl7.fhir.dstu3.model.DocumentReference _DocumentReference = parser
                    .parseResource(org.hl7.fhir.dstu3.model.DocumentReference.class, fhirResourceJson);
            return _DocumentReference;

        case "EligibilityRequest":
            org.hl7.fhir.dstu3.model.EligibilityRequest _EligibilityRequest = parser
                    .parseResource(org.hl7.fhir.dstu3.model.EligibilityRequest.class, fhirResourceJson);
            return _EligibilityRequest;

        case "EligibilityResponse":
            org.hl7.fhir.dstu3.model.EligibilityResponse _EligibilityResponse = parser
                    .parseResource(org.hl7.fhir.dstu3.model.EligibilityResponse.class, fhirResourceJson);
            return _EligibilityResponse;

        case "Encounter":
            org.hl7.fhir.dstu3.model.Encounter _Encounter = parser
                    .parseResource(org.hl7.fhir.dstu3.model.Encounter.class, fhirResourceJson);
            return _Encounter;

        case "Endpoint":
            org.hl7.fhir.dstu3.model.Endpoint _Endpoint = parser.parseResource(org.hl7.fhir.dstu3.model.Endpoint.class,
                    fhirResourceJson);
            return _Endpoint;

        case "EnrollmentRequest":
            org.hl7.fhir.dstu3.model.EnrollmentRequest _EnrollmentRequest = parser
                    .parseResource(org.hl7.fhir.dstu3.model.EnrollmentRequest.class, fhirResourceJson);
            return _EnrollmentRequest;

        case "EnrollmentResponse":
            org.hl7.fhir.dstu3.model.EnrollmentResponse _EnrollmentResponse = parser
                    .parseResource(org.hl7.fhir.dstu3.model.EnrollmentResponse.class, fhirResourceJson);
            return _EnrollmentResponse;

        case "EpisodeOfCare":
            org.hl7.fhir.dstu3.model.EpisodeOfCare _EpisodeOfCare = parser
                    .parseResource(org.hl7.fhir.dstu3.model.EpisodeOfCare.class, fhirResourceJson);
            return _EpisodeOfCare;

        case "ExpansionProfile":
            org.hl7.fhir.dstu3.model.ExpansionProfile _ExpansionProfile = parser
                    .parseResource(org.hl7.fhir.dstu3.model.ExpansionProfile.class, fhirResourceJson);
            return _ExpansionProfile;

        case "ExplanationOfBenefit":
            org.hl7.fhir.dstu3.model.ExplanationOfBenefit _ExplanationOfBenefit = parser
                    .parseResource(org.hl7.fhir.dstu3.model.ExplanationOfBenefit.class, fhirResourceJson);
            return _ExplanationOfBenefit;

        case "FamilyMemberHistory":
            org.hl7.fhir.dstu3.model.FamilyMemberHistory _FamilyMemberHistory = parser
                    .parseResource(org.hl7.fhir.dstu3.model.FamilyMemberHistory.class, fhirResourceJson);
            return _FamilyMemberHistory;

        case "Flag":
            org.hl7.fhir.dstu3.model.Flag _Flag = parser.parseResource(org.hl7.fhir.dstu3.model.Flag.class,
                    fhirResourceJson);
            return _Flag;

        case "Goal":
            org.hl7.fhir.dstu3.model.Goal _Goal = parser.parseResource(org.hl7.fhir.dstu3.model.Goal.class,
                    fhirResourceJson);
            return _Goal;

        case "GraphDefinition":
            org.hl7.fhir.dstu3.model.GraphDefinition _GraphDefinition = parser
                    .parseResource(org.hl7.fhir.dstu3.model.GraphDefinition.class, fhirResourceJson);
            return _GraphDefinition;

        case "Group":
            org.hl7.fhir.dstu3.model.Group _Group = parser.parseResource(org.hl7.fhir.dstu3.model.Group.class,
                    fhirResourceJson);
            return _Group;

        case "GuidanceResponse":
            org.hl7.fhir.dstu3.model.GuidanceResponse _GuidanceResponse = parser
                    .parseResource(org.hl7.fhir.dstu3.model.GuidanceResponse.class, fhirResourceJson);
            return _GuidanceResponse;

        case "HealthcareService":
            org.hl7.fhir.dstu3.model.HealthcareService _HealthcareService = parser
                    .parseResource(org.hl7.fhir.dstu3.model.HealthcareService.class, fhirResourceJson);
            return _HealthcareService;

        case "ImagingManifest":
            org.hl7.fhir.dstu3.model.ImagingManifest _ImagingManifest = parser
                    .parseResource(org.hl7.fhir.dstu3.model.ImagingManifest.class, fhirResourceJson);
            return _ImagingManifest;

        case "ImagingStudy":
            org.hl7.fhir.dstu3.model.ImagingStudy _ImagingStudy = parser
                    .parseResource(org.hl7.fhir.dstu3.model.ImagingStudy.class, fhirResourceJson);
            return _ImagingStudy;

        case "Immunization":
            org.hl7.fhir.dstu3.model.Immunization _Immunization = parser
                    .parseResource(org.hl7.fhir.dstu3.model.Immunization.class, fhirResourceJson);
            return _Immunization;

        case "ImmunizationRecommendation":
            org.hl7.fhir.dstu3.model.ImmunizationRecommendation _ImmunizationRecommendation = parser
                    .parseResource(org.hl7.fhir.dstu3.model.ImmunizationRecommendation.class, fhirResourceJson);
            return _ImmunizationRecommendation;

        case "ImplementationGuide":
            org.hl7.fhir.dstu3.model.ImplementationGuide _ImplementationGuide = parser
                    .parseResource(org.hl7.fhir.dstu3.model.ImplementationGuide.class, fhirResourceJson);
            return _ImplementationGuide;

        case "Library":
            org.hl7.fhir.dstu3.model.Library _Library = parser.parseResource(org.hl7.fhir.dstu3.model.Library.class,
                    fhirResourceJson);
            return _Library;

        case "Linkage":
            org.hl7.fhir.dstu3.model.Linkage _Linkage = parser.parseResource(org.hl7.fhir.dstu3.model.Linkage.class,
                    fhirResourceJson);
            return _Linkage;

        case "ListResource":
            org.hl7.fhir.dstu3.model.ListResource _ListResource = parser
                    .parseResource(org.hl7.fhir.dstu3.model.ListResource.class, fhirResourceJson);
            return _ListResource;

        case "Location":
            org.hl7.fhir.dstu3.model.Location _Location = parser.parseResource(org.hl7.fhir.dstu3.model.Location.class,
                    fhirResourceJson);
            return _Location;

        case "Measure":
            org.hl7.fhir.dstu3.model.Measure _Measure = parser.parseResource(org.hl7.fhir.dstu3.model.Measure.class,
                    fhirResourceJson);
            return _Measure;

        case "MeasureReport":
            org.hl7.fhir.dstu3.model.MeasureReport _MeasureReport = parser
                    .parseResource(org.hl7.fhir.dstu3.model.MeasureReport.class, fhirResourceJson);
            return _MeasureReport;

        case "Media":
            org.hl7.fhir.dstu3.model.Media _Media = parser.parseResource(org.hl7.fhir.dstu3.model.Media.class,
                    fhirResourceJson);
            return _Media;

        case "Medication":
            org.hl7.fhir.dstu3.model.Medication _Medication = parser
                    .parseResource(org.hl7.fhir.dstu3.model.Medication.class, fhirResourceJson);
            return _Medication;

        case "MedicationAdministration":
            org.hl7.fhir.dstu3.model.MedicationAdministration _MedicationAdministration = parser
                    .parseResource(org.hl7.fhir.dstu3.model.MedicationAdministration.class, fhirResourceJson);
            return _MedicationAdministration;

        case "MedicationDispense":
            org.hl7.fhir.dstu3.model.MedicationDispense _MedicationDispense = parser
                    .parseResource(org.hl7.fhir.dstu3.model.MedicationDispense.class, fhirResourceJson);
            return _MedicationDispense;

        case "MedicationRequest":
            org.hl7.fhir.dstu3.model.MedicationRequest _MedicationRequest = parser
                    .parseResource(org.hl7.fhir.dstu3.model.MedicationRequest.class, fhirResourceJson);
            return _MedicationRequest;

        case "MedicationStatement":
            org.hl7.fhir.dstu3.model.MedicationStatement _MedicationStatement = parser
                    .parseResource(org.hl7.fhir.dstu3.model.MedicationStatement.class, fhirResourceJson);
            return _MedicationStatement;

        case "MessageDefinition":
            org.hl7.fhir.dstu3.model.MessageDefinition _MessageDefinition = parser
                    .parseResource(org.hl7.fhir.dstu3.model.MessageDefinition.class, fhirResourceJson);
            return _MessageDefinition;

        case "MessageHeader":
            org.hl7.fhir.dstu3.model.MessageHeader _MessageHeader = parser
                    .parseResource(org.hl7.fhir.dstu3.model.MessageHeader.class, fhirResourceJson);
            return _MessageHeader;

        case "NamingSystem":
            org.hl7.fhir.dstu3.model.NamingSystem _NamingSystem = parser
                    .parseResource(org.hl7.fhir.dstu3.model.NamingSystem.class, fhirResourceJson);
            return _NamingSystem;

        case "NutritionOrder":
            org.hl7.fhir.dstu3.model.NutritionOrder _NutritionOrder = parser
                    .parseResource(org.hl7.fhir.dstu3.model.NutritionOrder.class, fhirResourceJson);
            return _NutritionOrder;

        case "Observation":
            org.hl7.fhir.dstu3.model.Observation _Observation = parser
                    .parseResource(org.hl7.fhir.dstu3.model.Observation.class, fhirResourceJson);
            return _Observation;

        case "OperationDefinition":
            org.hl7.fhir.dstu3.model.OperationDefinition _OperationDefinition = parser
                    .parseResource(org.hl7.fhir.dstu3.model.OperationDefinition.class, fhirResourceJson);
            return _OperationDefinition;

        case "OperationOutcome":
            org.hl7.fhir.dstu3.model.OperationOutcome _OperationOutcome = parser
                    .parseResource(org.hl7.fhir.dstu3.model.OperationOutcome.class, fhirResourceJson);
            return _OperationOutcome;

        case "Organization":
            org.hl7.fhir.dstu3.model.Organization _Organization = parser
                    .parseResource(org.hl7.fhir.dstu3.model.Organization.class, fhirResourceJson);
            return _Organization;

        case "Parameters":
            org.hl7.fhir.dstu3.model.Parameters _Parameters = parser
                    .parseResource(org.hl7.fhir.dstu3.model.Parameters.class, fhirResourceJson);
            return _Parameters;

        case "Patient":
            org.hl7.fhir.dstu3.model.Patient _Patient = parser.parseResource(org.hl7.fhir.dstu3.model.Patient.class,
                    fhirResourceJson);
            return _Patient;

        case "PaymentNotice":
            org.hl7.fhir.dstu3.model.PaymentNotice _PaymentNotice = parser
                    .parseResource(org.hl7.fhir.dstu3.model.PaymentNotice.class, fhirResourceJson);
            return _PaymentNotice;

        case "PaymentReconciliation":
            org.hl7.fhir.dstu3.model.PaymentReconciliation _PaymentReconciliation = parser
                    .parseResource(org.hl7.fhir.dstu3.model.PaymentReconciliation.class, fhirResourceJson);
            return _PaymentReconciliation;

        case "Person":
            org.hl7.fhir.dstu3.model.Person _Person = parser.parseResource(org.hl7.fhir.dstu3.model.Person.class,
                    fhirResourceJson);
            return _Person;

        case "PlanDefinition":
            org.hl7.fhir.dstu3.model.PlanDefinition _PlanDefinition = parser
                    .parseResource(org.hl7.fhir.dstu3.model.PlanDefinition.class, fhirResourceJson);
            return _PlanDefinition;

        case "Practitioner":
            org.hl7.fhir.dstu3.model.Practitioner _Practitioner = parser
                    .parseResource(org.hl7.fhir.dstu3.model.Practitioner.class, fhirResourceJson);
            return _Practitioner;

        case "PractitionerRole":
            org.hl7.fhir.dstu3.model.PractitionerRole _PractitionerRole = parser
                    .parseResource(org.hl7.fhir.dstu3.model.PractitionerRole.class, fhirResourceJson);
            return _PractitionerRole;

        case "Procedure":
            org.hl7.fhir.dstu3.model.Procedure _Procedure = parser
                    .parseResource(org.hl7.fhir.dstu3.model.Procedure.class, fhirResourceJson);
            return _Procedure;

        case "ProcedureRequest":
            org.hl7.fhir.dstu3.model.ProcedureRequest _ProcedureRequest = parser
                    .parseResource(org.hl7.fhir.dstu3.model.ProcedureRequest.class, fhirResourceJson);
            return _ProcedureRequest;

        case "ProcessRequest":
            org.hl7.fhir.dstu3.model.ProcessRequest _ProcessRequest = parser
                    .parseResource(org.hl7.fhir.dstu3.model.ProcessRequest.class, fhirResourceJson);
            return _ProcessRequest;

        case "ProcessResponse":
            org.hl7.fhir.dstu3.model.ProcessResponse _ProcessResponse = parser
                    .parseResource(org.hl7.fhir.dstu3.model.ProcessResponse.class, fhirResourceJson);
            return _ProcessResponse;

        case "Provenance":
            org.hl7.fhir.dstu3.model.Provenance _Provenance = parser
                    .parseResource(org.hl7.fhir.dstu3.model.Provenance.class, fhirResourceJson);
            return _Provenance;

        case "Questionnaire":
            org.hl7.fhir.dstu3.model.Questionnaire _Questionnaire = parser
                    .parseResource(org.hl7.fhir.dstu3.model.Questionnaire.class, fhirResourceJson);
            return _Questionnaire;

        case "QuestionnaireResponse":
            org.hl7.fhir.dstu3.model.QuestionnaireResponse _QuestionnaireResponse = parser
                    .parseResource(org.hl7.fhir.dstu3.model.QuestionnaireResponse.class, fhirResourceJson);
            return _QuestionnaireResponse;

        case "ReferralRequest":
            org.hl7.fhir.dstu3.model.ReferralRequest _ReferralRequest = parser
                    .parseResource(org.hl7.fhir.dstu3.model.ReferralRequest.class, fhirResourceJson);
            return _ReferralRequest;

        case "RelatedPerson":
            org.hl7.fhir.dstu3.model.RelatedPerson _RelatedPerson = parser
                    .parseResource(org.hl7.fhir.dstu3.model.RelatedPerson.class, fhirResourceJson);
            return _RelatedPerson;

        case "RequestGroup":
            org.hl7.fhir.dstu3.model.RequestGroup _RequestGroup = parser
                    .parseResource(org.hl7.fhir.dstu3.model.RequestGroup.class, fhirResourceJson);
            return _RequestGroup;

        case "ResearchStudy":
            org.hl7.fhir.dstu3.model.ResearchStudy _ResearchStudy = parser
                    .parseResource(org.hl7.fhir.dstu3.model.ResearchStudy.class, fhirResourceJson);
            return _ResearchStudy;

        case "ResearchSubject":
            org.hl7.fhir.dstu3.model.ResearchSubject _ResearchSubject = parser
                    .parseResource(org.hl7.fhir.dstu3.model.ResearchSubject.class, fhirResourceJson);
            return _ResearchSubject;

        case "RiskAssessment":
            org.hl7.fhir.dstu3.model.RiskAssessment _RiskAssessment = parser
                    .parseResource(org.hl7.fhir.dstu3.model.RiskAssessment.class, fhirResourceJson);
            return _RiskAssessment;

        case "Schedule":
            org.hl7.fhir.dstu3.model.Schedule _Schedule = parser.parseResource(org.hl7.fhir.dstu3.model.Schedule.class,
                    fhirResourceJson);
            return _Schedule;

        case "SearchParameter":
            org.hl7.fhir.dstu3.model.SearchParameter _SearchParameter = parser
                    .parseResource(org.hl7.fhir.dstu3.model.SearchParameter.class, fhirResourceJson);
            return _SearchParameter;

        case "Sequence":
            org.hl7.fhir.dstu3.model.Sequence _Sequence = parser.parseResource(org.hl7.fhir.dstu3.model.Sequence.class,
                    fhirResourceJson);
            return _Sequence;

        case "ServiceDefinition":
            org.hl7.fhir.dstu3.model.ServiceDefinition _ServiceDefinition = parser
                    .parseResource(org.hl7.fhir.dstu3.model.ServiceDefinition.class, fhirResourceJson);
            return _ServiceDefinition;

        case "Slot":
            org.hl7.fhir.dstu3.model.Slot _Slot = parser.parseResource(org.hl7.fhir.dstu3.model.Slot.class,
                    fhirResourceJson);
            return _Slot;

        case "Specimen":
            org.hl7.fhir.dstu3.model.Specimen _Specimen = parser.parseResource(org.hl7.fhir.dstu3.model.Specimen.class,
                    fhirResourceJson);
            return _Specimen;

        case "StructureDefinition":
            org.hl7.fhir.dstu3.model.StructureDefinition _StructureDefinition = parser
                    .parseResource(org.hl7.fhir.dstu3.model.StructureDefinition.class, fhirResourceJson);
            return _StructureDefinition;

        case "StructureMap":
            org.hl7.fhir.dstu3.model.StructureMap _StructureMap = parser
                    .parseResource(org.hl7.fhir.dstu3.model.StructureMap.class, fhirResourceJson);
            return _StructureMap;

        case "Subscription":
            org.hl7.fhir.dstu3.model.Subscription _Subscription = parser
                    .parseResource(org.hl7.fhir.dstu3.model.Subscription.class, fhirResourceJson);
            return _Subscription;

        case "Substance":
            org.hl7.fhir.dstu3.model.Substance _Substance = parser
                    .parseResource(org.hl7.fhir.dstu3.model.Substance.class, fhirResourceJson);
            return _Substance;

        case "SupplyDelivery":
            org.hl7.fhir.dstu3.model.SupplyDelivery _SupplyDelivery = parser
                    .parseResource(org.hl7.fhir.dstu3.model.SupplyDelivery.class, fhirResourceJson);
            return _SupplyDelivery;

        case "SupplyRequest":
            org.hl7.fhir.dstu3.model.SupplyRequest _SupplyRequest = parser
                    .parseResource(org.hl7.fhir.dstu3.model.SupplyRequest.class, fhirResourceJson);
            return _SupplyRequest;

        case "Task":
            org.hl7.fhir.dstu3.model.Task _Task = parser.parseResource(org.hl7.fhir.dstu3.model.Task.class,
                    fhirResourceJson);
            return _Task;

        case "TestReport":
            org.hl7.fhir.dstu3.model.TestReport _TestReport = parser
                    .parseResource(org.hl7.fhir.dstu3.model.TestReport.class, fhirResourceJson);
            return _TestReport;

        case "TestScript":
            org.hl7.fhir.dstu3.model.TestScript _TestScript = parser
                    .parseResource(org.hl7.fhir.dstu3.model.TestScript.class, fhirResourceJson);
            return _TestScript;

        case "ValueSet":
            org.hl7.fhir.dstu3.model.ValueSet _ValueSet = parser.parseResource(org.hl7.fhir.dstu3.model.ValueSet.class,
                    fhirResourceJson);
            return _ValueSet;

        case "VisionPrescription":
            org.hl7.fhir.dstu3.model.VisionPrescription _VisionPrescription = parser
                    .parseResource(org.hl7.fhir.dstu3.model.VisionPrescription.class, fhirResourceJson);
            return _VisionPrescription;

        default:
            throw new IllegalStateException("Unexpected value: " + resourceType);
        }
    }

    public org.hl7.fhir.r4.model.Resource parseR4(String fhirResourceJson, String resourceType) {
        switch (resourceType) {

        case "Account":
            org.hl7.fhir.r4.model.Account _Account = parser.parseResource(org.hl7.fhir.r4.model.Account.class,
                    fhirResourceJson);
            return _Account;

        case "ActivityDefinition":
            org.hl7.fhir.r4.model.ActivityDefinition _ActivityDefinition = parser
                    .parseResource(org.hl7.fhir.r4.model.ActivityDefinition.class, fhirResourceJson);
            return _ActivityDefinition;

        case "AdverseEvent":
            org.hl7.fhir.r4.model.AdverseEvent _AdverseEvent = parser
                    .parseResource(org.hl7.fhir.r4.model.AdverseEvent.class, fhirResourceJson);
            return _AdverseEvent;

        case "AllergyIntolerance":
            org.hl7.fhir.r4.model.AllergyIntolerance _AllergyIntolerance = parser
                    .parseResource(org.hl7.fhir.r4.model.AllergyIntolerance.class, fhirResourceJson);
            return _AllergyIntolerance;

        case "Appointment":
            org.hl7.fhir.r4.model.Appointment _Appointment = parser
                    .parseResource(org.hl7.fhir.r4.model.Appointment.class, fhirResourceJson);
            return _Appointment;

        case "AppointmentResponse":
            org.hl7.fhir.r4.model.AppointmentResponse _AppointmentResponse = parser
                    .parseResource(org.hl7.fhir.r4.model.AppointmentResponse.class, fhirResourceJson);
            return _AppointmentResponse;

        case "AuditEvent":
            org.hl7.fhir.r4.model.AuditEvent _AuditEvent = parser.parseResource(org.hl7.fhir.r4.model.AuditEvent.class,
                    fhirResourceJson);
            return _AuditEvent;

        case "Basic":
            org.hl7.fhir.r4.model.Basic _Basic = parser.parseResource(org.hl7.fhir.r4.model.Basic.class,
                    fhirResourceJson);
            return _Basic;

        case "Binary":
            org.hl7.fhir.r4.model.Binary _Binary = parser.parseResource(org.hl7.fhir.r4.model.Binary.class,
                    fhirResourceJson);
            return _Binary;

        case "BiologicallyDerivedProduct":
            org.hl7.fhir.r4.model.BiologicallyDerivedProduct _BiologicallyDerivedProduct = parser
                    .parseResource(org.hl7.fhir.r4.model.BiologicallyDerivedProduct.class, fhirResourceJson);
            return _BiologicallyDerivedProduct;

        case "BodyStructure":
            org.hl7.fhir.r4.model.BodyStructure _BodyStructure = parser
                    .parseResource(org.hl7.fhir.r4.model.BodyStructure.class, fhirResourceJson);
            return _BodyStructure;

        case "Bundle":
            org.hl7.fhir.r4.model.Bundle _Bundle = parser.parseResource(org.hl7.fhir.r4.model.Bundle.class,
                    fhirResourceJson);
            return _Bundle;

        case "CapabilityStatement":
            org.hl7.fhir.r4.model.CapabilityStatement _CapabilityStatement = parser
                    .parseResource(org.hl7.fhir.r4.model.CapabilityStatement.class, fhirResourceJson);
            return _CapabilityStatement;

        case "CarePlan":
            org.hl7.fhir.r4.model.CarePlan _CarePlan = parser.parseResource(org.hl7.fhir.r4.model.CarePlan.class,
                    fhirResourceJson);
            return _CarePlan;

        case "CareTeam":
            org.hl7.fhir.r4.model.CareTeam _CareTeam = parser.parseResource(org.hl7.fhir.r4.model.CareTeam.class,
                    fhirResourceJson);
            return _CareTeam;

        case "CatalogEntry":
            org.hl7.fhir.r4.model.CatalogEntry _CatalogEntry = parser
                    .parseResource(org.hl7.fhir.r4.model.CatalogEntry.class, fhirResourceJson);
            return _CatalogEntry;

        case "ChargeItem":
            org.hl7.fhir.r4.model.ChargeItem _ChargeItem = parser.parseResource(org.hl7.fhir.r4.model.ChargeItem.class,
                    fhirResourceJson);
            return _ChargeItem;

        case "ChargeItemDefinition":
            org.hl7.fhir.r4.model.ChargeItemDefinition _ChargeItemDefinition = parser
                    .parseResource(org.hl7.fhir.r4.model.ChargeItemDefinition.class, fhirResourceJson);
            return _ChargeItemDefinition;

        case "Claim":
            org.hl7.fhir.r4.model.Claim _Claim = parser.parseResource(org.hl7.fhir.r4.model.Claim.class,
                    fhirResourceJson);
            return _Claim;

        case "ClaimResponse":
            org.hl7.fhir.r4.model.ClaimResponse _ClaimResponse = parser
                    .parseResource(org.hl7.fhir.r4.model.ClaimResponse.class, fhirResourceJson);
            return _ClaimResponse;

        case "ClinicalImpression":
            org.hl7.fhir.r4.model.ClinicalImpression _ClinicalImpression = parser
                    .parseResource(org.hl7.fhir.r4.model.ClinicalImpression.class, fhirResourceJson);
            return _ClinicalImpression;

        case "CodeSystem":
            org.hl7.fhir.r4.model.CodeSystem _CodeSystem = parser.parseResource(org.hl7.fhir.r4.model.CodeSystem.class,
                    fhirResourceJson);
            return _CodeSystem;

        case "Communication":
            org.hl7.fhir.r4.model.Communication _Communication = parser
                    .parseResource(org.hl7.fhir.r4.model.Communication.class, fhirResourceJson);
            return _Communication;

        case "CommunicationRequest":
            org.hl7.fhir.r4.model.CommunicationRequest _CommunicationRequest = parser
                    .parseResource(org.hl7.fhir.r4.model.CommunicationRequest.class, fhirResourceJson);
            return _CommunicationRequest;

        case "CompartmentDefinition":
            org.hl7.fhir.r4.model.CompartmentDefinition _CompartmentDefinition = parser
                    .parseResource(org.hl7.fhir.r4.model.CompartmentDefinition.class, fhirResourceJson);
            return _CompartmentDefinition;

        case "Composition":
            org.hl7.fhir.r4.model.Composition _Composition = parser
                    .parseResource(org.hl7.fhir.r4.model.Composition.class, fhirResourceJson);
            return _Composition;

        case "ConceptMap":
            org.hl7.fhir.r4.model.ConceptMap _ConceptMap = parser.parseResource(org.hl7.fhir.r4.model.ConceptMap.class,
                    fhirResourceJson);
            return _ConceptMap;

        case "Condition":
            org.hl7.fhir.r4.model.Condition _Condition = parser.parseResource(org.hl7.fhir.r4.model.Condition.class,
                    fhirResourceJson);
            return _Condition;

        case "Consent":
            org.hl7.fhir.r4.model.Consent _Consent = parser.parseResource(org.hl7.fhir.r4.model.Consent.class,
                    fhirResourceJson);
            return _Consent;

        case "Contract":
            org.hl7.fhir.r4.model.Contract _Contract = parser.parseResource(org.hl7.fhir.r4.model.Contract.class,
                    fhirResourceJson);
            return _Contract;

        case "Coverage":
            org.hl7.fhir.r4.model.Coverage _Coverage = parser.parseResource(org.hl7.fhir.r4.model.Coverage.class,
                    fhirResourceJson);
            return _Coverage;

        case "CoverageEligibilityRequest":
            org.hl7.fhir.r4.model.CoverageEligibilityRequest _CoverageEligibilityRequest = parser
                    .parseResource(org.hl7.fhir.r4.model.CoverageEligibilityRequest.class, fhirResourceJson);
            return _CoverageEligibilityRequest;

        case "CoverageEligibilityResponse":
            org.hl7.fhir.r4.model.CoverageEligibilityResponse _CoverageEligibilityResponse = parser
                    .parseResource(org.hl7.fhir.r4.model.CoverageEligibilityResponse.class, fhirResourceJson);
            return _CoverageEligibilityResponse;

        case "DetectedIssue":
            org.hl7.fhir.r4.model.DetectedIssue _DetectedIssue = parser
                    .parseResource(org.hl7.fhir.r4.model.DetectedIssue.class, fhirResourceJson);
            return _DetectedIssue;

        case "Device":
            org.hl7.fhir.r4.model.Device _Device = parser.parseResource(org.hl7.fhir.r4.model.Device.class,
                    fhirResourceJson);
            return _Device;

        case "DeviceDefinition":
            org.hl7.fhir.r4.model.DeviceDefinition _DeviceDefinition = parser
                    .parseResource(org.hl7.fhir.r4.model.DeviceDefinition.class, fhirResourceJson);
            return _DeviceDefinition;

        case "DeviceMetric":
            org.hl7.fhir.r4.model.DeviceMetric _DeviceMetric = parser
                    .parseResource(org.hl7.fhir.r4.model.DeviceMetric.class, fhirResourceJson);
            return _DeviceMetric;

        case "DeviceRequest":
            org.hl7.fhir.r4.model.DeviceRequest _DeviceRequest = parser
                    .parseResource(org.hl7.fhir.r4.model.DeviceRequest.class, fhirResourceJson);
            return _DeviceRequest;

        case "DeviceUseStatement":
            org.hl7.fhir.r4.model.DeviceUseStatement _DeviceUseStatement = parser
                    .parseResource(org.hl7.fhir.r4.model.DeviceUseStatement.class, fhirResourceJson);
            return _DeviceUseStatement;

        case "DiagnosticReport":
            org.hl7.fhir.r4.model.DiagnosticReport _DiagnosticReport = parser
                    .parseResource(org.hl7.fhir.r4.model.DiagnosticReport.class, fhirResourceJson);
            return _DiagnosticReport;

        case "DocumentManifest":
            org.hl7.fhir.r4.model.DocumentManifest _DocumentManifest = parser
                    .parseResource(org.hl7.fhir.r4.model.DocumentManifest.class, fhirResourceJson);
            return _DocumentManifest;

        case "DocumentReference":
            org.hl7.fhir.r4.model.DocumentReference _DocumentReference = parser
                    .parseResource(org.hl7.fhir.r4.model.DocumentReference.class, fhirResourceJson);
            return _DocumentReference;

        case "EffectEvidenceSynthesis":
            org.hl7.fhir.r4.model.EffectEvidenceSynthesis _EffectEvidenceSynthesis = parser
                    .parseResource(org.hl7.fhir.r4.model.EffectEvidenceSynthesis.class, fhirResourceJson);
            return _EffectEvidenceSynthesis;

        case "Encounter":
            org.hl7.fhir.r4.model.Encounter _Encounter = parser.parseResource(org.hl7.fhir.r4.model.Encounter.class,
                    fhirResourceJson);
            return _Encounter;

        case "Endpoint":
            org.hl7.fhir.r4.model.Endpoint _Endpoint = parser.parseResource(org.hl7.fhir.r4.model.Endpoint.class,
                    fhirResourceJson);
            return _Endpoint;

        case "EnrollmentRequest":
            org.hl7.fhir.r4.model.EnrollmentRequest _EnrollmentRequest = parser
                    .parseResource(org.hl7.fhir.r4.model.EnrollmentRequest.class, fhirResourceJson);
            return _EnrollmentRequest;

        case "EnrollmentResponse":
            org.hl7.fhir.r4.model.EnrollmentResponse _EnrollmentResponse = parser
                    .parseResource(org.hl7.fhir.r4.model.EnrollmentResponse.class, fhirResourceJson);
            return _EnrollmentResponse;

        case "EpisodeOfCare":
            org.hl7.fhir.r4.model.EpisodeOfCare _EpisodeOfCare = parser
                    .parseResource(org.hl7.fhir.r4.model.EpisodeOfCare.class, fhirResourceJson);
            return _EpisodeOfCare;

        case "EventDefinition":
            org.hl7.fhir.r4.model.EventDefinition _EventDefinition = parser
                    .parseResource(org.hl7.fhir.r4.model.EventDefinition.class, fhirResourceJson);
            return _EventDefinition;

        case "Evidence":
            org.hl7.fhir.r4.model.Evidence _Evidence = parser.parseResource(org.hl7.fhir.r4.model.Evidence.class,
                    fhirResourceJson);
            return _Evidence;

        case "EvidenceVariable":
            org.hl7.fhir.r4.model.EvidenceVariable _EvidenceVariable = parser
                    .parseResource(org.hl7.fhir.r4.model.EvidenceVariable.class, fhirResourceJson);
            return _EvidenceVariable;

        case "ExampleScenario":
            org.hl7.fhir.r4.model.ExampleScenario _ExampleScenario = parser
                    .parseResource(org.hl7.fhir.r4.model.ExampleScenario.class, fhirResourceJson);
            return _ExampleScenario;

        case "ExplanationOfBenefit":
            org.hl7.fhir.r4.model.ExplanationOfBenefit _ExplanationOfBenefit = parser
                    .parseResource(org.hl7.fhir.r4.model.ExplanationOfBenefit.class, fhirResourceJson);
            return _ExplanationOfBenefit;

        case "FamilyMemberHistory":
            org.hl7.fhir.r4.model.FamilyMemberHistory _FamilyMemberHistory = parser
                    .parseResource(org.hl7.fhir.r4.model.FamilyMemberHistory.class, fhirResourceJson);
            return _FamilyMemberHistory;

        case "Flag":
            org.hl7.fhir.r4.model.Flag _Flag = parser.parseResource(org.hl7.fhir.r4.model.Flag.class, fhirResourceJson);
            return _Flag;

        case "Goal":
            org.hl7.fhir.r4.model.Goal _Goal = parser.parseResource(org.hl7.fhir.r4.model.Goal.class, fhirResourceJson);
            return _Goal;

        case "GraphDefinition":
            org.hl7.fhir.r4.model.GraphDefinition _GraphDefinition = parser
                    .parseResource(org.hl7.fhir.r4.model.GraphDefinition.class, fhirResourceJson);
            return _GraphDefinition;

        case "Group":
            org.hl7.fhir.r4.model.Group _Group = parser.parseResource(org.hl7.fhir.r4.model.Group.class,
                    fhirResourceJson);
            return _Group;

        case "GuidanceResponse":
            org.hl7.fhir.r4.model.GuidanceResponse _GuidanceResponse = parser
                    .parseResource(org.hl7.fhir.r4.model.GuidanceResponse.class, fhirResourceJson);
            return _GuidanceResponse;

        case "HealthcareService":
            org.hl7.fhir.r4.model.HealthcareService _HealthcareService = parser
                    .parseResource(org.hl7.fhir.r4.model.HealthcareService.class, fhirResourceJson);
            return _HealthcareService;

        case "ImagingStudy":
            org.hl7.fhir.r4.model.ImagingStudy _ImagingStudy = parser
                    .parseResource(org.hl7.fhir.r4.model.ImagingStudy.class, fhirResourceJson);
            return _ImagingStudy;

        case "Immunization":
            org.hl7.fhir.r4.model.Immunization _Immunization = parser
                    .parseResource(org.hl7.fhir.r4.model.Immunization.class, fhirResourceJson);
            return _Immunization;

        case "ImmunizationEvaluation":
            org.hl7.fhir.r4.model.ImmunizationEvaluation _ImmunizationEvaluation = parser
                    .parseResource(org.hl7.fhir.r4.model.ImmunizationEvaluation.class, fhirResourceJson);
            return _ImmunizationEvaluation;

        case "ImmunizationRecommendation":
            org.hl7.fhir.r4.model.ImmunizationRecommendation _ImmunizationRecommendation = parser
                    .parseResource(org.hl7.fhir.r4.model.ImmunizationRecommendation.class, fhirResourceJson);
            return _ImmunizationRecommendation;

        case "ImplementationGuide":
            org.hl7.fhir.r4.model.ImplementationGuide _ImplementationGuide = parser
                    .parseResource(org.hl7.fhir.r4.model.ImplementationGuide.class, fhirResourceJson);
            return _ImplementationGuide;

        case "InsurancePlan":
            org.hl7.fhir.r4.model.InsurancePlan _InsurancePlan = parser
                    .parseResource(org.hl7.fhir.r4.model.InsurancePlan.class, fhirResourceJson);
            return _InsurancePlan;

        case "Invoice":
            org.hl7.fhir.r4.model.Invoice _Invoice = parser.parseResource(org.hl7.fhir.r4.model.Invoice.class,
                    fhirResourceJson);
            return _Invoice;

        case "Library":
            org.hl7.fhir.r4.model.Library _Library = parser.parseResource(org.hl7.fhir.r4.model.Library.class,
                    fhirResourceJson);
            return _Library;

        case "Linkage":
            org.hl7.fhir.r4.model.Linkage _Linkage = parser.parseResource(org.hl7.fhir.r4.model.Linkage.class,
                    fhirResourceJson);
            return _Linkage;

        case "ListResource":
            org.hl7.fhir.r4.model.ListResource _ListResource = parser
                    .parseResource(org.hl7.fhir.r4.model.ListResource.class, fhirResourceJson);
            return _ListResource;

        case "Location":
            org.hl7.fhir.r4.model.Location _Location = parser.parseResource(org.hl7.fhir.r4.model.Location.class,
                    fhirResourceJson);
            return _Location;

        case "Measure":
            org.hl7.fhir.r4.model.Measure _Measure = parser.parseResource(org.hl7.fhir.r4.model.Measure.class,
                    fhirResourceJson);
            return _Measure;

        case "MeasureReport":
            org.hl7.fhir.r4.model.MeasureReport _MeasureReport = parser
                    .parseResource(org.hl7.fhir.r4.model.MeasureReport.class, fhirResourceJson);
            return _MeasureReport;

        case "Media":
            org.hl7.fhir.r4.model.Media _Media = parser.parseResource(org.hl7.fhir.r4.model.Media.class,
                    fhirResourceJson);
            return _Media;

        case "Medication":
            org.hl7.fhir.r4.model.Medication _Medication = parser.parseResource(org.hl7.fhir.r4.model.Medication.class,
                    fhirResourceJson);
            return _Medication;

        case "MedicationAdministration":
            org.hl7.fhir.r4.model.MedicationAdministration _MedicationAdministration = parser
                    .parseResource(org.hl7.fhir.r4.model.MedicationAdministration.class, fhirResourceJson);
            return _MedicationAdministration;

        case "MedicationDispense":
            org.hl7.fhir.r4.model.MedicationDispense _MedicationDispense = parser
                    .parseResource(org.hl7.fhir.r4.model.MedicationDispense.class, fhirResourceJson);
            return _MedicationDispense;

        case "MedicationKnowledge":
            org.hl7.fhir.r4.model.MedicationKnowledge _MedicationKnowledge = parser
                    .parseResource(org.hl7.fhir.r4.model.MedicationKnowledge.class, fhirResourceJson);
            return _MedicationKnowledge;

        case "MedicationRequest":
            org.hl7.fhir.r4.model.MedicationRequest _MedicationRequest = parser
                    .parseResource(org.hl7.fhir.r4.model.MedicationRequest.class, fhirResourceJson);
            return _MedicationRequest;

        case "MedicationStatement":
            org.hl7.fhir.r4.model.MedicationStatement _MedicationStatement = parser
                    .parseResource(org.hl7.fhir.r4.model.MedicationStatement.class, fhirResourceJson);
            return _MedicationStatement;

        case "MedicinalProduct":
            org.hl7.fhir.r4.model.MedicinalProduct _MedicinalProduct = parser
                    .parseResource(org.hl7.fhir.r4.model.MedicinalProduct.class, fhirResourceJson);
            return _MedicinalProduct;

        case "MedicinalProductAuthorization":
            org.hl7.fhir.r4.model.MedicinalProductAuthorization _MedicinalProductAuthorization = parser
                    .parseResource(org.hl7.fhir.r4.model.MedicinalProductAuthorization.class, fhirResourceJson);
            return _MedicinalProductAuthorization;

        case "MedicinalProductContraindication":
            org.hl7.fhir.r4.model.MedicinalProductContraindication _MedicinalProductContraindication = parser
                    .parseResource(org.hl7.fhir.r4.model.MedicinalProductContraindication.class, fhirResourceJson);
            return _MedicinalProductContraindication;

        case "MedicinalProductIndication":
            org.hl7.fhir.r4.model.MedicinalProductIndication _MedicinalProductIndication = parser
                    .parseResource(org.hl7.fhir.r4.model.MedicinalProductIndication.class, fhirResourceJson);
            return _MedicinalProductIndication;

        case "MedicinalProductIngredient":
            org.hl7.fhir.r4.model.MedicinalProductIngredient _MedicinalProductIngredient = parser
                    .parseResource(org.hl7.fhir.r4.model.MedicinalProductIngredient.class, fhirResourceJson);
            return _MedicinalProductIngredient;

        case "MedicinalProductInteraction":
            org.hl7.fhir.r4.model.MedicinalProductInteraction _MedicinalProductInteraction = parser
                    .parseResource(org.hl7.fhir.r4.model.MedicinalProductInteraction.class, fhirResourceJson);
            return _MedicinalProductInteraction;

        case "MedicinalProductManufactured":
            org.hl7.fhir.r4.model.MedicinalProductManufactured _MedicinalProductManufactured = parser
                    .parseResource(org.hl7.fhir.r4.model.MedicinalProductManufactured.class, fhirResourceJson);
            return _MedicinalProductManufactured;

        case "MedicinalProductPackaged":
            org.hl7.fhir.r4.model.MedicinalProductPackaged _MedicinalProductPackaged = parser
                    .parseResource(org.hl7.fhir.r4.model.MedicinalProductPackaged.class, fhirResourceJson);
            return _MedicinalProductPackaged;

        case "MedicinalProductPharmaceutical":
            org.hl7.fhir.r4.model.MedicinalProductPharmaceutical _MedicinalProductPharmaceutical = parser
                    .parseResource(org.hl7.fhir.r4.model.MedicinalProductPharmaceutical.class, fhirResourceJson);
            return _MedicinalProductPharmaceutical;

        case "MedicinalProductUndesirableEffect":
            org.hl7.fhir.r4.model.MedicinalProductUndesirableEffect _MedicinalProductUndesirableEffect = parser
                    .parseResource(org.hl7.fhir.r4.model.MedicinalProductUndesirableEffect.class, fhirResourceJson);
            return _MedicinalProductUndesirableEffect;

        case "MessageDefinition":
            org.hl7.fhir.r4.model.MessageDefinition _MessageDefinition = parser
                    .parseResource(org.hl7.fhir.r4.model.MessageDefinition.class, fhirResourceJson);
            return _MessageDefinition;

        case "MessageHeader":
            org.hl7.fhir.r4.model.MessageHeader _MessageHeader = parser
                    .parseResource(org.hl7.fhir.r4.model.MessageHeader.class, fhirResourceJson);
            return _MessageHeader;

        case "MolecularSequence":
            org.hl7.fhir.r4.model.MolecularSequence _MolecularSequence = parser
                    .parseResource(org.hl7.fhir.r4.model.MolecularSequence.class, fhirResourceJson);
            return _MolecularSequence;

        case "NamingSystem":
            org.hl7.fhir.r4.model.NamingSystem _NamingSystem = parser
                    .parseResource(org.hl7.fhir.r4.model.NamingSystem.class, fhirResourceJson);
            return _NamingSystem;

        case "NutritionOrder":
            org.hl7.fhir.r4.model.NutritionOrder _NutritionOrder = parser
                    .parseResource(org.hl7.fhir.r4.model.NutritionOrder.class, fhirResourceJson);
            return _NutritionOrder;

        case "Observation":
            org.hl7.fhir.r4.model.Observation _Observation = parser
                    .parseResource(org.hl7.fhir.r4.model.Observation.class, fhirResourceJson);
            return _Observation;

        case "ObservationDefinition":
            org.hl7.fhir.r4.model.ObservationDefinition _ObservationDefinition = parser
                    .parseResource(org.hl7.fhir.r4.model.ObservationDefinition.class, fhirResourceJson);
            return _ObservationDefinition;

        case "OperationDefinition":
            org.hl7.fhir.r4.model.OperationDefinition _OperationDefinition = parser
                    .parseResource(org.hl7.fhir.r4.model.OperationDefinition.class, fhirResourceJson);
            return _OperationDefinition;

        case "OperationOutcome":
            org.hl7.fhir.r4.model.OperationOutcome _OperationOutcome = parser
                    .parseResource(org.hl7.fhir.r4.model.OperationOutcome.class, fhirResourceJson);
            return _OperationOutcome;

        case "Organization":
            org.hl7.fhir.r4.model.Organization _Organization = parser
                    .parseResource(org.hl7.fhir.r4.model.Organization.class, fhirResourceJson);
            return _Organization;

        case "OrganizationAffiliation":
            org.hl7.fhir.r4.model.OrganizationAffiliation _OrganizationAffiliation = parser
                    .parseResource(org.hl7.fhir.r4.model.OrganizationAffiliation.class, fhirResourceJson);
            return _OrganizationAffiliation;

        case "Parameters":
            org.hl7.fhir.r4.model.Parameters _Parameters = parser.parseResource(org.hl7.fhir.r4.model.Parameters.class,
                    fhirResourceJson);
            return _Parameters;

        case "Patient":
            org.hl7.fhir.r4.model.Patient _Patient = parser.parseResource(org.hl7.fhir.r4.model.Patient.class,
                    fhirResourceJson);
            return _Patient;

        case "PaymentNotice":
            org.hl7.fhir.r4.model.PaymentNotice _PaymentNotice = parser
                    .parseResource(org.hl7.fhir.r4.model.PaymentNotice.class, fhirResourceJson);
            return _PaymentNotice;

        case "PaymentReconciliation":
            org.hl7.fhir.r4.model.PaymentReconciliation _PaymentReconciliation = parser
                    .parseResource(org.hl7.fhir.r4.model.PaymentReconciliation.class, fhirResourceJson);
            return _PaymentReconciliation;

        case "Person":
            org.hl7.fhir.r4.model.Person _Person = parser.parseResource(org.hl7.fhir.r4.model.Person.class,
                    fhirResourceJson);
            return _Person;

        case "PlanDefinition":
            org.hl7.fhir.r4.model.PlanDefinition _PlanDefinition = parser
                    .parseResource(org.hl7.fhir.r4.model.PlanDefinition.class, fhirResourceJson);
            return _PlanDefinition;

        case "Practitioner":
            org.hl7.fhir.r4.model.Practitioner _Practitioner = parser
                    .parseResource(org.hl7.fhir.r4.model.Practitioner.class, fhirResourceJson);
            return _Practitioner;

        case "PractitionerRole":
            org.hl7.fhir.r4.model.PractitionerRole _PractitionerRole = parser
                    .parseResource(org.hl7.fhir.r4.model.PractitionerRole.class, fhirResourceJson);
            return _PractitionerRole;

        case "Procedure":
            org.hl7.fhir.r4.model.Procedure _Procedure = parser.parseResource(org.hl7.fhir.r4.model.Procedure.class,
                    fhirResourceJson);
            return _Procedure;

        case "Provenance":
            org.hl7.fhir.r4.model.Provenance _Provenance = parser.parseResource(org.hl7.fhir.r4.model.Provenance.class,
                    fhirResourceJson);
            return _Provenance;

        case "Questionnaire":
            org.hl7.fhir.r4.model.Questionnaire _Questionnaire = parser
                    .parseResource(org.hl7.fhir.r4.model.Questionnaire.class, fhirResourceJson);
            return _Questionnaire;

        case "QuestionnaireResponse":
            org.hl7.fhir.r4.model.QuestionnaireResponse _QuestionnaireResponse = parser
                    .parseResource(org.hl7.fhir.r4.model.QuestionnaireResponse.class, fhirResourceJson);
            return _QuestionnaireResponse;

        case "RelatedPerson":
            org.hl7.fhir.r4.model.RelatedPerson _RelatedPerson = parser
                    .parseResource(org.hl7.fhir.r4.model.RelatedPerson.class, fhirResourceJson);
            return _RelatedPerson;

        case "RequestGroup":
            org.hl7.fhir.r4.model.RequestGroup _RequestGroup = parser
                    .parseResource(org.hl7.fhir.r4.model.RequestGroup.class, fhirResourceJson);
            return _RequestGroup;

        case "ResearchDefinition":
            org.hl7.fhir.r4.model.ResearchDefinition _ResearchDefinition = parser
                    .parseResource(org.hl7.fhir.r4.model.ResearchDefinition.class, fhirResourceJson);
            return _ResearchDefinition;

        case "ResearchElementDefinition":
            org.hl7.fhir.r4.model.ResearchElementDefinition _ResearchElementDefinition = parser
                    .parseResource(org.hl7.fhir.r4.model.ResearchElementDefinition.class, fhirResourceJson);
            return _ResearchElementDefinition;

        case "ResearchStudy":
            org.hl7.fhir.r4.model.ResearchStudy _ResearchStudy = parser
                    .parseResource(org.hl7.fhir.r4.model.ResearchStudy.class, fhirResourceJson);
            return _ResearchStudy;

        case "ResearchSubject":
            org.hl7.fhir.r4.model.ResearchSubject _ResearchSubject = parser
                    .parseResource(org.hl7.fhir.r4.model.ResearchSubject.class, fhirResourceJson);
            return _ResearchSubject;

        case "RiskAssessment":
            org.hl7.fhir.r4.model.RiskAssessment _RiskAssessment = parser
                    .parseResource(org.hl7.fhir.r4.model.RiskAssessment.class, fhirResourceJson);
            return _RiskAssessment;

        case "RiskEvidenceSynthesis":
            org.hl7.fhir.r4.model.RiskEvidenceSynthesis _RiskEvidenceSynthesis = parser
                    .parseResource(org.hl7.fhir.r4.model.RiskEvidenceSynthesis.class, fhirResourceJson);
            return _RiskEvidenceSynthesis;

        case "Schedule":
            org.hl7.fhir.r4.model.Schedule _Schedule = parser.parseResource(org.hl7.fhir.r4.model.Schedule.class,
                    fhirResourceJson);
            return _Schedule;

        case "SearchParameter":
            org.hl7.fhir.r4.model.SearchParameter _SearchParameter = parser
                    .parseResource(org.hl7.fhir.r4.model.SearchParameter.class, fhirResourceJson);
            return _SearchParameter;

        case "ServiceRequest":
            org.hl7.fhir.r4.model.ServiceRequest _ServiceRequest = parser
                    .parseResource(org.hl7.fhir.r4.model.ServiceRequest.class, fhirResourceJson);
            return _ServiceRequest;

        case "Slot":
            org.hl7.fhir.r4.model.Slot _Slot = parser.parseResource(org.hl7.fhir.r4.model.Slot.class, fhirResourceJson);
            return _Slot;

        case "Specimen":
            org.hl7.fhir.r4.model.Specimen _Specimen = parser.parseResource(org.hl7.fhir.r4.model.Specimen.class,
                    fhirResourceJson);
            return _Specimen;

        case "SpecimenDefinition":
            org.hl7.fhir.r4.model.SpecimenDefinition _SpecimenDefinition = parser
                    .parseResource(org.hl7.fhir.r4.model.SpecimenDefinition.class, fhirResourceJson);
            return _SpecimenDefinition;

        case "StructureDefinition":
            org.hl7.fhir.r4.model.StructureDefinition _StructureDefinition = parser
                    .parseResource(org.hl7.fhir.r4.model.StructureDefinition.class, fhirResourceJson);
            return _StructureDefinition;

        case "StructureMap":
            org.hl7.fhir.r4.model.StructureMap _StructureMap = parser
                    .parseResource(org.hl7.fhir.r4.model.StructureMap.class, fhirResourceJson);
            return _StructureMap;

        case "Subscription":
            org.hl7.fhir.r4.model.Subscription _Subscription = parser
                    .parseResource(org.hl7.fhir.r4.model.Subscription.class, fhirResourceJson);
            return _Subscription;

        case "Substance":
            org.hl7.fhir.r4.model.Substance _Substance = parser.parseResource(org.hl7.fhir.r4.model.Substance.class,
                    fhirResourceJson);
            return _Substance;

        case "SubstanceNucleicAcid":
            org.hl7.fhir.r4.model.SubstanceNucleicAcid _SubstanceNucleicAcid = parser
                    .parseResource(org.hl7.fhir.r4.model.SubstanceNucleicAcid.class, fhirResourceJson);
            return _SubstanceNucleicAcid;

        case "SubstancePolymer":
            org.hl7.fhir.r4.model.SubstancePolymer _SubstancePolymer = parser
                    .parseResource(org.hl7.fhir.r4.model.SubstancePolymer.class, fhirResourceJson);
            return _SubstancePolymer;

        case "SubstanceProtein":
            org.hl7.fhir.r4.model.SubstanceProtein _SubstanceProtein = parser
                    .parseResource(org.hl7.fhir.r4.model.SubstanceProtein.class, fhirResourceJson);
            return _SubstanceProtein;

        case "SubstanceReferenceInformation":
            org.hl7.fhir.r4.model.SubstanceReferenceInformation _SubstanceReferenceInformation = parser
                    .parseResource(org.hl7.fhir.r4.model.SubstanceReferenceInformation.class, fhirResourceJson);
            return _SubstanceReferenceInformation;

        case "SubstanceSourceMaterial":
            org.hl7.fhir.r4.model.SubstanceSourceMaterial _SubstanceSourceMaterial = parser
                    .parseResource(org.hl7.fhir.r4.model.SubstanceSourceMaterial.class, fhirResourceJson);
            return _SubstanceSourceMaterial;

        case "SubstanceSpecification":
            org.hl7.fhir.r4.model.SubstanceSpecification _SubstanceSpecification = parser
                    .parseResource(org.hl7.fhir.r4.model.SubstanceSpecification.class, fhirResourceJson);
            return _SubstanceSpecification;

        case "SupplyDelivery":
            org.hl7.fhir.r4.model.SupplyDelivery _SupplyDelivery = parser
                    .parseResource(org.hl7.fhir.r4.model.SupplyDelivery.class, fhirResourceJson);
            return _SupplyDelivery;

        case "SupplyRequest":
            org.hl7.fhir.r4.model.SupplyRequest _SupplyRequest = parser
                    .parseResource(org.hl7.fhir.r4.model.SupplyRequest.class, fhirResourceJson);
            return _SupplyRequest;

        case "Task":
            org.hl7.fhir.r4.model.Task _Task = parser.parseResource(org.hl7.fhir.r4.model.Task.class, fhirResourceJson);
            return _Task;

        case "TerminologyCapabilities":
            org.hl7.fhir.r4.model.TerminologyCapabilities _TerminologyCapabilities = parser
                    .parseResource(org.hl7.fhir.r4.model.TerminologyCapabilities.class, fhirResourceJson);
            return _TerminologyCapabilities;

        case "TestReport":
            org.hl7.fhir.r4.model.TestReport _TestReport = parser.parseResource(org.hl7.fhir.r4.model.TestReport.class,
                    fhirResourceJson);
            return _TestReport;

        case "TestScript":
            org.hl7.fhir.r4.model.TestScript _TestScript = parser.parseResource(org.hl7.fhir.r4.model.TestScript.class,
                    fhirResourceJson);
            return _TestScript;

        case "ValueSet":
            org.hl7.fhir.r4.model.ValueSet _ValueSet = parser.parseResource(org.hl7.fhir.r4.model.ValueSet.class,
                    fhirResourceJson);
            return _ValueSet;

        case "VerificationResult":
            org.hl7.fhir.r4.model.VerificationResult _VerificationResult = parser
                    .parseResource(org.hl7.fhir.r4.model.VerificationResult.class, fhirResourceJson);
            return _VerificationResult;

        case "VisionPrescription":
            org.hl7.fhir.r4.model.VisionPrescription _VisionPrescription = parser
                    .parseResource(org.hl7.fhir.r4.model.VisionPrescription.class, fhirResourceJson);
            return _VisionPrescription;

        default:
            throw new IllegalStateException("Unexpected value: " + resourceType);
        }
    }

    public org.hl7.fhir.r5.model.Resource parseR5(String fhirResourceJson, String resourceType) {
        switch (resourceType) {

        case "Account":
            org.hl7.fhir.r5.model.Account _Account = parser.parseResource(org.hl7.fhir.r5.model.Account.class,
                    fhirResourceJson);
            return _Account;

        case "ActivityDefinition":
            org.hl7.fhir.r5.model.ActivityDefinition _ActivityDefinition = parser
                    .parseResource(org.hl7.fhir.r5.model.ActivityDefinition.class, fhirResourceJson);
            return _ActivityDefinition;

        case "AdministrableProductDefinition":
            org.hl7.fhir.r5.model.AdministrableProductDefinition _AdministrableProductDefinition = parser
                    .parseResource(org.hl7.fhir.r5.model.AdministrableProductDefinition.class, fhirResourceJson);
            return _AdministrableProductDefinition;

        case "AdverseEvent":
            org.hl7.fhir.r5.model.AdverseEvent _AdverseEvent = parser
                    .parseResource(org.hl7.fhir.r5.model.AdverseEvent.class, fhirResourceJson);
            return _AdverseEvent;

        case "AllergyIntolerance":
            org.hl7.fhir.r5.model.AllergyIntolerance _AllergyIntolerance = parser
                    .parseResource(org.hl7.fhir.r5.model.AllergyIntolerance.class, fhirResourceJson);
            return _AllergyIntolerance;

        case "Appointment":
            org.hl7.fhir.r5.model.Appointment _Appointment = parser
                    .parseResource(org.hl7.fhir.r5.model.Appointment.class, fhirResourceJson);
            return _Appointment;

        case "AppointmentResponse":
            org.hl7.fhir.r5.model.AppointmentResponse _AppointmentResponse = parser
                    .parseResource(org.hl7.fhir.r5.model.AppointmentResponse.class, fhirResourceJson);
            return _AppointmentResponse;

        case "AuditEvent":
            org.hl7.fhir.r5.model.AuditEvent _AuditEvent = parser.parseResource(org.hl7.fhir.r5.model.AuditEvent.class,
                    fhirResourceJson);
            return _AuditEvent;

        case "Basic":
            org.hl7.fhir.r5.model.Basic _Basic = parser.parseResource(org.hl7.fhir.r5.model.Basic.class,
                    fhirResourceJson);
            return _Basic;

        case "Binary":
            org.hl7.fhir.r5.model.Binary _Binary = parser.parseResource(org.hl7.fhir.r5.model.Binary.class,
                    fhirResourceJson);
            return _Binary;

        case "BiologicallyDerivedProduct":
            org.hl7.fhir.r5.model.BiologicallyDerivedProduct _BiologicallyDerivedProduct = parser
                    .parseResource(org.hl7.fhir.r5.model.BiologicallyDerivedProduct.class, fhirResourceJson);
            return _BiologicallyDerivedProduct;

        case "BodyStructure":
            org.hl7.fhir.r5.model.BodyStructure _BodyStructure = parser
                    .parseResource(org.hl7.fhir.r5.model.BodyStructure.class, fhirResourceJson);
            return _BodyStructure;

        case "Bundle":
            org.hl7.fhir.r5.model.Bundle _Bundle = parser.parseResource(org.hl7.fhir.r5.model.Bundle.class,
                    fhirResourceJson);
            return _Bundle;

        case "CapabilityStatement":
            org.hl7.fhir.r5.model.CapabilityStatement _CapabilityStatement = parser
                    .parseResource(org.hl7.fhir.r5.model.CapabilityStatement.class, fhirResourceJson);
            return _CapabilityStatement;

        case "CapabilityStatement2":
            org.hl7.fhir.r5.model.CapabilityStatement2 _CapabilityStatement2 = parser
                    .parseResource(org.hl7.fhir.r5.model.CapabilityStatement2.class, fhirResourceJson);
            return _CapabilityStatement2;

        case "CarePlan":
            org.hl7.fhir.r5.model.CarePlan _CarePlan = parser.parseResource(org.hl7.fhir.r5.model.CarePlan.class,
                    fhirResourceJson);
            return _CarePlan;

        case "CareTeam":
            org.hl7.fhir.r5.model.CareTeam _CareTeam = parser.parseResource(org.hl7.fhir.r5.model.CareTeam.class,
                    fhirResourceJson);
            return _CareTeam;

        case "CatalogEntry":
            org.hl7.fhir.r5.model.CatalogEntry _CatalogEntry = parser
                    .parseResource(org.hl7.fhir.r5.model.CatalogEntry.class, fhirResourceJson);
            return _CatalogEntry;

        case "ChargeItem":
            org.hl7.fhir.r5.model.ChargeItem _ChargeItem = parser.parseResource(org.hl7.fhir.r5.model.ChargeItem.class,
                    fhirResourceJson);
            return _ChargeItem;

        case "ChargeItemDefinition":
            org.hl7.fhir.r5.model.ChargeItemDefinition _ChargeItemDefinition = parser
                    .parseResource(org.hl7.fhir.r5.model.ChargeItemDefinition.class, fhirResourceJson);
            return _ChargeItemDefinition;

        case "Citation":
            org.hl7.fhir.r5.model.Citation _Citation = parser.parseResource(org.hl7.fhir.r5.model.Citation.class,
                    fhirResourceJson);
            return _Citation;

        case "Claim":
            org.hl7.fhir.r5.model.Claim _Claim = parser.parseResource(org.hl7.fhir.r5.model.Claim.class,
                    fhirResourceJson);
            return _Claim;

        case "ClaimResponse":
            org.hl7.fhir.r5.model.ClaimResponse _ClaimResponse = parser
                    .parseResource(org.hl7.fhir.r5.model.ClaimResponse.class, fhirResourceJson);
            return _ClaimResponse;

        case "ClinicalImpression":
            org.hl7.fhir.r5.model.ClinicalImpression _ClinicalImpression = parser
                    .parseResource(org.hl7.fhir.r5.model.ClinicalImpression.class, fhirResourceJson);
            return _ClinicalImpression;

        case "ClinicalUseIssue":
            org.hl7.fhir.r5.model.ClinicalUseIssue _ClinicalUseIssue = parser
                    .parseResource(org.hl7.fhir.r5.model.ClinicalUseIssue.class, fhirResourceJson);
            return _ClinicalUseIssue;

        case "CodeSystem":
            org.hl7.fhir.r5.model.CodeSystem _CodeSystem = parser.parseResource(org.hl7.fhir.r5.model.CodeSystem.class,
                    fhirResourceJson);
            return _CodeSystem;

        case "Communication":
            org.hl7.fhir.r5.model.Communication _Communication = parser
                    .parseResource(org.hl7.fhir.r5.model.Communication.class, fhirResourceJson);
            return _Communication;

        case "CommunicationRequest":
            org.hl7.fhir.r5.model.CommunicationRequest _CommunicationRequest = parser
                    .parseResource(org.hl7.fhir.r5.model.CommunicationRequest.class, fhirResourceJson);
            return _CommunicationRequest;

        case "CompartmentDefinition":
            org.hl7.fhir.r5.model.CompartmentDefinition _CompartmentDefinition = parser
                    .parseResource(org.hl7.fhir.r5.model.CompartmentDefinition.class, fhirResourceJson);
            return _CompartmentDefinition;

        case "Composition":
            org.hl7.fhir.r5.model.Composition _Composition = parser
                    .parseResource(org.hl7.fhir.r5.model.Composition.class, fhirResourceJson);
            return _Composition;

        case "ConceptMap":
            org.hl7.fhir.r5.model.ConceptMap _ConceptMap = parser.parseResource(org.hl7.fhir.r5.model.ConceptMap.class,
                    fhirResourceJson);
            return _ConceptMap;

        case "Condition":
            org.hl7.fhir.r5.model.Condition _Condition = parser.parseResource(org.hl7.fhir.r5.model.Condition.class,
                    fhirResourceJson);
            return _Condition;

        case "ConditionDefinition":
            org.hl7.fhir.r5.model.ConditionDefinition _ConditionDefinition = parser
                    .parseResource(org.hl7.fhir.r5.model.ConditionDefinition.class, fhirResourceJson);
            return _ConditionDefinition;

        case "Consent":
            org.hl7.fhir.r5.model.Consent _Consent = parser.parseResource(org.hl7.fhir.r5.model.Consent.class,
                    fhirResourceJson);
            return _Consent;

        case "Contract":
            org.hl7.fhir.r5.model.Contract _Contract = parser.parseResource(org.hl7.fhir.r5.model.Contract.class,
                    fhirResourceJson);
            return _Contract;

        case "Coverage":
            org.hl7.fhir.r5.model.Coverage _Coverage = parser.parseResource(org.hl7.fhir.r5.model.Coverage.class,
                    fhirResourceJson);
            return _Coverage;

        case "CoverageEligibilityRequest":
            org.hl7.fhir.r5.model.CoverageEligibilityRequest _CoverageEligibilityRequest = parser
                    .parseResource(org.hl7.fhir.r5.model.CoverageEligibilityRequest.class, fhirResourceJson);
            return _CoverageEligibilityRequest;

        case "CoverageEligibilityResponse":
            org.hl7.fhir.r5.model.CoverageEligibilityResponse _CoverageEligibilityResponse = parser
                    .parseResource(org.hl7.fhir.r5.model.CoverageEligibilityResponse.class, fhirResourceJson);
            return _CoverageEligibilityResponse;

        case "DetectedIssue":
            org.hl7.fhir.r5.model.DetectedIssue _DetectedIssue = parser
                    .parseResource(org.hl7.fhir.r5.model.DetectedIssue.class, fhirResourceJson);
            return _DetectedIssue;

        case "Device":
            org.hl7.fhir.r5.model.Device _Device = parser.parseResource(org.hl7.fhir.r5.model.Device.class,
                    fhirResourceJson);
            return _Device;

        case "DeviceDefinition":
            org.hl7.fhir.r5.model.DeviceDefinition _DeviceDefinition = parser
                    .parseResource(org.hl7.fhir.r5.model.DeviceDefinition.class, fhirResourceJson);
            return _DeviceDefinition;

        case "DeviceMetric":
            org.hl7.fhir.r5.model.DeviceMetric _DeviceMetric = parser
                    .parseResource(org.hl7.fhir.r5.model.DeviceMetric.class, fhirResourceJson);
            return _DeviceMetric;

        case "DeviceRequest":
            org.hl7.fhir.r5.model.DeviceRequest _DeviceRequest = parser
                    .parseResource(org.hl7.fhir.r5.model.DeviceRequest.class, fhirResourceJson);
            return _DeviceRequest;

        case "DeviceUsage":
            org.hl7.fhir.r5.model.DeviceUsage _DeviceUsage = parser
                    .parseResource(org.hl7.fhir.r5.model.DeviceUsage.class, fhirResourceJson);
            return _DeviceUsage;

        case "DiagnosticReport":
            org.hl7.fhir.r5.model.DiagnosticReport _DiagnosticReport = parser
                    .parseResource(org.hl7.fhir.r5.model.DiagnosticReport.class, fhirResourceJson);
            return _DiagnosticReport;

        case "DocumentManifest":
            org.hl7.fhir.r5.model.DocumentManifest _DocumentManifest = parser
                    .parseResource(org.hl7.fhir.r5.model.DocumentManifest.class, fhirResourceJson);
            return _DocumentManifest;

        case "DocumentReference":
            org.hl7.fhir.r5.model.DocumentReference _DocumentReference = parser
                    .parseResource(org.hl7.fhir.r5.model.DocumentReference.class, fhirResourceJson);
            return _DocumentReference;

        case "Encounter":
            org.hl7.fhir.r5.model.Encounter _Encounter = parser.parseResource(org.hl7.fhir.r5.model.Encounter.class,
                    fhirResourceJson);
            return _Encounter;

        case "Endpoint":
            org.hl7.fhir.r5.model.Endpoint _Endpoint = parser.parseResource(org.hl7.fhir.r5.model.Endpoint.class,
                    fhirResourceJson);
            return _Endpoint;

        case "EnrollmentRequest":
            org.hl7.fhir.r5.model.EnrollmentRequest _EnrollmentRequest = parser
                    .parseResource(org.hl7.fhir.r5.model.EnrollmentRequest.class, fhirResourceJson);
            return _EnrollmentRequest;

        case "EnrollmentResponse":
            org.hl7.fhir.r5.model.EnrollmentResponse _EnrollmentResponse = parser
                    .parseResource(org.hl7.fhir.r5.model.EnrollmentResponse.class, fhirResourceJson);
            return _EnrollmentResponse;

        case "EpisodeOfCare":
            org.hl7.fhir.r5.model.EpisodeOfCare _EpisodeOfCare = parser
                    .parseResource(org.hl7.fhir.r5.model.EpisodeOfCare.class, fhirResourceJson);
            return _EpisodeOfCare;

        case "EventDefinition":
            org.hl7.fhir.r5.model.EventDefinition _EventDefinition = parser
                    .parseResource(org.hl7.fhir.r5.model.EventDefinition.class, fhirResourceJson);
            return _EventDefinition;

        case "Evidence":
            org.hl7.fhir.r5.model.Evidence _Evidence = parser.parseResource(org.hl7.fhir.r5.model.Evidence.class,
                    fhirResourceJson);
            return _Evidence;

        case "EvidenceReport":
            org.hl7.fhir.r5.model.EvidenceReport _EvidenceReport = parser
                    .parseResource(org.hl7.fhir.r5.model.EvidenceReport.class, fhirResourceJson);
            return _EvidenceReport;

        case "EvidenceVariable":
            org.hl7.fhir.r5.model.EvidenceVariable _EvidenceVariable = parser
                    .parseResource(org.hl7.fhir.r5.model.EvidenceVariable.class, fhirResourceJson);
            return _EvidenceVariable;

        case "ExampleScenario":
            org.hl7.fhir.r5.model.ExampleScenario _ExampleScenario = parser
                    .parseResource(org.hl7.fhir.r5.model.ExampleScenario.class, fhirResourceJson);
            return _ExampleScenario;

        case "ExplanationOfBenefit":
            org.hl7.fhir.r5.model.ExplanationOfBenefit _ExplanationOfBenefit = parser
                    .parseResource(org.hl7.fhir.r5.model.ExplanationOfBenefit.class, fhirResourceJson);
            return _ExplanationOfBenefit;

        case "FamilyMemberHistory":
            org.hl7.fhir.r5.model.FamilyMemberHistory _FamilyMemberHistory = parser
                    .parseResource(org.hl7.fhir.r5.model.FamilyMemberHistory.class, fhirResourceJson);
            return _FamilyMemberHistory;

        case "Flag":
            org.hl7.fhir.r5.model.Flag _Flag = parser.parseResource(org.hl7.fhir.r5.model.Flag.class, fhirResourceJson);
            return _Flag;

        case "Goal":
            org.hl7.fhir.r5.model.Goal _Goal = parser.parseResource(org.hl7.fhir.r5.model.Goal.class, fhirResourceJson);
            return _Goal;

        case "GraphDefinition":
            org.hl7.fhir.r5.model.GraphDefinition _GraphDefinition = parser
                    .parseResource(org.hl7.fhir.r5.model.GraphDefinition.class, fhirResourceJson);
            return _GraphDefinition;

        case "Group":
            org.hl7.fhir.r5.model.Group _Group = parser.parseResource(org.hl7.fhir.r5.model.Group.class,
                    fhirResourceJson);
            return _Group;

        case "GuidanceResponse":
            org.hl7.fhir.r5.model.GuidanceResponse _GuidanceResponse = parser
                    .parseResource(org.hl7.fhir.r5.model.GuidanceResponse.class, fhirResourceJson);
            return _GuidanceResponse;

        case "HealthcareService":
            org.hl7.fhir.r5.model.HealthcareService _HealthcareService = parser
                    .parseResource(org.hl7.fhir.r5.model.HealthcareService.class, fhirResourceJson);
            return _HealthcareService;

        case "ImagingStudy":
            org.hl7.fhir.r5.model.ImagingStudy _ImagingStudy = parser
                    .parseResource(org.hl7.fhir.r5.model.ImagingStudy.class, fhirResourceJson);
            return _ImagingStudy;

        case "Immunization":
            org.hl7.fhir.r5.model.Immunization _Immunization = parser
                    .parseResource(org.hl7.fhir.r5.model.Immunization.class, fhirResourceJson);
            return _Immunization;

        case "ImmunizationEvaluation":
            org.hl7.fhir.r5.model.ImmunizationEvaluation _ImmunizationEvaluation = parser
                    .parseResource(org.hl7.fhir.r5.model.ImmunizationEvaluation.class, fhirResourceJson);
            return _ImmunizationEvaluation;

        case "ImmunizationRecommendation":
            org.hl7.fhir.r5.model.ImmunizationRecommendation _ImmunizationRecommendation = parser
                    .parseResource(org.hl7.fhir.r5.model.ImmunizationRecommendation.class, fhirResourceJson);
            return _ImmunizationRecommendation;

        case "ImplementationGuide":
            org.hl7.fhir.r5.model.ImplementationGuide _ImplementationGuide = parser
                    .parseResource(org.hl7.fhir.r5.model.ImplementationGuide.class, fhirResourceJson);
            return _ImplementationGuide;

        case "Ingredient":
            org.hl7.fhir.r5.model.Ingredient _Ingredient = parser.parseResource(org.hl7.fhir.r5.model.Ingredient.class,
                    fhirResourceJson);
            return _Ingredient;

        case "InsurancePlan":
            org.hl7.fhir.r5.model.InsurancePlan _InsurancePlan = parser
                    .parseResource(org.hl7.fhir.r5.model.InsurancePlan.class, fhirResourceJson);
            return _InsurancePlan;

        case "Invoice":
            org.hl7.fhir.r5.model.Invoice _Invoice = parser.parseResource(org.hl7.fhir.r5.model.Invoice.class,
                    fhirResourceJson);
            return _Invoice;

        case "Library":
            org.hl7.fhir.r5.model.Library _Library = parser.parseResource(org.hl7.fhir.r5.model.Library.class,
                    fhirResourceJson);
            return _Library;

        case "Linkage":
            org.hl7.fhir.r5.model.Linkage _Linkage = parser.parseResource(org.hl7.fhir.r5.model.Linkage.class,
                    fhirResourceJson);
            return _Linkage;

        case "ListResource":
            org.hl7.fhir.r5.model.ListResource _ListResource = parser
                    .parseResource(org.hl7.fhir.r5.model.ListResource.class, fhirResourceJson);
            return _ListResource;

        case "Location":
            org.hl7.fhir.r5.model.Location _Location = parser.parseResource(org.hl7.fhir.r5.model.Location.class,
                    fhirResourceJson);
            return _Location;

        case "ManufacturedItemDefinition":
            org.hl7.fhir.r5.model.ManufacturedItemDefinition _ManufacturedItemDefinition = parser
                    .parseResource(org.hl7.fhir.r5.model.ManufacturedItemDefinition.class, fhirResourceJson);
            return _ManufacturedItemDefinition;

        case "Measure":
            org.hl7.fhir.r5.model.Measure _Measure = parser.parseResource(org.hl7.fhir.r5.model.Measure.class,
                    fhirResourceJson);
            return _Measure;

        case "MeasureReport":
            org.hl7.fhir.r5.model.MeasureReport _MeasureReport = parser
                    .parseResource(org.hl7.fhir.r5.model.MeasureReport.class, fhirResourceJson);
            return _MeasureReport;

        case "Medication":
            org.hl7.fhir.r5.model.Medication _Medication = parser.parseResource(org.hl7.fhir.r5.model.Medication.class,
                    fhirResourceJson);
            return _Medication;

        case "MedicationAdministration":
            org.hl7.fhir.r5.model.MedicationAdministration _MedicationAdministration = parser
                    .parseResource(org.hl7.fhir.r5.model.MedicationAdministration.class, fhirResourceJson);
            return _MedicationAdministration;

        case "MedicationDispense":
            org.hl7.fhir.r5.model.MedicationDispense _MedicationDispense = parser
                    .parseResource(org.hl7.fhir.r5.model.MedicationDispense.class, fhirResourceJson);
            return _MedicationDispense;

        case "MedicationKnowledge":
            org.hl7.fhir.r5.model.MedicationKnowledge _MedicationKnowledge = parser
                    .parseResource(org.hl7.fhir.r5.model.MedicationKnowledge.class, fhirResourceJson);
            return _MedicationKnowledge;

        case "MedicationRequest":
            org.hl7.fhir.r5.model.MedicationRequest _MedicationRequest = parser
                    .parseResource(org.hl7.fhir.r5.model.MedicationRequest.class, fhirResourceJson);
            return _MedicationRequest;

        case "MedicationUsage":
            org.hl7.fhir.r5.model.MedicationUsage _MedicationUsage = parser
                    .parseResource(org.hl7.fhir.r5.model.MedicationUsage.class, fhirResourceJson);
            return _MedicationUsage;

        case "MedicinalProductDefinition":
            org.hl7.fhir.r5.model.MedicinalProductDefinition _MedicinalProductDefinition = parser
                    .parseResource(org.hl7.fhir.r5.model.MedicinalProductDefinition.class, fhirResourceJson);
            return _MedicinalProductDefinition;

        case "MessageDefinition":
            org.hl7.fhir.r5.model.MessageDefinition _MessageDefinition = parser
                    .parseResource(org.hl7.fhir.r5.model.MessageDefinition.class, fhirResourceJson);
            return _MessageDefinition;

        case "MessageHeader":
            org.hl7.fhir.r5.model.MessageHeader _MessageHeader = parser
                    .parseResource(org.hl7.fhir.r5.model.MessageHeader.class, fhirResourceJson);
            return _MessageHeader;

        case "MolecularSequence":
            org.hl7.fhir.r5.model.MolecularSequence _MolecularSequence = parser
                    .parseResource(org.hl7.fhir.r5.model.MolecularSequence.class, fhirResourceJson);
            return _MolecularSequence;

        case "NamingSystem":
            org.hl7.fhir.r5.model.NamingSystem _NamingSystem = parser
                    .parseResource(org.hl7.fhir.r5.model.NamingSystem.class, fhirResourceJson);
            return _NamingSystem;

        case "NutritionIntake":
            org.hl7.fhir.r5.model.NutritionIntake _NutritionIntake = parser
                    .parseResource(org.hl7.fhir.r5.model.NutritionIntake.class, fhirResourceJson);
            return _NutritionIntake;

        case "NutritionOrder":
            org.hl7.fhir.r5.model.NutritionOrder _NutritionOrder = parser
                    .parseResource(org.hl7.fhir.r5.model.NutritionOrder.class, fhirResourceJson);
            return _NutritionOrder;

        case "NutritionProduct":
            org.hl7.fhir.r5.model.NutritionProduct _NutritionProduct = parser
                    .parseResource(org.hl7.fhir.r5.model.NutritionProduct.class, fhirResourceJson);
            return _NutritionProduct;

        case "Observation":
            org.hl7.fhir.r5.model.Observation _Observation = parser
                    .parseResource(org.hl7.fhir.r5.model.Observation.class, fhirResourceJson);
            return _Observation;

        case "ObservationDefinition":
            org.hl7.fhir.r5.model.ObservationDefinition _ObservationDefinition = parser
                    .parseResource(org.hl7.fhir.r5.model.ObservationDefinition.class, fhirResourceJson);
            return _ObservationDefinition;

        case "OperationDefinition":
            org.hl7.fhir.r5.model.OperationDefinition _OperationDefinition = parser
                    .parseResource(org.hl7.fhir.r5.model.OperationDefinition.class, fhirResourceJson);
            return _OperationDefinition;

        case "OperationOutcome":
            org.hl7.fhir.r5.model.OperationOutcome _OperationOutcome = parser
                    .parseResource(org.hl7.fhir.r5.model.OperationOutcome.class, fhirResourceJson);
            return _OperationOutcome;

        case "Organization":
            org.hl7.fhir.r5.model.Organization _Organization = parser
                    .parseResource(org.hl7.fhir.r5.model.Organization.class, fhirResourceJson);
            return _Organization;

        case "OrganizationAffiliation":
            org.hl7.fhir.r5.model.OrganizationAffiliation _OrganizationAffiliation = parser
                    .parseResource(org.hl7.fhir.r5.model.OrganizationAffiliation.class, fhirResourceJson);
            return _OrganizationAffiliation;

        case "PackagedProductDefinition":
            org.hl7.fhir.r5.model.PackagedProductDefinition _PackagedProductDefinition = parser
                    .parseResource(org.hl7.fhir.r5.model.PackagedProductDefinition.class, fhirResourceJson);
            return _PackagedProductDefinition;

        case "Parameters":
            org.hl7.fhir.r5.model.Parameters _Parameters = parser.parseResource(org.hl7.fhir.r5.model.Parameters.class,
                    fhirResourceJson);
            return _Parameters;

        case "Patient":
            org.hl7.fhir.r5.model.Patient _Patient = parser.parseResource(org.hl7.fhir.r5.model.Patient.class,
                    fhirResourceJson);
            return _Patient;

        case "PaymentNotice":
            org.hl7.fhir.r5.model.PaymentNotice _PaymentNotice = parser
                    .parseResource(org.hl7.fhir.r5.model.PaymentNotice.class, fhirResourceJson);
            return _PaymentNotice;

        case "PaymentReconciliation":
            org.hl7.fhir.r5.model.PaymentReconciliation _PaymentReconciliation = parser
                    .parseResource(org.hl7.fhir.r5.model.PaymentReconciliation.class, fhirResourceJson);
            return _PaymentReconciliation;

        case "Permission":
            org.hl7.fhir.r5.model.Permission _Permission = parser.parseResource(org.hl7.fhir.r5.model.Permission.class,
                    fhirResourceJson);
            return _Permission;

        case "Person":
            org.hl7.fhir.r5.model.Person _Person = parser.parseResource(org.hl7.fhir.r5.model.Person.class,
                    fhirResourceJson);
            return _Person;

        case "PlanDefinition":
            org.hl7.fhir.r5.model.PlanDefinition _PlanDefinition = parser
                    .parseResource(org.hl7.fhir.r5.model.PlanDefinition.class, fhirResourceJson);
            return _PlanDefinition;

        case "Practitioner":
            org.hl7.fhir.r5.model.Practitioner _Practitioner = parser
                    .parseResource(org.hl7.fhir.r5.model.Practitioner.class, fhirResourceJson);
            return _Practitioner;

        case "PractitionerRole":
            org.hl7.fhir.r5.model.PractitionerRole _PractitionerRole = parser
                    .parseResource(org.hl7.fhir.r5.model.PractitionerRole.class, fhirResourceJson);
            return _PractitionerRole;

        case "Procedure":
            org.hl7.fhir.r5.model.Procedure _Procedure = parser.parseResource(org.hl7.fhir.r5.model.Procedure.class,
                    fhirResourceJson);
            return _Procedure;

        case "Provenance":
            org.hl7.fhir.r5.model.Provenance _Provenance = parser.parseResource(org.hl7.fhir.r5.model.Provenance.class,
                    fhirResourceJson);
            return _Provenance;

        case "Questionnaire":
            org.hl7.fhir.r5.model.Questionnaire _Questionnaire = parser
                    .parseResource(org.hl7.fhir.r5.model.Questionnaire.class, fhirResourceJson);
            return _Questionnaire;

        case "QuestionnaireResponse":
            org.hl7.fhir.r5.model.QuestionnaireResponse _QuestionnaireResponse = parser
                    .parseResource(org.hl7.fhir.r5.model.QuestionnaireResponse.class, fhirResourceJson);
            return _QuestionnaireResponse;

        case "RegulatedAuthorization":
            org.hl7.fhir.r5.model.RegulatedAuthorization _RegulatedAuthorization = parser
                    .parseResource(org.hl7.fhir.r5.model.RegulatedAuthorization.class, fhirResourceJson);
            return _RegulatedAuthorization;

        case "RelatedPerson":
            org.hl7.fhir.r5.model.RelatedPerson _RelatedPerson = parser
                    .parseResource(org.hl7.fhir.r5.model.RelatedPerson.class, fhirResourceJson);
            return _RelatedPerson;

        case "RequestGroup":
            org.hl7.fhir.r5.model.RequestGroup _RequestGroup = parser
                    .parseResource(org.hl7.fhir.r5.model.RequestGroup.class, fhirResourceJson);
            return _RequestGroup;

        case "ResearchStudy":
            org.hl7.fhir.r5.model.ResearchStudy _ResearchStudy = parser
                    .parseResource(org.hl7.fhir.r5.model.ResearchStudy.class, fhirResourceJson);
            return _ResearchStudy;

        case "ResearchSubject":
            org.hl7.fhir.r5.model.ResearchSubject _ResearchSubject = parser
                    .parseResource(org.hl7.fhir.r5.model.ResearchSubject.class, fhirResourceJson);
            return _ResearchSubject;

        case "RiskAssessment":
            org.hl7.fhir.r5.model.RiskAssessment _RiskAssessment = parser
                    .parseResource(org.hl7.fhir.r5.model.RiskAssessment.class, fhirResourceJson);
            return _RiskAssessment;

        case "Schedule":
            org.hl7.fhir.r5.model.Schedule _Schedule = parser.parseResource(org.hl7.fhir.r5.model.Schedule.class,
                    fhirResourceJson);
            return _Schedule;

        case "SearchParameter":
            org.hl7.fhir.r5.model.SearchParameter _SearchParameter = parser
                    .parseResource(org.hl7.fhir.r5.model.SearchParameter.class, fhirResourceJson);
            return _SearchParameter;

        case "ServiceRequest":
            org.hl7.fhir.r5.model.ServiceRequest _ServiceRequest = parser
                    .parseResource(org.hl7.fhir.r5.model.ServiceRequest.class, fhirResourceJson);
            return _ServiceRequest;

        case "Slot":
            org.hl7.fhir.r5.model.Slot _Slot = parser.parseResource(org.hl7.fhir.r5.model.Slot.class, fhirResourceJson);
            return _Slot;

        case "Specimen":
            org.hl7.fhir.r5.model.Specimen _Specimen = parser.parseResource(org.hl7.fhir.r5.model.Specimen.class,
                    fhirResourceJson);
            return _Specimen;

        case "SpecimenDefinition":
            org.hl7.fhir.r5.model.SpecimenDefinition _SpecimenDefinition = parser
                    .parseResource(org.hl7.fhir.r5.model.SpecimenDefinition.class, fhirResourceJson);
            return _SpecimenDefinition;

        case "StructureDefinition":
            org.hl7.fhir.r5.model.StructureDefinition _StructureDefinition = parser
                    .parseResource(org.hl7.fhir.r5.model.StructureDefinition.class, fhirResourceJson);
            return _StructureDefinition;

        case "StructureMap":
            org.hl7.fhir.r5.model.StructureMap _StructureMap = parser
                    .parseResource(org.hl7.fhir.r5.model.StructureMap.class, fhirResourceJson);
            return _StructureMap;

        case "Subscription":
            org.hl7.fhir.r5.model.Subscription _Subscription = parser
                    .parseResource(org.hl7.fhir.r5.model.Subscription.class, fhirResourceJson);
            return _Subscription;

        case "SubscriptionStatus":
            org.hl7.fhir.r5.model.SubscriptionStatus _SubscriptionStatus = parser
                    .parseResource(org.hl7.fhir.r5.model.SubscriptionStatus.class, fhirResourceJson);
            return _SubscriptionStatus;

        case "SubscriptionTopic":
            org.hl7.fhir.r5.model.SubscriptionTopic _SubscriptionTopic = parser
                    .parseResource(org.hl7.fhir.r5.model.SubscriptionTopic.class, fhirResourceJson);
            return _SubscriptionTopic;

        case "Substance":
            org.hl7.fhir.r5.model.Substance _Substance = parser.parseResource(org.hl7.fhir.r5.model.Substance.class,
                    fhirResourceJson);
            return _Substance;

        case "SubstanceDefinition":
            org.hl7.fhir.r5.model.SubstanceDefinition _SubstanceDefinition = parser
                    .parseResource(org.hl7.fhir.r5.model.SubstanceDefinition.class, fhirResourceJson);
            return _SubstanceDefinition;

        case "SubstanceNucleicAcid":
            org.hl7.fhir.r5.model.SubstanceNucleicAcid _SubstanceNucleicAcid = parser
                    .parseResource(org.hl7.fhir.r5.model.SubstanceNucleicAcid.class, fhirResourceJson);
            return _SubstanceNucleicAcid;

        case "SubstancePolymer":
            org.hl7.fhir.r5.model.SubstancePolymer _SubstancePolymer = parser
                    .parseResource(org.hl7.fhir.r5.model.SubstancePolymer.class, fhirResourceJson);
            return _SubstancePolymer;

        case "SubstanceProtein":
            org.hl7.fhir.r5.model.SubstanceProtein _SubstanceProtein = parser
                    .parseResource(org.hl7.fhir.r5.model.SubstanceProtein.class, fhirResourceJson);
            return _SubstanceProtein;

        case "SubstanceReferenceInformation":
            org.hl7.fhir.r5.model.SubstanceReferenceInformation _SubstanceReferenceInformation = parser
                    .parseResource(org.hl7.fhir.r5.model.SubstanceReferenceInformation.class, fhirResourceJson);
            return _SubstanceReferenceInformation;

        case "SubstanceSourceMaterial":
            org.hl7.fhir.r5.model.SubstanceSourceMaterial _SubstanceSourceMaterial = parser
                    .parseResource(org.hl7.fhir.r5.model.SubstanceSourceMaterial.class, fhirResourceJson);
            return _SubstanceSourceMaterial;

        case "SupplyDelivery":
            org.hl7.fhir.r5.model.SupplyDelivery _SupplyDelivery = parser
                    .parseResource(org.hl7.fhir.r5.model.SupplyDelivery.class, fhirResourceJson);
            return _SupplyDelivery;

        case "SupplyRequest":
            org.hl7.fhir.r5.model.SupplyRequest _SupplyRequest = parser
                    .parseResource(org.hl7.fhir.r5.model.SupplyRequest.class, fhirResourceJson);
            return _SupplyRequest;

        case "Task":
            org.hl7.fhir.r5.model.Task _Task = parser.parseResource(org.hl7.fhir.r5.model.Task.class, fhirResourceJson);
            return _Task;

        case "TerminologyCapabilities":
            org.hl7.fhir.r5.model.TerminologyCapabilities _TerminologyCapabilities = parser
                    .parseResource(org.hl7.fhir.r5.model.TerminologyCapabilities.class, fhirResourceJson);
            return _TerminologyCapabilities;

        case "TestReport":
            org.hl7.fhir.r5.model.TestReport _TestReport = parser.parseResource(org.hl7.fhir.r5.model.TestReport.class,
                    fhirResourceJson);
            return _TestReport;

        case "TestScript":
            org.hl7.fhir.r5.model.TestScript _TestScript = parser.parseResource(org.hl7.fhir.r5.model.TestScript.class,
                    fhirResourceJson);
            return _TestScript;

        case "ValueSet":
            org.hl7.fhir.r5.model.ValueSet _ValueSet = parser.parseResource(org.hl7.fhir.r5.model.ValueSet.class,
                    fhirResourceJson);
            return _ValueSet;

        case "VerificationResult":
            org.hl7.fhir.r5.model.VerificationResult _VerificationResult = parser
                    .parseResource(org.hl7.fhir.r5.model.VerificationResult.class, fhirResourceJson);
            return _VerificationResult;

        case "VisionPrescription":
            org.hl7.fhir.r5.model.VisionPrescription _VisionPrescription = parser
                    .parseResource(org.hl7.fhir.r5.model.VisionPrescription.class, fhirResourceJson);
            return _VisionPrescription;

        default:
            throw new IllegalStateException("Unexpected value: " + resourceType);
        }
    }

    enum Version {
        DSTU3, R4, R5
    }
}
