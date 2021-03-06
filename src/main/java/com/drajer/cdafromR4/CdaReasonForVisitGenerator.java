package com.drajer.cdafromR4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.Encounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drajer.cda.utils.CdaGeneratorConstants;
import com.drajer.cda.utils.CdaGeneratorUtils;
import com.drajer.sof.model.LaunchDetails;
import com.drajer.sof.model.R4FhirData;


public class CdaReasonForVisitGenerator {
	
	private static final Logger logger = LoggerFactory.getLogger(CdaReasonForVisitGenerator.class);
	
	public static String generateReasonForVisitSection(R4FhirData data, LaunchDetails details) {
		
		StringBuilder sb = new StringBuilder(2000);
		
		Encounter encounter = data.getEncounter();
		
		// Will have to wait to discuss with vendors on Reason For Visit and how to obtain that information reliably..
		// Then we can generate better text.
		
		sb.append(CdaGeneratorUtils.getXmlForStartElement(CdaGeneratorConstants.COMP_EL_NAME));
        sb.append(CdaGeneratorUtils.getXmlForStartElement(CdaGeneratorConstants.SECTION_EL_NAME));
        
        sb.append(CdaGeneratorUtils.getXmlForTemplateId(CdaGeneratorConstants.REASON_FOR_VISIT_SEC_TEMPLATE_ID));
		
        sb.append(CdaGeneratorUtils.getXmlForCD(CdaGeneratorConstants.CODE_EL_NAME, 
                CdaGeneratorConstants.REASON_FOR_VISIT_SEC_CODE, 
                CdaGeneratorConstants.LOINC_CODESYSTEM_OID,
                CdaGeneratorConstants.LOINC_CODESYSTEM_NAME, 
                CdaGeneratorConstants.REASON_FOR_VISIT_SEC_CODE_NAME));
        
        // Add Title
        sb.append(CdaGeneratorUtils.getXmlForText(CdaGeneratorConstants.TITLE_EL_NAME, 
            CdaGeneratorConstants.REASON_FOR_VISIT_SEC_TITLE));

        // Add Narrative Text
        // Need to Discuss with vendors on how to best get this information. 
       // sb.append(CdaGeneratorUtils.getXmlForText(CdaGeneratorConstants.TEXT_EL_NAME, 
       //     "Reason Not Known"));
        
        sb.append(CdaGeneratorUtils.getXmlForStartElement(CdaGeneratorConstants.TEXT_EL_NAME));

        //Create Table Header.
        List<String> list = new ArrayList<String>();
        list.add(CdaGeneratorConstants.TEXT_EL_NAME);
        sb.append(CdaGeneratorUtils.getXmlForTableHeader(list, 
            CdaGeneratorConstants.TABLE_BORDER, 
            CdaGeneratorConstants.TABLE_WIDTH));

        // Add Table Body
        sb.append(CdaGeneratorUtils.getXmlForStartElement(CdaGeneratorConstants.TABLE_BODY_EL_NAME));

        // Add Body Rows
        int rowNum = 1;
        String text = CdaGeneratorConstants.UNKNOWN_REASON_FOR_VISIT;
        	
        if(encounter != null &&
        		encounter.getReasonCodeFirstRep() != null && 
        		encounter.getReasonCodeFirstRep().getCodingFirstRep() != null && 
        	    !StringUtils.isEmpty(encounter.getReasonCodeFirstRep().getCodingFirstRep().getDisplay())) {
        	
        	text = encounter.getReasonCodeFirstRep().getCodingFirstRep().getDisplay();
        }
        
        
        Map<String, String> bodyvals = new HashMap<String, String>();
        bodyvals.put(CdaGeneratorConstants.TEXT_EL_NAME, text);

        sb.append(CdaGeneratorUtils.AddTableRow(bodyvals, rowNum));

        sb.append(CdaGeneratorUtils.getXmlForEndElement(CdaGeneratorConstants.TABLE_BODY_EL_NAME));

        //End Table.
        sb.append(CdaGeneratorUtils.getXmlForEndElement(CdaGeneratorConstants.TABLE_EL_NAME));
        sb.append(CdaGeneratorUtils.getXmlForEndElement(CdaGeneratorConstants.TEXT_EL_NAME));

        // Complete the section end tags.
        sb.append(CdaGeneratorUtils.getXmlForEndElement(CdaGeneratorConstants.SECTION_EL_NAME));
        sb.append(CdaGeneratorUtils.getXmlForEndElement(CdaGeneratorConstants.COMP_EL_NAME));

		
		return sb.toString();
	}


}
