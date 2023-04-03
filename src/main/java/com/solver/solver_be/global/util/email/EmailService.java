package com.solver.solver_be.global.util.email;

import com.solver.solver_be.domain.company.entity.Company;
import com.solver.solver_be.domain.company.repository.CompanyRepository;
import com.solver.solver_be.global.exception.exceptionType.UserException;
import com.solver.solver_be.global.response.ResponseCode;
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
    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;
    private final CompanyRepository companyRepository;

    @Value("${spring.mail.username}")
    private String configEmail;

    private MimeMessage createEmailForm(String email, EmailRequestDto emailRequestDto) throws MessagingException {

        Optional<Company> foundCompany = companyRepository.findByCompanyName(emailRequestDto.getCompanyName());
        // 회사 존재 여부
        if (foundCompany.isEmpty()) {
            throw new UserException(ResponseCode.COMPANY_NOT_FOUND);
        }

        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("메세지가 도착 했습니다.");
        message.setFrom(configEmail);
        message.setText("회사 토큰 : " + foundCompany.get().getCompanyToken(), "utf-8", "html");

        redisUtil.setDataExpire(email, "회사 토큰 : " + foundCompany.get().getCompanyToken(), 60 * 30L);

        return message;
    }

    public void sendEmail(String toEmail, EmailRequestDto emailRequestDto) throws MessagingException {
        if (redisUtil.existData(toEmail)) {
            redisUtil.deleteData(toEmail);
        }

        MimeMessage emailForm = createEmailForm(toEmail, emailRequestDto);

        mailSender.send(emailForm);
    }
}