package com.academysi.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.cj.jdbc.MysqlDataSource;

public class DatabaseManager {

	private Connection conn;

	//Costruttore per connessione
	public DatabaseManager(String serverName, int portNumber, String user, String password) throws SQLException {
		conn = getConnection(serverName, portNumber, user, password);
	}
	
	private Connection getConnection(String serverName, int portNumber, String user, String password) throws SQLException {
		if (conn == null) {
			MysqlDataSource dataSource = new MysqlDataSource();
			dataSource.setServerName(serverName);
			dataSource.setPortNumber(portNumber);
			dataSource.setUser(user);
			dataSource.setPassword(password);
			
			conn = dataSource.getConnection();
		}
		return conn;
	}

	//Creazione DB
	public void createDatabase(String databaseName) throws SQLException {
		
		String sql = "CREATE DATABASE IF NOT EXISTS " + databaseName;
		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
			
			System.out.println("Database " + databaseName + " creato con successo.");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	//Creazione tabelle
	public void createTables(String databaseName, String tableName, String[] columnNames, String[] columnTypes) throws SQLException {
	    PreparedStatement ps = null;

	    try {
	        String useDB = "USE " + databaseName;
	        ps = conn.prepareStatement(useDB);
	        ps.executeUpdate();

	        //Costruisco la query
	        StringBuilder createTableQuery = new StringBuilder();
	        createTableQuery.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (");

	        //Aggiungo le colonne
	        for (int i = 0; i < columnNames.length; i++) {
	            createTableQuery.append(columnNames[i]).append(" ").append(columnTypes[i]);
	            if (i < columnNames.length - 1) {
	                createTableQuery.append(", ");
	            }
	        }
	        createTableQuery.append(")");

	        ps = conn.prepareStatement(createTableQuery.toString());
	        ps.executeUpdate();

	        System.out.println("Tabella " + tableName + " creata con successo nel database: " + databaseName);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        if (ps != null) {
	            try {
	                ps.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}

	//Inserimento utenti
	public void insertUser(int id, String cognome, String nome) throws SQLException {
		
		String sql = "INSERT INTO U (id, Cognome, Nome) VALUES (?, ?, ?)";
		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ps.setString(2, cognome);
			ps.setString(3, nome);
			ps.executeUpdate();
			
			System.out.println("User inserted successfully: " + id + ", " + cognome + ", " + nome);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//Lista per recuperare i libri prestati ad un utente
	public List<String> libriPrestatiUtente(String utenteCognome) throws SQLException {
	    List<String> result = new ArrayList<>();
	    String sql = "SELECT L.Titolo " +
	                 "FROM P " +
	                 "JOIN L ON P.id_L = L.id " +
	                 "JOIN U ON P.id_U = U.id " +
	                 "WHERE U.Cognome = ? " +
	                 "ORDER BY P.Inizio ASC";

	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        ps = conn.prepareStatement(sql);
	        ps.setString(1, utenteCognome);
	        rs = ps.executeQuery();

	        while (rs.next()) {
	            result.add(rs.getString("Titolo"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        if (rs != null) {
	            try {
	                rs.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        if (ps != null) {
	            try {
	                ps.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    return result;
	}
	
	//Lista per recuperare i tre maggiori lettori
	public List<String> primiTreLettori() throws SQLException {
		
	    List<String> result = new ArrayList<>();
	    String sql = "SELECT U.Nome, U.Cognome, COUNT(P.id_U) AS NumLibriLetti " +
	                 "FROM P " +
	                 "JOIN U ON P.id_U = U.id " +
	                 "GROUP BY U.id " +
	                 "ORDER BY NumLibriLetti DESC " +
	                 "LIMIT 3";

	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        ps = conn.prepareStatement(sql);
	        rs = ps.executeQuery(sql);

	        while (rs.next()) {
	            String nomeCognome = rs.getString("Nome") + " " + rs.getString("Cognome");
	            int numLibri = rs.getInt("NumLibriLetti");
	            result.add(nomeCognome + " ha letto " + numLibri + " libri");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        if (rs != null) {
	            try {
	                rs.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        if (ps != null) {
	            try {
	                ps.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    return result;
	}
	
	//Mappa per associare utenti a libri
	public Map<String, String> possessoriLibriNonRientrati() throws SQLException {
		
	    Map<String, String> result = new HashMap<>();
	    String sql = "SELECT U.Nome, U.Cognome, L.Titolo " +
	                 "FROM P " +
	                 "JOIN U ON P.id_U = U.id " +
	                 "JOIN L ON P.id_L = L.id " +
	                 "WHERE P.Fine IS NULL";

	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        ps = conn.prepareStatement(sql);
	        rs = ps.executeQuery(sql);

	        while (rs.next()) {
	            String nomeCognome = rs.getString("Nome") + " " + rs.getString("Cognome");
	            String titoloLibro = rs.getString("Titolo");
	            result.put(nomeCognome, titoloLibro);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        if (rs != null) {
	            try {
	                rs.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        if (ps != null) {
	            try {
	                ps.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    return result;
	}
	
	//Mappa per associare libri a prestiti
	public Map<String, String> storicoPrestitiUtente(int idUtente, Date dataInizio, Date dataFine) throws SQLException {
		
	    Map<String, String> result = new HashMap<>();
	    String sql = "SELECT L.Titolo, P.Inizio, P.Fine " +
	                 "FROM P " +
	                 "JOIN L ON P.id_L = L.id " +
	                 "WHERE P.id_U = ? AND P.Inizio BETWEEN ? AND ?";

	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        ps = conn.prepareStatement(sql);
	        ps.setInt(1, idUtente);
	        ps.setDate(2, new java.sql.Date(dataInizio.getTime()));
	        ps.setDate(3, new java.sql.Date(dataFine.getTime()));
	        rs = ps.executeQuery();

	        while (rs.next()) {
	            String titoloLibro = rs.getString("Titolo");
	            Date inizioPrestito = rs.getDate("Inizio");
	            Date finePrestito = rs.getDate("Fine");
	            result.put(titoloLibro, "Inizio prestito: " + inizioPrestito + ", Fine prestito: " + finePrestito);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        if (rs != null) {
	            try {
	                rs.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        if (ps != null) {
	            try {
	                ps.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    return result;
	}
	
	//Lista per recuperare i libri con numero prestiti
	public List<String> classificaLibriPrestati() throws SQLException {
		
	    List<String> result = new ArrayList<>();
	    String sql = "SELECT L.Titolo, COUNT(P.id_L) AS NumPrestiti " +
	                 "FROM P " +
	                 "JOIN L ON P.id_L = L.id " +
	                 "GROUP BY L.id " +
	                 "ORDER BY NumPrestiti DESC";

	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        ps = conn.prepareStatement(sql);
	        rs = ps.executeQuery(sql);

	        while (rs.next()) {
	            String titoloLibro = rs.getString("Titolo");
	            int numPrestiti = rs.getInt("NumPrestiti");
	            result.add(titoloLibro + " è stato prestato " + numPrestiti + " volte");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        if (rs != null) {
	            try {
	                rs.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        if (ps != null) {
	            try {
	                ps.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    return result;
	}
	
	//Lista per recuperare prestiti > 15gg
	public List<String> prestitiDurataSuperiore15Giorni() throws SQLException {
		
	    List<String> result = new ArrayList<>();
	    String sql = "SELECT U.Nome, U.Cognome, L.Titolo, P.Inizio, P.Fine " +
	                 "FROM P " +
	                 "JOIN U ON P.id_U = U.id " +
	                 "JOIN L ON P.id_L = L.id " +
	                 "WHERE DATEDIFF(P.Fine, P.Inizio) > 15";

	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        ps = conn.prepareStatement(sql);
	        rs = ps.executeQuery(sql);

	        while (rs.next()) {
	            String nomeCognome = rs.getString("Nome") + " " + rs.getString("Cognome");
	            String titoloLibro = rs.getString("Titolo");
	            Date inizioPrestito = rs.getDate("Inizio");
	            Date finePrestito = rs.getDate("Fine");
	            result.add(nomeCognome + " ha preso in prestito " + titoloLibro + " dal " + inizioPrestito + " al " + finePrestito);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        if (rs != null) {
	            try {
	                rs.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        if (ps != null) {
	            try {
	                ps.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    return result;
	}
}