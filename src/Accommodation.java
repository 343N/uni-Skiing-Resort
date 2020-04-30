import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashSet;
import java.io.Serializable;

public class Accommodation implements Identifiable, Serializable {

	static final long serialVersionUID = 1L;

	static long IDcount = 0;

	String name;
	String address;
	String description;
	double price;
	long ID;
	AccommodationType type;

	public Accommodation(String name, String address, String description, double price, AccommodationType at) {
		this.name = name;
		this.address = address;
		this.description = description;
		this.price = price;
		this.type = at;
		this.ID = IDcount++;
	}

	// this is a set of all bookings, so that availability depends on the dates
	// inputted
	HashSet<String> availabilityByDate = new HashSet<String>();

	public boolean isAvailable(LocalDate date) {
		String datestr = date.toString();
		if (availabilityByDate.contains(datestr))
			return false;
		else
			return true;
	}

	public boolean isAvailable(LocalDate startDate, LocalDate endDate) {
		if (endDate == null || startDate == null)
			return false;

		long days = endDate.toEpochDay() - startDate.toEpochDay();
		for (long i = 0; i < days; i++) {
			LocalDate newDate = startDate.plusDays(i);
			if (!isAvailable(newDate))
				return false;
		}
		return true;
	}

	public long getID() {
		return ID;
	}

	public void setID(short ID) {
		this.ID = ID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public AccommodationType getType() {
		return type;
	}

	public void setType(AccommodationType type) {
		this.type = type;
	}

	public HashSet<String> getAvailabilityByDate() {
		return availabilityByDate;
	}

	public void setAvailabilityByDate(HashSet<String> availabilityByDate) {
		this.availabilityByDate = availabilityByDate;
	}

	public String getStringID() {
		return this.name;
	}
	
	@Override
	public String toString() {
		String s;
		String typestr = type.toString();
		s = name;
		s += "\nID: " + ID;
		s += "\n\tType: " + typestr.substring(0,1).toUpperCase() + typestr.substring(1).toLowerCase();
		s += "\n\t" + address;
		s += "\n\t" + new DecimalFormat("###.##").format(price) + "$ per night.";
		s += "\n\tDescription: " + description;
		
		
		return s;
	}
}

enum AccommodationType {
	HOTEL, LODGE, APARTMENT
}
