package com.example.demo;

import com.example.demo.controller.AppointmentController;
import com.example.demo.middleware.ActionService;
import com.example.demo.middleware.ServerToMock;
import com.example.demo.model.Appointment;
import com.example.demo.model.MessageError;
import com.example.demo.repository.AppointmentRepository;
import org.exparity.hamcrest.date.LocalDateTimeMatchers;
import org.hamcrest.text.IsEmptyString;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.exparity.hamcrest.date.LocalDateMatchers;
import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest()
public class DemoApplicationTests {

	@Autowired
	AppointmentController appointmentController;
	@Autowired
	AppointmentRepository appointmentRepository;

	static List<Appointment> titiAppointments = new ArrayList<>();
	static List<Appointment> totoAppointments = new ArrayList<>();

	@BeforeClass
	public static void setup(){
		for(int i=9; i<=18;i++){
			LocalDateTime begin = LocalDateTime.now().withHour(i).withMinute(0).withSecond(0).withNano(0);
			LocalDateTime end = LocalDateTime.now().withHour(i+1).withMinute(0).withSecond(0).withNano(0);
			Appointment a = new Appointment(null,"Dr Toto", begin, end, "Title", "Description");
			totoAppointments.add(a);
		}
		for(int i=9; i<=18;i++){
			LocalDateTime begin = LocalDateTime.now().withHour(i).withMinute(0).withSecond(0).withNano(0);
			LocalDateTime end = LocalDateTime.now().withHour(i+1).withMinute(0).withSecond(0).withNano(0);
			Appointment a = new Appointment(null,"Dr Titi Rodriguez", begin, end, "Title", "Description");
			titiAppointments.add(a);
		}
	}

	@Before
	public void clean() {
		this.appointmentRepository.deleteAll();
	}

	@Test
	public void insertIfNotCollisionOnTheSameDoctor() {
		// GIVEW
		Appointment t1 = this.totoAppointments.get(0);
		Appointment t2 = this.titiAppointments.get(0);

		// WHEN
		ResponseEntity respToto = appointmentController.createAppointment(t1);
		ResponseEntity respTotoError = appointmentController.createAppointment(t1);

		ResponseEntity respTiti = appointmentController.createAppointment(t2);

		Appointment toto = (Appointment) respToto.getBody();
		Appointment titi = (Appointment) respTiti.getBody();

		MessageError totoError = (MessageError) respTotoError.getBody();

		// THEN
		assertThat(toto.getId(), is(not(equals(titi.getId()))));
		assertThat(toto.getDoctor(), is(not(equals(titi.getDoctor()))));
		assertThat(toto.getBegin(), not(LocalDateTimeMatchers.before(titi.getBegin())));
		assertThat(toto.getBegin(), not(LocalDateTimeMatchers.after(titi.getBegin())));
		assertThat(toto.getEnd(), not(LocalDateTimeMatchers.after(titi.getEnd())));
		assertThat(toto.getEnd(), not(LocalDateTimeMatchers.after(titi.getEnd())));

		assertThat(toto.getId(), not(isEmptyOrNullString()));
		assertThat(titi.getId(), not(isEmptyOrNullString()));
		assertThat(totoError.getDescription(), not(isEmptyOrNullString()));
	}

	@Test
	public void insertAndRetrieveAll() {
		// GIVEW
		List<Appointment> results;
		//WHEN
		for(Appointment appointment : totoAppointments){
			this.appointmentController.createAppointment(appointment);
		}
		results = this.appointmentController.getAppointments();

		// THEN
		for(Appointment appointment : totoAppointments){
			assertThat(appointment.getId(), not(isEmptyOrNullString()));
		}

		assertThat(Arrays.asList(results), hasItems(totoAppointments));
		assertThat(Arrays.asList(totoAppointments), hasItems(results));

	}

	@Test
	public void testUsingMockito() {
		ServerToMock serverToMock = Mockito.mock(ServerToMock.class);
		ActionService actionService = new ActionService(serverToMock);
		when(serverToMock.result()).thenReturn(true);
		assertThat(actionService.test(5,5), is(10));

		when(serverToMock.result()).thenReturn(false);
		assertThat(actionService.test(5,5), is(25));

	}

}
