package addRecord;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AddRecord
 */
@WebServlet("/AddRecord")
public class AddRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database = new Database();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddRecord() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
	    String id = (String) request.getParameter("id");
	   // System.out.println("id card" + id);
	    //int ids = Integer.parseInt(id);
	    
	    String answ = database.addRecordTime(id);
	    //System.out.println(database.getSoundName(id));
	   // System.out.println(database.getSoundName(id));
	    if (database.getSoundName(id).equals("nullsound") == false) {
	    	//answ +=",";
	    	answ += database.getSoundName(id);
	    }else {
	    	//answ +=",";
	    	answ += "notrecognized";
	    }
	    	/*if (answ.equals(null) ) {
	    		answ = "not_good";
	    	}*/
	  //  System.out.println(answ);
	    response.getWriter().write(answ);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
