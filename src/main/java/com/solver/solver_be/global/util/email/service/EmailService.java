package com.solver.solver_be.global.util.email.service;

import com.solver.solver_be.domain.company.entity.Company;
import com.solver.solver_be.domain.company.repository.CompanyRepository;
import com.solver.solver_be.domain.user.service.InfoProvider;
import com.solver.solver_be.global.exception.exceptionType.CompanyException;
import com.solver.solver_be.global.type.ErrorType;
import com.solver.solver_be.global.util.email.dto.EmailRequestDto;
import com.solver.solver_be.global.util.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final RedisUtil redisUtil;
    private final InfoProvider infoProvider;
    private final JavaMailSender mailSender;
    private final CompanyRepository companyRepository;

    @Value("${spring.mail.username}")
    private String configEmail;

    // 1. Company Token Mail Form
    private MimeMessage createEmailForm(EmailRequestDto emailRequestDto) throws MessagingException {

        Optional<Company> foundCompany = companyRepository.findByCompanyName(emailRequestDto.getCompanyName());
        // Company Existence
        if (foundCompany.isEmpty()) {
            throw new CompanyException(ErrorType.COMPANY_NOT_FOUND);
        }

        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, emailRequestDto.getEmail());
        message.setSubject("메세지가 도착 했습니다.");
        message.setFrom(configEmail);
        message.setText("회사 토큰 : " + foundCompany.get().getCompanyToken(), "utf-8", "html");

        redisUtil.setDataExpire(emailRequestDto.getEmail(), "회사 토큰 : " + foundCompany.get().getCompanyToken(), 60 * 30L);

        return message;
    }

    // 2. Send corporate token mail
    public void sendEmail(EmailRequestDto emailRequestDto) throws MessagingException {

        // Clear existing data
        if (redisUtil.existData(emailRequestDto.getEmail())) {
            redisUtil.deleteData(emailRequestDto.getEmail());
        }

        MimeMessage emailForm = createEmailForm(emailRequestDto);

        mailSender.send(emailForm);
    }

    // 3. Create Authentication Code Mail Form
    private MimeMessage createAuthCode(EmailRequestDto emailRequestDto) throws MessagingException {

        String authCode = infoProvider.createdCode();

        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, emailRequestDto.getEmail());
        message.setSubject("안녕하세요 인증번호입니다.");
        message.setFrom(configEmail);
        message.setText("인증 코드: " + authCode, "utf-8", "html");

        redisUtil.setDataExpire(emailRequestDto.getEmail(), authCode, 60 * 30L);

        return message;
    }

    // 4. Sending authentication code mail to email
    public void sendAuthCode(EmailRequestDto emailRequestDto) throws MessagingException {

        // Clear existing data
        if (redisUtil.existData(emailRequestDto.getEmail())) {
            redisUtil.deleteData(emailRequestDto.getEmail());
        }

        MimeMessage emailForm = createAuthCode(emailRequestDto);

        mailSender.send(emailForm);
    }

    // 5. Code Verification
    public Boolean verifyEmailCode(String email, String code) {

        String codeFoundByEmail = redisUtil.getData(email);
        if (codeFoundByEmail == null) {
            return false;
        }
        return codeFoundByEmail.equals(code);
    }

    // 6. Get the ID you found by mail
    public void sendUserSearchEmail(String toEmail, String userId) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, toEmail);
        message.setSubject("아이디 찾기 성공");
        message.setFrom(configEmail);
        message.setText("아이디: " + userId, "utf-8", "html");

        mailSender.send(message);
    }

    // 7. Receive new password by mail
    public void sendPasswordResetEmail(String toEmail, String newPassword) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, toEmail);
        message.setSubject("비밀번호 재설정 요청");
        message.setFrom(configEmail);
        message.setText("새로운 비밀번호: " + newPassword, "utf-8", "html");

        mailSender.send(message);
    }

    // 8. Create lobby ID and send mail
    public void sendLobbyId(String toEmail, String userId, String password, String companyName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, toEmail);
        message.setSubject(companyName + " 로비 아이디가 도착했습니다.");
        message.setFrom(configEmail);
        message.setText("로비 아이디 : " + userId + " 로비 비밀번호 : " + password, "utf-8", "html");

        mailSender.send(message);
    }

}