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
@Table(name = "sensor")
public class Sensor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;	
	@Column(name = "name")
	private String name;
	@Column(name = "type")
	private String type;
	
	@OneToMany(mappedBy="sensor", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
//	@JoinColumn(name="id", nullable=false)
//	private Observation observation;
	@JsonIgnore
	List<Observation> observations;
	
	public Sensor(String name, String type) {
		this.name = name;
		this.type = type;
	}
	
//	public Sensor(int id, String name, String type) {
//		super();
//		this.id = id;
//		this.name = name;
//		this.type = type;
//	}
	
	public List<Observation> getObservations() {
		return observations;
	}

	public void setObservations(List<Observation> observations) {
		this.observations = observations;
	}

	public Sensor() {
		
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
