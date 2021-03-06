package com.drajer.routing.impl;

import com.drajer.eca.model.PatientExecutionState;
import com.drajer.eca.model.SubmitEicrAction;
import com.drajer.routing.EicrSender;
import com.drajer.sof.model.LaunchDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DirectEicrSender extends EicrSender {
	
	private final Logger logger = LoggerFactory.getLogger(DirectEicrSender.class);
	
	private static final String FILE_NAME = "eICR Report";

	@Override
	public void sendData(Object context, String data) {
		
		logger.info(" **** START Executing Direct SEND **** ");
			
		if (context instanceof LaunchDetails) {

			logger.info(" Obtained Launch Details ");
			LaunchDetails details = (LaunchDetails) context;
			ObjectMapper mapper = new ObjectMapper();
			PatientExecutionState state = null;

			try {
				state = mapper.readValue(details.getStatus(), PatientExecutionState.class);			
			} catch (JsonMappingException e1) {
				
				String msg = "Unable to read/write execution state";
				logger.error(msg);
				e1.printStackTrace();
				throw new RuntimeException(msg);
				
			} catch (JsonProcessingException e1) {
				
				String msg = "Unable to read/write execution state";
				logger.error(msg);
				e1.printStackTrace();
				throw new RuntimeException(msg);
			}
		
			InputStream is = IOUtils.toInputStream(data, StandardCharsets.UTF_8);
			
			try {
				
				logger.info(" Sending Mail from " + details.getDirectUser() + " to : " + details.getDirectRecipient());
				sendMail(details.getDirectHost(), details.getDirectUser(), details.getDirectPwd(), details.getDirectRecipient(), is, DirectEicrSender.FILE_NAME);
				
			} catch (Exception e) {
				
				String msg = "Unable to send Direct Message";
				logger.error(msg);
				
				e.printStackTrace();

				throw new RuntimeException(msg);
			}
		}
		
		
	}
		
	public void sendMail(String host, String username, String password, String receipientAddr, InputStream is, String filename) throws Exception{
			
		Properties props = new Properties();
			
			
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.auth.mechanisms", "PLAIN");
			props.setProperty("mail.smtp.ssl.trust", "*");

			Session session = Session.getInstance(props, null);
			
			logger.info(" Retrieve Session instance for sending Direct mail ");

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(receipientAddr));
			message.setSubject("eICR Report ");
			message.setText("eICR Report");
			BodyPart messageBodyPart = new MimeBodyPart();
			Multipart multipart = new MimeMultipart();
			DataSource source = new ByteArrayDataSource(is,"application/xml; charset=UTF-8");
			messageBodyPart.setDataHandler(new DataHandler(source));

			messageBodyPart.setFileName(filename + "_eICRReport.xml");
			

			multipart.addBodyPart(messageBodyPart);
			
			// Send the complete message parts
			message.setContent(multipart);
			
			logger.info(" Completed constructing the Message ");
			Transport transport = session.getTransport("smtp");
			transport.connect(host, 25, username, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			
			logger.info(" Finished sending Direct Message ");
		}
		
}
