//import java.text.DecimalFormat;
import java.time.LocalDate;
import java.io.Serializable;

public class Customer implements Identifiable, Serializable {

	static final long serialVersionUID = 1L;
	
	static long IDcounter = 0;
	String name;
	String phone;
	String address;
	long ID;
	LocalDate creationDate;

	SkillLevel sl;

	public Customer(String name, String phone, String address, SkillLevel sl) {
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.sl = sl;
		creationDate = LocalDate.now();
		this.ID = IDcounter++;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public SkillLevel getSkillLevel() {
		return sl;
	}

	public void setSkillLevel(SkillLevel sl) {
		this.sl = sl;
	}

	public long getID() {
		
		return ID;
	}
	
	public String getStringID() {
		return this.name;
	}
	
	@Override
	public String toString() {
		String s;
		String typestr = sl.toString();
		s = name;
		s += "\nID: " + ID;
		s += "\n\tPhone: " + phone;
		s += "\n\tAddress: " + address;
		s += "\n\tSkill Level: " + typestr.substring(0,1).toUpperCase() + typestr.substring(1).toLowerCase();
//		s += "\n\t" + new DecimalFormat("###.##").format(price) + "$ per night.";
		
		
		return s;
	}

	

}

enum SkillLevel {
	BEGINNER, INTERMEDIATE, EXPERT
}