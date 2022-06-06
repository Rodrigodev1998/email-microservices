package com.ms.email.services;

import com.ms.email.enums.StatusEmail;
import com.ms.email.models.EmailModel;
import com.ms.email.repositories.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {

    @Autowired
    EmailRepository emailRepository;

    private JavaMailSender emailSender;
    public EmailModel sendEmail(EmailModel emailModel) {
        //setar a data de envio
        emailModel.setSendDateEmail(LocalDateTime.now());
        //CASO OCORRAR TUDO CERTO, SERÁ SALVO COM STATUS SENT(ENVIADO)
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailModel.getEmailTo());
            message.setTo(emailModel.getEmailTo());
            message.setSubject(emailModel.getSubject());
            message.setText(emailModel.getText());
            emailSender.send(message);

            emailModel.setStatusEmail(StatusEmail.SENT);
        }catch (MailException e){
            //CASO CONTRARIO SERÁ SALVO COMO STATUS ERROR
            emailModel.setStatusEmail(StatusEmail.ERROR);
        }finally {
            //FINALIZO COM O STATUS DEFINIDO
            return emailRepository.save(emailModel);
        }
    }
}
