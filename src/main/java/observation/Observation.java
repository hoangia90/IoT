package observation;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import devices.Producer;
import devices.Sensor;


@Entity
@Table(name = "observation")
public class Observation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//	@JoinColumn(name="producer_id")
	@JoinColumn(name="producer_id",referencedColumnName="id")
	@JsonIgnore 
	Producer producer;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//	@JoinColumn(name="sensor_id")
	@JoinColumn(name="sensor_id",referencedColumnName="id")
	@JsonIgnore 
	Sensor sensor;

	@Column(name = "location")
	private String location;
	@Column(name = "value")
	private String value;
	
	public Observation() {

	}
	
//	public Observation(String location, String value) {
//		this.location = location;
//		this.value = value;
//	}
	
	public Observation(Producer producer, Sensor sensor, String location, String value) {
		this.producer = producer;
		this.sensor = sensor;
		this.location = location;
		this.value = value;
		
//		Set<Observation> observations = new HashSet<>();
//		observations.add(this);
//		this.getProducer().setObservations(observations);
//		this.getSensor().setObservations(observations);
		
	}
	
//	public Observation(int id, Producer producer, Sensor sensor, String location, String value) {
//		this.id = id;
//		this.producer = producer;
//		this.sensor = sensor;
//		this.location = location;
//		this.value = value;
//	}



	public int getId() {
		return id;
	}

	public Producer getProducer() {
		return producer;
	}

	public void setProducer(Producer producer) {
		this.producer = producer;
	}

	public Sensor getSensor() {
		return sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
