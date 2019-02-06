package addRecord;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Database {
	
	private final String user = /*"root" *//*"root"*/"passagesys";
    private final String url = "jdbc:mysql://localhost:3306/passage_system?useUnicode=true&characterEncoding=UTF-8";
    private final String password =/*"serverps"*//*"valdistroer"*/ "AstZhq4";

    private Connection connection;
    private Statement statement;
    private SQLException ex = new SQLException();
    private ResultSet resultSet;
    
    public Database() {
    	 try {
         	// System.out.println("class foundStart");
         	 Class.forName("com.mysql.jdbc.Driver");
             // System.out.println("class foundFin");
          } catch (Exception e) {
         	 	//System.out.println("class problem1");
         	 	//System.out.println(e);
         	 	//System.out.println("class problem2");
         	 	e.toString();
         	 	System.out.println(e.toString());
         	 	//System.out.println("error connection");
          }
     }
    
    private void sendQuery(String query) {
        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
        		ex = e;
        } finally {
            try {
            	System.out.println(ex.toString()); 
                connection.close();
                statement.close();
            } catch (SQLException e) {

            }
        }
    }
    
    private void closeDB() {
        try {
            connection.close();
            statement.close();
            resultSet.close();
        } catch (SQLException e) {

        }
    }  
    
    
    public String addRecordTime(String  id ) {
    	isRegistredWorker(id);
    	//birhday(id);
    	SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String curDate = dateFormat.format(new Date());
        
       // String status =  statusWorker(curDate, id);
        
        //день рождения
    	//System.out.println(isFirstCom(id));
    	/*if (birhday(id) == 1 && isFirstCom(id) == 0) {
    		System.out.println("birthday");
    	}*/
        
    	String giveIdWork = " SELECT * FROM workerfio WHERE workerIdCard =" + "\"" + id + 
    			"\"" + "AND status =" + "\"" + "Работает" + "\"";
    	String idWorker = new String();
    	//System.out.println(giveIdWork);
	    	try{
		        connection = DriverManager.getConnection(url, user, password);
		        statement = connection.createStatement();
		        resultSet = statement.executeQuery(giveIdWork);;
		        while (resultSet.next()) {
		        	idWorker = resultSet.getString("personellNumber");
		        }
	
	    	}catch (SQLException e) {
	
	        } finally {
	            closeDB();
	        }
    	
	    	 String statusCom =  statusWorker(curDate, idWorker);
	    	 
    		String query = "INSERT INTO worker VALUES(" + 
    				"\"" +  id + "\""  + "," +
    				"\"" + curDate.toString( ) + "\"" +  "," +
    				"\"" + idWorker  + "\""  + "," +
    				"\"" + statusCom + "\"" + ");" ;
    	//System.out.println(query);	
    	sendQuery(query);
    //Сообщение работнику
    	//System.out.println(isThereMess(id, statusCom) );
    	/*if (isThereMess(id, statusCom) == null) {
    		System.out.println("goood");
    	}*/
    	String soundAnswer = new String();
    	String message = isThereMess(id, statusCom);
    	/*if (message.isEmpty()) {
    		System.out.println("goood");
    	}*/
    	   	
    	if (birhday(idWorker) == 1 && isFirstCom(idWorker) == 1) {
    		//System.out.println("birthday");
        /*	if (message.isEmpty() == false) {
        		soundAnswer = "birthday,"+ message ;
        	}else {*/
        		soundAnswer = "birthday,";
        	//}
    	}
    /*	if (isFirstCom(idWorker) == 1) {
    		soundAnswer += "newyear,";
    	}
    	*/
    	if (message.isEmpty() == false) {
    		soundAnswer +=  message ;
    	}
    	if (Integer.parseInt(statusCom) == 101 ) {
    		soundAnswer +=  "late,";
    	}
    	if (Integer.parseInt(statusCom) < 105 ) {
    		soundAnswer +=  "entry,";
    	}else {
    		soundAnswer +=  "exit,";
    	}
    	return soundAnswer;
    	//return  isThereMess(idWorker, status) + status;
    	
    }
    
    public String getSoundName(String id) {
    	String answ = null;
    	int kol = 0;
    	String query = "SELECT filename FROM sound INNER JOIN workerfio ON "
    			+ "  sound.personellNumber = workerfio.personellNumber WHERE workerIdCard = '" + id + "';";
    	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(query);
	        
	        while (resultSet.next()) {
	        answ = resultSet.getString("filename");
	        kol++;
	        }

    	}catch (SQLException e) {

        } finally {
            closeDB();
        }
    	if (kol == 0) {
    		return answ+= "unknoun";
    	}

    	return answ ;
    	
    }
    
    private String statusWorker (String curDate, String idWorker) {
       // String status ;
    	//System.out.println("привет ");
        if(isLate(curDate, idWorker) == 0){
        	return	 "101";
    		
    	}
        if(isLate(curDate, idWorker) == 1){
        	return	   "102";
    	}
        if(isLate(curDate, idWorker) == 2 && direction(idWorker) == 100 || direction(idWorker) == 101 || direction(idWorker) == 102){
        	return	   "200";
    	}
        else {
       	return   "100";
    	}
    }

    private int isLate (String date, String idWorker) {
    	String answ = null;
    	SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        String curDate = dateFormat.format(new Date());
        
        String queryTypeWorkTime = "SELECT * FROM workerfio WHERE personellNumber ="+ "\"" + idWorker + 
    			"\"" + "AND status ="+ "\"" + "Работает" + "\"" ;
        //System.out.println(queryTypeWorkTime);
        String typeWorkTime = new String();
        try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(queryTypeWorkTime);;
	        while (resultSet.next()) {
	        	typeWorkTime = resultSet.getString("typeWorkingHour");
	        }
	       // System.out.println(typeWorkTime);

    	}catch (SQLException e) {

        } finally {
            closeDB();
        }
        
    	String query = "SELECT startWorkTime FROM workingHours WHERE id = "+ "\"" + typeWorkTime + 
    			"\""+ ";";
    	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(query);
	        while (resultSet.next()) {
	        	answ = resultSet.getString("startWorkTime");
	        }
	        answ = curDate + " " + answ;
	       // System.out.println(date.compareTo(answ));
	       // System.out.println(answ);
	        //System.out.println(isFirstCom(id));
	        if (date.compareTo(answ) > 0 && isFirstCom(idWorker) == 0 ) { //опоздание 
	        	return 0;
	        }
	        if (date.compareTo(answ) < 0 && isFirstCom(idWorker) == 0 ) { //первый вход не опоздание
	        	return 1;
	        }
	        /*if ( isFirstCom(id) == 1 ) {
	        return 2;	
	        }*/
	        
	      
	        
	  
	        	
    	}catch (SQLException e) {

        } finally {
            closeDB();
        }
    	
    
    	
   // return  1;
    	  return 2;
    }
    
    private int isFirstCom (String idWorker) {
    	int good = 0;
    	
    	
    	SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        String curDate = dateFormat.format(new Date());
        curDate += " 23:59:59";
        String curDates = dateFormat.format(new Date());
        
       
      /*  String birthday = "1969-02-05";
        SimpleDateFormat birDateFormat = new SimpleDateFormat("YYYY");
        String curBirDate = birDateFormat.format(new Date());
        birthday = curBirDate +  birthday.substring(4, 10);
        
        if (curDates.equals(birthday)) {
        	System.out.println( birthday );
        }*/
        
    	String query = "SELECT * FROM worker WHERE workerId ='" + idWorker +
    			"' AND eventTime >= '"+ curDates + "' AND eventTime <= '"+ curDate +"' ;";
    	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(query);
	        while (resultSet.next()) {
	        	good++;
	        }
	        //System.out.println(curDate);
	        //System.out.println(good+"  good");
	        
	        if (good == 0) {
	        	return 0;
	        }
	  
	        	
    	}catch (SQLException e) {

        } finally {
            closeDB();
        }
    	
    
    	
    return  good;
    	
    }
    
    private int birhday(String idWorker) {
    	
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY");
        String curDates = dateFormat.format(new Date());
    	
    	String queryBirthday = "SELECT * FROM workerfio WHERE personellNumber =" + "\"" +  idWorker + "\""  + " ;";
    	//System.out.println(queryBirthday);
    	String birthday = new String();
//СВЕЖЕЕ ДОБАВЛЕНИЕ НЕ ПРОВЕРЕНО
    	if(birthday.equals("null")) {
    		return 0;
    	}else
    	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(queryBirthday);
	        while (resultSet.next()) {
	        	birthday = resultSet.getString("birthday");
	        	
	        }

	        	
    	}catch (SQLException e) {

        } finally {
            closeDB();
        }
    	if(birthday.equals("null")) {
    		return 0;
    	}
    	
    	SimpleDateFormat birDateFormat = new SimpleDateFormat("YYYY");
        String curBirDate = birDateFormat.format(new Date());
        //System.out.println(birthday + " new");
        birthday =  birthday.substring(0, 6) + curBirDate ;
        //System.out.println(birthday + " new");
        //System.out.println(curDates);
        if (curDates.equals(birthday)) {
        	//System.out.println("birthday yes");
        	return 1;
        }
        
       return 0;
    }
    
    private int direction(String idWorker) {
     	String answ = null;

        
    	String query = "SELECT statuscom FROM worker WHERE workerId ='" + idWorker + "' ;";
    	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(query);
	        while (resultSet.next()) {
	        	answ = resultSet.getString("statuscom");
	        }

	        	
    	}catch (SQLException e) {

        } finally {
            closeDB();
        }    	
    
    	
    return  Integer.parseInt(answ);
    }
    
    private void isRegistredWorker (String id) {
    	String query = "SELECT * FROM workerfio WHERE workerIdCard ='" + id + "' ;";
    	int isRegistered = 0;
    	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(query);
	        while (resultSet.next()) {
	        	isRegistered++;
	        }

	//System.out.println(isRegistered);       	
    	}catch (SQLException e) {

        } finally {
            closeDB();
        }  
    	
    	if (isRegistered == 0) {
    		
        	String queryWorkFIO = "INSERT INTO workerfio ( workerIdCard, personellNumber," + 
        			"surname, name, patronymic, birthday, status, typeWorkingHour, department, position)  VALUES("  + 
        			"\""+  id + "\""  + "," +
    				"\"" + id + "\"" +  "," + 
        			"\"" + "Unknown" + "\"" +  "," +
    				"\"" + "Visitor" + "\"" +  "," + 
        			"\"" + id + "\"" + "," +
    				"\"" + "null" + "\"" +  "," + 
        			"\"" + "Работает" + "\"" + "," +
    				"\"" + "1" + "\"" + "," + 
    				"\"" + "10" + "\"" + "," +
    				"\"" + "null" + "\"" +");" ;
        		
        	//System.out.println(queryWorkFIO);
        		sendQuery(queryWorkFIO);
        		
        		int isThere = 0;
        		String giveSound = " SELECT filename FROM sound WHERE personellNumber =" + "\""+  id + "\"" ;
            	try{
        	        connection = DriverManager.getConnection(url, user, password);
        	        statement = connection.createStatement();
        	        resultSet = statement.executeQuery(giveSound);;
        	        while (resultSet.next()) {
        	        	isThere++;
        	        }

            	}catch (SQLException e) {

                } finally {
                    closeDB();
                }
        		//System.out.println(isThere);
        		if (isThere == 0) {
            	String queryWorkSound = "INSERT INTO sound VALUES(" + 
            			"\""+  id + "\""  + "," + 
            			"\""+ "nullsound" + "\"" +");"; 
        		/*"\"" +  tableNum + "\""  + "," +
        		"\"" + sound + "\"" +  ");" ;*/
        		//System.out.println(queryWorkSound);
            		sendQuery(queryWorkSound);
        		}
    	}
    }

    private String isThereMess(String id, String statusCom) {
    	//System.out.println("id com  "+id);
    	//System.out.println("status com  "+statusCom);
    	String personellNumber = new String();
    	String givePersonellNumber = " SELECT * FROM workerfio WHERE workerIdCard =" + "\"" + id + "\"" + 
    	"AND status =" + "\"" + "Работает" + "\"" ;
    	//System.out.println(givePersonellNumber);
    	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(givePersonellNumber);;
	        while (resultSet.next()) {
	        	personellNumber = resultSet.getString("personellNumber");
	        	//System.out.println("person numb   "+personellNumber);
	        }

    	}catch (SQLException e) {

        } finally {
            closeDB();
        }
    	
    	String answ = new String();
    	
    	String isComplete = new String();
    	
    	String giveMess = " SELECT * FROM message WHERE personellNumber =" + "\"" + personellNumber +   
    			"\"" + "AND isComplete != "+ "\"" + "0" + "\"";
    	//System.out.println(giveMess);
    	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(giveMess);;
	        while (resultSet.next()) {
	        //	System.out.println(resultSet.getString("isComplete"));
	        //	System.out.println(resultSet.getString("fileToPlay"));
	        	
	        	//answ += resultSet.getString("fileToPlay") + ",";
	        	isComplete = resultSet.getString("isComplete");
	        	int intIsComplete = Integer.parseInt(isComplete);
	        	int intStatus = Integer.parseInt(statusCom);
	        	
	        	if ( resultSet.getString("notification_type").equals("вход") && intStatus > 105 ) {
	        		break;
	        	}else {
	        		answ += resultSet.getString("fileToPlay") + ",";
		        	if( intStatus > 105 ) {
			        		intIsComplete = intIsComplete - 10;
			        	}
			        	else {
			        		intIsComplete = intIsComplete - 20;
			        	}
			        	if (intIsComplete < 0 ) {
			        		intIsComplete = 0;
			        	}
			        	
			        	String updateMes = "UPDATE  message SET "
			        			+ "isComplete =" + "\"" +  intIsComplete + "\"" +
			        			"WHERE id =" + "\"" + resultSet.getString("id") + "\"" + ";" ;
			        	sendQuery(updateMes);
			        	//personellNumber = resultSet.getString("personellNumber");
		        }
	        }

    	}catch (SQLException e) {

        } finally {
            closeDB();
        }
    	return answ;
    	
    }

    private void closeEntry() {
    	
    }
}
