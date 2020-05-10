package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dbUtil.DBUtil;
import devices.Producer;
import devices.Sensor;
import observation.Observation;

import com.fasterxml.jackson.databind.ObjectMapper;


@WebServlet(name = "Main Server", urlPatterns = "/main")
public class main extends HttpServlet {
	
	private static final long serialVersionUID = -4751096228274971485L;
	HttpServletRequest request; 
	HttpServletResponse response;
	List<String> parameterNames;

	
	
	//This help writing the additional methods faster - no need to declare arguments
	void setRequest (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.request = request;
		this.response = response;
		parameterNames = new ArrayList<String>(request.getParameterMap().keySet());
		if (parameterNames.isEmpty()) {
			System.err.println("No parameter request!");
		}
	}
	
	boolean checkParam(int position, String name, String value) {
		if (position < parameterNames.size() && name.equals(parameterNames.get(position)) && value.equals(request.getParameter(parameterNames.get(position)))) {
			return true;
		} else {
			System.err.println("Param checked failed!");
			return false;
		}
	}
	
	String getValueAt(int position) {
		return request.getParameter(parameterNames.get(position));
	}
	
	boolean isUser() {
		ArrayList<User> users = DBUtil.selectAllUser();
		boolean b = false;
		String name = "";
		for (int i = 0; i < parameterNames.size(); i++) {
			if ((parameterNames.get(i)).equals("user")) {
				name = getValueAt(i);
				break;
			}	
		}
		for (User u : users) { 		      
			if ((u.getName().toString()).equals(name)) {
				b = true;
				break;
			}
		}
		return b;
	}
	
	boolean isSerial(String s) {
		ArrayList<String> serials = DBUtil.selectAllSerial();
		boolean b = false;

		for (String s2 : serials) { 		      
			if ((s2).equals(s)) {
				b = true;
				break;
			}
		}
		return b;
	}
	

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		setRequest(request, response);

