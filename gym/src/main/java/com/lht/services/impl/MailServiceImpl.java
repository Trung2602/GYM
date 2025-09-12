/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.Account;
import com.lht.pojo.Customer;
import com.lht.reponsitories.AccountRepository;
import com.lht.reponsitories.CustomerRepository;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 *
 * @author admin
 */
public class MailServiceImpl {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private CustomerRepository customerRepository;

    public void sendWelcome(String mail) {
        Optional<Account> optionalAcc = accountRepository.findByMail(mail);

        if (optionalAcc.isPresent()) {
            Account acc = optionalAcc.get();

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(mail);
            message.setSubject("Chào mừng đến với GYM");

            String today = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new Date());

            message.setText("Xin chào " + acc.getName() + ",\n\n"
                    + "Bạn đã tham gia vào ngày " + today + ".\n"
                    + "Cảm ơn bạn đã tham gia!");

            mailSender.send(message);
        } else {
            System.out.println("Không tìm thấy tài khoản với email: " + mail);
        }
    }

    public void sendOverdue(String mail) {
        Optional<Customer> optionalAcc = customerRepository.findByMail(mail);
        if (optionalAcc.isPresent()) {
            Customer customer = optionalAcc.get();

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(mail);
            message.setSubject("Thông báo gia hạn thành viên");

            message.setText("Xin chào " + customer.getName() + ",\n\n"
                    + "Bạn sẽ hết hạn thành viên vào ngày " + customer.getExpiryDate() + ".\n"
                    + "Bạn nên thanh toán gói tập mới trước ngày hết hạn!");

            mailSender.send(message);
        } else {
            System.out.println("Không tìm thấy tài khoản với email: " + mail);
        }
    }

}

//@Service
//public class OverdueReminderScheduler {
//
//    @Autowired
//    private BorrowRepository borrowRepository;
//
//    @Autowired
//    private EmailService emailService;
//
//    // Chạy lúc 8h sáng mỗi ngày
//    @Scheduled(cron = "0 0 8 * * ?")
//    public void sendOverdueEmails() {
//        LocalDate today = LocalDate.now();
//
//        List<Borrow> overdueBorrows = borrowRepository.findByDueDateBeforeAndReturnDateIsNull(today);
//
//        for (Borrow b : overdueBorrows) {
//            String email = "duw3210@gmail.com";
//            String username = b.getUser().getFirstName();
//            String bookTitle = b.getPrintBook().getBook().getTitle();
//            LocalDate dueDate = b.getDueDate();
//
//            emailService.sendOverdueReminder(email, username, bookTitle, dueDate);
//        }
//            String email = "2251012038duc@ou.edu.vn";
//            String username = "Duc";
//            String bookTitle = "Đắc nhân tâm";
//            LocalDate dueDate = today;
//            emailService.sendOverdueReminder(email, username, bookTitle, dueDate);
//    }
// Gọi ngay sau khi app khởi động
//    @PostConstruct
//    public void runAfterStartup() {
//        sendOverdueEmails();
//    }
//}
