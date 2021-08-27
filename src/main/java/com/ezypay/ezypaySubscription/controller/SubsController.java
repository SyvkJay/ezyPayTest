package com.ezypay.ezypaySubscription.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezypay.ezypaySubscription.entity.SubsRequest;
import com.ezypay.ezypaySubscription.entity.SubsResponse;

@RestController
@RequestMapping("/ezypaysubscription")
public class SubsController {
	
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	DateTimeFormatter dtformatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	@PostMapping("/add")
    public ResponseEntity<SubsResponse> addSubscription(@RequestBody SubsRequest subsRequest) {
    	
    	System.out.println("test ezypay subscription");
    	
    	String message = "OK";
    	
    	SubsResponse response = new SubsResponse();
    	
    	String errorMessage = checkParam(subsRequest);
    	
    	if(!errorMessage.isBlank())
    	{
    		response.setMessage(errorMessage);
    		return  new ResponseEntity<SubsResponse>(response, HttpStatus.PRECONDITION_FAILED);
    	}
    	
    	response.setMessage(message);
    	response.setAmount(subsRequest.getAmount());
    	response.setSubscriptionType(subsRequest.getSubscriptionType());
    	
    	try {
			response.setInvoiceDateList(populateInvoice(subsRequest.getSubscriptionType(), 
					formatter.parse(subsRequest.getStartDate()), formatter.parse(subsRequest.getEndDate())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //return productRepository.save(product);
    	return  new ResponseEntity<SubsResponse>(response, HttpStatus.OK);
    }
	
	
	
	List<String> populateInvoice(String subscriptionType, Date startDate, Date endDate)
	{
		List<String> invoiceList = new ArrayList<String>();
		
		int addNumOfDays = 1; //if daily
		
		if(subscriptionType.equalsIgnoreCase("weekly"))
		{
			addNumOfDays = 7;
		}
		else if(subscriptionType.equalsIgnoreCase("monthly"))
		{
			addNumOfDays = 30;
		}
		
		LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		for (LocalDate date = start; date.isBefore(end.plusDays(1)); date = date.plusDays(0)) {			
		    System.out.println(date);		    
		    invoiceList.add(date.format(dtformatter));
		    date = date.plusDays(addNumOfDays);
		}
		
		return invoiceList;
	}
	
	String checkParam(SubsRequest subsRequest)
	{
		String message = "message is null";
		boolean checkEmptyPassed = false;
		
		//check if request is null
		if(subsRequest != null)
		{
			message = ""; //means all have value
			
			if(ObjectUtils.isEmpty(subsRequest.getAmount()))
			{
				message = "amount is empty";
			}
			else if(ObjectUtils.isEmpty(subsRequest.getSubscriptionType()))
			{
				message = "subscription type is empty";
			}
			else if(ObjectUtils.isEmpty(subsRequest.getDayWeekMonth()))
			{
				message = "day/month is empty";
			}
			else if(ObjectUtils.isEmpty(subsRequest.getStartDate()))
			{
				message = "start date is empty";
			}
			else if(ObjectUtils.isEmpty(subsRequest.getEndDate()))
			{
				message = "end date is empty";
			}
		}
		
		//if all request has value, check request format
		if(message.isBlank())
		{
			//check amount type format
			try
			{
				Integer.parseInt(subsRequest.getAmount());
			}
			catch (NumberFormatException nfe) {
		        return "wrong format for amount";
		    }
			
			//check subscription type format
			if(!(subsRequest.getSubscriptionType().equalsIgnoreCase("daily") || 
					subsRequest.getSubscriptionType().equalsIgnoreCase("weekly") ||
					subsRequest.getSubscriptionType().equalsIgnoreCase("monthly")))
			{
				return "invalid value for subscription type";
			}
			
			Date startDate = null;
			Date endDate = null;
			
			//check start/end date format
			try {
				startDate = formatter.parse(subsRequest.getStartDate());
				endDate = formatter.parse(subsRequest.getEndDate());
			} catch (ParseException e) {
				return "invalid start/end date format";
			}
			
			//get day for start date
			if(subsRequest.getSubscriptionType().equalsIgnoreCase("daily") || 
					subsRequest.getSubscriptionType().equalsIgnoreCase("weekly"))
			{
				String dayOfStartDate = getDayStringNew(startDate, new Locale("en","US"));
				
				if(!dayOfStartDate.equalsIgnoreCase(subsRequest.getDayWeekMonth()))
					return "start date not match with entered day of week";
			}
			else
			{
				if(!subsRequest.getDayWeekMonth().equalsIgnoreCase(subsRequest.getStartDate().substring(0, 2)))
					return "start date not match with entered day of month";
			}
			
			//make sure not exceed 3 months (90 days)
			long difference = endDate.getTime() - startDate.getTime();
		    float daysBetween = (difference / (1000*60*60*24));
			
		    if(daysBetween >= 90)
		    	return "start/end date exceeded maximum duration of 3 months";
		}
		
		return message;
	}
	
	public static int getDayNumberNew(LocalDate date) {
	    DayOfWeek day = date.getDayOfWeek();
	    return day.getValue();
	}
	
	public static String getDayStringNew(Date date, Locale locale) {
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	    DayOfWeek day = localDate.getDayOfWeek();
	    return day.getDisplayName(TextStyle.FULL, locale);
	}
	
}