		if (!parameterNames.isEmpty() && isUser()) {
//		if (!parameterNames.isEmpty()) {
			if (checkParam(0, "req", "insert") && checkParam(1, "table", "sensor") && !parameterNames.get(2).isEmpty() && parameterNames.get(2).equals("name") && !parameterNames.get(3).isEmpty() && parameterNames.get(3).equals("type") && !parameterNames.get(4).isEmpty() && parameterNames.get(4).equals("user")) {
				Sensor sensor = new Sensor(getValueAt(2), getValueAt(3));
				DBUtil.createSensor(sensor);
				String reqJsonString = new Gson().toJson(sensor);
				PrintWriter out = response.getWriter();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				out.print(reqJsonString);
				out.flush();	
			} else if (checkParam(0, "req", "insert") && checkParam(1, "table", "producer") && !parameterNames.get(2).isEmpty() && parameterNames.get(2).equals("serial") && !parameterNames.get(3).isEmpty() && parameterNames.get(3).equals("name") && !parameterNames.get(4).isEmpty() && parameterNames.get(4).equals("model") && !parameterNames.get(5).isEmpty() &&  parameterNames.get(5).equals("manufacture") && !parameterNames.get(6).isEmpty() && parameterNames.get(6).equals("user")) {
				Producer producer = new Producer(getValueAt(2), getValueAt(3), getValueAt(4), getValueAt(5));
				DBUtil.createProducer(producer);
				String reqJsonString = new Gson().toJson(producer);
				PrintWriter out = response.getWriter();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				out.print(reqJsonString);
				out.flush();
			} else if (checkParam(0, "req", "insert") && checkParam(1, "table", "observation") && !parameterNames.get(2).isEmpty() && parameterNames.get(2).equals("sensorid") && !parameterNames.get(3).isEmpty() && parameterNames.get(3).equals("producerid") && !parameterNames.get(4).isEmpty() && parameterNames.get(4).equals("location") && !parameterNames.get(5).isEmpty() && parameterNames.get(5).equals("value") && !parameterNames.get(6).isEmpty() && parameterNames.get(6).equals("serial") && isSerial(getValueAt(6)) && !parameterNames.get(7).isEmpty() && parameterNames.get(7).equals("user")) {
				Producer producer = DBUtil.findByProducerID(Integer.parseInt(getValueAt(2)));
				Sensor sensor = DBUtil.findBySensorID(Integer.parseInt(getValueAt(3)));
				Observation observation = new Observation(producer, sensor, getValueAt(4),  getValueAt(5));
				DBUtil.createObservation(observation, Integer.parseInt(getValueAt(2)), Integer.parseInt(getValueAt(3)));
				String reqJsonString = "";
				ObjectMapper mapper = new ObjectMapper();
				try {
		            // Java objects to JSON string - compact-print
//		            reqJsonString = mapper.writeValueAsString(observation);
//		            System.out.println(reqJsonString);
		            // Java objects to JSON string - pretty-print
		            reqJsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(observation);
//		            System.out.println(reqJsonString);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
				PrintWriter out = response.getWriter();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				out.print(reqJsonString);
				out.flush();
			
			
			} else if (checkParam(0, "req", "delete") && checkParam(1, "table", "sensor")  && !parameterNames.get(2).isEmpty() && parameterNames.get(2).equals("id") && !parameterNames.get(3).isEmpty() && parameterNames.get(3).equals("user")) {
				DBUtil.deleteSensor(Integer.parseInt(getValueAt(2)));
				JsonObject limitInfo = new JsonObject();
				limitInfo.addProperty("Msg", "Delete sensor" + Integer.parseInt(getValueAt(2)));
				String reqJsonString = new Gson().toJson(limitInfo);
				PrintWriter out = response.getWriter();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				out.print(reqJsonString);
				out.flush();
			} else if (checkParam(0, "req", "delete") && checkParam(1, "table", "producer")  && !parameterNames.get(2).isEmpty() && parameterNames.get(2).equals("sid") && !parameterNames.get(3).isEmpty() && parameterNames.get(3).equals("user")) {
				DBUtil.deleteProducer(Integer.parseInt(getValueAt(2)));
				JsonObject limitInfo = new JsonObject();
				limitInfo.addProperty("Msg", "Delete producer" + Integer.parseInt(getValueAt(2)));
				String reqJsonString = new Gson().toJson(limitInfo);
				PrintWriter out = response.getWriter();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				out.print(reqJsonString);
				out.flush();
			} else if (checkParam(0, "req", "delete") && checkParam(1, "table", "observation")  && !parameterNames.get(2).isEmpty() && parameterNames.get(2).equals("id") && !parameterNames.get(3).isEmpty() && parameterNames.get(3).equals("user")) {
				DBUtil.deleteObservation(Integer.parseInt(getValueAt(2)));
				JsonObject limitInfo = new JsonObject();
				limitInfo.addProperty("Msg", "Delete observation" + Integer.parseInt(getValueAt(2)));
				String reqJsonString = new Gson().toJson(limitInfo);
				PrintWriter out = response.getWriter();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				out.print(reqJsonString);
				out.flush();
			
			
			} else if (checkParam(0, "req", "show") && checkParam(1, "table", "sensor") && !parameterNames.get(2).isEmpty() && parameterNames.get(2).equals("user")) {
				List<Sensor> list = DBUtil.selectAllSensor();
				String reqJsonString = "";
				ObjectMapper mapper = new ObjectMapper();
				try {
		            // Java objects to JSON string - compact-print
//		            reqJsonString = mapper.writeValueAsString(list);
//		            System.out.println(reqJsonString);
		            // Java objects to JSON string - pretty-print
		            reqJsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);
//		            System.out.println(reqJsonString);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
				PrintWriter out = response.getWriter();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				out.print(reqJsonString);
				out.flush();
			} else if (checkParam(0, "req", "show") && checkParam(1, "table", "producer") && !parameterNames.get(2).isEmpty() && parameterNames.get(2).equals("user")) {
				List<Producer> list = DBUtil.selectAllProducer();
				String reqJsonString = "";
				ObjectMapper mapper = new ObjectMapper();
				try {
		            // Java objects to JSON string - compact-print
//		            reqJsonString = mapper.writeValueAsString(list);
//		            System.out.println(reqJsonString);
		            // Java objects to JSON string - pretty-print
		            reqJsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);
//		            System.out.println(reqJsonString);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
				PrintWriter out = response.getWriter();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				out.print(reqJsonString);
				out.flush();
			} else if (checkParam(0, "req", "show") && checkParam(1, "table", "observation") && !parameterNames.get(2).isEmpty() && parameterNames.get(2).equals("user")) {
				List<Observation> list = DBUtil.selectAllObservation();
				String reqJsonString = "";
				ObjectMapper mapper = new ObjectMapper();
				try {
		            // Java objects to JSON string - compact-print
//		            reqJsonString = mapper.writeValueAsString(list);
//		            System.out.println(reqJsonString);
		            // Java objects to JSON string - pretty-print
		            reqJsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);
//		            System.out.println(reqJsonString);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
				PrintWriter out = response.getWriter();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				out.print(reqJsonString);
				out.flush();
			
			
			} else {
				JsonObject limitInfo = new JsonObject();
				limitInfo.addProperty("Error", "This kind of request is not supported!!!");
				String reqJsonString = new Gson().toJson(limitInfo);
				PrintWriter out = response.getWriter();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				out.print(reqJsonString);
				out.flush();
			}
			
//			JsonObject limitInfo = new JsonObject();
//			limitInfo.addProperty("counter", counter.getCounter());
//			limitInfo.addProperty("requestLimit", limit);
//
//			String reqJsonString = new Gson().toJson(limitInfo);
//
//			PrintWriter out = response.getWriter();
//			response.setContentType("application/json");
//			response.setCharacterEncoding("UTF-8");
//			out.print(reqJsonString);
//			out.flush();
//		}


		} else {
			response.sendRedirect("index.jsp");
			
//			JsonObject limitInfo = new JsonObject();
//			limitInfo.addProperty("Error", "This request is not supported");
//			String reqJsonString = new Gson().toJson(limitInfo);
//			PrintWriter out = response.getWriter();
//			response.setContentType("application/json");
//			response.setCharacterEncoding("UTF-8");
//			out.print(reqJsonString);
//			out.flush();
			 
//			Sensor req2 = new Sensor("abc","sdf");
////		 	increment();
////		 	Request req2 = new Request( rid, n, request.getRequestURL().toString(), counter.getCounter(), limit);
//			DBUtil.create(req2);
//
//			// Json format
//			String reqJsonString = new Gson().toJson(req2);
//
//			PrintWriter out = response.getWriter();
//			response.setContentType("application/json");
//			response.setCharacterEncoding("UTF-8");
//			out.print(reqJsonString);
//			out.flush();
		}
	}

	
	@Override
	public void init() throws ServletException {
		System.out.println("Servlet " + this.getServletName() + " has started");
//		Sensor sensor1 = new Sensor("DHT11", "Temperature");
//		Sensor sensor2 = new Sensor("DHT22", "Humidity");
//		Sensor sensor3 = new Sensor("zolertiaZ1", "Temperature");
//		DBUtil.createSensor(sensor1);
//		DBUtil.createSensor(sensor2);
//		DBUtil.createSensor(sensor3);
//		Producer producer1 = new Producer("iotproducer1", "B Pi 3", "UK");
//		Producer producer2 = new Producer("iotproducer2", "B Pi 4", "FR");
//		DBUtil.createProducer(producer1);
//		DBUtil.createProducer(producer2);
	}

	@Override
	public void destroy() {
		System.out.println("Servlet " + this.getServletName() + " has stopped");
	}
	
}
