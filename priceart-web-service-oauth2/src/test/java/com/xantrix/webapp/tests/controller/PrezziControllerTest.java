package com.xantrix.webapp.tests.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet; 
import java.util.Set;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.xantrix.webapp.Application;
import com.xantrix.webapp.entities.DettListini;
import com.xantrix.webapp.entities.Listini;
import com.xantrix.webapp.repository.ListinoRepository;

@TestPropertySource(locations="classpath:application-list100.properties")
@ContextConfiguration(classes = Application.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class PrezziControllerTest 
{
	 	private MockMvc mockMvc;

	    @Autowired
		private WebApplicationContext wac;
	    
	    @Autowired
	    private ListinoRepository listinoRepository;
	    
	    String IdList = "100";
	    String IdList2 = "101";
	    String CodArt = "002000301";
	    Double Prezzo = 1.00;
	    Double Prezzo2 = 2.00;

	    @BeforeEach
		public void setup()
		{
			this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
			
			//Inserimento Dati Listino 100
			InsertDatiListino(IdList,"Listino Test 100",CodArt,Prezzo);
			
			//Inserimento Dati Listino 101
			InsertDatiListino(IdList2,"Listino Test 101",CodArt,Prezzo2);

	    }
		
		private void InsertDatiListino(String IdList, String Descrizione, String CodArt, Double Prezzo)
		{
			
			Listini listinoTest = new Listini(IdList,Descrizione,"No");
	    	
	    	Set<DettListini> dettListini = new HashSet<>();
	    	DettListini dettListTest = new DettListini(CodArt,Prezzo, listinoTest);
	    	dettListini.add(dettListTest);
	    	
	    	listinoTest.setDettListini(dettListini);
	    	
	    	listinoRepository.save(listinoTest);
		}
		
		@Test
		@Order(1)
		public void testGetPrzCodArt() throws Exception
		{
			mockMvc.perform(MockMvcRequestBuilders.get("/api/prezzi/" + CodArt)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$").value("1.0")) 
				.andReturn();
		}
		
		@Test
		@Order(2)
		public void testGetPrzCodArt2() throws Exception
		{
			String Url = String.format("/api/prezzi/%s/%s",CodArt,IdList2);
					
			mockMvc.perform(MockMvcRequestBuilders.get(Url)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$").value("2.0")) 
				.andReturn();
		}
		
		@Test
		@Order(3)
		public void testDelPrezzo() throws Exception
		{
			String Url = String.format("/api/prezzi/elimina/%s/%s/",CodArt,IdList);
			
			mockMvc.perform(MockMvcRequestBuilders.delete(Url)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.code").value("200 OK"))
					.andExpect(jsonPath("$.message").value("Eliminazione Prezzo Eseguita Con Successo"))
					.andDo(print());
		}
		
		 
		@AfterEach
		public void ClearData()
		{
			Optional<Listini> listinoTest = listinoRepository.findById(IdList);
	    	listinoRepository.delete(listinoTest.get());
	    	
	    	listinoTest = listinoRepository.findById(IdList2);
	    	listinoRepository.delete(listinoTest.get());
		}
		
		
		

}
