package com.hcl.mortgage.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.hcl.mortgage.dto.CustomerRequestDto;
import com.hcl.mortgage.entity.Account;
import com.hcl.mortgage.entity.Customer;
import com.hcl.mortgage.repository.AccountRepository;
import com.hcl.mortgage.repository.CustomerRepository;
import com.hcl.mortgage.util.Email;
import com.hcl.mortgage.util.Sms;

@Service
public class CustomerServiceImpl implements CustomerService{

	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	JavaMailSender javaMailSender;
	Sms sms=new Sms();
	Email email=new Email();
	@Override
	public String registerCustomer(CustomerRequestDto customerRequestDto) {
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		Customer customer=new Customer();
		BeanUtils.copyProperties(customerRequestDto, customer);
		DateTimeFormatter formatter=DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate date=LocalDate.parse(customerRequestDto.getDob(), formatter);
		Period period=Period.between(date, LocalDate.now());
		if(period.getYears()<18) {
			
		}
		if(!customerRequestDto.getEmailId().matches(regex)) {
			
		}
		if(Long.toString(customerRequestDto.getMobileNumber()).length()<10) {
			
		}
		
		String randomPassword = RandomStringUtils.randomAlphanumeric(10);	
		Account account=new Account();
		long accountNumber = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
		customer.setPassword(randomPassword);
		customer.setDateOfBirth(date);
		customerRepository.save(customer);
		account.setAccountNumber(accountNumber);
		account.setAccountBalance(100000.0);
		account.setCustomerId(customer.getCustomerId());
		accountRepository.save(account);
		sms.sendSms(customerRequestDto.getMobileNumber(),accountNumber,randomPassword,"Salary");
		email.sendEmail(customerRequestDto.getEmailId(),accountNumber,randomPassword,javaMailSender,"Salary");
		return null;
	}

}
