package com.cmpe275.cusr.service;

import com.cmpe275.cusr.model.Cancelledtrains;
import com.cmpe275.cusr.model.Tickets;
import com.cmpe275.cusr.model.Trains;
import com.cmpe275.cusr.repositories.CancelledtrainsRepository;
import com.cmpe275.cusr.repositories.TicketsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import static java.time.temporal.ChronoUnit.MINUTES;

@Service @Transactional
public class TrainCancellationService {

	@Autowired
	CancelledtrainsRepository cancelledtrainsRepository;

	@Autowired
	TicketsRepository ticketsRepository;

	@Autowired SearchService2 searchService2;

	@Autowired
	AvailabilityService availabilityService;

	@Autowired ReservationService reservationService;

	@Autowired EmailService emailService;

	public boolean cancelTrain(String trainName, String date){

		List<Tickets> passengers = null;
		Cancelledtrains ct = new Cancelledtrains(trainName, date);
		Boolean status = false;
		try {
			cancelledtrainsRepository.save(ct);
			status = true;
		}
		catch (Exception e){
			System.out.println(e);
			throw e;
		}

		passengers = getPassengers(ct);
		if (passengers !=null) {
			cancelTkts(passengers);
			boolean replacePossible = checkReplacements(passengers);
			if (replacePossible == true)
				performReplacements(passengers);
			else
				informPassengers(passengers);
			return status;
		}
		else {
			return status;
		}
	}

	public List<Tickets> getPassengers(Cancelledtrains ct){
		List<Tickets> passengers = new ArrayList<>();

		try {
			ticketsRepository.findByTrainAndJdate(ct.getName(), ct.getDate()).forEach(passengers::add);
			return passengers;
		}
		catch (NullPointerException e){
			System.out.println("NullPointer occured");
			return null;
		}

	}

	public void cancelTkts(List<Tickets> tickets){

		List<Long> tktIds = new ArrayList<>();
		for(Tickets t : tickets) {
			tktIds.add(t.getTktid());
		}
		ticketsRepository.deleteByTktidIn(tktIds);
	}

	public boolean checkReplacements(List<Tickets> tickets){

		String startTime = tickets.get(0).getStime();
		char startStation = (char)((int) tickets.get(0).getStart());
		System.out.println("start station = " + startStation);
		char endStation = (char)((int)tickets.get(0).getEnd());
		System.out.println("end station = " + endStation);
		String endTime = tickets.get(0).getEtime();
		String date = tickets.get(0).getJdate();
		Character trainType = tickets.get(0).getTraintype();

		for (Tickets t: tickets)
			{
				int pcount = t.getPass_count();
				// search train options for a ticket
				List<Trains> options = searchService2.searchTrains(startStation, startTime, endStation, trainType, date);
				int counter = 0;

				// check availability in train options, if any ticket is unavailable for re booking return false, else true
				for (Trains option: options){

					int available = availabilityService.checkAvailability(startStation, endStation, option.getName(), date);
					if (pcount <= available){
						counter = 1;
						break;
					}
				}
				if (counter == 1)
					continue;
				else
					return false;
			}
		return true;
	}

	public void performReplacements(List<Tickets> tickets){

		String startTime = tickets.get(0).getStime();
		char startStation = (char)((int) tickets.get(0).getStart());
		System.out.println("start station = " + startStation);
		char endStation = (char)((int)tickets.get(0).getEnd());
		System.out.println("end station = " + endStation);
		String endTime = tickets.get(0).getEtime();
		String date = tickets.get(0).getJdate();
		Character trainType = tickets.get(0).getTraintype();

		for (Tickets t: tickets)
			{

				int pcount = t.getPass_count();
				// search train options for a ticket
				List<Trains> options = searchService2.searchTrains(startStation, startTime, endStation, trainType, date);

				int len = options.size();
				while (len > 0){
					Trains nearestTrain = getNearestTrain(options, startTime, startStation);
					int available = availabilityService.checkAvailability(startStation, endStation, nearestTrain.getName(), date);
					if (pcount >= available){
						// make reservation and send email
						Tickets newtkt = new Tickets(t.getTrain(), t.getJdate(), t.getStart(), t.getEnd(), t.getEmail(), t.getFare(), t.getPass_count(), t.getStime(), t.getEtime(), t.getTraintype());
						Long newtktId = reservationService.bookTicket(newtkt);
						sendRebookingMail(newtkt);
						break;
					}
					else {
						options.remove(nearestTrain);
						len = len - 1;
					}
				}


	}
}

	public Trains getNearestTrain(List<Trains> options, String startTime, Character startStation){

		Trains nearest = null;
		LocalTime st = LocalTime.parse(startTime);
		LocalTime first = LocalTime.parse(searchService2.getstationTime(options.get(0).getName(), startStation));
		Long diff = st.until(first, MINUTES);
		System.out.println("Difference between start station times" + diff);

		for (Trains t: options){
			LocalTime timeI = LocalTime.parse(searchService2.getstationTime(t.getName(), startStation));
			Long diffnew = st.until(timeI, MINUTES);
			if (diffnew < diff)
				{
					diff = diffnew;
					nearest = t;
				}
		}
		return nearest;
	}

	public void informPassengers(List<Tickets> tickets){
		for (Tickets t: tickets)
		{
			sendCancellationMail(t);
		}
	}

	public void sendRebookingMail(Tickets tkt){

		String message = "Hello! Due to the cancellation of your orignal train, you have been rebooked " +
				"to another train. Details of your new ticket are:" + " Train name:" + tkt.getTrain() + "Train time:" + tkt.getStime()+
				"Ticket ID: " + tkt.getTktid();
		try {
			emailService.sendRebookingConfirmation(tkt.getEmail(), message);
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	public void sendCancellationMail(Tickets tkt){
		String message = "This email is to inform you that your ticket: " + tkt.getTktid() + "has been cancelled due to train " +
				"cancellation, please book another ticket";
		try {
			emailService.sendRebookingConfirmation(tkt.getEmail(), message);
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}

	}
}
