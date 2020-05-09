package devices;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import observation.Observation;


@Entity
@Table(name="producer")
public class Producer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "serial")
	private String serial;
	@Column(name = "name")
	private String name;
	@Column(name = "model")
	private String model;
	@Column(name = "manufacture")
	private String manufacture;
	
	@OneToMany(mappedBy="producer", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
//	@JoinColumn(name="id", nullable=false)
//	private Observation observation;
	@JsonIgnore
	List<Observation> observations;

	public Producer() {
		
	}
	
	public Producer(String serial, String name, String model, String manufacture) {
		this.serial = serial;
		this.name = name;
		this.model = model;
		this.manufacture = manufacture;
	}
	
//	public Producer(int id, String name, String model, String manufacture) {
//		super();
//		this.id = id;
//		this.name = name;
//		this.model = model;
//		this.manufacture = manufacture;
//	}
	
	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}
	
	public List<Observation> getObservations() {
		return observations;
	}

	public void setObservations(List<Observation> observations) {
		this.observations = observations;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getmanufacture() {
		return manufacture;
	}
	public void setmanufacture(String manufacture) {
		this.manufacture = manufacture;
	}
}
