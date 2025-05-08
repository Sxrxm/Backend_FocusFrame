package com.example.service;

import com.example.model.Paciente;
import com.example.model.User;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private User user;

    @Transactional
    public void enviarCorreoConEnlace(String email, String enlace, User user, Paciente paciente) {
        if (email == null || email.isEmpty()) {
            System.err.println("Error: El correo electrónico es nulo o vacío.");
            return;
        }

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Completa tu registro");

            String body = "<html><body>" +
                    "Hola, <span style=\"color: #5603ad;\">" + paciente.getNombre() + " " + paciente.getApellido() + "</span><br><br>" +
                    "Para completar tu perfil, haz clic en el siguiente enlace:<br>" +
                    "<a href=\"" + enlace + "\" style=\"color: #5603ad;\">" + enlace + "</a><br><br>" +
                    "Este enlace te llevará a una página donde podrás crear tu nombre de usuario y establecer una contraseña.<br><br>" +
                    "Bienvenido a <h2 style=\"color: #5603ad;\"> FocusFrame </h2> <br>" +
                    "</body></html>";

            helper.setText(body, true);

            mailSender.send(message);
            System.out.println("Correo enviado a: " + email);

        } catch (MailException | jakarta.mail.MessagingException e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
