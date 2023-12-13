package com.xantrix.webapp.entities;

import java.io.Serializable;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.validation.constraints.Min;

//import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "dettlistini")
@Getter
@Setter
public class DettListini implements Serializable
{
	private static final long serialVersionUID = 8777751177774522519L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID")
	private Integer id;
	
	@Size(min = 5, max = 20, message = "{Size.DettListini.codArt.Validation}")
	@NotNull(message = "{NotNull.DettListini.codArt.Validation}")
	@Column(name = "CODART")
	private String codArt;
	
	@Min(value = (long) 0.01, message = "{Min.DettListini.prezzo.Validation}")
	@Column(name = "PREZZO")
	private Double prezzo; 
	
	@ManyToOne
	//@EqualsAndHashCode.Exclude
	@JoinColumn(name = "IDLIST", referencedColumnName = "id")
	@JsonBackReference
	private Listini listino;
	
	public DettListini() {}
	
	public DettListini(String CodArt, Double Prezzo, Listini Listino)
	{
		this.codArt = CodArt;
		this.prezzo = Prezzo;
		this.listino = Listino;
	}
}
