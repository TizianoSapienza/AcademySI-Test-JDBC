package com.academysi.db;

import java.sql.Date;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) {
		try {
			//Istanza DBManager
			DatabaseManager dbManager = new DatabaseManager("localhost", 3306, "root", "admin");

			//Istanza DB
			dbManager.createDatabase("test_jdbc");
			
			//Tabella U
	        String[] columnNamesU = {"id", "Nome", "Cognome"};
	        String[] columnTypesU = {"INT PRIMARY KEY", "VARCHAR(255)", "VARCHAR(255)"};
	        dbManager.createTables("test_jdbc", "U", columnNamesU, columnTypesU);

	        //Tabella L
	        String[] columnNamesL = {"id", "Titolo", "Autore"};
	        String[] columnTypesL = {"INT PRIMARY KEY", "VARCHAR(255)", "VARCHAR(255)"};
	        dbManager.createTables("test_jdbc", "L", columnNamesL, columnTypesL);

	        //Tabella P
	        String[] columnNamesP = {"id", "Inizio", "Fine", "id_U", "id_L"};
	        String[] columnTypesP = {"INT PRIMARY KEY", "DATE", "DATE", "INT", "INT"};
	        dbManager.createTables("test_jdbc", "P", columnNamesP, columnTypesP);
	        
	        System.out.println();

			//Utenti
//			dbManager.insertUser(1, "Rossi", "Mario");
//			dbManager.insertUser(2, "Verdi", "Andrea");
//			dbManager.insertUser(3, "Bianchi", "Massimo");
//			dbManager.insertUser(4, "Vallieri", "Sara");
//			dbManager.insertUser(5, "Graviglia", "Marco");
//			dbManager.insertUser(6, "Esposito", "Marzia");
			
			
			//Query1
			System.out.println("Libri prestati a Vallieri");
			System.out.println(dbManager.libriPrestatiUtente("Vallieri"));
			System.out.println();
			
			//Query2
			System.out.println("Primi tre lettori:");
			System.out.println(dbManager.primiTreLettori());
			System.out.println();
			
			//Query3
			System.out.println("Possessori di libri non ritornati:");
			System.out.println(dbManager.possessoriLibriNonRientrati());
			System.out.println();
			
			//Query4
			Date dataInizio = Date.valueOf("2004-01-01");
	        Date dataFine = Date.valueOf("2006-06-20");
	        System.out.println("Prestiti dell'utente selezionato nel periodo specificato:");
			System.out.println(dbManager.storicoPrestitiUtente(6, dataInizio, dataFine));
			System.out.println();
			
			//Query5
			System.out.println("Classifica dei libri prestati:");
			System.out.println(dbManager.classificaLibriPrestati());
			System.out.println();
			
			//Query6
			System.out.println("Prestiti con durata maggiore di 15 gg:");
			System.out.println(dbManager.prestitiDurataSuperiore15Giorni());
			System.out.println();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
